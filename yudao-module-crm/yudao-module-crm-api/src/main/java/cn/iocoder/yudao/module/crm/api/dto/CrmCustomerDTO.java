package cn.iocoder.yudao.module.crm.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrmCustomerDTO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 客户的用户编号
     */
    private Long userId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 家庭地址
     */
    private String address;
    /**
     * 工作单位
     */
    private String myWork;
    /**
     * 工作电话
     */
    private String workPhone;
    /**
     * 家庭地址
     */
    private String workAddress;
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 证件类型
     */
    private Byte cerType;
    /**
     * 教育程度
     */
    private Byte eduType;
    /**
     * 职业类型
     */
    private Byte professionType;
    /**
     * 性别
     */
    private Byte sex;
    /**
     * 民族
     */
    private Byte ethnic;
    /**
     * 出生日期
     */
    private LocalDate birthDay;
    /**
     * 身份编号
     */
    private String idCard;
    /**
     * 证件签发机构
     */
    private String department;
    /**
     * 证件有效期
     */
    private String expirationDate;
    /**
     * 证件像地址
     */
    private String avatar;
}
