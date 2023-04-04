package cn.iocoder.yudao.module.system.controller.admin.dict.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

@Schema(description = "APP - 数据字典精简 Response VO")
@Data
public class AppDictTypeSimpleRespVO {

    @Schema(description = "字典类型", required = true, example = "crm_id_type")
    private String dictType;

    @Schema(description = "字典名称", required = true, example = "证件类型")
    private String dictName;

    @Schema(description = "字典数据", required = true)
    private LinkedHashMap<String, Object> data;

}
