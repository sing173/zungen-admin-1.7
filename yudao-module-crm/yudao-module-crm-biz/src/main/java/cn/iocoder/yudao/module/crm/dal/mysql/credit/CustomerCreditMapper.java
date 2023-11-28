package cn.iocoder.yudao.module.crm.dal.mysql.credit;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.crm.dal.dataobject.credit.CustomerCreditDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.crm.controller.admin.credit.vo.*;

/**
 * 客户征信信息 Mapper
 *
 * @author admin
 */
@Mapper
public interface CustomerCreditMapper extends BaseMapperX<CustomerCreditDO> {

    default PageResult<CustomerCreditDO> selectPage(CustomerCreditPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CustomerCreditDO>()
                .eqIfPresent(CustomerCreditDO::getEducation, reqVO.getEducation())
                .eqIfPresent(CustomerCreditDO::getIdCardProvince, reqVO.getIdCardProvince())
                .eqIfPresent(CustomerCreditDO::getMaxRepay, reqVO.getMaxRepay())
                .eqIfPresent(CustomerCreditDO::getCrdLimit, reqVO.getCrdLimit())
                .eqIfPresent(CustomerCreditDO::getHasHouseLoan, reqVO.getHasHouseLoan())
                .eqIfPresent(CustomerCreditDO::getHasCar, reqVO.getHasCar())
                .eqIfPresent(CustomerCreditDO::getIdCardCity, reqVO.getIdCardCity())
                .eqIfPresent(CustomerCreditDO::getIdCardDistrict, reqVO.getIdCardDistrict())
                .eqIfPresent(CustomerCreditDO::getMobileReal, reqVO.getMobileReal())
                .eqIfPresent(CustomerCreditDO::getMobileProvince, reqVO.getMobileProvince())
                .eqIfPresent(CustomerCreditDO::getMobileCity, reqVO.getMobileCity())
                .eqIfPresent(CustomerCreditDO::getPubExecCnt, reqVO.getPubExecCnt())
                .eqIfPresent(CustomerCreditDO::getPersDishCnt, reqVO.getPersDishCnt())
                .eqIfPresent(CustomerCreditDO::getCrdLoanApp, reqVO.getCrdLoanApp())
                .eqIfPresent(CustomerCreditDO::getMonthRepay, reqVO.getMonthRepay())
                .eqIfPresent(CustomerCreditDO::getCrdUtil, reqVO.getCrdUtil())
                .eqIfPresent(CustomerCreditDO::getCurrOD, reqVO.getCurrOD())
                .eqIfPresent(CustomerCreditDO::getUsedLimit, reqVO.getUsedLimit())
                .eqIfPresent(CustomerCreditDO::getCrdOirgCnt, reqVO.getCrdOirgCnt())
                .eqIfPresent(CustomerCreditDO::getOrgCntUsed, reqVO.getOrgCntUsed())
                .eqIfPresent(CustomerCreditDO::getCrdHistAge, reqVO.getCrdHistAge())
                .eqIfPresent(CustomerCreditDO::getOverDueCnt, reqVO.getOverDueCnt())
                .eqIfPresent(CustomerCreditDO::getNonBankApp1, reqVO.getNonBankApp1())
                .eqIfPresent(CustomerCreditDO::getNonBankApp3, reqVO.getNonBankApp3())
                .eqIfPresent(CustomerCreditDO::getNonBankApp6, reqVO.getNonBankApp6())
                .eqIfPresent(CustomerCreditDO::getNonBankApp12, reqVO.getNonBankApp12())
                .eqIfPresent(CustomerCreditDO::getNonBankQry6, reqVO.getNonBankQry6())
                .eqIfPresent(CustomerCreditDO::getNonBankQry12, reqVO.getNonBankQry12())
                .eqIfPresent(CustomerCreditDO::getBadDebtCnt, reqVO.getBadDebtCnt())
                .eqIfPresent(CustomerCreditDO::getAssetDispCnt, reqVO.getAssetDispCnt())
                .eqIfPresent(CustomerCreditDO::getGuarCompCnt, reqVO.getGuarCompCnt())
                .eqIfPresent(CustomerCreditDO::getMaxOverdueLoan, reqVO.getMaxOverdueLoan())
                .eqIfPresent(CustomerCreditDO::getMaxOverdueCrd, reqVO.getMaxOverdueCrd())
                .eqIfPresent(CustomerCreditDO::getMaxodPeriod, reqVO.getMaxodPeriod())
                .eqIfPresent(CustomerCreditDO::getOverdueLoanCnt, reqVO.getOverdueLoanCnt())
                .eqIfPresent(CustomerCreditDO::getOverdueCrdCnt, reqVO.getOverdueCrdCnt())
                .eqIfPresent(CustomerCreditDO::getOverdueodCnt, reqVO.getOverdueodCnt())
                .eqIfPresent(CustomerCreditDO::getOrgsWithOverdue, reqVO.getOrgsWithOverdue())
                .eqIfPresent(CustomerCreditDO::getCreditInqCnt, reqVO.getCreditInqCnt())
                .eqIfPresent(CustomerCreditDO::getCrdAppInq360, reqVO.getCrdAppInq360())
                .eqIfPresent(CustomerCreditDO::getOverdue, reqVO.getOverdue())
                .eqIfPresent(CustomerCreditDO::getOverdueOD, reqVO.getOverdueOD())
                .eqIfPresent(CustomerCreditDO::getGuaranteeBadStatus, reqVO.getGuaranteeBadStatus())
                .eqIfPresent(CustomerCreditDO::getCreditApprovalQueries, reqVO.getCreditApprovalQueries())
                .eqIfPresent(CustomerCreditDO::getLoanApprovalQueries360, reqVO.getLoanApprovalQueries360())
                .eqIfPresent(CustomerCreditDO::getCardApprovalQueries360, reqVO.getCardApprovalQueries360())
                .eqIfPresent(CustomerCreditDO::getInterpretationRelativePosition, reqVO.getInterpretationRelativePosition())
                .betweenIfPresent(CustomerCreditDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CustomerCreditDO::getCustomerId, reqVO.getCustomerId())
                .orderByDesc(CustomerCreditDO::getId));
    }

    default List<CustomerCreditDO> selectList(CustomerCreditExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<CustomerCreditDO>()
                .eqIfPresent(CustomerCreditDO::getEducation, reqVO.getEducation())
                .eqIfPresent(CustomerCreditDO::getIdCardProvince, reqVO.getIdCardProvince())
                .eqIfPresent(CustomerCreditDO::getMaxRepay, reqVO.getMaxRepay())
                .eqIfPresent(CustomerCreditDO::getCrdLimit, reqVO.getCrdLimit())
                .eqIfPresent(CustomerCreditDO::getHasHouseLoan, reqVO.getHasHouseLoan())
                .eqIfPresent(CustomerCreditDO::getHasCar, reqVO.getHasCar())
                .eqIfPresent(CustomerCreditDO::getIdCardCity, reqVO.getIdCardCity())
                .eqIfPresent(CustomerCreditDO::getIdCardDistrict, reqVO.getIdCardDistrict())
                .eqIfPresent(CustomerCreditDO::getMobileReal, reqVO.getMobileReal())
                .eqIfPresent(CustomerCreditDO::getMobileProvince, reqVO.getMobileProvince())
                .eqIfPresent(CustomerCreditDO::getMobileCity, reqVO.getMobileCity())
                .eqIfPresent(CustomerCreditDO::getPubExecCnt, reqVO.getPubExecCnt())
                .eqIfPresent(CustomerCreditDO::getPersDishCnt, reqVO.getPersDishCnt())
                .eqIfPresent(CustomerCreditDO::getCrdLoanApp, reqVO.getCrdLoanApp())
                .eqIfPresent(CustomerCreditDO::getMonthRepay, reqVO.getMonthRepay())
                .eqIfPresent(CustomerCreditDO::getCrdUtil, reqVO.getCrdUtil())
                .eqIfPresent(CustomerCreditDO::getCurrOD, reqVO.getCurrOD())
                .eqIfPresent(CustomerCreditDO::getUsedLimit, reqVO.getUsedLimit())
                .eqIfPresent(CustomerCreditDO::getCrdOirgCnt, reqVO.getCrdOirgCnt())
                .eqIfPresent(CustomerCreditDO::getOrgCntUsed, reqVO.getOrgCntUsed())
                .eqIfPresent(CustomerCreditDO::getCrdHistAge, reqVO.getCrdHistAge())
                .eqIfPresent(CustomerCreditDO::getOverDueCnt, reqVO.getOverDueCnt())
                .eqIfPresent(CustomerCreditDO::getNonBankApp1, reqVO.getNonBankApp1())
                .eqIfPresent(CustomerCreditDO::getNonBankApp3, reqVO.getNonBankApp3())
                .eqIfPresent(CustomerCreditDO::getNonBankApp6, reqVO.getNonBankApp6())
                .eqIfPresent(CustomerCreditDO::getNonBankApp12, reqVO.getNonBankApp12())
                .eqIfPresent(CustomerCreditDO::getNonBankQry6, reqVO.getNonBankQry6())
                .eqIfPresent(CustomerCreditDO::getNonBankQry12, reqVO.getNonBankQry12())
                .eqIfPresent(CustomerCreditDO::getBadDebtCnt, reqVO.getBadDebtCnt())
                .eqIfPresent(CustomerCreditDO::getAssetDispCnt, reqVO.getAssetDispCnt())
                .eqIfPresent(CustomerCreditDO::getGuarCompCnt, reqVO.getGuarCompCnt())
                .eqIfPresent(CustomerCreditDO::getMaxOverdueLoan, reqVO.getMaxOverdueLoan())
                .eqIfPresent(CustomerCreditDO::getMaxOverdueCrd, reqVO.getMaxOverdueCrd())
                .eqIfPresent(CustomerCreditDO::getMaxodPeriod, reqVO.getMaxodPeriod())
                .eqIfPresent(CustomerCreditDO::getOverdueLoanCnt, reqVO.getOverdueLoanCnt())
                .eqIfPresent(CustomerCreditDO::getOverdueCrdCnt, reqVO.getOverdueCrdCnt())
                .eqIfPresent(CustomerCreditDO::getOverdueodCnt, reqVO.getOverdueodCnt())
                .eqIfPresent(CustomerCreditDO::getOrgsWithOverdue, reqVO.getOrgsWithOverdue())
                .eqIfPresent(CustomerCreditDO::getCreditInqCnt, reqVO.getCreditInqCnt())
                .eqIfPresent(CustomerCreditDO::getCrdAppInq360, reqVO.getCrdAppInq360())
                .eqIfPresent(CustomerCreditDO::getOverdue, reqVO.getOverdue())
                .eqIfPresent(CustomerCreditDO::getOverdueOD, reqVO.getOverdueOD())
                .eqIfPresent(CustomerCreditDO::getGuaranteeBadStatus, reqVO.getGuaranteeBadStatus())
                .eqIfPresent(CustomerCreditDO::getCreditApprovalQueries, reqVO.getCreditApprovalQueries())
                .eqIfPresent(CustomerCreditDO::getLoanApprovalQueries360, reqVO.getLoanApprovalQueries360())
                .eqIfPresent(CustomerCreditDO::getCardApprovalQueries360, reqVO.getCardApprovalQueries360())
                .eqIfPresent(CustomerCreditDO::getInterpretationRelativePosition, reqVO.getInterpretationRelativePosition())
                .betweenIfPresent(CustomerCreditDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CustomerCreditDO::getCustomerId, reqVO.getCustomerId())
                .orderByDesc(CustomerCreditDO::getId));
    }

}
