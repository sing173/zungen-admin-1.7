package cn.iocoder.yudao.module.bpm.controller.admin.definition;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.io.IoUtils;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.model.*;
import cn.iocoder.yudao.module.bpm.convert.definition.BpmModelConvert;
import cn.iocoder.yudao.module.bpm.service.definition.BpmModelService;
import com.zungen.wb.api.business.BusinessAreaFeign;
import com.zungen.wb.api.design.BusinessEventFeign;
import com.zungen.wb.enums.UserType;
import com.zungen.wb.model.business.BusinessAreaListVO;
import com.zungen.wb.model.business.BusinessEventSimpleListVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 流程模型")
@RestController
@RequestMapping("/bpm/model")
@Validated
public class BpmModelController {
    @DubboReference
    private BusinessAreaFeign businessAreaApi;

    @DubboReference
    private BusinessEventFeign businessEventApi;

    @Resource
    private BpmModelService modelService;

    @GetMapping("/page")
    @Operation(summary = "获得模型分页")
    public CommonResult<PageResult<BpmModelPageItemRespVO>> getModelPage(BpmModelPageReqVO pageVO) {
        return success(modelService.getModelPage(pageVO));
    }

    @GetMapping("/get")
    @Operation(summary = "获得模型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('bpm:model:query')")
    public CommonResult<BpmModelRespVO> getModel(@RequestParam("id") String id) {
        BpmModelRespVO model = modelService.getModel(id);
        return success(model);
    }

    @PostMapping("/create")
    @Operation(summary = "新建模型")
    @PreAuthorize("@ss.hasPermission('bpm:model:create')")
    public CommonResult<String> createModel(@Valid @RequestBody BpmModelCreateReqVO createRetVO) {
        return success(modelService.createModel(createRetVO, null));
    }

    @PutMapping("/update")
    @Operation(summary = "修改模型")
    @PreAuthorize("@ss.hasPermission('bpm:model:update')")
    public CommonResult<Boolean> updateModel(@Valid @RequestBody BpmModelUpdateReqVO modelVO) {
        modelService.updateModel(modelVO);
        return success(true);
    }

    @PostMapping("/import")
    @Operation(summary = "导入模型")
    @PreAuthorize("@ss.hasPermission('bpm:model:import')")
    public CommonResult<String> importModel(@Valid BpmModeImportReqVO importReqVO) throws IOException {
        BpmModelCreateReqVO createReqVO = BpmModelConvert.INSTANCE.convert(importReqVO);
        // 读取文件
        String bpmnXml = IoUtils.readUtf8(importReqVO.getBpmnFile().getInputStream(), false);
        return success(modelService.createModel(createReqVO, bpmnXml));
    }

    @PostMapping("/deploy")
    @Operation(summary = "部署模型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('bpm:model:deploy')")
    public CommonResult<Boolean> deployModel(@RequestParam("id") String id) {
        modelService.deployModel(id);
        return success(true);
    }

    @PutMapping("/update-state")
    @Operation(summary = "修改模型的状态", description = "实际更新的部署的流程定义的状态")
    @PreAuthorize("@ss.hasPermission('bpm:model:update')")
    public CommonResult<Boolean> updateModelState(@Valid @RequestBody BpmModelUpdateStateReqVO reqVO) {
        modelService.updateModelState(reqVO.getId(), reqVO.getState());
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除模型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('bpm:model:delete')")
    public CommonResult<Boolean> deleteModel(@RequestParam("id") String id) {
        modelService.deleteModel(id);
        return success(true);
    }

    @Operation(summary = "获取业务领域列表")
    @GetMapping("/findAllBusinessArea")
    @PreAuthorize("@ss.hasPermission('bpm:model:query')")
    public CommonResult<List<BusinessAreaListVO>> getBusinessAreaListByUserId() {
        String userId = WebFrameworkUtils.getLoginUserId().toString();
//        assert userId != null;
        if (UserType.SUPER_ADMIN.getUserId().equals(userId)) {
            return success(businessAreaApi.findAll());
        }
        return success(businessAreaApi.findAllByManagerUserId(userId));
    }

    @Operation(summary = "获取业务事件列表")
    @Parameter(name = "businessAreaId", description = "业务领域id", required = true, example = "1024")
    @GetMapping(value = "/findAllBusinessEvent")
    @PreAuthorize("@ss.hasPermission('bpm:model:query')")
    public CommonResult<List<BusinessEventSimpleListVO>> findAll(@RequestParam(value = "businessAreaId") String businessAreaId){
        List<BusinessEventSimpleListVO> list = businessEventApi.findAllByBusinessAreaId(businessAreaId);
        return success(list);
    }
}
