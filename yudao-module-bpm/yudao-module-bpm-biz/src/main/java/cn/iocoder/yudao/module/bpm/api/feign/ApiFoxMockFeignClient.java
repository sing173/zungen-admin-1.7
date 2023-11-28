package cn.iocoder.yudao.module.bpm.api.feign;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import feign.Body;
import feign.RequestLine;

import java.util.Map;

/**
 * 通过Feign调用本地的Mock接口
 */
public interface ApiFoxMockFeignClient {

    /**
     * 获取客户征信模拟数据
     * @return
     */
    @RequestLine("GET /m1/1225484-0-default/admin-api/crm/customer-credit/get")
    CommonResult<Map<String, Object>> getCustomerCredit();
}
