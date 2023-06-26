package cn.iocoder.yudao.module.bpm.enums;

public interface BpmOrderRedisKeyConstants {
    //工单池集合 Redis Set，key为bpm:order:pool:kill，value成员为工单类型+id
    String BPM_ORDER_POOL_KILL_KEY = "bpm:order:pool:kill";
    //工单可参与抢单人员集合 Redis Set，key为bpm:order:pool:kill:auth:工单类型:工单id，value成员为人员id
    String BPM_ORDER_POOL_KILL_AUTH_KEY = "bpm:order:pool:kill:auth";
    //工单已抢到单人员集合 Redis Set，key为bpm:order:pool:kill:users:人员id，value成员为 工单类型:id
    String BPM_ORDER_POOL_KILL_USERS_KEY = "bpm:order:pool:kill:users";
    //工单池 Redis Set，作为查询工单池可抢工单信息用
    String BPM_ORDER_POOL_SEARCH_KEY = "bpm:order:pool:search";
    //进件工单类型
    String BPM_ORDER_CRM_INPUT_KEY = "crm_customer_input";


}
