package cn.iocoder.yudao.module.infra.api.logger;

import cn.iocoder.yudao.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import cn.iocoder.yudao.module.infra.service.logger.ApiAccessLogService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * API 访问日志的 API 实现类
 *
 * @author 芋道源码
 */
@DubboService
@Validated
public class ApiAccessLogApiImpl implements ApiAccessLogApi {

    @Resource
    private ApiAccessLogService apiAccessLogService;

    @Override
    public void createApiAccessLog(ApiAccessLogCreateReqDTO createDTO) {
        apiAccessLogService.createApiAccessLog(createDTO);
    }

}
