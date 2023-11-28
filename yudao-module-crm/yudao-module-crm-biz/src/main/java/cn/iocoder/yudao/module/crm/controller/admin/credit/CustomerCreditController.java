package cn.iocoder.yudao.module.crm.controller.admin.credit;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.crm.controller.admin.credit.vo.*;
import cn.iocoder.yudao.module.crm.dal.dataobject.credit.CustomerCreditDO;
import cn.iocoder.yudao.module.crm.convert.credit.CustomerCreditConvert;
import cn.iocoder.yudao.module.crm.service.credit.CustomerCreditService;

@Tag(name = "管理后台 - 客户征信信息")
@RestController
@RequestMapping("/crm/customer-credit")
@Validated
public class CustomerCreditController {

    @Resource
    private CustomerCreditService customerCreditService;

    @PostMapping("/create")
    @Operation(summary = "创建客户征信信息")
    @PreAuthorize("@ss.hasPermission('crm:customer-credit:create')")
    public CommonResult<Long> createCustomerCredit(@Valid @RequestBody CustomerCreditCreateReqVO createReqVO) {
        return success(customerCreditService.createCustomerCredit(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新客户征信信息")
    @PreAuthorize("@ss.hasPermission('crm:customer-credit:update')")
    public CommonResult<Boolean> updateCustomerCredit(@Valid @RequestBody CustomerCreditUpdateReqVO updateReqVO) {
        customerCreditService.updateCustomerCredit(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除客户征信信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:customer-credit:delete')")
    public CommonResult<Boolean> deleteCustomerCredit(@RequestParam("id") Long id) {
        customerCreditService.deleteCustomerCredit(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得客户征信信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:customer-credit:query')")
    public CommonResult<CustomerCreditRespVO> getCustomerCredit(@RequestParam("id") Long id) {
        CustomerCreditDO customerCredit = customerCreditService.getCustomerCredit(id);
        return success(CustomerCreditConvert.INSTANCE.convert(customerCredit));
    }

    @GetMapping("/list")
    @Operation(summary = "获得客户征信信息列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('crm:customer-credit:query')")
    public CommonResult<List<CustomerCreditRespVO>> getCustomerCreditList(@RequestParam("ids") Collection<Long> ids) {
        List<CustomerCreditDO> list = customerCreditService.getCustomerCreditList(ids);
        return success(CustomerCreditConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得客户征信信息分页")
    @PreAuthorize("@ss.hasPermission('crm:customer-credit:query')")
    public CommonResult<PageResult<CustomerCreditRespVO>> getCustomerCreditPage(@Valid CustomerCreditPageReqVO pageVO) {
        PageResult<CustomerCreditDO> pageResult = customerCreditService.getCustomerCreditPage(pageVO);
        return success(CustomerCreditConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出客户征信信息 Excel")
    @PreAuthorize("@ss.hasPermission('crm:customer-credit:export')")
    @OperateLog(type = EXPORT)
    public void exportCustomerCreditExcel(@Valid CustomerCreditExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<CustomerCreditDO> list = customerCreditService.getCustomerCreditList(exportReqVO);
        // 导出 Excel
        List<CustomerCreditExcelVO> datas = CustomerCreditConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "客户征信信息.xls", "数据", CustomerCreditExcelVO.class, datas);
    }

}
