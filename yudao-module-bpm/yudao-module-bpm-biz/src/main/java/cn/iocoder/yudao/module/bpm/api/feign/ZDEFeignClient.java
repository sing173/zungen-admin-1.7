package cn.iocoder.yudao.module.bpm.api.feign;

import com.zungen.common.message.EventOutData;
import com.zungen.common.message.JsonMessage;
import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 通过Feign调用决策引擎服务
 */
public interface ZDEFeignClient {

    @RequestLine("POST /decision")
    @Headers({"Content-Type: application/json"})
    @Body("{body}")
    JsonMessage<EventOutData> decision(@Param("body") String body);
}
