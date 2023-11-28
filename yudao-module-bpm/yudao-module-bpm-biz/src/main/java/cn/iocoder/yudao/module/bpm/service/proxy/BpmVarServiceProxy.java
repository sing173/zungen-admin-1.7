package cn.iocoder.yudao.module.bpm.service.proxy;

import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.module.bpm.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.bpm.service.crm.BpmCrmCreditService;
import cn.iocoder.yudao.module.bpm.service.crm.BpmCrmInputService;

import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 工作流运行期间调用内部服务的静态代理类
 */
public class BpmVarServiceProxy implements BpmVarService{
    public static final String TABLE_CRM_CUSTOMER = "crm_customer";
    public static final String TABLE_CRM_INPUT = "bpm_crm_input";
    public static final String TABLE_CRM_CUSTOMER_CREDIT = "crm_customer_credit";

    /**
     * 委托类，获取表单数据
     */
    private final BpmVarService bpmVarService;

    /**
     * 与引擎交互需要提供数据源对象定义（proto文件），此字段即数据源对象的类名
     * 暂时与数据表绑定死，即工作流的数据表与数据源protobuf对象一一对应
     * 即crm_customer 对应 com.zungen.proto.entity.CrmInputFormEntity$Customer
     */
    public final String classFullName;

    public BpmVarServiceProxy(BpmVarService bpmVarService, String classFullName) {
        this.bpmVarService = bpmVarService;
        this.classFullName = classFullName;
    }

    /**
     * 工作流表单绑定数据表，获取数据的服务是根据数据表名的，所以暂时通过表名实例化服务（可能每个工作流都有自己的业务实现）
     * 暂时以CRM作为实现工作流与决策引擎结合的样例
     * @param dataTableName
     * @return
     */
    public static BpmVarServiceProxy getInstance(String dataTableName) {
        switch (dataTableName) {
            case TABLE_CRM_CUSTOMER :
                return new BpmVarServiceProxy(
                        SpringUtil.getBean(BpmCrmInputService.class),
                        "com.zungen.proto.entity.CrmInputFormEntity$Customer");
            case TABLE_CRM_INPUT:
                //TODO 暂未构建$Input 的proto文件
                return new BpmVarServiceProxy(
                        SpringUtil.getBean(BpmCrmInputService.class),
                        "com.zungen.proto.entity.CrmInputFormEntity$Input");
            case TABLE_CRM_CUSTOMER_CREDIT:
                return new BpmVarServiceProxy(
                        SpringUtil.getBean(BpmCrmCreditService.class),
                        "com.zungen.proto.entity.AoyunDataSource$LoanOrder");
            default:
                throw exception(ErrorCodeConstants.BPM_ZDE_DS_API_NOT_EXISTS);

        }
    }

    @Override
    public Map<String, Object> getBpmVars(String dataTable, Long dataId) {
        return bpmVarService.getBpmVars(dataTable, dataId);
    }
}
