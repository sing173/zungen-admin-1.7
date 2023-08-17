package cn.iocoder.yudao.module.bpm.service;

import java.util.Map;


public interface BpmVarService {
    Map<String, Object> getBpmVars(String dataTable, Long dataId);
}
