package cn.iocoder.yudao.module.bpm.controller.admin.crm;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.bpm.controller.admin.crm.vo.*;
import cn.iocoder.yudao.module.bpm.convert.crm.BpmCrmInputConvert;
import cn.iocoder.yudao.module.bpm.dal.dataobject.crm.BpmCrmInputDO;
import cn.iocoder.yudao.module.bpm.service.crm.BpmCrmInputService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 进件工单")
@RestController
@RequestMapping("/bpm/crm/input")
@Validated
public class BpmCrmInputController {

    @Resource
    private BpmCrmInputService crmInputService;


    @PostMapping("/create")
    @Operation(summary = "创建进件工单")
    @PreAuthorize("@ss.hasPermission('bpm:crm-input:create')")
    public CommonResult<Long> createCrmInput(@Valid @RequestBody BpmCrmInputCreateReqVO createReqVO) {
        return success(crmInputService.createCrmInput(getLoginUserId(), createReqVO));
    }

    @GetMapping("/get")
    @Operation(summary = "获得进件工单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('bpm:crm-input:query')")
    public CommonResult<BpmCrmInputRespVO> getCrmInput(@RequestParam("id") Long id) {
        BpmCrmInputDO crmInput = crmInputService.getCrmInput(id);
        return success(BpmCrmInputConvert.INSTANCE.convert(crmInput));
    }

    @GetMapping("/page")
    @Operation(summary = "获得进件工单分页")
    @PreAuthorize("@ss.hasPermission('bpm:crm-input:query')")
    public CommonResult<PageResult<BpmCrmInputRespVO>> getCrmInputPage(@Valid BpmCrmInputPageReqVO pageVO) {
        PageResult<BpmCrmInputDO> pageResult = crmInputService.getCrmInputPage(pageVO);
        return success(BpmCrmInputConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出进件工单 Excel")
    @PreAuthorize("@ss.hasPermission('bpm:crm-input:export')")
    @OperateLog(type = EXPORT)
    public void exportCrmInputExcel(@Valid BpmCrmInputExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<BpmCrmInputDO> list = crmInputService.getCrmInputList(exportReqVO);
        // 导出 Excel
        List<BpmCrmInputExcelVO> datas = BpmCrmInputConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "进件工单.xls", "数据", BpmCrmInputExcelVO.class, datas);
    }

    @GetMapping("/view")
    @Operation(summary = "展示进件工单活动详情")
    @PreAuthorize("@ss.hasPermission('bpm:crm-input:update')")
    public CommonResult<Map<String, Object>> getBpmVars(@RequestParam("dataTable") String dataTable, @RequestParam("dataId") Long dataId) {
        return success(crmInputService.getBpmVars(dataTable, dataId));
    }

    @PostMapping("/update")
    @Operation(summary = "审批任务更新表单")
    @PreAuthorize("@ss.hasPermission('bpm:crm-input:update')")
    public CommonResult<Boolean> updateBpmVars(@Valid @RequestBody BpmCrmInputVarUpdateVO varUpdateVO) {
        crmInputService.updateBpmVars(varUpdateVO);
        return success(true);
    }



}
