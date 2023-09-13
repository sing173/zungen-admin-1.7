package cn.iocoder.yudao.module.trade.service.brokerage.user;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.BooleanUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.trade.controller.admin.brokerage.user.vo.BrokerageUserPageReqVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.brokerage.user.BrokerageUserDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.config.TradeConfigDO;
import cn.iocoder.yudao.module.trade.dal.mysql.brokerage.user.BrokerageUserMapper;
import cn.iocoder.yudao.module.trade.enums.brokerage.BrokerageBindModeEnum;
import cn.iocoder.yudao.module.trade.enums.brokerage.BrokerageEnabledConditionEnum;
import cn.iocoder.yudao.module.trade.service.config.TradeConfigService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.trade.enums.ErrorCodeConstants.*;

/**
 * 分销用户 Service 实现类
 *
 * @author owen
 */
@Service
@Validated
public class BrokerageUserServiceImpl implements BrokerageUserService {

    @Resource
    private BrokerageUserMapper brokerageUserMapper;

    @Resource
    private TradeConfigService tradeConfigService;

    @Override
    public BrokerageUserDO getBrokerageUser(Long id) {
        return brokerageUserMapper.selectById(id);
    }

    @Override
    public List<BrokerageUserDO> getBrokerageUserList(Collection<Long> ids) {
        return brokerageUserMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<BrokerageUserDO> getBrokerageUserPage(BrokerageUserPageReqVO pageReqVO) {
        return brokerageUserMapper.selectPage(pageReqVO);
    }

    @Override
    public void updateBrokerageUserId(Long id, Long bindUserId) {
        // 校验存在
        validateBrokerageUserExists(id);

        // 情况一：清除推广员
        if (bindUserId == null) {
            // 清除推广员
            brokerageUserMapper.updateBindUserIdAndBindUserTimeToNull(id);
            return;
        }

        // 情况二：修改推广员
        // TODO @疯狂：要复用一些 validateCanBindUser 的校验哈；
        brokerageUserMapper.updateById(new BrokerageUserDO().setId(id)
                .setBindUserId(bindUserId).setBindUserTime(LocalDateTime.now()));
    }

    @Override
    public void updateBrokerageUserEnabled(Long id, Boolean enabled) {
        // 校验存在
        validateBrokerageUserExists(id);
        if (BooleanUtil.isTrue(enabled)) {
            // 开通推广资格
            brokerageUserMapper.updateById(new BrokerageUserDO().setId(id)
                    .setBrokerageEnabled(true).setBrokerageTime(LocalDateTime.now()));
        } else {
            // 取消推广资格
            brokerageUserMapper.updateEnabledFalseAndBrokerageTimeToNull(id);
        }
    }

    private void validateBrokerageUserExists(Long id) {
        if (brokerageUserMapper.selectById(id) == null) {
            throw exception(BROKERAGE_USER_NOT_EXISTS);
        }
    }

    @Override
    public BrokerageUserDO getBindBrokerageUser(Long id) {
        return Optional.ofNullable(id)
                .map(this::getBrokerageUser)
                .map(BrokerageUserDO::getBindUserId)
                .map(this::getBrokerageUser)
                .orElse(null);
    }

    @Override
    public void updateUserPrice(Long id, Integer price) {
        if (price > 0) {
            brokerageUserMapper.updatePriceIncr(id, price);
        } else if (price < 0) {
            brokerageUserMapper.updatePriceDecr(id, price);
        }
    }

    @Override
    public void updateUserFrozenPrice(Long id, Integer frozenPrice) {
        if (frozenPrice > 0) {
            brokerageUserMapper.updateFrozenPriceIncr(id, frozenPrice);
        } else if (frozenPrice < 0) {
            brokerageUserMapper.updateFrozenPriceDecr(id, frozenPrice);
        }
    }

    @Override
    public void updateFrozenPriceDecrAndPriceIncr(Long id, Integer frozenPrice) {
        Assert.isTrue(frozenPrice < 0);
        int updateRows = brokerageUserMapper.updateFrozenPriceDecrAndPriceIncr(id, frozenPrice);
        if (updateRows == 0) {
            throw exception(BROKERAGE_USER_FROZEN_PRICE_NOT_ENOUGH);
        }
    }

    @Override
    public Long getBrokerageUserCountByBindUserId(Long bindUserId) {
        // TODO @疯狂：mapper 封装下哈；不直接在 service 调用这种基础 mapper 的基础方法
        return brokerageUserMapper.selectCount(BrokerageUserDO::getBindUserId, bindUserId);
    }

    // TODO @疯狂：因为现在 user 会存在使用验证码直接注册，所以 isNewUser 不太好传递；我们是不是可以约定绑定的时间，createTime 在 30 秒内，就认为新用户；
    @Override
    public boolean bindBrokerageUser(Long userId, Long bindUserId, Boolean isNewUser) {
        // TODO @疯狂：userId 为空，搞到参数校验里哇；
        if (userId == null) {
            throw exception(0);
        }

        // 1. 获得分销用户
        boolean isNewBrokerageUser = false;
        BrokerageUserDO brokerageUser = brokerageUserMapper.selectById(userId);
        if (brokerageUser == null) { // 分销用户不存在的情况：1. 新注册；2. 旧数据；3. 分销功能关闭后又打开
            isNewBrokerageUser = true;
            brokerageUser = new BrokerageUserDO().setId(userId).setBrokerageEnabled(false).setBrokeragePrice(0).setFrozenPrice(0);
        }

        // 2.1 校验能否绑定
        boolean validated = validateCanBindUser(brokerageUser, bindUserId, isNewUser);
        if (!validated) {
            return false;
        }

        // 2.2 绑定用户
        if (isNewBrokerageUser) {
            Integer enabledCondition = tradeConfigService.getTradeConfig().getBrokerageEnabledCondition();
            if (BrokerageEnabledConditionEnum.ALL.getCondition().equals(enabledCondition)) { // 人人分销：用户默认就有分销资格
                // TODO @疯狂：应该设置下 brokerageTime，而不是 bindUserTime
                brokerageUser.setBrokerageEnabled(true).setBindUserTime(LocalDateTime.now());
            }
            // TODO @疯狂：这里是不是要设置 bindUserId、bindUserTime 字段哈；
            brokerageUserMapper.insert(brokerageUser);
        } else {
            brokerageUserMapper.updateById(new BrokerageUserDO().setId(userId)
                    .setBindUserId(bindUserId).setBindUserTime(LocalDateTime.now()));
        }
        return true;
    }

    // TODO @疯狂：validate 方法，一般不返回 true false，而是抛出异常；如果要返回 true false 这种，方法名字可以改成 isUserCanBind
    private boolean validateCanBindUser(BrokerageUserDO user, Long bindUserId, Boolean isNewUser) {
        // TODO @疯狂：bindUserId 为空，搞到参数校验里哇；
        if (bindUserId == null) {
            return false;
        }

        // 校验分销功能是否启用
        TradeConfigDO tradeConfig = tradeConfigService.getTradeConfig();
        if (tradeConfig == null || BooleanUtil.isFalse(tradeConfig.getBrokerageEnabled())) {
            return false;
        }

        // 校验绑定自己
        if (Objects.equals(user.getId(), bindUserId)) {
            throw exception(BROKERAGE_BIND_SELF);
        }

        // 校验要绑定的用户有无推广资格
        BrokerageUserDO bindUser = brokerageUserMapper.selectById(bindUserId);
        if (bindUser == null || BooleanUtil.isFalse(bindUser.getBrokerageEnabled())) {
            throw exception(BROKERAGE_BIND_USER_NOT_ENABLED);
        }

        // 校验分佣模式：仅可后台手动设置推广员
        if (BrokerageEnabledConditionEnum.ADMIN.getCondition().equals(tradeConfig.getBrokerageEnabledCondition())) {
            throw exception(BROKERAGE_BIND_CONDITION_ADMIN);
        }

        // 校验分销关系绑定模式
        if (BrokerageBindModeEnum.REGISTER.getMode().equals(tradeConfig.getBrokerageBindMode())) {
            if (!BooleanUtil.isTrue(isNewUser)) {
                throw exception(BROKERAGE_BIND_MODE_REGISTER); // 只有在注册时可以绑定
            }
        } else if (BrokerageBindModeEnum.ANYTIME.getMode().equals(tradeConfig.getBrokerageBindMode())) {
            if (user.getBindUserId() != null) {
                throw exception(BROKERAGE_BIND_OVERRIDE); // 已绑定了推广人
            }
        }

        // TODO @疯狂：这块是不是一直查询到根节点，中间不允许出现自己；就是不能形成环。虽然目前是 2 级，但是未来可能会改多级； = = 环的话，就会存在问题哈
        // A->B->A：下级不能绑定自己的上级,   A->B->C->A可以!!
        if (Objects.equals(user.getId(), bindUser.getBindUserId())) {
            throw exception(BROKERAGE_BIND_LOOP);
        }
        return true;
    }

}
