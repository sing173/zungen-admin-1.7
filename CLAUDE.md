# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

**芋道快速开发平台 (yudao)** - 基于 Spring Boot 的多模块快速开发平台，采用MyBatis Plus + Dubbo + Redis 技术栈。

### 技术栈
- **后端**: Spring Boot 2.7.16 (JDK 8), Spring Security, MyBatis Plus 3.5.3.2
- **RPC**: Apache Dubbo
- **数据库**: MySQL（支持Oracle/PostgreSQL/SQL Server/达梦DM/TiDB）
- **缓存**: Redis + Redisson
- **消息队列**: RabbitMQ（可选）
- **搜索引擎**: Elasticsearch（可选）
- **工作流**: Flowable
- **前端**: Vue3 + element-plus / Vue3 + vben(ant-design-vue)
- **小程序**: uni-app

## 项目结构

### Maven 多模块架构

```
yungen-admin-1.7/
├── yudao-dependencies/          # 依赖管理模块（BOM）
├── yudao-framework/             # 框架基础模块
│   ├── yudao-spring-boot-starter-*  # 各种Starter
│   ├── yudao-tenant-core/       # 多租户核心
│   ├── yudao-security-core/     # 安全框架
│   ├── yudao-social-core/       # 社交登录框架
│   └── ...
├── yudao-module-*/              # 业务模块
│   ├── yudao-module-system/     # 系统管理（用户、角色、权限）
│   ├── yudao-module-member/     # 会员中心
│   ├── yudao-module-mp/         # 微信公众号
│   ├── yudao-module-mall/       # 商城系统
│   │   ├── yudao-module-product-api/  # 商品API
│   │   ├── yudao-module-product-biz/ # 商品业务
│   │   ├── yudao-module-trade-api/   # 交易API（含分销）
│   │   ├── yudao-module-trade-biz/  # 交易业务
│   │   ├── yudao-module-promotion-api/ # 促销API
│   │   └── yudao-module-promotion-biz/
│   ├── yudao-module-pay/        # 支付系统
│   ├── yudao-module-bpm/        # 工作流
│   ├── yudao-module-crm/        # CRM
│   ├── yudao-module-report/     # 报表
│   ├── yudao-module-infra/      # 基础设施
│   └── zungen-module-mqtt/      # MQTT物联网模块
├── yudao-server/                # 服务启动器（所有服务聚合）
└── sql/                         # 数据库脚本
    ├── yudao/                   # 各模块SQL
    └── upgrade/                 # 升级脚本
```

### 模块依赖关系
- API模块（`*-api`）定义DTO和Service接口，被其他模块依赖
- Biz模块（`*-biz`）实现Service，通过Dubbo提供RPC服务
- 前端项目（`yudao-ui-admin*`）独立仓库，通过HTTP调用后端API

### 关键设计模式
1. **分层架构**: API层(接口) + Biz层(实现) + DAL层(数据访问)
2. **DDD思想**: DO(实体) + Convert(转换器) + Service(业务逻辑)
3. **多租户**: 继承 `TenantBaseDO`，自动注入 `tenant_id`
4. **权限**: 基于Spring Security + RBAC + 数据权限（基于机构）
5. **社交登录**: 使用JustAuth框架集成多平台OAuth

## 构建和开发

### 环境要求
- JDK 8 (或 JDK 17 for master-boot3分支)
- Maven 3.6+
- MySQL 8.0+ (或其他支持的数据库)
- Redis 5.0+
- Node.js 16+ (前端开发)

### 构建命令

```bash
# 完整构建（跳过测试，推荐用于本地快速构建）
mvn clean install -Dmaven.test.skip=true

# 完整构建（包含测试）
mvn clean install

# 仅编译
mvn clean compile

# 构建并打包Docker镜像（需要Docker环境）
mvn clean package -DskipTests
docker build -t zungen-admin:latest .

# 仅运行某个模块的测试
mvn test -pl yudao-module-member/yudao-module-member-biz -am

# 跳过特定模块编译（如测试模块）
mvn clean install -DskipTests -pl '!yudao-module-*/src/test'
```

