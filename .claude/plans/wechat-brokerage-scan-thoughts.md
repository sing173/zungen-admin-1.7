# 微信扫码分销功能 - 思考过程与解决方案记录

> 创建时间：2025-04-01
> PR: https://github.com/sing173/zungen-admin-1.7/pull/1
> 分支：feature/wechat-brokerage-scan

---

## 一、需求理解

### 原始需求
用户想要开发一个通过扫微信码进行分销的裂变式营销功能。

### 核心业务流程
1. 分销员生成专属微信二维码
2. 潜在客户扫描二维码关注公众号
3. 自动建立分销关系（谁推广了谁）
4. 客户后续购买，分销员获得佣金
5. 裂变效应：新客户也可以成为分销员，继续推广

### 关键问题
- **如何识别扫码用户**？→ 微信openId
- **如何关联会员系统**？→ 需要建立openId与会员ID的映射
- **如何绑定分销关系**？→ 调用现有BrokerageApi.bindUser()
- **EventKey格式设计**？→ `brokerage_{distributorId}_{sceneId}`

---

## 二、代码探索发现

### 1. 分销系统现状（yudao-module-mall）
**发现**：
- `BrokerageUserDO`：分销用户表，与MemberUser 1:1关联（id即MemberUser.id）
- 关键字段：
  - `id`：会员ID
  - `bindUserId`：上级推广员ID（建立分销关系）
  - `brokerageEnabled`：是否有推广资格
  - `brokeragePrice`：可提现佣金
- `BrokerageRecordDO`：佣金记录表，记录每一笔订单的佣金分配
- `BrokerageApi.bindUser(userId, bindUserId, isNewUser)`：核心绑定方法

**重要发现**：
- 分销关系存储在 `BrokerageUser.bindUserId` 字段
- `bindUser()` 方法内部已有完整的校验逻辑（防止重复绑定、循环绑定等）
- 支持两种绑定模式：REGISTER（仅注册时可绑定）和ANYTIME（随时可绑定）

### 2. 微信公众号模块（yudao-module-mp）
**发现**：
- `MpAccountServiceImpl.qrCodeCreateLastTicket()`：生成临时二维码（30天有效）
- `ScanHandler`：扫码事件处理器，**当前是空实现**（抛出UnsupportedOperationException）
- `MpUserDO`：公众号粉丝表，包含openid、subscribeStatus等字段
- 公众号回调地址：`/mp/open/{appId}`

**关键点**：
- ScanHandler是处理扫码事件的最佳位置
- 当前需要我们自己实现扫码业务逻辑
- 微信推送的scan事件包含：EventKey、FromUser（openId）、ToUser（公众号）

### 3. 会员系统（yudao-module-member）
**发现**：
- `MemberUserDO`：会员用户表
  - 核心字段：id, mobile（手机号）, nickname, avatar, status等
  - **没有openid字段** ⚠️
- `MemberUserService` 提供通过mobile查询，**没有通过openId查询的方法**
- 社交登录通过 `SocialUserDO` + `SocialUserBindDO` 关联会员

**关键问题**：
- **如何将微信openId映射到会员userId？**
  - 方案A：添加MemberUserDO.openid字段（简单直接）
  - 方案B：通过SocialUserBind表查询（需要公众号授权code，scan事件没有）
  - 方案C：引导用户授权后绑定（需要额外流程）

### 4. 系统架构理解
- **Dubbo RPC**：模块间通过Dubbo通信，API模块定义接口，Biz模块实现
- **多租户**：所有DO继承TenantBaseDO，自动tenant_id字段
- **CommonResult**：统一响应格式，包含success、data、msg
- **@Reference**：Dubbo引用注解，用于跨模块调用

---

## 三、关键技术决策

### 决策1：如何建立openId与userId的关联？

**选项对比**：

