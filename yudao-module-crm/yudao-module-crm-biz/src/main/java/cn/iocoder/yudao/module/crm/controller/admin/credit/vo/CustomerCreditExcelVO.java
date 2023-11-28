package cn.iocoder.yudao.module.crm.controller.admin.credit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 客户征信信息 Excel VO
 *
 * @author admin
 */
@Data
public class CustomerCreditExcelVO {

    @ExcelProperty("主键")
    private Integer id;

    @ExcelProperty("学历")
    private Integer education;

    @ExcelProperty("身份证号省份")
    private String idCardProvince;

    @ExcelProperty("10年内发放已还款6月的未结清房贷的最大月还款金额")
    private Long maxRepay;

    @ExcelProperty("准贷记卡额度")
    private Long crdLimit;

    @ExcelProperty("是否有房贷")
    private Integer hasHouseLoan;

    @ExcelProperty("是否有车")
    private Integer hasCar;

    @ExcelProperty("身份证号城市")
    private String idCardCity;

    @ExcelProperty("身份证号区县")
    private String idCardDistrict;

    @ExcelProperty("手机实名认证")
    private String mobileReal;

    @ExcelProperty("手机归属省份")
    private String mobileProvince;

    @ExcelProperty("手机归属城市")
    private String mobileCity;

    @ExcelProperty("执行公开信息条数")
    private Integer pubExecCnt;

    @ExcelProperty("司法研究院_个人失信条数")
    private Integer persDishCnt;

    @ExcelProperty("近6个月征信信用卡审批和贷款审批次数")
    private Integer crdLoanApp;

    @ExcelProperty("月还款")
    private Long monthRepay;

    @ExcelProperty("额度使用率")
    private Long crdUtil;

    @ExcelProperty("准贷记卡当前透支")
    private Long currOD;

    @ExcelProperty("贷记卡已用额度")
    private Long usedLimit;

    @ExcelProperty("信用卡机构数")
    private Integer crdOirgCnt;

    @ExcelProperty("信用卡已用额度大于0的机构数")
    private Integer orgCntUsed;

    @ExcelProperty("信用记录账龄")
    private Integer crdHistAge;

    @ExcelProperty("过去24个月逾期次数")
    private Integer overDueCnt;

    @ExcelProperty("百融身份证非银近1个月申请机构数")
    private Integer nonBankApp1;

    @ExcelProperty("百融身份证非银近3个月申请机构数")
    private Integer nonBankApp3;

    @ExcelProperty("百融身份证非银近6个月申请机构数")
    private Integer nonBankApp6;

    @ExcelProperty("百融身份证非银近12个月申请机构数")
    private Integer nonBankApp12;

    @ExcelProperty("百融身份证非银近6个月查询次数")
    private Integer nonBankQry6;

    @ExcelProperty("百融身份证非银近12个月查询次数")
    private Integer nonBankQry12;

    @ExcelProperty("呆账笔数")
    private Integer badDebtCnt;

    @ExcelProperty("资产处置笔数")
    private Integer assetDispCnt;

    @ExcelProperty("保证人代偿笔数")
    private Integer guarCompCnt;

    @ExcelProperty("贷款当前最大逾期期数")
    private Integer maxOverdueLoan;

    @ExcelProperty("贷记卡当前最大逾期期数")
    private Integer maxOverdueCrd;

    @ExcelProperty("准贷记卡当前最大透支期数")
    private Integer maxodPeriod;

    @ExcelProperty("贷款当前还款状态出现逾期账户数")
    private Integer overdueLoanCnt;

    @ExcelProperty("贷记卡当前还款状态出现逾期账户数")
    private Integer overdueCrdCnt;

    @ExcelProperty("准贷记卡当前还款状态出现透支30天以上账户数")
    private Integer overdueodCnt;

    @ExcelProperty("贷款信用卡还款状态出现逾期的机构数")
    private Integer orgsWithOverdue;

    @ExcelProperty("近3个月个人征信查询机构数")
    private Integer creditInqCnt;

    @ExcelProperty("近1年个人征信查询机构数")
    private Integer crdAppInq360;

    @ExcelProperty("逾期金额")
    private Long overdue;

    @ExcelProperty("透支金额")
    private Long overdueOD;

    @ExcelProperty("外担保最坏状态")
    private Integer guaranteeBadStatus;

    @ExcelProperty("信贷审批查询次数（6个月内）")
    private Integer creditApprovalQueries;

    @ExcelProperty("贷款审批查询次数（360天）")
    private Integer loanApprovalQueries360;

    @ExcelProperty("信用卡审批查询次数（360天）")
    private Integer cardApprovalQueries360;

    @ExcelProperty("数字解读相对位置")
    private Integer interpretationRelativePosition;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("客户编号")
    private Integer customerId;

}
