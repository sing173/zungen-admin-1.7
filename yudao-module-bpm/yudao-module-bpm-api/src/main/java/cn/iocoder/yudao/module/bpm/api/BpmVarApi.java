package cn.iocoder.yudao.module.bpm.api;

import java.util.Map;

/**
 * 工作流restfull接口，目前是内部调用，使用feignClient实现
 */
public interface BpmVarApi {

    /**
     * 获取工作流表单的变量
     *
     * @param dataTable
     * @param dataId
     * @return
     */
    Map<String, Object> getBpmVars(String dataTable, Long dataId);

}
