package cn.iocoder.yudao.module.bpm.convert.crm;

import cn.iocoder.yudao.module.bpm.mq.message.BpmTaskMqttMessage;
import cn.iocoder.yudao.module.bpm.mq.message.BpmTaskNotifyMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BpmTaskMessageConvert {
    BpmTaskMessageConvert INSTANCE = Mappers.getMapper(BpmTaskMessageConvert.class);

    BpmTaskMqttMessage convert(BpmTaskNotifyMessage bean);
}
