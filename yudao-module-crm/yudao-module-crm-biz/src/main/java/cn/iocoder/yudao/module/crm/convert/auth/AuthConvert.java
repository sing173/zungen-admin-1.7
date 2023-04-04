package cn.iocoder.yudao.module.crm.convert.auth;

import cn.iocoder.yudao.module.crm.controller.admin.customer.vo.CrmCustomerCreateReqVO;
import cn.iocoder.yudao.module.crm.controller.admin.customer.vo.CrmSmsSendReqVO;
import cn.iocoder.yudao.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import cn.iocoder.yudao.module.system.api.sms.dto.code.SmsCodeUseReqDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthConvert {
    AuthConvert INSTANCE = Mappers.getMapper(AuthConvert.class);

    SmsCodeSendReqDTO convert(CrmSmsSendReqVO reqVO);

    SmsCodeUseReqDTO convert(CrmCustomerCreateReqVO createReqVO, Integer scene, String userIp);
}
