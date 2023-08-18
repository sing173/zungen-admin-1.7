package cn.iocoder.yudao.module.bpm.service.proxy;

import java.util.Map;


public interface BpmVarService {
    Map<String, Object> getBpmVars(String dataTable, Long dataId);
}