### 运行项目

**方式一：IDE运行**
1. 导入项目为Maven项目（IntelliJ IDEA或Eclipse）
2. 运行 `yudao-server/src/main/java/cn.iocoder.yudao.server.YudaoServerApplication.java`

**方式二：Docker Compose**
```bash
# 1. 复制环境配置
cp docker.env .env

# 2. 编辑.env配置数据库密码等信息
# 3. 启动服务
docker-compose up -d

# 4. 初始化数据库
docker-compose exec mysql mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" < sql/yudao.sql
```

**方式三：直接运行JAR**
```bash
# 1. 确保数据库已初始化
mysql -uroot -p < sql/yudao.sql

# 2. 修改yudao-server/src/main/resources/application.yml配置数据库连接

# 3. 运行
java -jar yudao-server/target/yudao-server.jar
```

### 数据库初始化
```bash
# 完整SQL（含初始数据）
cat sql/yudao.sql | mysql -u root -p yudao

# 仅表结构
cat sql/yudao-schema.sql | mysql -u root -p yudao

# 基础数据
cat sql/yudao-menu.sql | mysql -u root -p yudao
```

### 本地开发配置

1. **数据库配置**: 修改 `yudao-server/src/main/resources/application.yml`
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/yudao?useUnicode=true&characterEncoding=utf8
       username: root
       password: yourpassword
   ```

2. **Redis配置**: 确保本地Redis运行在6379端口

3. **微信公众号配置** (yudao-module-mp):
   - 修改 `MpAccountDO` 相关配置或通过管理后台添加公众号账号

4. **Dubbo配置**: 默认单机直连，无需Zookeeper（生产环境建议）

## 常见开发任务

### 1. 新增一个API接口
1. 在 `*-api` 模块定义 Service 接口和 DTO
2. 在 `*-biz` 模块创建 ServiceImpl 实现，并添加 `@Service` 注解
3. 在 `*-biz` 模块创建 Controller（通常已存在基础控制器）

### 2. 新增数据库表
1. 在 `*-biz/src/main/java/.../dal/dataobject/` 创建 `XXXDO.java` 继承 `BaseDO` 或 `TenantBaseDO`
2. 在 `*-biz/src/main/java/.../dal/mysql/` 创建 `XXXMapper.java` 继承 `BaseMapperX<XXXDO>`
3. 在 `sql/yudao-{module}.sql` 添加建表语句
4. 运行 `mvn compile` 生成代码

### 3. 调用其他模块服务
使用 Dubbo RPC 引用（在API模块定义接口，Biz模块实现）：
```java
@Reference(protocol = "dubbo", timeout = 30000, check = false)
private BrokerageApi brokerageApi;
```

### 4. 添加单元测试
```bash
# 运行所有单元测试
mvn test

# 运行特定模块测试
mvn test -pl yudao-module-member/yudao-module-member-biz

