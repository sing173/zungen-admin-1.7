package cn.iocoder.yudao.module.promotion.service.discount;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.discount.vo.DiscountActivityBaseVO;
import cn.iocoder.yudao.module.promotion.controller.admin.discount.vo.DiscountActivityCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.discount.vo.DiscountActivityPageReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.discount.vo.DiscountActivityUpdateReqVO;
import cn.iocoder.yudao.module.promotion.convert.discount.DiscountActivityConvert;
import cn.iocoder.yudao.module.promotion.dal.dataobject.discount.DiscountActivityDO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.discount.DiscountProductDO;
import cn.iocoder.yudao.module.promotion.dal.mysql.discount.DiscountActivityMapper;
import cn.iocoder.yudao.module.promotion.dal.mysql.discount.DiscountProductMapper;
import cn.iocoder.yudao.module.promotion.enums.common.PromotionActivityStatusEnum;
import cn.iocoder.yudao.module.promotion.util.PromotionUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.module.promotion.enums.ErrorCodeConstants.*;

/**
 * 限时折扣 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DiscountActivityServiceImpl implements DiscountActivityService {

    @Resource
    private DiscountActivityMapper discountActivityMapper;
    @Resource
    private DiscountProductMapper discountProductMapper;

    @Override
    public List<DiscountProductDO> getMatchDiscountProductList(Collection<Long> skuIds) {
        // TODO 芋艿：开启、满足 skuId、日期内
        return null;
    }

    @Override
    public Long createDiscountActivity(DiscountActivityCreateReqVO createReqVO) {
        // 校验商品是否冲突
        validateDiscountActivityProductConflicts(null, createReqVO.getProducts());

        // 插入活动
        DiscountActivityDO discountActivity = DiscountActivityConvert.INSTANCE.convert(createReqVO)
                .setStatus(PromotionUtils.calculateActivityStatus(createReqVO.getEndTime()));
        discountActivityMapper.insert(discountActivity);
        // 插入商品
        List<DiscountProductDO> discountProducts = convertList(createReqVO.getProducts(),
                product -> DiscountActivityConvert.INSTANCE.convert(product).setActivityId(discountActivity.getId()));
        discountProductMapper.insertBatch(discountProducts);
        // 返回
        return discountActivity.getId();
    }

    @Override
    public void updateDiscountActivity(DiscountActivityUpdateReqVO updateReqVO) {
        // 校验存在
        DiscountActivityDO discountActivity = validateDiscountActivityExists(updateReqVO.getId());
        if (discountActivity.getStatus().equals(PromotionActivityStatusEnum.CLOSE.getStatus())) { // 已关闭的活动，不能修改噢
            throw exception(DISCOUNT_ACTIVITY_UPDATE_FAIL_STATUS_CLOSED);
        }
        // 校验商品是否冲突
        validateDiscountActivityProductConflicts(updateReqVO.getId(), updateReqVO.getProducts());

        // 更新活动
        DiscountActivityDO updateObj = DiscountActivityConvert.INSTANCE.convert(updateReqVO)
                .setStatus(PromotionUtils.calculateActivityStatus(updateReqVO.getEndTime()));
        discountActivityMapper.updateById(updateObj);
        // 更新商品
        updateDiscountProduct(updateReqVO);
    }

    private void updateDiscountProduct(DiscountActivityUpdateReqVO updateReqVO) {
        List<DiscountProductDO> dbDiscountProducts = discountProductMapper.selectListByActivityId(updateReqVO.getId());
        // 计算要删除的记录
        List<Long> deleteIds = convertList(dbDiscountProducts, DiscountProductDO::getId,
                discountProductDO -> updateReqVO.getProducts().stream()
                        .noneMatch(product -> DiscountActivityConvert.INSTANCE.isEquals(discountProductDO, product)));
        if (CollUtil.isNotEmpty(deleteIds)) {
            discountProductMapper.deleteBatchIds(deleteIds);
        }
        // 计算新增的记录
        List<DiscountProductDO> newDiscountProducts = convertList(updateReqVO.getProducts(),
                product -> DiscountActivityConvert.INSTANCE.convert(product).setActivityId(updateReqVO.getId()));
        newDiscountProducts.removeIf(product -> dbDiscountProducts.stream().anyMatch(
                dbProduct -> DiscountActivityConvert.INSTANCE.isEquals(dbProduct, product))); // 如果匹配到，说明是更新的
        if (CollectionUtil.isNotEmpty(newDiscountProducts)) {
            discountProductMapper.insertBatch(newDiscountProducts);
        }
    }

    // TODO 芋艿：校验逻辑简化，只查询时间冲突的活动，开启状态的。
    /**
     * 校验商品是否冲突
     *
     * @param id 编号
     * @param products 商品列表
     */
    private void validateDiscountActivityProductConflicts(Long id, List<DiscountActivityBaseVO.Product> products) {
        if (CollUtil.isEmpty(products)) {
            return;
        }
        // 查询商品参加的活动
        List<DiscountProductDO> discountActivityProductList = null;
//                getRewardProductListBySkuIds(
//                convertSet(products, DiscountActivityBaseVO.Product::getSkuId),
//                asList(PromotionActivityStatusEnum.WAIT.getStatus(), PromotionActivityStatusEnum.RUN.getStatus()));
        if (id != null) { // 排除自己这个活动
            discountActivityProductList.removeIf(product -> id.equals(product.getActivityId()));
        }
        // 如果非空，则说明冲突
        if (CollUtil.isNotEmpty(discountActivityProductList)) {
            throw exception(DISCOUNT_ACTIVITY_SPU_CONFLICTS);
        }
    }

    @Override
    public void closeRewardActivity(Long id) {
        // 校验存在
        DiscountActivityDO dbDiscountActivity = validateDiscountActivityExists(id);
        if (dbDiscountActivity.getStatus().equals(PromotionActivityStatusEnum.CLOSE.getStatus())) { // 已关闭的活动，不能关闭噢
            throw exception(DISCOUNT_ACTIVITY_CLOSE_FAIL_STATUS_CLOSED);
        }
        if (dbDiscountActivity.getStatus().equals(PromotionActivityStatusEnum.END.getStatus())) { // 已关闭的活动，不能关闭噢
            throw exception(DISCOUNT_ACTIVITY_CLOSE_FAIL_STATUS_END);
        }

        // 更新为关闭。
        DiscountActivityDO updateObj = new DiscountActivityDO().setId(id).setStatus(PromotionActivityStatusEnum.CLOSE.getStatus());
        discountActivityMapper.updateById(updateObj);
    }

    @Override
    public void deleteDiscountActivity(Long id) {
        // 校验存在
        DiscountActivityDO discountActivity = validateDiscountActivityExists(id);
        if (!discountActivity.getStatus().equals(PromotionActivityStatusEnum.CLOSE.getStatus())) { // 未关闭的活动，不能删除噢
            throw exception(DISCOUNT_ACTIVITY_DELETE_FAIL_STATUS_NOT_CLOSED);
        }

        // 删除
        discountActivityMapper.deleteById(id);
    }

    private DiscountActivityDO validateDiscountActivityExists(Long id) {
        DiscountActivityDO discountActivity = discountActivityMapper.selectById(id);
        if (discountActivity == null) {
            throw exception(DISCOUNT_ACTIVITY_NOT_EXISTS);
        }
        return discountActivity;
    }

    @Override
    public DiscountActivityDO getDiscountActivity(Long id) {
        return discountActivityMapper.selectById(id);
    }

    @Override
    public PageResult<DiscountActivityDO> getDiscountActivityPage(DiscountActivityPageReqVO pageReqVO) {
        return discountActivityMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DiscountProductDO> getDiscountProductsByActivityId(Long activityId) {
        return discountProductMapper.selectListByActivityId(activityId);
    }

}
