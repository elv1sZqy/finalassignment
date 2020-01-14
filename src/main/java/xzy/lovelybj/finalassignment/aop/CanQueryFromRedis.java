package xzy.lovelybj.finalassignment.aop;

import java.lang.annotation.*;

/**
 * @author zhuQiYun
 * @create 2020/1/14
 * @description :
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CanQueryFromRedis {
}
