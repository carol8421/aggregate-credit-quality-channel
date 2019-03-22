package com.aggregate.framework.open.common.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisHandler {

    private RedisTemplate redisTemplate;

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }
    @PostConstruct
    public void init(){
        log.debug("[RedisHandler] is init!");
    }




    /**
     * 获取String类型key-value
     *
     * @return
     * @paramkey
     */
    public Object getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取String类型key-value
     *
     * @return
     * @paramkey
     */
    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置String类型key-value并添加过期时间(毫秒单位)
     *
     * @paramkey
     * @paramvalue
     * @paramtime过期时间,毫秒单位
     */
    public void setForTimeMS(String key, String value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MILLISECONDS);
    }

    /**
     * 设置String类型key-value并添加过期时间(分钟单位)
     *
     * @paramkey
     * @paramvalue
     * @paramtime过期时间,分钟单位
     */
    public void setForTimeMIN(String key, Object value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
    }


    /**
     * 设置String类型key-value并添加过期时间(分钟单位)
     *
     * @paramkey
     * @paramvalue
     * @paramtime过期时间,分钟单位
     */
    public void setForTimeCustom(String key, String value, long time, TimeUnit type) {
        redisTemplate.opsForValue().set(key, value, time, type);
    }

    /**
     * 如果key存在则覆盖,并返回旧值.
     * 如果不存在,返回null并添加
     *
     * @return
     * @paramkey
     * @paramvalue
     */
    public String getAndSet(String key, String value) {
        return (String) redisTemplate.opsForValue().getAndSet(key, value);
    }


    /**
     * 批量添加key-value(重复的键会覆盖)
     *
     * @paramkeyAndValue
     */
    public void batchSet(Map<String, String> keyAndValue) {
        redisTemplate.opsForValue().multiSet(keyAndValue);
    }

    /**
     * 批量添加key-value只有在键不存在时,才添加
     * map中只要有一个key存在,则全部不添加
     *
     * @paramkeyAndValue
     */
    public void batchSetIfAbsent(Map<String, String> keyAndValue) {
        redisTemplate.opsForValue().multiSetIfAbsent(keyAndValue);
    }

    /**
     * 对一个key-value的值进行加减操作,
     * 如果该key不存在将创建一个key并赋值该number
     * 如果key存在,但value不是长整型,将报错
     *
     * @paramkey
     * @paramnumber
     */
    public Long increment(String key, long number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    /**
     * 对一个key-value的值进行加减操作,
     * 如果该key不存在将创建一个key并赋值该number
     * 如果key存在,但value不是纯数字,将报错
     *
     * @paramkey
     * @paramnumber
     */
    public Double increment(String key, double number) {
        return redisTemplate.opsForValue().increment(key, number);
    }


    /**
     * 给一个指定的key值附加过期时间
     *
     * @return
     * @paramkey
     * @paramtime
     * @paramtype
     */
    public boolean expire(String key, long time, TimeUnit type) {
        return redisTemplate.boundValueOps(key).expire(time, type);
    }

    /**
     * 移除指定key的过期时间
     *
     * @return
     * @paramkey
     */
    public boolean persist(String key) {
        return redisTemplate.boundValueOps(key).persist();
    }


    /**
     * 获取指定key的过期时间
     *
     * @return
     * @paramkey
     */
    public Long getExpire(String key) {
        return redisTemplate.boundValueOps(key).getExpire();
    }

    /**
     * 修改key
     *
     * @return
     * @paramkey
     */
    public void rename(String key, String newKey) {
        redisTemplate.boundValueOps(key).rename(newKey);
    }

    /**
     * 删除key-value
     *
     * @return
     * @paramkey
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    //hash操作

    /**
     * 添加Hash键值对
     *
     * @paramkey
     * @paramhashKey
     * @paramvalue
     */
    public void put(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 批量添加hash的键值对
     * 有则覆盖,没有则添加
     *
     * @paramkey
     * @parammap
     */
    public void putAll(String key, Map<String, String> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 添加hash键值对.不存在的时候才添加
     *
     * @return
     * @paramkey
     * @paramhashKey
     * @paramvalue
     */
    public boolean putIfAbsent(String key, String hashKey, String value) {
        return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }


    /**
     * 删除指定hash的HashKey
     *
     * @paramkey
     * @paramhashKeys
     * @return删除成功的数量
     */
    public Long delete(String key, String... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }


    /**
     * 给指定hash的hashkey做增减操作
     *
     * @return
     * @paramkey
     * @paramhashKey
     * @paramnumber
     */
    public Long increment(String key, String hashKey, long number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 给指定hash的hashkey做增减操作
     *
     * @return
     * @paramkey
     * @paramhashKey
     * @paramnumber
     */
    public Double increment(String key, String hashKey, Double number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 获取指定key下的hashkey
     *
     * @return
     * @paramkey
     * @paramhashKey
     */
    public Object getHashKey(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }


    /**
     * 验证指定key下有没有指定的hashkey
     *
     * @return
     * @paramkey
     * @paramhashKey
     */
    public boolean hashKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 验证指定key是否存在
     *
     * @return
     * @paramkey
     * @paramhashKey
     */
    public boolean hashKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取key下的所有hashkey字段名
     *
     * @return
     * @paramkey
     */
    public Set<Object> hashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取对应keys的值
     * @author 大叔
     * @methodName hashGetAll
     * @param keyName
     * @param keys
     * @return java.util.List<java.lang.Object>
     * @exception
     * @date 2019/1/5 12:00
     */
    public List<Object> hashGetAll(String keyName,Collection<Object> keys){
        return redisTemplate.opsForHash().multiGet(keyName,keys);
    }

    /**
     * 获取指定hash下面的键值对数量
     *
     * @return
     * @paramkey
     */
    public Long hashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    //List操作

    /**
     * 指定list从左入栈
     *
     * @paramkey
     * @return当前队列的长度
     */
    public Long leftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 指定list从左出栈
     * 如果列表没有元素,会堵塞到列表一直有元素或者超时为止
     *
     * @paramkey
     * @return出栈的值
     */
    public Object leftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从左边依次入栈
     * 导入顺序按照Collection顺序
     * 如:abc=>cba
     *
     * @return
     * @paramkey
     * @paramvalues
     */
    public Long leftPushAll(String key, Collection<Object> values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 指定list从右入栈
     *
     * @paramkey
     * @return当前队列的长度
     */
    public Long rightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 指定list从右出栈
     * 如果列表没有元素,会堵塞到列表一直有元素或者超时为止
     *
     * @paramkey
     * @return出栈的值
     */
    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 从右边依次入栈
     * 导入顺序按照Collection顺序
     * 如:abc=>abc
     *
     * @return
     * @paramkey
     * @paramvalues
     */
    public Long rightPushAll(String key, Collection<Object> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }


    /**
     * 根据下标获取值
     *
     * @return
     * @paramkey
     * @paramindex
     */
    public Object popIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }


    /**
     * 获取列表指定长度
     *
     * @return
     * @paramkey
     * @paramindex
     */
    public Long listSize(String key, long index) {
        return redisTemplate.opsForList().size(key);
    }


    /**
     * 获取列表指定范围内的所有值
     *
     * @return
     * @paramkey
     * @paramstart
     * @paramend
     */
    public List<Object> listRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }


    /**
     * 删除key中值为value的count个数.
     *
     * @paramkey
     * @paramcount
     * @paramvalue
     * @return成功删除的个数
     */
    public Long listRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }


    /**
     * 删除列表[start,end]以外的所有元素
     *
     * @paramkey
     * @paramstart
     * @paramend
     */
    public void listTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);

    }

    /**
     * 将key右出栈,并左入栈到key2
     *
     * @paramkey右出栈的列表
     * @paramkey2左入栈的列表
     * @return操作的值
     */
    public Object rightPopAndLeftPush(String key, String key2) {
        return redisTemplate.opsForList().rightPopAndLeftPush(key, key2);

    }

    //set操作无序不重复集合

    /**
     * 添加set元素
     *
     * @return
     * @paramkey
     * @paramvalues
     */
    public Long add(String key, String... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 获取两个集合的差集
     *
     * @return
     * @paramkey
     * @paramkey2
     */
    public Set<Object> difference(String key, String otherkey) {
        return redisTemplate.opsForSet().difference(key, otherkey);
    }


    /**
     * 获取key和集合collections中的key集合的差集
     *
     * @return
     * @paramkey
     * @paramcollections
     */
    public Set<Object> difference(String key, Collection<Object> otherKeys) {
        return redisTemplate.opsForSet().difference(key, otherKeys);
    }

    /**
     * 将key与otherkey的差集,添加到新的newKey集合中
     *
     * @paramkey
     * @paramotherkey
     * @paramnewKey
     * @return返回差集的数量
     */
    public Long differenceAndStore(String key, String otherkey, String newKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherkey, newKey);
    }

    /**
     * 将key和集合collections中的key集合的差集添加到newkey集合中
     *
     * @paramkey
     * @paramotherKeys
     * @paramnewKey
     * @return返回差集的数量
     */
    public Long differenceAndStore(String key, Collection<Object> otherKeys, String newKey) {
        return redisTemplate.opsForSet().differenceAndStore(newKey, otherKeys, newKey);
    }

    /**
     * 删除一个或多个集合中的指定值
     *
     * @paramkey
     * @paramvalues
     * @return成功删除数量
     */
    public Long remove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 删除一个集合中的所有KEY 对应value
     * @param keys
     */
    public void remove(Collection  keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 随机移除一个元素,并返回出来
     *
     * @return
     * @paramkey
     */
    public Object randomSetPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 随机获取一个元素
     *
     * @return
     * @paramkey
     */
    public Object randomSet(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 随机获取指定数量的元素,同一个元素可能会选中两次
     *
     * @return
     * @paramkey
     * @paramcount
     */
    public List<Object> randomSet(String key, long count) {
        return redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 随机获取指定数量的元素,去重(同一个元素只能选择两一次)
     *
     * @return
     * @paramkey
     * @paramcount
     */
    public Set<Object> randomSetDistinct(String key, long count) {
        return redisTemplate.opsForSet().distinctRandomMembers(key, count);
    }

    /**
     * 将key中的value转入到destKey中
     *
     * @paramkey
     * @paramvalue
     * @paramdestKey
     * @return返回成功与否
     */
    public boolean moveSet(String key, Object value, String destKey) {
        return redisTemplate.opsForSet().move(key, value, destKey);
    }

    /**
     * 无序集合的大小
     *
     * @return
     * @paramkey
     */
    public Long setSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 判断set集合中是否有value
     *
     * @return
     * @paramkey
     * @paramvalue
     */
    public boolean isMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 返回key和othere的并集
     *
     * @return
     * @paramkey
     * @paramotherKey
     */
    public Set<Object> unionSet(String key, String otherKey) {
        return redisTemplate.opsForSet().union(key, otherKey);
    }

    /**
     * 返回key和otherKeys的并集
     *
     * @return
     * @paramkey
     * @paramotherKeykey的集合
     */
    public Set<Object> unionSet(String key, Collection<Object> otherKeys) {
        return redisTemplate.opsForSet().union(key, otherKeys);
    }

    /**
     * 将key与otherKey的并集,保存到destKey中
     *
     * @paramkey
     * @paramotherKey
     * @paramdestKey
     * @returndestKey数量
     */
    public Long unionAndStoreSet(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * 将key与otherKey的并集,保存到destKey中
     *
     * @paramkey
     * @paramotherKeys
     * @paramdestKey
     * @returndestKey数量
     */
    public Long unionAndStoreSet(String key, Collection<Object> otherKeys, String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 返回集合中所有元素
     *
     * @return
     * @paramkey
     */
    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    //Zset根据socre排序不重复每个元素附加一个socredouble类型的属性(double可以重复)

    /**
     * 添加ZSet元素
     *
     * @paramkey
     * @paramvalue
     * @paramscore
     */
    public boolean add(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 批量添加Zset<br>
     * Set<TypedTuple<Object>>tuples=newHashSet<>();<br>
     * TypedTuple<Object>objectTypedTuple1=newDefaultTypedTuple<Object>("zset-5",9.6);<br>
     * tuples.add(objectTypedTuple1);
     *
     * @return
     * @paramkey
     * @paramtuples
     */
    public Long batchAddZset(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        return redisTemplate.opsForZSet().add(key, tuples);
    }

    /**
     * Zset删除一个或多个元素
     *
     * @return
     * @paramkey
     * @paramvalues
     */
    public Long removeZset(String key, String... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 对指定的zset的value值,socre属性做增减操作
     *
     * @return
     * @paramkey
     * @paramvalue
     * @paramscore
     */
    public Double incrementScore(String key, Object value, double score) {
        return redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    /**
     * 获取key中指定value的排名(从0开始,从小到大排序)
     *
     * @return
     * @paramkey
     * @paramvalue
     */
    public Long rank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 获取key中指定value的排名(从0开始,从大到小排序)
     *
     * @return
     * @paramkey
     * @paramvalue
     */
    public Long reverseRank(String key, Object value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 获取索引区间内的排序结果集合(从0开始,从小到大,带上分数)
     *
     * @return
     * @paramkey
     * @paramstart
     * @paramend
     */
    public Set<ZSetOperations.TypedTuple<Object>> rangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * 获取索引区间内的排序结果集合(从0开始,从小到大,只有列名)
     *
     * @return
     * @paramkey
     * @paramstart
     * @paramend
     */
    public Set<Object> range(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取分数范围内的[min,max]的排序结果集合(从小到大,只有列名)
     *
     * @return
     * @paramkey
     * @parammin
     * @parammax
     */
    public Set<Object> rangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 获取分数范围内的[min,max]的排序结果集合(从小到大,集合带分数)
     *
     * @return
     * @paramkey
     * @parammin
     * @parammax
     */
    public Set<ZSetOperations.TypedTuple<Object>> rangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    /**
     * 返回分数范围内指定count数量的元素集合,并且从offset下标开始(从小到大,不带分数的集合)
     *
     * @return
     * @paramkey
     * @parammin
     * @parammax
     * @paramoffset从指定下标开始
     * @paramcount输出指定元素数量
     */
    public Set<Object> rangeByScore(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
    }

    /**
     * 返回分数范围内指定count数量的元素集合,并且从offset下标开始(从小到大,带分数的集合)
     *
     * @return
     * @paramkey
     * @parammin
     * @parammax
     * @paramoffset从指定下标开始
     * @paramcount输出指定元素数量
     */
    public Set<ZSetOperations.TypedTuple<Object>> rangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * 获取索引区间内的排序结果集合(从0开始,从大到小,只有列名)
     *
     * @return
     * @paramkey
     * @paramstart
     * @paramend
     */
    public Set<Object> reverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 获取索引区间内的排序结果集合(从0开始,从大到小,带上分数)
     *
     * @return
     * @paramkey
     * @paramstart
     * @paramend
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    /**
     * 获取分数范围内的[min,max]的排序结果集合(从大到小,集合不带分数)
     *
     * @return
     * @paramkey
     * @parammin
     * @parammax
     */
    public Set<Object> reverseRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 获取分数范围内的[min,max]的排序结果集合(从大到小,集合带分数)
     *
     * @return
     * @paramkey
     * @parammin
     * @parammax
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
    }

    /**
     * 返回分数范围内指定count数量的元素集合,并且从offset下标开始(从大到小,不带分数的集合)
     *
     * @return
     * @paramkey
     * @parammin
     * @parammax
     * @paramoffset从指定下标开始
     * @paramcount输出指定元素数量
     */
    public Set<Object> reverseRangeByScore(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
    }

    /**
     * 返回分数范围内指定count数量的元素集合,并且从offset下标开始(从大到小,带分数的集合)
     *
     * @return
     * @paramkey
     * @parammin
     * @parammax
     * @paramoffset从指定下标开始
     * @paramcount输出指定元素数量
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * 返回指定分数区间[min,max]的元素个数
     *
     * @return
     * @paramkey
     * @parammin
     * @parammax
     */
    public long countZSet(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * 返回zset集合数量
     *
     * @return
     * @paramkey
     */
    public long sizeZset(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取指定成员的score值
     *
     * @return
     * @paramkey
     * @paramvalue
     */
    public Double score(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 删除指定索引位置的成员,其中成员分数按(从小到大)
     *
     * @return
     * @paramkey
     * @paramstart
     * @paramend
     */
    public Long removeRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 删除指定分数范围内的成员[main,max],其中成员分数按(从小到大)
     *
     * @return
     * @paramkey
     * @parammin
     * @parammax
     */
    public Long removeRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * key和other两个集合的并集,保存在destKey集合中,列名相同的score相加
     *
     * @return
     * @paramkey
     * @paramotherKey
     * @paramdestKey
     */
    public Long unionAndStoreZset(String key, String otherKey, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * key和otherKeys多个集合的并集,保存在destKey集合中,列名相同的score相加
     *
     * @return
     * @paramkey
     * @paramotherKeys
     * @paramdestKey
     */
    public Long unionAndStoreZset(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * key和otherKey两个集合的交集,保存在destKey集合中
     *
     * @return
     * @paramkey
     * @paramotherKey
     * @paramdestKey
     */
    public Long intersectAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);
    }

    /**
     * key和otherKeys多个集合的交集,保存在destKey集合中
     *
     * @return
     * @paramkey
     * @paramotherKeys
     * @paramdestKey
     */
    public Long intersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey);
    }


    //==================================================map 操作===============================================================
    /**
     * 向redis中添加一个Map
     * @param key
     * @param map
     */
    public void putALL(String key, Map<String,Object> map){
        redisTemplate.opsForHash().putAll(key,map);
    }


    /**
     * 获取key下的所有hashkey和value
     *
     * @return
     * @paramkey
     */
    public Map<String,Object> getHashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }


    /**
     * 设置String类型key-value
     *
     * @paramkey
     * @paramvalue
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取key下的所有hash key
     *
     * @return
     * @paramkey
     */
    public Set<String> getHashEntriesKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取key下的所有hash Values
     *
     * @return
     * @paramkey
     */
    public List<Object> getHashEntriesValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }


    /**
     * 获取key-map下对应mapKey的value值
     * @param key
     * @param mapKey
     * @return
     */
    public Object getHashEntriesBykey(String key,String mapKey) {
        return redisTemplate.opsForHash().get(key,mapKey);
    }
}
