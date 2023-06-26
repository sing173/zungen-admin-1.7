package cn.iocoder.yudao.module.crm.api.customer;

import cn.iocoder.yudao.module.crm.api.CrmCustomerApi;
import cn.iocoder.yudao.module.crm.api.dto.CrmCustomerDTO;
import cn.iocoder.yudao.module.crm.convert.customer.CrmCustomerConvert;
import cn.iocoder.yudao.module.crm.dal.dataobject.customer.CrmCustomerDO;
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


}
