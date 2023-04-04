package cn.iocoder.yudao.module.crm.convert.customer;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.crm.controller.admin.customer.vo.*;
import cn.iocoder.yudao.module.crm.dal.dataobject.customer.CrmCustomerDO;

/**
 * 客户信息 Convert
 *
 * @author admin
 */
@Mapper
public interface CrmCustomerConvert {

    CrmCustomerConvert INSTANCE = Mappers.getMapper(CrmCustomerConvert.class);

    CrmCustomerDO convert(CrmCustomerCreateReqVO bean);

    CrmCustomerDO convert(CrmCustomerUpdateReqVO bean);

    CrmCustomerRespVO convert(CrmCustomerDO bean);

    List<CrmCustomerRespVO> convertList(List<CrmCustomerDO> list);

    PageResult<CrmCustomerRespVO> convertPage(PageResult<CrmCustomerDO> page);

    List<CrmCustomerExcelVO> convertList02(List<CrmCustomerDO> list);

}
