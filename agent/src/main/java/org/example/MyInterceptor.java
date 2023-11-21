package org.example;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;


@Slf4j
public class MyInterceptor {
    @RuntimeType
    public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable) {
        long start = System.currentTimeMillis();
        log.info("agent test: before method invoke! Method name: " + method.getName());
        System.out.println("agent test: before method invoke! Method name: " + method.getName());
        try {
            return callable.call();
        } catch (Exception e) {
            // 进行异常信息上报
            System.out.println("方法执行发生异常" + e.getMessage());
        } finally {
            System.out.println("agent test: after method invoke! Method name: " + method.getName());
            System.out.println(method + ": took " + (System.currentTimeMillis() - start) + " millisecond");
        }
        return null;
    }
}
