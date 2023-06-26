package cn.iocoder.yudao.module.system.api.notify;

import java.util.Map;

public interface NotifyTemplateApi {
    /**
     * 获取格式化站内信内容
     *
     * @param templateCode 站内信模板编号
     * @param params 站内信内容的参数
     * @return 格式化后的内容
     */
    String formatNotifyTemplateContent(String templateCode, Map<String, Object> params);
}