| 方案 | 实现复杂度 | 数据一致性 | 实时性 | 评估 |
|------|-----------|-----------|-------|------|
| A：MemberUser添加openid字段 | 低 | 高（直接） | 实时 | ✅ 选中 |
| B：通过SocialUserBind表 | 中（需要code授权） | 中 | 非实时 | ❌ scan事件无code |
| C：扫码后引导授权 | 高（需H5页面+回调） | 高 | 延迟 | ❌ MVP阶段暂缓 |

**结论**：选择方案A，最小改动，直连查询。

**实施**：
1. 修改 `MemberUserDO.java`：新增 `private String openid;`
2. 修改 `MemberUserMapper.java`：新增 `selectByOpenId(String openid)`
3. 修改 `MemberUserService.java` + `ServiceImpl`：新增 `getByOpenId(String openid)`

---

### 决策2：ScanHandler中如果找不到会员用户怎么办？

**场景**：用户扫码时，可能还没有注册会员（未绑定手机号）

**选项**：
1. **抛出异常** → 影响用户体验，不符合业务逻辑
2. **静默忽略** → 错失潜在客户，无法建立分销关系
3. **引导注册** → 发送客服消息，提供注册链接（需要H5页面）

**决策**：MVP阶段选择**记录日志并返回**，后续TODO实现引导流程。

**代码实现**：
```java
MemberUserDO user = memberUserService.getByOpenId(openId);
if (user == null) {
    log.info("扫码用户未注册会员，openId={}", openId);
    // TODO：发送客服消息，引导注册
    return;
}
```

---

### 决策3：bindByScan的参数设计

**问题**：`BrokerageApi.bindUser()` 已有 `userId, bindUserId, isNewUser` 参数，新方法如何设计？

**考虑因素**：
- **统一性**：参数应保持与现有方法一致
- **场景区分**：扫码场景需要记录sceneId（渠道统计）
- **返回值**：现有bindUser返回boolean，但RPC更适合CommonResult

**设计方案**：
```java
// 方案1：复用bindUser，sceneId通过其他方式传递（如ThreadLocal）
// 方案2：新增独立方法
CommonResult<Boolean> bindByScan(Long distributorId, Long userId, @Nullable Long sceneId);
```

**选型**：方案2 ✅
- 理由：扫码场景有额外的sceneId参数，独立方法更清晰
- 实现：在 `BrokerageUserServiceImpl.bindByScan()` 中调用 `bindBrokerageUser()`
- TODO：sceneId暂未使用，预留扩展扫码日志表

---

### 决策4：Dubbo RPC引用方式

**如何让ScanHandler调用BrokerageApi？**

**选项**：
1. **@Autowired注入BrokerageApi** → 需要在MP模块的配置中启用RPC
2. **@Reference注入** → Dubbo标准方式，需要配置协议

**查看其他模块**：
```java
// 发现其他模块（如MpStatisticsServiceImpl）使用@Resource注入本地Service
// 但跨模块调用Dubbo服务时，应该使用@Reference
```

**决策**：使用 `@Reference` 注解
```java
@Reference(protocol = CommonConstants.DUBBO_PROTOCOL, timeout = 30000, retries = 0, check = false)
private BrokerageApi brokerageApi;
```

**注意**：`check = false` 避免启动时检查（Dubbo服务可能在另一进程）

---

### 决策5：EventKey格式设计

**需求**：扫码后需要知道：
- 哪个分销员（distributorId）
- 哪个渠道/场景（sceneId，可选）

**候选格式**：
1. `brokerage_{distributorId}`（简单）
2. `brokerage_{distributorId}_{sceneId}`（支持场景）
3. `brokerage_{distributorId}:{sceneId}`（分隔符）

**选型**：方案2 ✅
- 使用下划线分隔（与现有系统保持一致，如其他场景值）
- sceneId可选，兼容简单场景

**解析逻辑**：
```java
String[] parts = eventKey.split("_");
Long distributorId = Long.valueOf(parts[1]);
Long sceneId = parts.length > 2 ? Long.valueOf(parts[2]) : null;
```