# 运行单个测试类
mvn test -Dtest=MemberUserServiceImplTest -pl yudao-module-member/yudao-module-member-biz
```

### 5. 代码生成器使用
前端项目中集成了代码生成器，通过UI界面配置生成CRUD代码：
- 访问: http://localhost:8080/devtools/codegen
- 选择表 → 配置字段 → 生成前后端代码

### 6. 微信公众号开发
- 配置公众号AppID/AppSecret（管理后台：微信公众号 → 账号）
- ScanHandler实现扫码事件处理逻辑（位于 `yudao-module-mp`）
- 消息处理器位于 `yudao-module-mp/src/main/java/.../handler/`

### 7. 社交登录集成
- 参考 `yudao-framework/yudao-social-core` 模块
- 配置第三方应用ID/Secret（管理后台：系统 → 应用管理）
- 用户绑定逻辑在 `yudao-module-system` 的 `SocialUserService`

## 重要配置说明

### 应用配置 (application.yml)
- `server.port`: 默认8080
- `spring.datasource`: 数据库连接
- `spring.redis`: Redis连接
- `dubbo.protocol.port`: Dubbo端口，默认20881
- `yudao.security`: 安全配置（JWT、Token过期时间）
- `yudao.tenant`: 多租户配置（是否开启）

### 分布式配置
项目使用微服务架构可选组件，当前为单体打包模式，RPC通过`dubbo.protocol`配置直连端口。

## 调试和故障排查

### 查看日志
- 控制台日志直接输出
- 日志级别配置：`logback-spring.xml`
- 生产环境建议输出到文件

### 常见问题

1. **Dubbo服务未找到**
   - 检查模块是否实现了对应接口并加上了 `@Service`
   - 检查依赖关系，API模块是否正确引入

2. **多租户数据隔离失效**
   - 确保DAO层继承 `TenantBaseDO`
   - 查询时使用 `LambdaQueryWrapperX` 自动注入租户ID

3. **权限错误**
   - 检查是否添加了 `@PreAuthorized` 注解
   - 用户角色是否分配了对应菜单权限

4. **数据库字段不存在**
   - DO类字段与数据库表字段需对应（使用驼峰转下划线）
   - 运行SQL初始化脚本

## 测试策略

- **单元测试**: 位于各模块 `src/test/java`，使用JUnit 5 + Mockito
- **集成测试**: 位于各模块 `src/test-integration`，需要完整环境
- **API测试**: 使用Swagger UI (http://localhost:8080/doc.html)

## 目录命名约定

- `*Api.java`: Service接口（暴露给其他模块调用）
- `*ServiceImpl.java`: Service实现
- `*DO.java`: 数据库实体对象
- `*Mapper.java`: MyBatis Mapper接口
- `*Controller.java`: Web控制器
- `*Convert.java`: DTO/DO转换器
- `*ReqVO.java`: 请求VO
- `*RespVO.java`: 响应VO

## Git工作流

- `master` 分支: 稳定版本
- 功能分支命名: `feature/xxx` 或 `feat/xxx`
- 修复分支命名: `fix/xxx` 或 `hotfix/xxx`

提交规范遵循Conventional Commits：
- `feat:` 新功能
- `fix:` 修复bug
- `docs:` 文档
- `refactor:` 重构
- `test:` 测试
- `chore:` 构建或工具

## 外部依赖

### 必须的（编译期）
- Maven Central: Spring Boot, MyBatis Plus, Dubbo
- 私有仓库: Huawei Cloud / Aliyun Maven镜像（配置在pom.xml）

### 运行时的可选依赖
- 微信SDK: `me.chanjar.weixin:weixin-java-mp`
- 支付宝/微信支付: `com.alipay.sdk:alipay-sdk-java` 等
- 云存储: MinIO / 阿里云OSS / 腾讯云COS
- 短信: 阿里云/腾讯云SDK

## 扩展阅读

- 官方文档: https://doc.iocoder.cn/
- 视频教程: https://doc.iocoder.cn/video/
- 前端仓库: https://github.com/yudaocode/yudao-ui-admin-vue3
- 问题反馈: https://github.com/YunaiV/ruoyi-vue-pro/issues

## Claude Code 特别说明

当你需要在这个项目中工作时：

1. **阅读代码优先**: 先使用`Read`查看相关文件，理解现有架构
2. **遵循现有模式**: 新代码应尽量模仿现有模块的结构和风格
3. **注意多模块依赖**: 修改API会影响所有调用方，Biz实现要保证线程安全
4. **尊重TenantBaseDO**: 新增表考虑多租户隔离
5. **保持向后兼容**: API变更应遵循语义化版本，避免破坏性修改
6. **测试覆盖**: 重要业务逻辑建议添加单元测试
7. **非侵入式**: 第三方调用使用RPC引用，避免直接依赖

---

**最后更新**: 2025-04-02
维护者: Minson
