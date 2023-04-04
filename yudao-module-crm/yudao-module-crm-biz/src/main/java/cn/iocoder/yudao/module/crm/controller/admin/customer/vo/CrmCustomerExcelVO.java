package cn.iocoder.yudao.module.crm.controller.admin.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 客户信息 Excel VO
 *
 * @author admin
 */
@Data
public class CrmCustomerExcelVO {

    @ExcelProperty("主键")
    private Long id;

    @ExcelProperty("客户的用户编号")
    private Long userId;

    @ExcelProperty("担保人的身份认证id")
    private Long identityId;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("手机号码")
    private String mobile;

    @ExcelProperty("身份编号")
    private String idCard;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
