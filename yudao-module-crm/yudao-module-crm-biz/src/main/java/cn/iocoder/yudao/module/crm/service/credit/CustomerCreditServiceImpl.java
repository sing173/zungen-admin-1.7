package cn.iocoder.yudao.module.crm.service.credit;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.crm.controller.admin.credit.vo.*;
import cn.iocoder.yudao.module.crm.dal.dataobject.credit.CustomerCreditDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.crm.convert.credit.CustomerCreditConvert;
import cn.iocoder.yudao.module.crm.dal.mysql.credit.CustomerCreditMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.crm.enums.ErrorCodeConstants.*;

/**
 * 客户征信信息 Service 实现类
 *
 * @author admin
 */
@Service
@Validated
public class CustomerCreditServiceImpl implements CustomerCreditService {

    @Resource
    private CustomerCreditMapper customerCreditMapper;

    @Override
    public Long createCustomerCredit(CustomerCreditCreateReqVO createReqVO) {
        // 插入
        CustomerCreditDO customerCredit = CustomerCreditConvert.INSTANCE.convert(createReqVO);
        customerCreditMapper.insert(customerCredit);
        // 返回
        return customerCredit.getId();
    }

    @Override
    public void updateCustomerCredit(CustomerCreditUpdateReqVO updateReqVO) {
        // 校验存在
        validateCustomerCreditExists(updateReqVO.getId());
        // 更新
        CustomerCreditDO updateObj = CustomerCreditConvert.INSTANCE.convert(updateReqVO);
        customerCreditMapper.updateById(updateObj);
    }

    @Override
    public void deleteCustomerCredit(Long id) {
        // 校验存在
        validateCustomerCreditExists(id);
        // 删除
        customerCreditMapper.deleteById(id);
    }

    private void validateCustomerCreditExists(Long id) {
        if (customerCreditMapper.selectById(id) == null) {
            throw exception(CUSTOMER_NOT_EXISTS);
        }
    }

    @Override
    public CustomerCreditDO getCustomerCredit(Long id) {
        return customerCreditMapper.selectById(id);
    }

    @Override
    public List<CustomerCreditDO> getCustomerCreditList(Collection<Long> ids) {
        return customerCreditMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<CustomerCreditDO> getCustomerCreditPage(CustomerCreditPageReqVO pageReqVO) {
        return customerCreditMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CustomerCreditDO> getCustomerCreditList(CustomerCreditExportReqVO exportReqVO) {
        return customerCreditMapper.selectList(exportReqVO);
    }

}
