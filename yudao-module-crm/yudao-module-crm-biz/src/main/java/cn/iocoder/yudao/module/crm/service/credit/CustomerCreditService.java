package cn.iocoder.yudao.module.crm.service.credit;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.crm.controller.admin.credit.vo.*;
import cn.iocoder.yudao.module.crm.dal.dataobject.credit.CustomerCreditDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 客户征信信息 Service 接口
 *
 * @author admin
 */
public interface CustomerCreditService {

    /**
     * 创建客户征信信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCustomerCredit(@Valid CustomerCreditCreateReqVO createReqVO);

    /**
     * 更新客户征信信息
     *
     * @param updateReqVO 更新信息
     */
    void updateCustomerCredit(@Valid CustomerCreditUpdateReqVO updateReqVO);

    /**
     * 删除客户征信信息
     *
     * @param id 编号
     */
    void deleteCustomerCredit(Long id);

    /**
     * 获得客户征信信息
     *
     * @param id 编号
     * @return 客户征信信息
     */
    CustomerCreditDO getCustomerCredit(Long id);

    /**
     * 获得客户征信信息列表
     *
     * @param ids 编号
     * @return 客户征信信息列表
     */
    List<CustomerCreditDO> getCustomerCreditList(Collection<Long> ids);

    /**
     * 获得客户征信信息分页
     *
     * @param pageReqVO 分页查询
     * @return 客户征信信息分页
     */
    PageResult<CustomerCreditDO> getCustomerCreditPage(CustomerCreditPageReqVO pageReqVO);

    /**
     * 获得客户征信信息列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 客户征信信息列表
     */
    List<CustomerCreditDO> getCustomerCreditList(CustomerCreditExportReqVO exportReqVO);

}
