package cn.iocoder.yudao.module.bpm.controller.admin.crm.vo;

import lombok.*;

import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 进件工单 Excel VO
 *
 * @author admin
 */
@Data
public class BpmCrmInputExcelVO {

    @ExcelProperty("客户进件工单主键")
    private Long id;

    @ExcelProperty("工单编号")
    private String orderNo;

    @ExcelProperty("客户标识")
    private Long crmCustomerId;

    @ExcelProperty("客户图像标识")
    private Long crmCustomerImageId;

    @ExcelProperty("产品标识")
    private Long crmProductId;

    @ExcelProperty("流程实例的编号")
    private String processInstanceId;

    @ExcelProperty("工单状态")
    private Byte status;

    @ExcelProperty("审批时间")
    private LocalDateTime auditTime;

    @ExcelProperty("审批类型(自动、人工)")
    private String auditType;

    @ExcelProperty("审批意见")
    private String auditRemark;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
