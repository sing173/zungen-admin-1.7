package cn.iocoder.yudao.module.bpm.dal.dataobject.crm;

import lombok.*;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 进件工单 DO
 *
 * @author admin
 */
@TableName("bpm_crm_input")
@KeySequence("bpm_crm_input_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BpmCrmInputDO extends BaseDO {

    /**
     * 客户进件工单主键
     */
    @TableId
    private Long id;
    /**
     * 工单编号
     */
    private String orderNo;
    /**
     * 客户标识
     */
    private Long crmCustomerId;
    /**
     * 客户图像标识
     */
    private Long crmCustomerImageId;
    /**
     * 产品标识
     */
    private Long crmProductId;
    /**
     * 流程实例的编号
     */
    private String processInstanceId;
    /**
     * 工单状态
     */
    private Byte status;
    /**
     * 审批时间
     */
    private LocalDateTime auditTime;
    /**
     * 审批类型(自动、人工)
     */
    private Byte auditType;
    /**
     * 审批意见
     */
    private String auditRemark;
    /**
     * 审批人
     */
    private Long auditorId;

    /**
     * 决策结果-决绝码
     */
    private String rejectCode;

    /**
     * 决策结果-申请评分
     */
    private Float applyScore;
}
