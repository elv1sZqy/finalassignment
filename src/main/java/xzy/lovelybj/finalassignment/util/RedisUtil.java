package xzy.lovelybj.finalassignment.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zhuQiYun
 * @create 2020/1/14
 * @description :
 */
@Component
public class RedisUtil {


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取排行列表
     *
     * @param key
     * @param from
     * @param size
     * @return
     */
    public Set<String> getRang(String key, Integer from, Integer size) {
        return redisTemplate.opsForZSet().reverseRange(key, from, size);
    }

    /**
     * 获取排行列表并且有分数
     *
     * @param key
     * @param from
     * @param size
     * @return
     */
    public Set<String> getRangWithScores(String key, Integer from, Integer size) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, from, size);
    }

    public void addRang(String rangName, String key, Integer scores) {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        if (Objects.nonNull(zSetOperations.score(rangName, key))) {
            zSetOperations.incrementScore(rangName, key, scores);
            return;
        }
        zSetOperations.add(rangName, key, scores);
    }

    public Map<String, Object> getSingleRankAndScore(String rangName, String key) {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        Map<String, Object> map = new HashMap<>();
        Long rank = zSetOperations.rank(rangName, key);
        Double score = zSetOperations.score(rangName, key);
        map.put("rank", rank);
        map.put("score", score);
        return map;
    }
}
