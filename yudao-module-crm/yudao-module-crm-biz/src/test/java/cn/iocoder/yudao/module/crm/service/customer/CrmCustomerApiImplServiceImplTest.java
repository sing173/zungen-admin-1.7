package cn.iocoder.yudao.module.crm.service.customer;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.crm.controller.admin.customer.vo.*;
import cn.iocoder.yudao.module.crm.dal.dataobject.customer.CrmCustomerDO;
import cn.iocoder.yudao.module.crm.dal.mysql.customer.CrmCustomerMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.crm.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
* {@link CrmCustomerServiceImpl} 的单元测试类
*
* @author admin
*/
@Import(CrmCustomerServiceImpl.class)
public class CrmCustomerApiImplServiceImplTest extends BaseDbUnitTest {

    @Resource
    private CrmCustomerServiceImpl customerService;

    @Resource
    private CrmCustomerMapper customerMapper;

    @Test
    public void testCreateCustomer_success() {
        // 准备参数
        CrmCustomerCreateReqVO reqVO = randomPojo(CrmCustomerCreateReqVO.class);

        // 调用
        Long customerId = customerService.createCustomer(reqVO);
        // 断言
        assertNotNull(customerId);
        // 校验记录的属性是否正确
        CrmCustomerDO customer = customerMapper.selectById(customerId);
        assertPojoEquals(reqVO, customer);
    }

    @Test
    public void testUpdateCustomer_success() {
        // mock 数据
        CrmCustomerDO dbCustomer = randomPojo(CrmCustomerDO.class);
        customerMapper.insert(dbCustomer);// @Sql: 先插入出一条存在的数据
        // 准备参数
        CrmCustomerUpdateReqVO reqVO = randomPojo(CrmCustomerUpdateReqVO.class, o -> {
            o.setId(dbCustomer.getId()); // 设置更新的 ID
        });

        // 调用
        customerService.updateCustomer(reqVO);
        // 校验是否更新正确
        CrmCustomerDO customer = customerMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, customer);
    }

    @Test
    public void testUpdateCustomer_notExists() {
        // 准备参数
        CrmCustomerUpdateReqVO reqVO = randomPojo(CrmCustomerUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> customerService.updateCustomer(reqVO), CUSTOMER_NOT_EXISTS);
    }

    @Test
    public void testDeleteCustomer_success() {
        // mock 数据
        CrmCustomerDO dbCustomer = randomPojo(CrmCustomerDO.class);
        customerMapper.insert(dbCustomer);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbCustomer.getId();

        // 调用
        customerService.deleteCustomer(id);
       // 校验数据不存在了
       assertNull(customerMapper.selectById(id));
    }

    @Test
    public void testDeleteCustomer_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> customerService.deleteCustomer(id), CUSTOMER_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCustomerPage() {
       // mock 数据
       CrmCustomerDO dbCustomer = randomPojo(CrmCustomerDO.class, o -> { // 等会查询到
           o.setUserId(null);
           o.setName(null);
           o.setMobile(null);
           o.setIdCard(null);
           o.setCreateTime(null);
       });
       customerMapper.insert(dbCustomer);
       // 测试 userId 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setUserId(null)));
       // 测试 name 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setName(null)));
       // 测试 mobile 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setMobile(null)));
       // 测试 idCard 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setIdCard(null)));
       // 测试 createTime 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setCreateTime(null)));
       // 准备参数
       CrmCustomerPageReqVO reqVO = new CrmCustomerPageReqVO();
       reqVO.setUserId(null);
       reqVO.setIdentityId(null);
       reqVO.setName(null);
       reqVO.setMobile(null);
       reqVO.setIdCard(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<CrmCustomerDO> pageResult = customerService.getCustomerPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbCustomer, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCustomerList() {
       // mock 数据
       CrmCustomerDO dbCustomer = randomPojo(CrmCustomerDO.class, o -> { // 等会查询到
           o.setUserId(null);
           o.setName(null);
           o.setMobile(null);
           o.setIdCard(null);
           o.setCreateTime(null);
       });
       customerMapper.insert(dbCustomer);
       // 测试 userId 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setUserId(null)));
       // 测试 name 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setName(null)));
       // 测试 mobile 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setMobile(null)));
       // 测试 idCard 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setIdCard(null)));
       // 测试 createTime 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setCreateTime(null)));
       // 准备参数
       CrmCustomerExportReqVO reqVO = new CrmCustomerExportReqVO();
       reqVO.setUserId(null);
       reqVO.setIdentityId(null);
       reqVO.setName(null);
       reqVO.setMobile(null);
       reqVO.setIdCard(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       List<CrmCustomerDO> list = customerService.getCustomerList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbCustomer, list.get(0));
    }

}
