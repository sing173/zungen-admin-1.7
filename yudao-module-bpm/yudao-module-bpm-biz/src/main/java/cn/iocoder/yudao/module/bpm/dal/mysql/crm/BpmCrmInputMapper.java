package cn.iocoder.yudao.module.bpm.dal.mysql.crm;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.bpm.dal.dataobject.crm.BpmCrmInputDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.bpm.controller.admin.crm.vo.*;

/**
 * 进件工单 Mapper
 *
 * @author admin
 */
@Mapper
public interface BpmCrmInputMapper extends BaseMapperX<BpmCrmInputDO> {

    default PageResult<BpmCrmInputDO> selectPage(BpmCrmInputPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BpmCrmInputDO>()
                .eqIfPresent(BpmCrmInputDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(BpmCrmInputDO::getCrmProductId, reqVO.getCrmProductId())
                .eqIfPresent(BpmCrmInputDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BpmCrmInputDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BpmCrmInputDO::getId));
    }

    default List<BpmCrmInputDO> selectList(BpmCrmInputExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<BpmCrmInputDO>()
                .eqIfPresent(BpmCrmInputDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(BpmCrmInputDO::getCrmCustomerId, reqVO.getCrmCustomerId())
                .eqIfPresent(BpmCrmInputDO::getCrmCustomerImageId, reqVO.getCrmCustomerImageId())
                .eqIfPresent(BpmCrmInputDO::getCrmProductId, reqVO.getCrmProductId())
                .eqIfPresent(BpmCrmInputDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .eqIfPresent(BpmCrmInputDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BpmCrmInputDO::getAuditTime, reqVO.getAuditTime())
                .eqIfPresent(BpmCrmInputDO::getAuditType, reqVO.getAuditType())
                .eqIfPresent(BpmCrmInputDO::getAuditRemark, reqVO.getAuditRemark())
                .betweenIfPresent(BpmCrmInputDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BpmCrmInputDO::getId));
    }

}
