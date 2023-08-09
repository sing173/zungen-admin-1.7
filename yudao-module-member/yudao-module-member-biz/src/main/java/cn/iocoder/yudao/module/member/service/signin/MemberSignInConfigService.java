package cn.iocoder.yudao.module.member.service.signin;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.signin.vo.MemberSignInConfigCreateReqVO;
import cn.iocoder.yudao.module.member.controller.admin.signin.vo.MemberSignInConfigPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.signin.vo.MemberSignInConfigUpdateReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.signin.MemberSignInConfigDO;

import javax.validation.Valid;

/**
 * 积分签到规则 Service 接口
 *
 * @author QingX
 */
public interface MemberSignInConfigService {

    /**
     * 创建积分签到规则
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createSignInConfig(@Valid MemberSignInConfigCreateReqVO createReqVO);

    /**
     * 更新积分签到规则
     *
     * @param updateReqVO 更新信息
     */
    void updateSignInConfig(@Valid MemberSignInConfigUpdateReqVO updateReqVO);

    /**
     * 删除积分签到规则
     *
     * @param id 编号
     */
    void deleteSignInConfig(Integer id);

    /**
     * 获得积分签到规则
     *
     * @param id 编号
     * @return 积分签到规则
     */
    MemberSignInConfigDO getSignInConfig(Integer id);

    /**
     * 获得积分签到规则分页
     *
     * @param pageReqVO 分页查询
     * @return 积分签到规则分页
     */
    PageResult<MemberSignInConfigDO> getSignInConfigPage(MemberSignInConfigPageReqVO pageReqVO);

}
