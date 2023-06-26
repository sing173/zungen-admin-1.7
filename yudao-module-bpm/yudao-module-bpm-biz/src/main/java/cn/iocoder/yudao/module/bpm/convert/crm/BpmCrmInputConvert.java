package cn.iocoder.yudao.module.bpm.convert.crm;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.bpm.controller.admin.crm.vo.*;
import cn.iocoder.yudao.module.bpm.dal.dataobject.crm.BpmCrmInputDO;

/**
 * 进件工单 Convert
 *
 * @author admin
 */
@Mapper
public interface BpmCrmInputConvert {

    BpmCrmInputConvert INSTANCE = Mappers.getMapper(BpmCrmInputConvert.class);

    BpmCrmInputDO convert(BpmCrmInputCreateReqVO bean);

    BpmCrmInputRespVO convert(BpmCrmInputDO bean);

    List<BpmCrmInputRespVO> convertList(List<BpmCrmInputDO> list);

    PageResult<BpmCrmInputRespVO> convertPage(PageResult<BpmCrmInputDO> page);

    List<BpmCrmInputExcelVO> convertList02(List<BpmCrmInputDO> list);
}
