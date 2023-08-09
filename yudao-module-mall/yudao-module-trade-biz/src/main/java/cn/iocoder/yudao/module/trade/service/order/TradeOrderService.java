package cn.iocoder.yudao.module.trade.service.order;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.trade.controller.admin.order.vo.TradeOrderDeliveryReqVO;
import cn.iocoder.yudao.module.trade.controller.admin.order.vo.TradeOrderPageReqVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderCreateReqVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderPageReqVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderSettlementReqVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderSettlementRespVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.item.AppTradeOrderItemCommentCreateReqVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.singleton;

/**
 * 交易订单 Service 接口
 *
 * @author LeeYan9
 * @since 2022-08-26
 */
public interface TradeOrderService {

    // =================== Order ===================

    /**
     * 获得订单结算信息
     *
     * @param userId 登录用户
     * @param settlementReqVO 订单结算请求
     * @return 订单结算结果
     */
    AppTradeOrderSettlementRespVO settlementOrder(Long userId, AppTradeOrderSettlementReqVO settlementReqVO);

    /**
     * 【会员】创建交易订单
     *
     * @param userId 登录用户
     * @param userIp 用户 IP 地址
     * @param createReqVO 创建交易订单请求模型
     * @return 交易订单的
     */
    TradeOrderDO createOrder(Long userId, String userIp, AppTradeOrderCreateReqVO createReqVO);

    /**
     * 更新交易订单已支付
     *
     * @param id 交易订单编号
     * @param payOrderId 支付订单编号
     */
    void updateOrderPaid(Long id, Long payOrderId);

    /**
     * 【管理员】发货交易订单
     *
     * @param userId 管理员编号
     * @param deliveryReqVO 发货请求
     */
    void deliveryOrder(Long userId, TradeOrderDeliveryReqVO deliveryReqVO);

    /**
     * 【会员】收货交易订单
     *
     * @param userId 用户编号
     * @param id 订单编号
     */
    void receiveOrder(Long userId, Long id);

    /**
     * 获得指定编号的交易订单
     *
     * @param id 交易订单编号
     * @return 交易订单
     */
    TradeOrderDO getOrder(Long id);

    /**
     * 获得指定用户，指定的交易订单
     *
     * @param userId 用户编号
     * @param id 交易订单编号
     * @return 交易订单
     */
    TradeOrderDO getOrder(Long userId, Long id);

    /**
     * 【管理员】获得交易订单分页
     *
     * @param reqVO 分页请求
     * @return 交易订单
     */
    PageResult<TradeOrderDO> getOrderPage(TradeOrderPageReqVO reqVO);

    /**
     * 【会员】获得交易订单分页
     *
     * @param userId 用户编号
     * @param reqVO 分页请求
     * @return 交易订单
     */
    PageResult<TradeOrderDO> getOrderPage(Long userId, AppTradeOrderPageReqVO reqVO);

    /**
     * 【会员】获得交易订单数量
     *
     * @param userId       用户编号
     * @param status       订单状态。如果为空，则不进行筛选
     * @param commonStatus 评价状态。如果为空，则不进行筛选
     * @return 订单数量
     */
    Long getOrderCount(Long userId, Integer status, Boolean commonStatus);

    // =================== Order Item ===================

    /**
     * 获得指定用户，指定的交易订单项
     *
     * @param userId 用户编号
     * @param itemId 交易订单项编号
     * @return 交易订单项
     */
    TradeOrderItemDO getOrderItem(Long userId, Long itemId);

    /**
     * 更新交易订单项的售后状态
     *
     * @param id 交易订单项编号
     * @param oldAfterSaleStatus 当前售后状态；如果不符，更新后会抛出异常
     * @param newAfterSaleStatus 目标售后状态
     * @param refundPrice 退款金额；当订单项退款成功时，必须传递该值
     */
    void updateOrderItemAfterSaleStatus(Long id, Integer oldAfterSaleStatus,
                                        Integer newAfterSaleStatus, Integer refundPrice);

    /**
     * 根据交易订单项编号数组，查询交易订单项
     *
     * @param ids 交易订单项编号数组
     * @return 交易订单项数组
     */
    List<TradeOrderItemDO> getOrderItemList(Collection<Long> ids);

    /**
     * 根据交易订单编号，查询交易订单项
     *
     * @param orderId 交易订单编号
     * @return 交易订单项数组
     */
    default List<TradeOrderItemDO> getOrderItemListByOrderId(Long orderId) {
        return getOrderItemListByOrderId(singleton(orderId));
    }

    /**
     * 根据交易订单编号数组，查询交易订单项
     *
     * @param orderIds 交易订单编号数组
     * @return 交易订单项数组
     */
    List<TradeOrderItemDO> getOrderItemListByOrderId(Collection<Long> orderIds);

    /**
     * 得到订单项通过 订单项 id 和用户 id
     *
     * @param orderItemId 订单项 id
     * @param loginUserId 登录用户 id
     * @return 得到订单项
     */
    TradeOrderItemDO getOrderItemByIdAndUserId(Long orderItemId, Long loginUserId);

    /**
     * 得到订单通过 id 和 用户 id
     *
     * @param orderId     订单 id
     * @param loginUserId 登录用户 id
     * @return 得到订单
     */
    TradeOrderDO getOrderByIdAndUserId(Long orderId, Long loginUserId);

    /**
     * 创建订单项的评论
     *
     * @param createReqVO 创建请求
     * @return 得到评价 id
     */
    Long createOrderItemComment(AppTradeOrderItemCommentCreateReqVO createReqVO);

}
