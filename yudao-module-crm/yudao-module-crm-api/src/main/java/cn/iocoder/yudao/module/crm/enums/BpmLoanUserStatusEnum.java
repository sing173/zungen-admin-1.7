package cn.iocoder.yudao.module.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BPM 模型的表单类型的枚举
 *
 * @author admin
 */
@Getter
@AllArgsConstructor
public enum BpmLoanUserStatusEnum {

    NORMAL(10, "正常"),
    CUSTOM(20, "业务表单")
    ;

    private final Integer status;
    private final String desc;
}
