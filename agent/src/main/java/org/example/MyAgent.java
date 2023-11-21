package org.example;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

/**
 * @author wangyang
 */
@Slf4j
public class MyAgent {

    public static void premain(String agentArgs,
                               Instrumentation inst) {
//        System.out.println("premain");
//        log.info("premain");
//        new AgentBuilder.Default()
//                .type(ElementMatchers.nameContainsIgnoreCase("com"))
//                .transform((builder, type, classLoader, module, protectionDomain) ->
//                        builder.method(ElementMatchers.any())
//                                .intercept(MethodDelegation.to(MyInterceptor.class))
//                ).installOn(instrumentation);
        System.out.printf("premain, agentArgs: %s%n", agentArgs);
        System.out.printf("premain, inst: %s%n", inst);

        try {
            // 拦截spring controller
            AgentBuilder.Identified.Extendable builder1 = new AgentBuilder.Default()
                    // 拦截@Controller 和 @RestController的类
                    .type(ElementMatchers.isAnnotatedWith(ElementMatchers.named("org.springframework.stereotype.Controller")
                            .or(ElementMatchers.named("org.springframework.web.bind.annotation.RestController"))))
                    .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) ->
                            // 拦截 @RestMapping 或者 @Get/Post/Put/DeleteMapping
                            builder.method(ElementMatchers.isPublic().and(ElementMatchers.isAnnotatedWith(
                                            ElementMatchers.nameStartsWith("org.springframework.web.bind.annotation")
                                                    .and(ElementMatchers.nameEndsWith("Mapping")))))
                                    // 拦截后交给 SpringControllerInterceptor 处理
                                    .intercept(MethodDelegation.to(MyInterceptor.class)));
            // 装载到 instrumentation 上
            builder1.installOn(inst);
        } catch (Exception e) {
            System.out.printf("agent error, inst: %s%n", e.getMessage());
            e.printStackTrace();
        }
    }
}