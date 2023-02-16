package com.test.categorymanager.aspect;

import lombok.NoArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@NoArgsConstructor
@Component
public class LoggerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerAspect.class);

    @Pointcut("execution(* com.test.categorymanager.service.impl.*.save*(*))")
    public void savePointcut() {
    }

    @AfterReturning(pointcut = "savePointcut()", returning = "object")
    public void logAfterSave(JoinPoint jointPoint, Object object) {
        LOGGER.info("Category saved: [" + object.toString() + "]");
    }
}