---

## 四、遇到的坑与解决方案

### 坑1：BrokerageUserServiceImpl 没有实现 BrokerageApi

**问题**：最初在 `BrokerageUserServiceImpl` 实现了 `bindByScan`，但 `BrokerageApiImpl` 没有调用它。

**解决**：
1. 给 `BrokerageApi` 接口添加 `bindByScan()` 方法签名
2. 在 `BrokerageApiImpl` 中实现该方法：
```java
@Override
public CommonResult<Boolean> bindByScan(Long distributorId, Long userId, Long sceneId) {
    boolean success = brokerageUserService.bindByScan(distributorId, userId, sceneId);
    return CommonResult.success(success);
}
```

---

### 坑2：MemberUserService 导入 javax.annotation.Resource 冲突

**问题**：MemberUserService是接口，不能使用@Resource（那是实现类的注入注解）

**解决**：
- 接口仅声明方法，无需注解
- 实现类 `MemberUserServiceImpl` 使用 `@Resource` 注入 `MemberUserMapper`

---

### 坑3：Dubbo Reference 协议配置

**问题**：使用 `@Reference` 时如果不配置protocol，可能默认引用其他协议（如tri）导致连接失败

**解决**：明确指定 `protocol = CommonConstants.DUBBO_PROTOCOL`（值为"dubbo"）

同时注意超时时间设置 `timeout = 30000`（30秒），因为绑定逻辑可能涉及数据库查询。

---

### 坑4：ScanHandler.handle() 返回值

**问题**：方法签名返回 `WxMpXmlOutMessage`，如果返回null会怎样？

**阅读Weixin-Java-MP文档**：
- 返回 `null` 表示不主动回复用户（passive response）
- 返回 `WxMpXmlOutMessage` 会立即回复用户
- 如果需要后续发送客服消息，必须返回null

**决策**：返回null，后续通过 `MpMessageService` 发送客服消息（TODO）

---

### 坑5：Git分支管理与PR创建

**问题**：直接在master上提交，无法创建PR（GitHub要求分支不同）

**错误操作**：
1. 在master上commit
2. 切换feature分支是干净的
3. 强制重置master导致提交丢失

**正确流程**：
```bash
# 1. 从master创建并切换到特性分支
git checkout -b feature/wechat-brokerage-scan

# 2. 在特性分支上开发并提交
git add . && git commit -m "..."

# 3. 推送到远程
git push -u origin feature/wechat-brokerage-scan

# 4. 创建PR（此时有commits差异）
gh pr create --title "..." --body "..."

# 如果需要修改，继续在feature分支commit，PR自动更新
```

---

## 五、安全性考虑

### 1. 参数校验
- `bindByScan` 方法中检查 `distributorId` 和 `userId` 非空
- 调用 `bindBrokerageUser` 内部已经有完善的校验（防止自绑定、循环绑定等）

### 2. 权限控制
- 扫码事件由微信服务器推送，服务端需验证签名（由Weixin-Java-MP框架处理）
- 我们只处理已鉴权的消息

### 3. 数据隔离
- 多租户架构：BrokerageUserDO继承TenantBaseDO，自动隔离租户数据
- ScanHandler需要注入 `TenantContextHolder` 吗？
  - **查看DubboProviderFilter**：RPC调用时会从Header中提取tenantId
  - 微信公众号回调不属于RPC调用，需手动设置租户上下文吗？
  - **答案**：微信回调属于HTTP请求，已通过 `MpOpenController` 的拦截器处理租户（需要验证）

**风险点**：如果ScanHandler中没有正确设置租户ID，可能导致数据越权。
**缓解措施**：通过 `MpAccountService` 获取公众号所属租户，在ScanHandler中设置 `TenantContextHolder`（TODO）

---

## 六、性能考量

