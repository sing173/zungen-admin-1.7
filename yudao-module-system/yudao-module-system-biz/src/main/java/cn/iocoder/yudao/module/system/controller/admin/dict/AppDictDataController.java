package cn.iocoder.yudao.module.system.controller.admin.dict;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.data.DictDataExportReqVO;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.type.DictTypeExportReqVO;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.AppDictTypeSimpleRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.dict.DictDataDO;
import cn.iocoder.yudao.module.system.dal.dataobject.dict.DictTypeDO;
import cn.iocoder.yudao.module.system.service.dict.DictDataService;
import cn.iocoder.yudao.module.system.service.dict.DictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "APP2B - 字典数据")
@RestController
@RequestMapping("/system/dict-data")
@Validated
public class AppDictDataController {
    @Resource
    private DictDataService dictDataService;

    @Resource
    private DictTypeService dictTypeService;

    @PermitAll
    @GetMapping("/list-app-simple")
    @Parameter(name = "dictType", description = "字典类型，传入前缀如crm", required = true)
    @Operation(summary = "获得全部字典数据列表", description = "一般用于APP缓存字典数据在本地")
    public CommonResult<List<AppDictTypeSimpleRespVO>> getSimpleDictDataList(@RequestParam("dictType") String dictType) {
        //先通过前缀模糊匹配找出字典类型
        DictTypeExportReqVO dictTypeExportReqVO = new DictTypeExportReqVO();
        dictTypeExportReqVO.setType(dictType);
        dictTypeExportReqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());
        List<DictTypeDO> dictTypeDOList = dictTypeService.getDictTypeList(dictTypeExportReqVO);
        List<AppDictTypeSimpleRespVO> dictDataSimpleRespVOS = new ArrayList<>();
        dictTypeDOList.forEach(dictTypeDO -> {
            //再根据具体的字典类型dictType查找字典数据
            DictDataExportReqVO dataExportReqVO = new DictDataExportReqVO();
            dataExportReqVO.setDictType(dictTypeDO.getType());
            dataExportReqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());
            List<DictDataDO> dictDataDOS = dictDataService.getDictDataList(dataExportReqVO);

            AppDictTypeSimpleRespVO appDictTypeSimpleRespVO = new AppDictTypeSimpleRespVO();
            LinkedHashMap<String, Object> dataMap = new LinkedHashMap<>();
            appDictTypeSimpleRespVO.setData(dataMap);
            dictDataSimpleRespVOS.add(appDictTypeSimpleRespVO);
            dictDataDOS.forEach(dictDataDO -> {
                dataMap.putIfAbsent(dictDataDO.getLabel(), dictDataDO.getValue());
                appDictTypeSimpleRespVO.setDictType(dictTypeDO.getType());
                appDictTypeSimpleRespVO.setDictName(dictTypeDO.getName());
            });
        });

        return success(dictDataSimpleRespVOS);
    }
}
