-- 参数
local orderId = ARGV[1] -- 正在抢的工单id
local orderType = ARGV[2] -- 工单类型
local orderTypeAndID = orderType .. ':' .. orderId -- 工单类型:工单id
local userId = ARGV[3] -- 抢单人id

-- key
local orderPoolKey = KEYS[1] -- 工单池key
local authKey = KEYS[2] .. ':' .. orderTypeAndID -- 工单人员权限key
local userOrderKey = KEYS[3] .. ':' .. userId -- 抢单人已抢到的工单

-- TODO 考虑是否加锁？

-- 查找工单池Key是否有当前正在抢的工单id
if (redis.call('sismember', orderPoolKey, orderTypeAndID) == 0) then
    -- 没有则说明已经被抢返回0
    return 0
end

-- 查找当前用户是否有权限抢单
if (redis.call('sismember', authKey, userId) == 0) then
    -- 没有则说明该用户不能抢当前工单
    return -1
end

-- 查找当前用户是否已经抢单
if (redis.call('sismember', userOrderKey, orderTypeAndID) == 1) then
    -- 存在则说明当前用户重复抢单
    return -2
end

-- 限制每个人员只能抢两张单
if (redis.call('scard', userOrderKey) == 2) then
    return -3
end

-- 在人员已抢到的工单加入当前工单id说明已抢到，后续完成工单后删除
redis.call('sadd', userOrderKey, orderTypeAndID)


-- 最后发送消息到队列中，由消费者异步处理工单后续业务，如更新当前工单任务审批人为抢单人
-- XADD key 队列名 消息id（*代表由redis生成） 消息（k1 v1 k2 v2 ...）
--redis.call('xadd', 'bpm.order.streams', '*', 'id', tonumber(orderId), 'userId', tonumber(userId))

return 1

