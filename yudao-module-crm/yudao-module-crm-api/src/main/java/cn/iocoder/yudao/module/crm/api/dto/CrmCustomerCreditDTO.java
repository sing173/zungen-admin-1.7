package cn.iocoder.yudao.module.crm.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrmCustomerCreditDTO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 学历
     */
    private Integer education;
    /**
     * 身份证号省份
     */
    private String idCardProvince;
    /**
     * 10年内发放已还款6月的未结清房贷的最大月还款金额
     */
    private Long maxRepay;
    /**
     * 准贷记卡额度
     */
    private Long crdLimit;
    /**
     * 是否有房贷
     */
    private Integer hasHouseLoan;
    /**
     * 是否有车
     */
    private Integer hasCar;
    /**
     * 身份证号城市
     */
    private String idCardCity;
    /**
     * 身份证号区县
     */
    private String idCardDistrict;
    /**
     * 手机实名认证
     */
    private String mobileReal;
    /**
     * 手机归属省份
     */
    private String mobileProvince;
    /**
     * 手机归属城市
     */
    private String mobileCity;
    /**
     * 执行公开信息条数
     */
    private Integer pubExecCnt;
    /**
     * 司法研究院_个人失信条数
     */
    private Integer persDishCnt;
    /**
     * 近6个月征信信用卡审批和贷款审批次数
     */
    private Integer crdLoanApp;
    /**
     * 月还款
     */
    private Long monthRepay;
    /**
     * 额度使用率
     */
    private Long crdUtil;
    /**
     * 准贷记卡当前透支
     */
    private Long currOD;
    /**
     * 贷记卡已用额度
     */
    private Long usedLimit;
    /**
     * 信用卡机构数
     */
    private Integer crdOirgCnt;
    /**
     * 信用卡已用额度大于0的机构数
     */
    private Integer orgCntUsed;
    /**
     * 信用记录账龄
     */
    private Integer crdHistAge;
    /**
     * 过去24个月逾期次数
     */
    private Integer overDueCnt;
    /**
     * 百融身份证非银近1个月申请机构数
     */
    private Integer nonBankApp1;
    /**
     * 百融身份证非银近3个月申请机构数
     */
    private Integer nonBankApp3;
    /**
     * 百融身份证非银近6个月申请机构数
     */
    private Integer nonBankApp6;
    /**
     * 百融身份证非银近12个月申请机构数
     */
    private Integer nonBankApp12;
    /**
     * 百融身份证非银近6个月查询次数
     */
    private Integer nonBankQry6;
    /**
     * 百融身份证非银近12个月查询次数
     */
    private Integer nonBankQry12;
    /**
     * 呆账笔数
     */
    private Integer badDebtCnt;
    /**
     * 资产处置笔数
     */
    private Integer assetDispCnt;
    /**
     * 保证人代偿笔数
     */
    private Integer guarCompCnt;
    /**
     * 贷款当前最大逾期期数
     */
    private Integer maxOverdueLoan;
    /**
     * 贷记卡当前最大逾期期数
     */
    private Integer maxOverdueCrd;
    /**
     * 准贷记卡当前最大透支期数
     */
    private Integer maxodPeriod;
    /**
     * 贷款当前还款状态出现逾期账户数
     */
    private Integer overdueLoanCnt;
    /**
     * 贷记卡当前还款状态出现逾期账户数
     */
    private Integer overdueCrdCnt;
    /**
     * 准贷记卡当前还款状态出现透支30天以上账户数
     */
    private Integer overdueodCnt;
    /**
     * 贷款信用卡还款状态出现逾期的机构数
     */
    private Integer orgsWithOverdue;
    /**
     * 近3个月个人征信查询机构数
     */
    private Integer creditInqCnt;
    /**
     * 近1年个人征信查询机构数
     */
    private Integer crdAppInq360;
    /**
     * 逾期金额
     */
    private Long overdue;
    /**
     * 透支金额
     */
    private Long overdueOD;
    /**
     * 外担保最坏状态
     */
    private Integer guaranteeBadStatus;
    /**
     * 信贷审批查询次数（6个月内）
     */
    private Integer creditApprovalQueries;
    /**
     * 贷款审批查询次数（360天）
     */
    private Integer loanApprovalQueries360;
    /**
     * 信用卡审批查询次数（360天）
     */
    private Integer cardApprovalQueries360;
    /**
     * 数字解读相对位置
     */
    private Integer interpretationRelativePosition;
    /**
     * 客户编号
     */
    private Long customerId;
}
