package cn.iocoder.yudao.module.bpm.service.crm;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.bpm.controller.admin.crm.vo.BpmCrmInputCreateReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.crm.vo.BpmCrmInputExportReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.crm.vo.BpmCrmInputPageReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.crm.vo.BpmCrmInputVarUpdateVO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.crm.BpmCrmInputDO;
import cn.iocoder.yudao.module.bpm.service.proxy.BpmVarService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 进件工单 Service 接口
 *
 * @author admin
 */
public interface BpmCrmInputService extends BpmVarService {

    /**
     * 创建进件工单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCrmInput(Long userId, @Valid BpmCrmInputCreateReqVO createReqVO);

    /**
     * 更新进件工单
     *
     * @param updateReqVO 更新信息
     */
    void updateCrmInputStatus(Long id, Integer result);


    /**
     * 获得进件工单
     *
     * @param id 编号
     * @return 进件工单
     */
    BpmCrmInputDO getCrmInput(Long id);

    /**
     * 获得进件工单分页
     *
     * @param pageReqVO 分页查询
     * @return 进件工单分页
     */
    PageResult<BpmCrmInputDO> getCrmInputPage(BpmCrmInputPageReqVO pageReqVO);

    /**
     * 获得进件工单列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 进件工单列表
     */
    List<BpmCrmInputDO> getCrmInputList(BpmCrmInputExportReqVO exportReqVO);

    /**
     * 获取进件工单审批任务展示的表单数据
     * @param dataTable 需要读取的数据表名
     * @param dataId 数据表的主键id
     * @return
     */
    Map<String, Object> getBpmVars(String dataTable, Long dataId);

    Boolean updateBpmVars(BpmCrmInputVarUpdateVO varUpdateVO);
}
