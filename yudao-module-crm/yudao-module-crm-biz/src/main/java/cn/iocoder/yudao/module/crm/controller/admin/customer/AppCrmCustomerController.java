package cn.iocoder.yudao.module.crm.controller.admin.customer;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.crm.controller.admin.customer.vo.*;
import cn.iocoder.yudao.module.crm.convert.auth.AuthConvert;
import cn.iocoder.yudao.module.crm.convert.customer.CrmCustomerConvert;
import cn.iocoder.yudao.module.crm.dal.dataobject.customer.CrmCustomerDO;
import cn.iocoder.yudao.module.crm.service.customer.CrmCustomerService;
import cn.iocoder.yudao.module.system.api.sms.SmsCodeApi;
import cn.iocoder.yudao.module.system.enums.sms.SmsSceneEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "APP2B - 客户信息")
@RestController
@RequestMapping("/crm/app/customer")
@Validated
public class AppCrmCustomerController {

    @Resource
    private CrmCustomerService customerService;

    @Resource
    private SmsCodeApi smsCodeApi;

    @PostMapping("/sms")
    @Operation(summary = "发送短信验证码")
    public CommonResult<Boolean> sendSms(@Valid @RequestBody CrmSmsSendReqVO reqVO) {
        smsCodeApi.sendSmsCode(AuthConvert.INSTANCE.convert(reqVO).setCreateIp(getClientIP()));

        return success(true);
    }

    @PostMapping("/create")
    @Operation(summary = "创建客户信息")
    public CommonResult<Long> createCustomer(@Valid @RequestBody CrmCustomerCreateReqVO createReqVO) {
        // 校验验证码
        String userIp = getClientIP();
        smsCodeApi.useSmsCode(AuthConvert.INSTANCE.convert(createReqVO, SmsSceneEnum.CRM_CUSTOMER_MOBILE_CHECK.getScene(), userIp));
        


        return success(customerService.createCustomer(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新客户信息")
    public CommonResult<Boolean> updateCustomer(@Valid @RequestBody CrmCustomerUpdateReqVO updateReqVO) {
        customerService.updateCustomer(updateReqVO);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得客户信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<CrmCustomerRespVO> getCustomer(@RequestParam("id") Long id) {
        CrmCustomerDO customer = customerService.getCustomer(id);
        return success(CrmCustomerConvert.INSTANCE.convert(customer));
    }

    @GetMapping("/list")
    @Operation(summary = "获得客户信息列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    public CommonResult<List<CrmCustomerRespVO>> getCustomerList(@RequestParam("ids") Collection<Long> ids) {
        List<CrmCustomerDO> list = customerService.getCustomerList(ids);
        return success(CrmCustomerConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得客户信息分页")
    public CommonResult<PageResult<CrmCustomerRespVO>> getCustomerPage(@Valid CrmCustomerPageReqVO pageVO) {
        PageResult<CrmCustomerDO> pageResult = customerService.getCustomerPage(pageVO);
        return success(CrmCustomerConvert.INSTANCE.convertPage(pageResult));
    }
}
