package cn.iocoder.yudao.module.trade.framework.aftersalelog.core.annotations;

import cn.iocoder.yudao.module.trade.enums.aftersale.AfterSaleOperateTypeEnum;

import java.lang.annotation.*;

/**
 * 售后日志的注解
 *
 * 写在方法上时，会自动记录售后日志
 *
 * @author 陈賝
 * @since 2023/6/8 17:04
 * @see cn.iocoder.yudao.module.trade.framework.aftersalelog.core.aop.AfterSaleLogAspect
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AfterSaleLog {

    /**
     * 售后 ID
     */
    String id();

    /**
     * 操作类型
     */
    AfterSaleOperateTypeEnum operateType();

    /**
     * 日志内容
     */
    String content() default "";

}