### 1. 数据库查询次数
- 扫码流程：
  1. `memberUserMapper.selectByOpenId(openId)` → 1次
  2. `brokerageUserMapper.selectById(distributorId)` → 1次（bindByScan内）
  3. `bindBrokerageUser()` 内部多次查询check → 2-3次
- **总计约4-5次查询**，可接受

### 2. 并发场景
- `bindBrokerageUser` 方法是否有并发问题？
  - 查看代码：使用 `brokerageUserMapper.updateById()` 更新，存在覆盖风险
  - 但业务允许重复绑定时会抛出 `BROKERAGE_BIND_OVERRIDE` 异常
  - **解决方案**：通过数据库乐观锁或事务隔离级别保证一致性（现有代码已处理）

### 3. 缓存策略
- 二维码生成已有缓存（`qrCodeCreateLastTicket`）
- 扫码后绑定结果无需缓存（直接写入DB）

---

## 七、未完成的工作（TODO）

### 1. 扫码用户未注册处理（优先级：高）
**现状**：`if (user == null)` 直接return，用户流失

**解决方案**：
- 发送客服消息，包含注册H5链接（带上分销员参数）
- H5页面通过微信授权获取openId，自动注册并绑定

**需要实现**：
- `MpMessageService.sendCustomerMessage(openId, article/url)`
- 前端H5页面：微信授权 + 手机号绑定（复用现有社交登录流程）

---

### 2. 扫码日志统计（优先级：中）
**价值**：统计各渠道扫码转化率、分销员推广效果

**方案**：
```sql
CREATE TABLE trade_brokerage_scan_log (
    id BIGINT PRIMARY KEY,
    distributor_id BIGINT NOT NULL,   -- 分销员ID
    user_id BIGINT,                   -- 扫码用户ID（可能为空）
    scene_id BIGINT,                  -- 场景ID
    openid VARCHAR(64),               -- 微信openId
    mp_app_id VARCHAR(64),            -- 公众号ID
    create_time DATETIME
);
```

**实现**：
- 在 `bindByScan`  successes后插入日志
- 创建 `BrokerageScanLogDO` + `BrokerageScanLogMapper`
- 提供查询API供管理后台使用

---

### 3. 绑定成功通知（优先级：中）
**功能**：扫码成功后，通过模板消息通知分销员

**实现**：
- `MpMessageService.sendTemplateMessage()`
- 消息模板："恭喜！您成功推荐了新用户，昵称：xxx，时间：xxx"

---

### 4. 裂变海报生成（优先级：低）
**需求**：用户绑定后，生成专属海报（含用户二维码），方便分享

**方案**：
- 复用 `qrCodeCreateLastTicket` 生成用户专属二维码（scene_id=userId）
- 使用图片合成工具（Thumbnailator/Apache ImgScalr）将二维码叠加到海报模板
- 上传到COS/OSS，返回URL

**文件位置**：建议新增 `BrokerageApi.generatePoster(userId, sceneId)` → 返回海报URL

---

### 5. 租户上下文处理（优先级：高）
**问题**：ScanHandler中可能丢失租户ID，导致DB查询跨租户

**解决方案**：
- 通过 `MpAccountService` 根据appId查询账号，获取tenantId
- 在ScanHandler开始时设置 `TenantContextHolder.setTenantId(tenantId)`

```java
Long tenantId = mpAccountService.getTenantIdByAppId(appId);
TenantContextHolder.setTenantId(tenantId);
```

---

## 八、测试建议

### 单元测试
```java
@Test
public void testBindByScan_Success() {
    // given
    Long distributorId = 1L, userId = 2L, sceneId = 100L;
    when(brokerageUserMapper.selectById(distributorId)).thenReturn(new BrokerageUserDO().setBrokerageEnabled(true));
    // when
    CommonResult<Boolean> result = brokerageUserService.bindByScan(distributorId, userId, sceneId);
    // then
    assertTrue(result.isSuccess());
}

@Test
public void testBindByScan_DistributorNotFound() {
    when(brokerageUserMapper.selectById(999L)).thenReturn(null);
    CommonResult<Boolean> result = brokerageUserService.bindByScan(999L, 2L, null);
    assertFalse(result.isSuccess());
}
```

