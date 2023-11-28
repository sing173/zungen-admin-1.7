package cn.iocoder.yudao.module.bpm.service.crm;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.iocoder.yudao.module.bpm.service.crm.BpmCrmCreditService;
import cn.iocoder.yudao.module.crm.api.CrmCustomerApi;
import cn.iocoder.yudao.module.crm.api.dto.CrmCustomerCreditDTO;
import cn.iocoder.yudao.module.crm.api.dto.CrmCustomerDTO;
import com.alibaba.fastjson.JSON;
import com.googlecode.protobuf.format.JsonFormat;
import com.zungen.proto.entity.AoyunCreditData;
import com.zungen.proto.entity.AoyunDataSource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 为进件工单做征信审批，请求决策前的数据特殊处理
 */
@Service
@Validated
public class BpmCrmCreditServiceImpl implements BpmCrmCreditService {
    @Resource
    CrmCustomerApi crmCustomerApi;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    private static final String CREDIT_DATA_PERSONAL_KEY = "flink:com.zungen.proto.entity.AoyunCreditData$PersonalData:";
    private static final String CREDIT_DATA_KEY = "flink:com.zungen.proto.entity.AoyunCreditData$CreditData:";

    @Override
    public Map<String, Object> getBpmVars(String dataTable, Long dataId) {
        //找出客户征信信息
        CrmCustomerCreditDTO customerCreditDTO = crmCustomerApi.getCustomerCredit(dataId);
        Assert.notNull(customerCreditDTO, "没找到客户征信信息(id={})", dataId);
        //找出客户信息
        CrmCustomerDTO customerDTO = crmCustomerApi.getCustomer(customerCreditDTO.getCustomerId());
        Assert.notNull(customerDTO, "没找到客户信息(id={})", customerCreditDTO.getCustomerId());

        //缓存客户征信信息(json)
        stringRedisTemplate.opsForValue().set(CREDIT_DATA_KEY+customerDTO.getIdCard(), JSON.toJSONString(customerCreditDTO));

        //组装客户信息为protobuf结构，然后范围map到流程引擎代理昨晚参数post到决策引擎中
        AoyunDataSource.LoanOrder loanOrder = AoyunDataSource.LoanOrder.newBuilder()
                .setCustomerName(customerDTO.getName())
                .setCustomerMobile(customerDTO.getMobile())
                .setCustomer(AoyunDataSource.Customer.newBuilder()
                        .setIdCard(customerDTO.getIdCard())
                        .setAddress(customerDTO.getAddress())
                        .setAge(customerDTO.getAge())
                        .setSex(customerDTO.getSex()))
                .build();

        return BeanUtil.beanToMap(loanOrder);
    }
}
