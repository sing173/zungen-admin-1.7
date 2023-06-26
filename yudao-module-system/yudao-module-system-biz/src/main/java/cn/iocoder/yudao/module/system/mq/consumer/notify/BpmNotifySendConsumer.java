package cn.iocoder.yudao.module.system.mq.consumer.notify;

import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.mq.core.stream.AbstractStreamMessageListener;
import cn.iocoder.yudao.module.bpm.mq.message.BpmTaskNotifyMessage;
import cn.iocoder.yudao.module.system.service.notify.NotifySendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 针对 {@link BpmTaskNotifyMessage} 的消费者
 * 发送工单系统任务的消息(如抢单任务创建，提示员工抢单）
 *
 */
@Component
@Slf4j
public class BpmNotifySendConsumer extends AbstractStreamMessageListener<BpmTaskNotifyMessage> {

    @Resource
    private NotifySendService notifySendService;

    @Override
    public void onMessage(BpmTaskNotifyMessage message) {
        log.info("[onMessage][消息内容({})]", message);
        //循环站内信发送给每一位人员

        message.getUserIds().forEach(userId -> {
            notifySendService.sendSingleNotifyToAdmin(userId,
                    message.getTemplateCode(), MapUtils.convertMap(message.getTemplateParams()));
        });

    }
}
