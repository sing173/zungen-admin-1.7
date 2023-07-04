---
--- 异步处理工单抢单队列
--- Created by dell.
--- DateTime: 2023/6/25 17:32
---

-- 参数
local orderId = ARGV[1]
local orderType = ARGV[2] -- 工单类型
local orderTypeAndID = orderType .. ':' .. orderId -- 工单类型:工单id
local userId = ARGV[3]

-- key
local orderPoolKey = KEYS[1]
local orderSearchKey = KEYS[2] .. ':' .. orderTypeAndID
local authKey = KEYS[3] .. ':' .. orderTypeAndID -- 工单人员权限key
local userOrderKey = KEYS[4] .. ':' .. userId -- 抢单人已抢到的工单


-- 判断是否当前人员有没抢到工单
if (redis.call('sismember', userOrderKey, orderTypeAndID) == 0) then
    -- 不存在则说明当前用户没有抢到单
    return ''
end

-- 删除工单池对应的工单id、人员权限列表、当前人员已抢单的工单
redis.call('srem', orderPoolKey, orderTypeAndID)
redis.call('del', authKey)
redis.call('srem', userOrderKey, orderTypeAndID)

-- 获取并返回工单简要信息,用cjson解析，更新状态并重新set，最后返回json字符串
local orderInfoStr = redis.call('get', orderSearchKey)
local orderInfJson = cjson.decode(orderInfoStr)
orderInfJson.status = 10
orderInfJson.auditor = userId
orderInfoStr = cjson.encode(orderInfJson)
redis.call('set', orderSearchKey, orderInfoStr)
-- 删除工单查询列表对应的工单 TODO 后续持久化到数据库后可删除
--redis.call('del', orderSearchKey)
return orderInfoStr





