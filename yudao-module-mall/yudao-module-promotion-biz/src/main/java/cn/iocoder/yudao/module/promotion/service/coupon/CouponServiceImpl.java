package cn.iocoder.yudao.module.promotion.service.coupon;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.promotion.controller.admin.coupon.vo.coupon.CouponPageReqVO;
import cn.iocoder.yudao.module.promotion.convert.coupon.CouponConvert;
import cn.iocoder.yudao.module.promotion.dal.dataobject.coupon.CouponDO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.coupon.CouponTemplateDO;
import cn.iocoder.yudao.module.promotion.dal.mysql.coupon.CouponMapper;
import cn.iocoder.yudao.module.promotion.enums.coupon.CouponStatusEnum;
import cn.iocoder.yudao.module.promotion.enums.coupon.CouponTakeTypeEnum;
import cn.iocoder.yudao.module.promotion.enums.coupon.CouponTemplateValidityTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.module.promotion.enums.ErrorCodeConstants.*;
import static java.util.Arrays.asList;

/**
 * 优惠劵 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CouponServiceImpl implements CouponService {

    @Resource
    private CouponTemplateService couponTemplateService;

    @Resource
    private CouponMapper couponMapper;

    @Resource
    private MemberUserApi memberUserApi;

    @Override
    public CouponDO validCoupon(Long id, Long userId) {
        CouponDO coupon = couponMapper.selectByIdAndUserId(id, userId);
        if (coupon == null) {
            throw exception(COUPON_NOT_EXISTS);
        }
        validCoupon(coupon);
        return coupon;
    }

    @Override
    public void validCoupon(CouponDO coupon) {
        // 校验状态
        if (ObjectUtil.notEqual(coupon.getStatus(), CouponStatusEnum.UNUSED.getStatus())) {
            throw exception(COUPON_STATUS_NOT_UNUSED);
        }
        // 校验有效期；为避免定时器没跑，实际优惠劵已经过期
        if (LocalDateTimeUtils.isBetween(coupon.getValidStartTime(), coupon.getValidEndTime())) {
            throw exception(COUPON_VALID_TIME_NOT_NOW);
        }
    }

    @Override
    public PageResult<CouponDO> getCouponPage(CouponPageReqVO pageReqVO) {
        // 获得用户编号
        Set<Long> userIds = null;
        if (StrUtil.isNotEmpty(pageReqVO.getNickname())) {
            userIds = CollectionUtils.convertSet(memberUserApi.getUserListByNickname(pageReqVO.getNickname()),
                    MemberUserRespDTO::getId);
            if (CollUtil.isEmpty(userIds)) {
                return PageResult.empty();
            }
        }
        // 分页查询
        return couponMapper.selectPage(pageReqVO, userIds);
    }

    @Override
    public void useCoupon(Long id, Long userId, Long orderId) {
        // 校验优惠劵
        validCoupon(id, userId);

        // 更新状态
        int updateCount = couponMapper.updateByIdAndStatus(id, CouponStatusEnum.UNUSED.getStatus(),
                new CouponDO().setStatus(CouponStatusEnum.USED.getStatus())
                        .setUseOrderId(orderId).setUseTime(LocalDateTime.now()));
        if (updateCount == 0) {
            throw exception(COUPON_STATUS_NOT_UNUSED);
        }
    }

    @Override
    public void returnUsedCoupon(Long id) {
        // 校验存在
        CouponDO coupon = couponMapper.selectById(id);
        if (coupon == null) {
            throw exception(COUPON_NOT_EXISTS);
        }
        // 校验状态
        if (ObjectUtil.notEqual(coupon.getTemplateId(), CouponStatusEnum.USED.getStatus())) {
            throw exception(COUPON_STATUS_NOT_USED);
        }

        // 退还
        Integer status = LocalDateTimeUtils.beforeNow(coupon.getValidEndTime())
                ? CouponStatusEnum.EXPIRE.getStatus() // 退还时可能已经过期了
                : CouponStatusEnum.UNUSED.getStatus();
        int updateCount = couponMapper.updateByIdAndStatus(id, CouponStatusEnum.UNUSED.getStatus(),
                new CouponDO().setStatus(status));
        if (updateCount == 0) {
            throw exception(COUPON_STATUS_NOT_USED);
        }

        // TODO 增加优惠券变动记录？
    }

    @Override
    @Transactional
    public void deleteCoupon(Long id) {
        // 校验存在
        validateCouponExists(id);

        // 更新优惠劵
        int deleteCount = couponMapper.delete(id,
                asList(CouponStatusEnum.UNUSED.getStatus(), CouponStatusEnum.EXPIRE.getStatus()));
        if (deleteCount == 0) {
            throw exception(COUPON_DELETE_FAIL_USED);
        }
        // 减少优惠劵模板的领取数量 -1
        couponTemplateService.updateCouponTemplateTakeCount(id, -1);
    }

    @Override
    public List<CouponDO> getCouponList(Long userId, Integer status) {
        return couponMapper.selectListByUserIdAndStatus(userId, status);
    }

    private void validateCouponExists(Long id) {
        if (couponMapper.selectById(id) == null) {
            throw exception(COUPON_NOT_EXISTS);
        }
    }

    @Override
    public Long getUnusedCouponCount(Long userId) {
        return couponMapper.selectCountByUserIdAndStatus(userId, CouponStatusEnum.UNUSED.getStatus());
    }

    @Override
    public void takeCoupon(Long templateId, Set<Long> userIds, CouponTakeTypeEnum takeType) {
        CouponTemplateDO template = couponTemplateService.getCouponTemplate(templateId);
        // 1. 过滤掉达到领取限制的用户
        removeTakeLimitUser(userIds, template);
        // 2. 校验优惠劵是否可以领取
        validateCouponTemplateCanTake(template, userIds, takeType);

        // 3. 批量保存优惠劵
        couponMapper.insertBatch(convertList(userIds, userId -> CouponConvert.INSTANCE.convert(template, userId)));

        // 3. 增加优惠劵模板的领取数量
        couponTemplateService.updateCouponTemplateTakeCount(templateId, userIds.size());
    }

    /**
     * 校验优惠券是否可以领取
     *
     * @param couponTemplate 优惠券模板
     * @param userIds        领取人列表
     * @param takeType       领取方式
     */
    private void validateCouponTemplateCanTake(CouponTemplateDO couponTemplate, Set<Long> userIds, CouponTakeTypeEnum takeType) {
        // 如果所有用户都领取过，则抛出异常
        if (CollUtil.isEmpty(userIds)) {
            throw exception(COUPON_TEMPLATE_USER_ALREADY_TAKE);
        }

        // 校验模板
        if (couponTemplate == null) {
            throw exception(COUPON_TEMPLATE_NOT_EXISTS);
        }
        // 校验剩余数量
        if (couponTemplate.getTakeCount() + userIds.size() > couponTemplate.getTotalCount()) {
            throw exception(COUPON_TEMPLATE_NOT_ENOUGH);
        }
        // 校验"固定日期"的有效期类型是否过期
        if (CouponTemplateValidityTypeEnum.DATE.getType().equals(couponTemplate.getValidityType())) {
            if (LocalDateTimeUtils.beforeNow(couponTemplate.getValidEndTime())) {
                throw exception(COUPON_TEMPLATE_EXPIRED);
            }
        }
        // 校验领取方式
        if (ObjectUtil.notEqual(couponTemplate.getTakeType(), takeType.getValue())) {
            throw exception(COUPON_TEMPLATE_CANNOT_TAKE);
        }
    }

    /**
     * 过滤掉达到领取上线的用户
     *
     * @param userIds 用户编号数组
     * @param couponTemplate 优惠劵模版
     */
    private void removeTakeLimitUser(Set<Long> userIds, CouponTemplateDO couponTemplate) {
        if (couponTemplate.getTakeLimitCount() <= 0) {
            return;
        }
        // 查询已领过券的用户
        List<CouponDO> alreadyTakeCoupons = couponMapper.selectListByTemplateIdAndUserId(couponTemplate.getId(), userIds);
        if (CollUtil.isEmpty(alreadyTakeCoupons)) {
            return;
        }
        // 移除达到领取限制的用户
        Map<Long, Integer> userTakeCountMap = CollStreamUtil.groupBy(alreadyTakeCoupons, CouponDO::getUserId, Collectors.summingInt(c -> 1));
        userIds.removeIf(userId -> MapUtil.getInt(userTakeCountMap, userId, 0) >= couponTemplate.getTakeLimitCount());
    }

}