### 集成测试
1. 准备测试数据：
   - 创建会员用户A（分销员），记录ID
   - 创建会员用户B（普通用户），设置openid='test_openid'
   - BrokerageUser表确保A的 `brokerageEnabled=true`

2. 模拟微信扫码事件：
```java
WxMpXmlMessage message = new WxMpXmlMessage();
message.setEventKey("brokerage_" + distributorId + "_1");
message.setFromUser("test_openid");
scanHandler.handle(message, ...);
```

3. 验证：
   - BrokerageUser(brokerageUser=B.id).bindUserId == A.id
   - 日志输出正确

---

## 九、后续优化方向（排序）

1. **P0 - 租户上下文修复**：ScanHandler中正确设置tenantId
2. **P0 - 未注册用户引导**：发送客服消息，引导H5注册授权
3. **P1 - 扫码日志统计**：BrokerageScanLogDO + 管理后台查询
4. **P1 - 绑定成功通知**：模板消息通知分销员
5. **P2 - 裂变海报生成**：自动合成海报并上传
6. **P2 - 多级分销支持**：当前一级，未来可能需支持多层级
7. **P2 - 重复扫码处理优化**：更新bindUserId时记录时间，禁止频繁切换

---

## 十、关键文件清单（修改和新增）

### 修改文件
1. `/yudao-module-mall/yudao-module-trade-api/.../BrokerageApi.java`
2. `/yudao-module-mall/yudao-module-trade-biz/.../BrokerageApiImpl.java`
3. `/yudao-module-mall/.../service/brokerage/user/BrokerageUserServiceImpl.java`
4. `/yudao-module-member/.../MemberUserDO.java`
5. `/yudao-module-member/.../dal/mysql/user/MemberUserMapper.java`
6. `/yudao-module-member/.../service/user/MemberUserService.java`
7. `/yudao-module-member/.../service/user/MemberUserServiceImpl.java`
8. `/yudao-module-mp/.../handler/other/ScanHandler.java`

### 待新增文件（TODO）
9. `/yudao-module-trade-biz/.../dal/dataobject/brokerage/BrokerageScanLogDO.java`
10. `/yudao-module-trade-biz/.../dal/mysql/brokerage/BrokerageScanLogMapper.java`
11. `/sql/yudao-module-mall/trade_brokerage_scan_log.sql`
12. 可能新增：`MpCustomerMessageService`（封装客服消息）

---

## 十一、参考资料

### 相关代码位置
- **分销核心**：`yudao-module-mall/yudao-module-trade-biz/.../service/brokerage/`
- **公众号回调**：`yudao-module-mp/.../controller/MpOpenController.java`
- **二维码生成**：`yudao-module-mp/.../service/impl/MpAccountServiceImpl.java:qrCodeCreateLastTicket()`
- **Dubbo过滤器**：`yudao-server/.../rpc/DubboProviderFilter.java`（租户上下文传递）

### 微信事件文档
- scan事件：用户扫描带参数二维码时触发
- EventKey：二维码中的参数，最多64字节
- 公众号消息类型：https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html

---

## 十二、总结

**本次PR实现了MVP核心流程**：
- ✅ 扫码事件可识别分销员
- ✅ 绑定关系写入DB
- ✅ 复用现有分销校验逻辑

**核心依赖变更**：
- MemberUser增加openid字段（向前兼容）

**开放问题**（待后续解决）：
- 未注册用户如何引导？
- 扫码日志如何统计？
- 租户上下文是否完整？

**下一步行动**：
1. 测试MVP功能（需搭建微信测试环境）
2. 实现TODO中的租户上下文处理
3. 实现未注册用户引导流程
4. 补充单元测试和集成测试

---

**PR状态**：已提交，等待审查和合并
