package cn.iocoder.yudao.module.crm.dal.dataobject.customer;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 客户信息 DO
 *
 * @author admin
 */
@TableName("crm_customer")
@KeySequence("crm_customer_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmCustomerDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
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
     * 状态
     */
    private Byte status;
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
    private LocalDateTime birthDay;
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
