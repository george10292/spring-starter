package com.dmdev.logging.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
public class CommonPointcuts {
    /*
    @within проверяет аннотацию на уровне класса
     */
    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void isControllerLayer() {
    }

    /*
    within проверяет class type name
     */
    @Pointcut("within(com.dmdev.*.service.*Service)")
    public void isServiceLayer() {}
}
