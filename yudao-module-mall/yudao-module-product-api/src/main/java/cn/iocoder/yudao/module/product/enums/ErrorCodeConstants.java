package cn.iocoder.yudao.module.product.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Product 错误码枚举类
 *
 * product 系统，使用 1-008-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 商品分类相关 1008001000 ============
    ErrorCode CATEGORY_NOT_EXISTS = new ErrorCode(1008001000, "商品分类不存在");
    ErrorCode CATEGORY_PARENT_NOT_EXISTS = new ErrorCode(1008001001, "父分类不存在");
    ErrorCode CATEGORY_PARENT_NOT_FIRST_LEVEL = new ErrorCode(1008001002, "父分类不能是二级分类");
    ErrorCode CATEGORY_EXISTS_CHILDREN = new ErrorCode(1008001003, "存在子分类，无法删除");
    ErrorCode CATEGORY_DISABLED = new ErrorCode(1008001004, "商品分类({})已禁用，无法使用");
    ErrorCode CATEGORY_HAVE_BIND_SPU = new ErrorCode(1008001005, "类别下存在商品，无法删除");

    // ========== 商品品牌相关编号 1008002000 ==========
    ErrorCode BRAND_NOT_EXISTS = new ErrorCode(1008002000, "品牌不存在");
    ErrorCode BRAND_DISABLED = new ErrorCode(1008002001, "品牌已禁用");
    ErrorCode BRAND_NAME_EXISTS = new ErrorCode(1008002002, "品牌名称已存在");

    // ========== 商品属性项 1008003000 ==========
    ErrorCode PROPERTY_NOT_EXISTS = new ErrorCode(1008003000, "属性项不存在");
    ErrorCode PROPERTY_EXISTS = new ErrorCode(1008003001, "属性项的名称已存在");
    ErrorCode PROPERTY_DELETE_FAIL_VALUE_EXISTS = new ErrorCode(1008003002, "属性项下存在属性值，无法删除");

    // ========== 商品属性值 1008004000 ==========
    ErrorCode PROPERTY_VALUE_NOT_EXISTS = new ErrorCode(1008004000, "属性值不存在");
    ErrorCode PROPERTY_VALUE_EXISTS = new ErrorCode(1008004001, "属性值的名称已存在");

    // ========== 商品 SPU 1008005000 ==========
    ErrorCode SPU_NOT_EXISTS = new ErrorCode(1008005000, "商品 SPU 不存在");
    ErrorCode SPU_SAVE_FAIL_CATEGORY_LEVEL_ERROR = new ErrorCode(1008005001, "商品分类不正确，原因：必须使用第二级的商品分类及以下");
    ErrorCode SPU_NOT_ENABLE = new ErrorCode(1008005002, "商品 SPU 不处于上架状态");
    ErrorCode SPU_NOT_RECYCLE = new ErrorCode(1008005003, "商品 SPU 不处于回收站状态");

    // ========== 商品 SKU 1008006000 ==========
    ErrorCode SKU_NOT_EXISTS = new ErrorCode(1008006000, "商品 SKU 不存在");
    ErrorCode SKU_PROPERTIES_DUPLICATED = new ErrorCode(1008006001, "商品 SKU 的属性组合存在重复");
    ErrorCode SPU_ATTR_NUMBERS_MUST_BE_EQUALS = new ErrorCode(1008006002, "一个 SPU 下的每个 SKU，其属性项必须一致");
    ErrorCode SPU_SKU_NOT_DUPLICATE = new ErrorCode(1008006003, "一个 SPU 下的每个 SKU，必须不重复");
    ErrorCode SKU_STOCK_NOT_ENOUGH = new ErrorCode(1008006004, "商品 SKU 库存不足");

    // ========== 商品 评价 1008007000 ==========
    ErrorCode COMMENT_NOT_EXISTS = new ErrorCode(1008007000, "商品评价不存在");
    ErrorCode COMMENT_ORDER_EXISTS = new ErrorCode(1008007001, "订单的商品评价已存在");

    // ========== 商品 收藏 1008008000 ==========
    ErrorCode FAVORITE_EXISTS = new ErrorCode(1008008000, "该商品已经被收藏");
    ErrorCode FAVORITE_NOT_EXISTS = new ErrorCode(1008008001, "商品收藏不存在");

}
