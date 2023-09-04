package cn.iocoder.yudao.module.trade.framework.aftersalelog.core.aop;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.util.spring.SpringExpressionUtils;
import cn.iocoder.yudao.framework.operatelog.core.service.OperateLog;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import cn.iocoder.yudao.module.trade.framework.aftersalelog.core.annotations.AfterSaleLog;
import cn.iocoder.yudao.module.trade.framework.aftersalelog.core.dto.TradeAfterSaleLogCreateReqDTO;
import cn.iocoder.yudao.module.trade.framework.aftersalelog.core.service.AfterSaleLogService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.util.json.JsonUtils.toJsonString;
import static java.util.Arrays.asList;

/**
 * 记录售后日志的 AOP 切面
 *
 * @author 陈賝
 * @since 2023/6/13 13:54
 */
@Slf4j
@Aspect
public class AfterSaleLogAspect {

    @Resource
    private AfterSaleLogService afterSaleLogService;
    /**
     * 售前状态
     */
    private static final ThreadLocal<Integer> BEFORE_STATUS = new ThreadLocal<>();
    /**
     * 售后状态
     */
    private static final ThreadLocal<Integer> AFTER_STATUS = new ThreadLocal<>();
    /**
     * 操作类型
     */
    private final static String OPERATE_TYPE = "operateType";
    /**
     * ID
     */
    private final static String ID = "id";
    /**
     * 操作明细
     */
    private final static String CONTENT = "content";

    /**
     * 切面存入日志
     */
    @AfterReturning(pointcut = "@annotation(afterSaleLog)", returning = "info")
    public void doAfterReturning(JoinPoint joinPoint, AfterSaleLog afterSaleLog, Object info) {
        try {
            // 日志对象拼接
            Integer userType = WebFrameworkUtils.getLoginUserType();
            Long id = WebFrameworkUtils.getLoginUserId();
            Map<String, String> formatObj = spelFormat(joinPoint, info);
            TradeAfterSaleLogCreateReqDTO dto = new TradeAfterSaleLogCreateReqDTO()
                    .setUserId(id)
                    .setUserType(userType)
                    .setAfterSaleId(MapUtil.getLong(formatObj, ID))
                    .setOperateType(MapUtil.getStr(formatObj, OPERATE_TYPE))
                    .setBeforeStatus(BEFORE_STATUS.get())
                    .setAfterStatus(AFTER_STATUS.get())
                    .setContent(MapUtil.getStr(formatObj, CONTENT));
            // 异步存入数据库
            afterSaleLogService.createLog(dto);
        } catch (Exception exception) {
            log.error("[doAfterReturning][afterSaleLog({}) 日志记录错误]", toJsonString(afterSaleLog), exception);
        }finally {
            clearThreadLocal();
        }
    }

    /**
     * 获取描述信息
     */
    public static Map<String, String> spelFormat(JoinPoint joinPoint, Object info) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AfterSaleLog afterSaleLogPoint = signature.getMethod().getAnnotation(AfterSaleLog.class);
        HashMap<String, String> result = Maps.newHashMapWithExpectedSize(2);
        Map<String, Object> spelMap = SpringExpressionUtils.parseExpression(joinPoint, info,
                asList(afterSaleLogPoint.id(), afterSaleLogPoint.content()));
        // TODO @chenchen：是不是抽成 3 个方法好点；毕竟 map 太抽象了；；
        // 售后ID
        String id = MapUtil.getStr(spelMap, afterSaleLogPoint.id());
        result.put(ID, id);
        // 操作类型
        String operateType = afterSaleLogPoint.operateType().description();
        result.put(OPERATE_TYPE, operateType);
        // 日志内容
        String content = MapUtil.getStr(spelMap, afterSaleLogPoint.content());
        if (ObjectUtil.isNotNull(afterSaleLogPoint.operateType())) {
            content += operateType;
        }
        result.put(CONTENT, content);
        return result;
    }

    public static void setBeforeStatus(Integer beforestatus) {
        BEFORE_STATUS.set(beforestatus);
    }

    public static void setAfterStatus(Integer afterStatus) {
        AFTER_STATUS.set(afterStatus);
    }

    private static void clearThreadLocal() {
        AFTER_STATUS.remove();
        BEFORE_STATUS.remove();
    }

}
