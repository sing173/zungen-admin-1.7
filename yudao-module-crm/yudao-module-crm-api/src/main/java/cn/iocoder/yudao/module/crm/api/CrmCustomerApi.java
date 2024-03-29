package cn.iocoder.yudao.module.crm.api;

import cn.iocoder.yudao.module.crm.api.dto.CrmCustomerCreditDTO;
import cn.iocoder.yudao.module.crm.api.dto.CrmCustomerDTO;

/**
 * 客户关系模块 - 客户信息API
 */
public interface CrmCustomerApi {
    Boolean validateCustomerExists(Long id);

    /**
     * 获得客户信息
     *
     * @param id 编号
     * @return 客户信息
     */
    CrmCustomerDTO getCustomer(Long id);

    /**
     * 获得客户征信信息
     * @param id
     * @return
     */
    CrmCustomerCreditDTO getCustomerCredit(Long id);

    /**
     * 创建客户信用信息
     * @param customerCreditDTO
     * @return
     */
    Long createCustomerCredit(CrmCustomerCreditDTO customerCreditDTO);

    /**
     * 更新客户信用信息
     * @param customerCreditDTO
     */
    void updateCustomerCredit(CrmCustomerCreditDTO customerCreditDTO);
}
