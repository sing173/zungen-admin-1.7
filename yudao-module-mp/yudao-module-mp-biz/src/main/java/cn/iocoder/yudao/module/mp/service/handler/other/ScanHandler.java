package cn.iocoder.yudao.module.mp.service.handler.other;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.trade.api.brokerage.BrokerageApi;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 扫码的事件处理器 - 分销功能
 *
 * EventKey 格式：brokerage_{distributorId}_{sceneId?}
 */
@Component
@Slf4j
public class ScanHandler implements WxMpMessageHandler {

    @Resource
    private BrokerageApi brokerageApi;

    @Resource
    private MemberUserApi memberUserApi;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> context,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        String eventKey = wxMpXmlMessage.getEventKey();
        String fromUser = wxMpXmlMessage.getFromUser();
        String toUser = wxMpXmlMessage.getToUser();

        log.info("微信公众号扫码事件：EventKey={}, FromUser={}", eventKey, fromUser);

        // 处理分销扫码事件
        if (eventKey != null && eventKey.startsWith("brokerage_")) {
            try {
                handleBrokerageScan(eventKey, fromUser);
            } catch (Exception e) {
                log.error("处理分销扫码事件异常", e);
                // 继续抛出，让框架处理异常（避免影响其他处理）
                throw new WxErrorException("处理分销扫码异常", e);
            }
        }

        // 返回空响应，不主动回复（通过客服消息后续通知）
        return null;
    }

    /**
     * 处理分销扫码逻辑
     *
     * @param eventKey 事件Key，格式：brokerage_{distributorId}_{sceneId?}
     * @param openId   扫码用户OpenID
     */
    private void handleBrokerageScan(String eventKey, String openId) {
        // 1. 解析参数
        String[] parts = eventKey.split("_");
        if (parts.length < 2) {
            log.warn("无效的分销扫码EventKey格式：{}", eventKey);
            return;
        }

        Long distributorId = Long.valueOf(parts[1]);
        Long sceneId = parts.length > 2 ? Long.valueOf(parts[2]) : null;

        log.info("解析分销扫码参数：distributorId={}, sceneId={}", distributorId, sceneId);

        // 2. 通过OpenID查询会员用户
        MemberUserRespDTO user = memberUserApi.getByOpenId(openId);
        if (user == null) {
            log.info("扫码用户未注册会员，openId={}", openId);
            // TODO：此时应引导用户注册/授权，绑定openId
            // 实现逻辑：发送客服消息，提供注册链接
            return;
        }

        Long userId = user.getId();

        // 3. 检查是否已绑定分销关系
        // TODO：通过BrokerageApi.getBrokerageUser(userId)检查bindUserId
        // 如果已绑定，根据配置决定是否更新

        // 4. 调用绑定API
        CommonResult<Boolean> result = brokerageApi.bindByScan(distributorId, userId, sceneId);
        if (!result.isSuccess() || result.getData() == null || !result.getData()) {
            log.warn("绑定分销关系失败：distributorId={}, userId={}, errorMsg={}",
                    distributorId, userId, result.getMsg());
            return;
        }

        log.info("绑定分销关系成功：distributorId={}, userId={}", distributorId, userId);

        // 5. TODO：发送绑定成功通知（模板消息）
    }

}
