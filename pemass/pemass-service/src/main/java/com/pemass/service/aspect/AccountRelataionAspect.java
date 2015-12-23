package com.pemass.service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @Description: AccountRelataionAspect
 * @Author: luoc
 * @CreateTime: 2015-01-20 17:34
 */
@Component
@Aspect
public class AccountRelataionAspect {

    @After(value = "execution(public * com.pemass.service.pemass.biz.impl.AccountRelationServiceImpl.updateRelieveRelation(..))")
    public void afterSave() {
        System.out.println("===============after updateRelieveRelation================");

    }
//    @Before(value = "execution(public * com.pemass.service.pemass.biz.AccountRelationService.updateRelieveRelation(..))")
//    public void beforeSave() {
//        System.out.println("===============before updateRelieveRelation================");
//
//    }
    @Around("execution(public * com.pemass.service.pemass.biz.impl.AccountRelationServiceImpl.updateRelieveRelation(..))")
    public void aroundQuery(ProceedingJoinPoint pjp) {
        System.out.println("Around update.  1");
        try {

            pjp.proceed();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Around update.  2");
    }
}
