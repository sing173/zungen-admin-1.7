package cn.iocoder.yudao.module.crm.service.customer;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.crm.controller.admin.customer.vo.*;
import cn.iocoder.yudao.module.crm.dal.dataobject.customer.CrmCustomerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 客户信息 Service 接口
 *
 * @author admin
 */
public interface CrmCustomerService {

    /**
     * 创建客户信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCustomer(@Valid CrmCustomerCreateReqVO createReqVO);

    /**
     * 更新客户信息
     *
     * @param updateReqVO 更新信息
     */
    void updateCustomer(@Valid CrmCustomerUpdateReqVO updateReqVO);

    /**
     * 删除客户信息
     *
     * @param id 编号
     */
    void deleteCustomer(Long id);

    /**
     * 获得客户信息
     *
     * @param id 编号
     * @return 客户信息
     */
    CrmCustomerDO getCustomer(Long id);

    /**
     * 获得客户信息列表
     *
     * @param ids 编号
     * @return 客户信息列表
     */
    List<CrmCustomerDO> getCustomerList(Collection<Long> ids);

    /**
     * 获得客户信息分页
     *
     * @param pageReqVO 分页查询
     * @return 客户信息分页
     */
    PageResult<CrmCustomerDO> getCustomerPage(CrmCustomerPageReqVO pageReqVO);

    /**
     * 获得客户信息列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 客户信息列表
     */
    List<CrmCustomerDO> getCustomerList(CrmCustomerExportReqVO exportReqVO);

}
