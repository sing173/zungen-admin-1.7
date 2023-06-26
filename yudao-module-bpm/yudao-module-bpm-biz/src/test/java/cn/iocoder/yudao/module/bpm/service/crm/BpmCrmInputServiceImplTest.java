package cn.iocoder.yudao.module.bpm.service.crm;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.bpm.controller.admin.crm.vo.*;
import cn.iocoder.yudao.module.bpm.dal.dataobject.crm.BpmCrmInputDO;
import cn.iocoder.yudao.module.bpm.dal.mysql.crm.BpmCrmInputMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;
import java.util.*;

import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.bpm.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
* {@link BpmCrmInputServiceImpl} 的单元测试类
*
* @author admin
*/
@Import(BpmCrmInputServiceImpl.class)
public class BpmCrmInputServiceImplTest extends BaseDbUnitTest {

    @Resource
    private BpmCrmInputServiceImpl crmInputService;

    @Resource
    private BpmCrmInputMapper crmInputMapper;

    @Test
    public void testCreateCrmInput_success() {
        // 准备参数
        BpmCrmInputCreateReqVO reqVO = randomPojo(BpmCrmInputCreateReqVO.class);

        // 调用
        Long crmInputId = crmInputService.createCrmInput(1L, reqVO);
        // 断言
        assertNotNull(crmInputId);
        // 校验记录的属性是否正确
        BpmCrmInputDO crmInput = crmInputMapper.selectById(crmInputId);
        assertPojoEquals(reqVO, crmInput);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCrmInputPage() {
       // mock 数据
       BpmCrmInputDO dbCrmInput = randomPojo(BpmCrmInputDO.class, o -> { // 等会查询到
           o.setOrderNo(null);
           o.setCrmCustomerId(null);
           o.setCrmCustomerImageId(null);
           o.setCrmProductId(null);
           o.setProcessInstanceId(null);
           o.setStatus(null);
           o.setAuditTime(null);
           o.setAuditType(null);
           o.setAuditRemark(null);
           o.setCreateTime(null);
       });
       crmInputMapper.insert(dbCrmInput);
       // 测试 orderNo 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setOrderNo(null)));
       // 测试 crmCustomerId 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setCrmCustomerId(null)));
       // 测试 crmCustomerImageId 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setCrmCustomerImageId(null)));
       // 测试 crmProductId 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setCrmProductId(null)));
       // 测试 processInstanceId 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setProcessInstanceId(null)));
       // 测试 status 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setStatus(null)));
       // 测试 auditTime 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setAuditTime(null)));
       // 测试 auditType 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setAuditType(null)));
       // 测试 auditRemark 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setAuditRemark(null)));
       // 测试 createTime 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setCreateTime(null)));
       // 准备参数
       BpmCrmInputPageReqVO reqVO = new BpmCrmInputPageReqVO();
       reqVO.setOrderNo(null);
       reqVO.setCrmProductId(null);
       reqVO.setStatus(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<BpmCrmInputDO> pageResult = crmInputService.getCrmInputPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbCrmInput, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCrmInputList() {
       // mock 数据
       BpmCrmInputDO dbCrmInput = randomPojo(BpmCrmInputDO.class, o -> { // 等会查询到
           o.setOrderNo(null);
           o.setCrmCustomerId(null);
           o.setCrmCustomerImageId(null);
           o.setCrmProductId(null);
           o.setProcessInstanceId(null);
           o.setStatus(null);
           o.setAuditTime(null);
           o.setAuditType(null);
           o.setAuditRemark(null);
           o.setCreateTime(null);
       });
       crmInputMapper.insert(dbCrmInput);
       // 测试 orderNo 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setOrderNo(null)));
       // 测试 crmCustomerId 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setCrmCustomerId(null)));
       // 测试 crmCustomerImageId 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setCrmCustomerImageId(null)));
       // 测试 crmProductId 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setCrmProductId(null)));
       // 测试 processInstanceId 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setProcessInstanceId(null)));
       // 测试 status 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setStatus(null)));
       // 测试 auditTime 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setAuditTime(null)));
       // 测试 auditType 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setAuditType(null)));
       // 测试 auditRemark 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setAuditRemark(null)));
       // 测试 createTime 不匹配
       crmInputMapper.insert(cloneIgnoreId(dbCrmInput, o -> o.setCreateTime(null)));
       // 准备参数
       BpmCrmInputExportReqVO reqVO = new BpmCrmInputExportReqVO();
       reqVO.setOrderNo(null);
       reqVO.setCrmCustomerId(null);
       reqVO.setCrmCustomerImageId(null);
       reqVO.setCrmProductId(null);
       reqVO.setProcessInstanceId(null);
       reqVO.setStatus(null);
       reqVO.setAuditTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setAuditType(null);
       reqVO.setAuditRemark(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       List<BpmCrmInputDO> list = crmInputService.getCrmInputList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbCrmInput, list.get(0));
    }

}
