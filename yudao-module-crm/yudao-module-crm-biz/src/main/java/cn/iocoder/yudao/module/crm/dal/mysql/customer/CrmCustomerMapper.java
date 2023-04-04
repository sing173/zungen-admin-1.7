package cn.iocoder.yudao.module.crm.dal.mysql.customer;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.crm.dal.dataobject.customer.CrmCustomerDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.crm.controller.admin.customer.vo.*;

/**
 * 客户信息 Mapper
 *
 * @author admin
 */
@Mapper
public interface CrmCustomerMapper extends BaseMapperX<CrmCustomerDO> {

    default PageResult<CrmCustomerDO> selectPage(CrmCustomerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CrmCustomerDO>()
                .eqIfPresent(CrmCustomerDO::getUserId, reqVO.getUserId())
                .eqIfPresent(CrmCustomerDO::getIdentityId, reqVO.getIdentityId())
                .likeIfPresent(CrmCustomerDO::getName, reqVO.getName())
                .eqIfPresent(CrmCustomerDO::getMobile, reqVO.getMobile())
                .eqIfPresent(CrmCustomerDO::getIdCard, reqVO.getIdCard())
                .betweenIfPresent(CrmCustomerDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CrmCustomerDO::getId));
    }

    default List<CrmCustomerDO> selectList(CrmCustomerExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<CrmCustomerDO>()
                .eqIfPresent(CrmCustomerDO::getUserId, reqVO.getUserId())
                .eqIfPresent(CrmCustomerDO::getIdentityId, reqVO.getIdentityId())
                .likeIfPresent(CrmCustomerDO::getName, reqVO.getName())
                .eqIfPresent(CrmCustomerDO::getMobile, reqVO.getMobile())
                .eqIfPresent(CrmCustomerDO::getIdCard, reqVO.getIdCard())
                .betweenIfPresent(CrmCustomerDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CrmCustomerDO::getId));
    }

}
