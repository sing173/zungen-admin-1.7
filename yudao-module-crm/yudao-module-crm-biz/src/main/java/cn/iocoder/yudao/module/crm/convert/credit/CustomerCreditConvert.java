package cn.iocoder.yudao.module.crm.convert.credit;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.crm.api.dto.CrmCustomerCreditDTO;
import cn.iocoder.yudao.module.crm.controller.admin.credit.vo.CustomerCreditCreateReqVO;
import cn.iocoder.yudao.module.crm.controller.admin.credit.vo.CustomerCreditExcelVO;
import cn.iocoder.yudao.module.crm.controller.admin.credit.vo.CustomerCreditRespVO;
import cn.iocoder.yudao.module.crm.controller.admin.credit.vo.CustomerCreditUpdateReqVO;
import cn.iocoder.yudao.module.crm.dal.dataobject.credit.CustomerCreditDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 客户征信信息 Convert
 *
 * @author admin
 */
@Mapper
public interface CustomerCreditConvert {

    CustomerCreditConvert INSTANCE = Mappers.getMapper(CustomerCreditConvert.class);

    CustomerCreditDO convert(CustomerCreditCreateReqVO bean);

    CustomerCreditDO convert(CustomerCreditUpdateReqVO bean);

    CustomerCreditRespVO convert(CustomerCreditDO bean);

    List<CustomerCreditRespVO> convertList(List<CustomerCreditDO> list);

    PageResult<CustomerCreditRespVO> convertPage(PageResult<CustomerCreditDO> page);

    List<CustomerCreditExcelVO> convertList02(List<CustomerCreditDO> list);

    CrmCustomerCreditDTO convert2(CustomerCreditDO customerCreditDO);

    CustomerCreditCreateReqVO convert(CrmCustomerCreditDTO crmCustomerCreditDTO);

    CustomerCreditUpdateReqVO convert2(CrmCustomerCreditDTO crmCustomerCreditDTO);
}
