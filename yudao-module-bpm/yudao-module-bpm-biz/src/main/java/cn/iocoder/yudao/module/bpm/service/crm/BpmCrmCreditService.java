package cn.iocoder.yudao.module.bpm.service.crm;

import cn.iocoder.yudao.module.bpm.service.proxy.BpmVarService;

import java.util.Map;

/**
 * 客户征信特殊 Service 接口
 *
 * @author admin
 */
public interface BpmCrmCreditService extends BpmVarService {

    /**
     * 获取客户信息、征信信息的表单数据
     * @param dataTable 需要读取的数据表名
     * @param dataId 数据表的主键id
     * @return
     */
    Map<String, Object> getBpmVars(String dataTable, Long dataId);
}
