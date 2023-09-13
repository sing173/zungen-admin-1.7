package cn.iocoder.yudao.module.trade.service.brokerage.user;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.trade.controller.admin.brokerage.user.vo.BrokerageUserPageReqVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.brokerage.user.BrokerageUserDO;

import java.util.Collection;
import java.util.List;

/**
 * 分销用户 Service 接口
 *
 * @author owen
 */
public interface BrokerageUserService {

    /**
     * 获得分销用户
     *
     * @param id 编号
     * @return 分销用户
     */
    BrokerageUserDO getBrokerageUser(Long id);

    /**
     * 获得分销用户列表
     *
     * @param ids 编号
     * @return 分销用户列表
     */
    List<BrokerageUserDO> getBrokerageUserList(Collection<Long> ids);

    /**
     * 获得分销用户分页
     *
     * @param pageReqVO 分页查询
     * @return 分销用户分页
     */
    PageResult<BrokerageUserDO> getBrokerageUserPage(BrokerageUserPageReqVO pageReqVO);

    /**
     * 修改推广员编号
     *
     * @param id         用户编号
     * @param bindUserId 推广员编号
     */
    void updateBrokerageUserId(Long id, Long bindUserId);

    /**
     * 修改推广资格
     *
     * @param id      用户编号
     * @param enabled 推广资格
     */
    void updateBrokerageUserEnabled(Long id, Boolean enabled);

    /**
     * 获得用户的推广人
     *
     * @param id 用户编号
     * @return 用户的推广人
     */
    BrokerageUserDO getBindBrokerageUser(Long id);

    /**
     * 更新用户佣金
     *
     * @param id    用户编号
     * @param price 用户可用佣金
     */
    void updateUserPrice(Long id, Integer price);

    /**
     * 更新用户冻结佣金
     *
     * @param id          用户编号
     * @param frozenPrice 用户冻结佣金
     */
    void updateUserFrozenPrice(Long id, Integer frozenPrice);

    /**
     * 更新用户冻结佣金（减少），更新用户佣金（增加）
     *
     * @param id          用户编号
     * @param frozenPrice 减少冻结佣金（负数）
     */
    void updateFrozenPriceDecrAndPriceIncr(Long id, Integer frozenPrice);

    // TODO @疯狂：这个后面可能要支持下，二级
    /**
     * 获得推广用户数量（一级）
     *
     * @param bindUserId 绑定的推广员编号
     * @return 推广用户数量
     */
    Long getBrokerageUserCountByBindUserId(Long bindUserId);

    /**
     * 【会员】绑定推广员
     *
     * @param userId     用户编号
     * @param bindUserId 推广员编号
     * @param isNewUser  是否为新用户
     * @return 是否绑定
     */
    boolean bindBrokerageUser(Long userId, Long bindUserId, Boolean isNewUser);

}
