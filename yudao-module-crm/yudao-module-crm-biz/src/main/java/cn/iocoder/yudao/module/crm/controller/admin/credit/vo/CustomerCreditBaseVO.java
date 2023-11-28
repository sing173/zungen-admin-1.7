package cn.iocoder.yudao.module.crm.controller.admin.credit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 客户征信信息 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class CustomerCreditBaseVO {

    @Schema(description = "学历")
    private Integer education;

    @Schema(description = "身份证号省份")
    private String idCardProvince;

    @Schema(description = "10年内发放已还款6月的未结清房贷的最大月还款金额")
    private Long maxRepay;

    @Schema(description = "准贷记卡额度")
    private Long crdLimit;

    @Schema(description = "是否有房贷")
    private Integer hasHouseLoan;

    @Schema(description = "是否有车")
    private Integer hasCar;

    @Schema(description = "身份证号城市")
    private String idCardCity;

    @Schema(description = "身份证号区县")
    private String idCardDistrict;

    @Schema(description = "手机实名认证")
    private String mobileReal;

    @Schema(description = "手机归属省份")
    private String mobileProvince;

    @Schema(description = "手机归属城市")
    private String mobileCity;

    @Schema(description = "执行公开信息条数")
    private Integer pubExecCnt;

    @Schema(description = "司法研究院_个人失信条数")
    private Integer persDishCnt;

    @Schema(description = "近6个月征信信用卡审批和贷款审批次数")
    private Integer crdLoanApp;

    @Schema(description = "月还款")
    private Long monthRepay;

    @Schema(description = "额度使用率")
    private Long crdUtil;

    @Schema(description = "准贷记卡当前透支")
    private Long currOD;

    @Schema(description = "贷记卡已用额度")
    private Long usedLimit;

    @Schema(description = "信用卡机构数")
    private Integer crdOirgCnt;

    @Schema(description = "信用卡已用额度大于0的机构数")
    private Integer orgCntUsed;

    @Schema(description = "信用记录账龄")
    private Integer crdHistAge;

    @Schema(description = "过去24个月逾期次数")
    private Integer overDueCnt;

    @Schema(description = "百融身份证非银近1个月申请机构数")
    private Integer nonBankApp1;

    @Schema(description = "百融身份证非银近3个月申请机构数")
    private Integer nonBankApp3;

    @Schema(description = "百融身份证非银近6个月申请机构数")
    private Integer nonBankApp6;

    @Schema(description = "百融身份证非银近12个月申请机构数")
    private Integer nonBankApp12;

    @Schema(description = "百融身份证非银近6个月查询次数")
    private Integer nonBankQry6;

    @Schema(description = "百融身份证非银近12个月查询次数")
    private Integer nonBankQry12;

    @Schema(description = "呆账笔数")
    private Integer badDebtCnt;

    @Schema(description = "资产处置笔数")
    private Integer assetDispCnt;

    @Schema(description = "保证人代偿笔数")
    private Integer guarCompCnt;

    @Schema(description = "贷款当前最大逾期期数")
    private Integer maxOverdueLoan;

    @Schema(description = "贷记卡当前最大逾期期数")
    private Integer maxOverdueCrd;

    @Schema(description = "准贷记卡当前最大透支期数")
    private Integer maxodPeriod;

    @Schema(description = "贷款当前还款状态出现逾期账户数")
    private Integer overdueLoanCnt;

    @Schema(description = "贷记卡当前还款状态出现逾期账户数")
    private Integer overdueCrdCnt;

    @Schema(description = "准贷记卡当前还款状态出现透支30天以上账户数")
    private Integer overdueodCnt;

    @Schema(description = "贷款信用卡还款状态出现逾期的机构数")
    private Integer orgsWithOverdue;

    @Schema(description = "近3个月个人征信查询机构数")
    private Integer creditInqCnt;

    @Schema(description = "近1年个人征信查询机构数")
    private Integer crdAppInq360;

    @Schema(description = "逾期金额")
    private Long overdue;

    @Schema(description = "透支金额")
    private Long overdueOD;

    @Schema(description = "外担保最坏状态", example = "2")
    private Integer guaranteeBadStatus;

    @Schema(description = "信贷审批查询次数（6个月内）")
    private Integer creditApprovalQueries;

    @Schema(description = "贷款审批查询次数（360天）")
    private Integer loanApprovalQueries360;

    @Schema(description = "信用卡审批查询次数（360天）")
    private Integer cardApprovalQueries360;

    @Schema(description = "数字解读相对位置")
    private Integer interpretationRelativePosition;

    @Schema(description = "客户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "22158")
    @NotNull(message = "客户编号不能为空")
    private Long customerId;

}
