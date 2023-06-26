package cn.iocoder.yudao.module.system.api.notify;

import cn.iocoder.yudao.module.system.dal.dataobject.notify.NotifyTemplateDO;
import cn.iocoder.yudao.module.system.service.notify.NotifyTemplateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class NotifyTemplateApiImpl implements NotifyTemplateApi{
    @Resource
    private NotifyTemplateService notifyTemplateService;

    @Override
    public String formatNotifyTemplateContent(String templateCode, Map<String, Object> params) {
        NotifyTemplateDO notifyTemplateDO = notifyTemplateService.getNotifyTemplateByCodeFromCache(templateCode);

        return notifyTemplateService.formatNotifyTemplateContent(notifyTemplateDO.getContent(), params);
    }
}
