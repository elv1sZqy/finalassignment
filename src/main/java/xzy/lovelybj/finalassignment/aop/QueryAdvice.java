package xzy.lovelybj.finalassignment.aop;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xzy.lovelybj.finalassignment.bean.Poem;
import xzy.lovelybj.finalassignment.util.RedisUtil;

import java.util.List;


/**
 * @author zhuQiYun
 * @create 2020/1/14
 * @description :
 */
@Component
@Aspect
public class QueryAdvice {
    private Logger log = LoggerFactory.getLogger(QueryAdvice.class);

    @Autowired
    private RedisUtil redisUtil;

    @Around("execution(* xzy.lovelybj.finalassignment.controller.*.*(..)) && @annotation(CanQueryFromRedis)")
    public Object advice(ProceedingJoinPoint proceedingJoinPoint) {
        String requestName = proceedingJoinPoint.getSignature().getName();
        log.info("请求的接口为:" + requestName);

        //获取传入参数的值
        Object[] args = proceedingJoinPoint.getArgs();
        String poet = (String) args[0];
        if (StringUtils.isBlank(poet)) {
            return null;
        }
        String dynasty = "".equals((String) args[1]) ? "all" : (String) args[1];
        String redisKey = "redis-" + poet + "-" + dynasty;

        log.info("诗人或者诗句的查询条件为:" + poet + ",朝代为:" + dynasty + ",redisKey:" + redisKey);

        if (redisUtil.exists(redisKey)) {
            return getPoemListFromRedis(redisKey);
        }
        List<Poem> proceed = getPoemListFromES(proceedingJoinPoint, redisKey);
        return proceed;
    }

    private List<Poem> getPoemListFromRedis(String redisKey) {
        log.info("从缓存中取值");
        List<Poem> poemList = (List<Poem>) redisUtil.get(redisKey);
        return poemList;
    }

    private List<Poem> getPoemListFromES(ProceedingJoinPoint proceedingJoinPoint, String redisKey) {
        log.info("从ES查询");
        // 开始执行方法
        long start = System.currentTimeMillis();
        List<Poem> proceed = null;
        try {
            proceed = (List<Poem>) proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        long useTime = (System.currentTimeMillis() - start) / 1000;
        if (CollectionUtils.isNotEmpty(proceed)) {
            redisUtil.set(redisKey, proceed, 3600L);
        }
        log.info("耗时:[" + useTime + "]s");
        return proceed;
    }
}
