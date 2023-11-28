package cn.iocoder.yudao.module.crm.api.customer;

import cn.iocoder.yudao.module.crm.api.CrmCustomerApi;
import cn.iocoder.yudao.module.crm.api.dto.CrmCustomerCreditDTO;
import cn.iocoder.yudao.module.crm.api.dto.CrmCustomerDTO;
import cn.iocoder.yudao.module.crm.controller.admin.credit.vo.CustomerCreditCreateReqVO;
import cn.iocoder.yudao.module.crm.controller.admin.credit.vo.CustomerCreditUpdateReqVO;
import cn.iocoder.yudao.module.crm.convert.credit.CustomerCreditConvert;
import cn.iocoder.yudao.module.crm.convert.customer.CrmCustomerConvert;
import cn.iocoder.yudao.module.crm.dal.dataobject.credit.CustomerCreditDO;
import cn.iocoder.yudao.module.crm.dal.dataobject.customer.CrmCustomerDO;
import cn.iocoder.yudao.module.crm.service.credit.CustomerCreditService;
import cn.iocoder.yudao.module.crm.service.customer.CrmCustomerService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.crm.enums.ErrorCodeConstants.CUSTOMER_NOT_EXISTS;

/**
 * 客户关系模块 - 客户信息API
 */
@Service
@Validated
public class CrmCustomerApiImpl implements CrmCustomerApi {

    @Resource
    CrmCustomerService customerService;

    @Resource
    CustomerCreditService customerCreditService;

    public Boolean validateCustomerExists(Long id) {
        return customerService.getCustomer(id) != null;

    }

    @Override
    public CrmCustomerDTO getCustomer(Long id) {
        CrmCustomerDO customerDO = customerService.getCustomer(id);
        if(customerDO == null) {
            throw exception(CUSTOMER_NOT_EXISTS);
        }

        return CrmCustomerConvert.INSTANCE.convert2(customerDO);
    }

    @Override
    public CrmCustomerCreditDTO getCustomerCredit(Long id) {
        CustomerCreditDO customerCreditDO = customerCreditService.getCustomerCredit(id);
        if(customerCreditDO == null) {
            throw exception(CUSTOMER_NOT_EXISTS);
        }

        return CustomerCreditConvert.INSTANCE.convert2(customerCreditDO);
    }

    @Override
    public Long createCustomerCredit(CrmCustomerCreditDTO customerCreditDTO) {
        CustomerCreditCreateReqVO createReqVO = CustomerCreditConvert.INSTANCE.convert(customerCreditDTO);
        return customerCreditService.createCustomerCredit(createReqVO);
    }

    @Override
    public void updateCustomerCredit(CrmCustomerCreditDTO customerCreditDTO) {
        CustomerCreditUpdateReqVO updateReqVO = CustomerCreditConvert.INSTANCE.convert2(customerCreditDTO);
        customerCreditService.updateCustomerCredit(updateReqVO);
    }
}
