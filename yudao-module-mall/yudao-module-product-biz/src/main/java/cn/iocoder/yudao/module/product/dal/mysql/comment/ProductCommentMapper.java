package cn.iocoder.yudao.module.product.dal.mysql.comment;


import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.product.controller.admin.comment.vo.ProductCommentPageReqVO;
import cn.iocoder.yudao.module.product.controller.app.comment.vo.AppCommentPageReqVO;
import cn.iocoder.yudao.module.product.dal.dataobject.comment.ProductCommentDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductCommentMapper extends BaseMapperX<ProductCommentDO> {

    default PageResult<ProductCommentDO> selectPage(ProductCommentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductCommentDO>()
                .likeIfPresent(ProductCommentDO::getUserNickname, reqVO.getUserNickname())
                .eqIfPresent(ProductCommentDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(ProductCommentDO::getSpuId, reqVO.getSpuId())
                .eqIfPresent(ProductCommentDO::getScores, reqVO.getScores())
                .betweenIfPresent(ProductCommentDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(ProductCommentDO::getSpuName, reqVO.getSpuName())
                .orderByDesc(ProductCommentDO::getId));
    }

    static void appendTabQuery(LambdaQueryWrapperX<ProductCommentDO> queryWrapper, Integer type) {
        // TODO @puhui999：是不是不用 apply 拉？直接用 mybatis 的方法就好啦
        // 构建好评查询语句：好评计算 总评 >= 4
        if (ObjectUtil.equal(type, AppCommentPageReqVO.GOOD_COMMENT)) {
            queryWrapper.apply("scores >= 4");
        }
        // 构建中评查询语句：中评计算 总评 >= 3 且 总评 < 4
        if (ObjectUtil.equal(type, AppCommentPageReqVO.MEDIOCRE_COMMENT)) {
            queryWrapper.apply("scores >=3 and scores < 4");
        }
        // 构建差评查询语句：差评计算 总评 < 3
        if (ObjectUtil.equal(type, AppCommentPageReqVO.NEGATIVE_COMMENT)) {
            queryWrapper.apply("scores < 3");
        }
    }

    default PageResult<ProductCommentDO> selectPage(AppCommentPageReqVO reqVO, Boolean visible) {
        LambdaQueryWrapperX<ProductCommentDO> queryWrapper = new LambdaQueryWrapperX<ProductCommentDO>()
                .eqIfPresent(ProductCommentDO::getSpuId, reqVO.getSpuId())
                .eqIfPresent(ProductCommentDO::getVisible, visible);
        // 构建评价查询语句
        appendTabQuery(queryWrapper, reqVO.getType());
        // 按评价时间排序最新的显示在前面
        queryWrapper.orderByDesc(ProductCommentDO::getCreateTime);
        return selectPage(reqVO, queryWrapper);
    }

    default ProductCommentDO selectByUserIdAndOrderItemIdAndSpuId(Long userId, Long orderItemId, Long skuId) {
        return selectOne(new LambdaQueryWrapperX<ProductCommentDO>()
                .eq(ProductCommentDO::getUserId, userId)
                .eq(ProductCommentDO::getOrderItemId, orderItemId)
                .eq(ProductCommentDO::getSpuId, skuId));
    }

    default Long selectCountBySpuId(Long spuId, Boolean visible, Integer type) {
        LambdaQueryWrapperX<ProductCommentDO> queryWrapper = new LambdaQueryWrapperX<ProductCommentDO>()
                .eqIfPresent(ProductCommentDO::getSpuId, spuId)
                .eqIfPresent(ProductCommentDO::getVisible, visible);
        // 构建评价查询语句
        appendTabQuery(queryWrapper, type);
        return selectCount(queryWrapper);
    }

    default PageResult<ProductCommentDO> selectCommentList(Long spuId, Integer count) {
        // 构建分页查询条件
        return selectPage(new PageParam().setPageSize(count), new LambdaQueryWrapperX<ProductCommentDO>()
                .eqIfPresent(ProductCommentDO::getSpuId, spuId)
                .orderByDesc(ProductCommentDO::getCreateTime)
        );
    }

}
