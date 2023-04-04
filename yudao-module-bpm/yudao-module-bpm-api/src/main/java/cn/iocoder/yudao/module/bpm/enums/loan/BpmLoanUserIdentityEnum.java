package cn.iocoder.yudao.module.bpm.enums.loan;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BPM 贷款人身份三要素认证状态枚举
 *
 * @author admin
 */
@Getter
@AllArgsConstructor
public enum BpmLoanUserIdentityEnum {

    NO_SUBMIT(10, "未验证"),
    REJECT(20, "验证不通过"),
    PASS(30, "已验证"),
    OVERTIME(40, "已过期"),
    INVALID(50, "作废")
    ;

    private final Integer status;
    private final String desc;
}
