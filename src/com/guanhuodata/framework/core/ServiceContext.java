package com.guanhuodata.framework.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServiceContext {
    private static ServiceContext sc;
    private static ApplicationContext appCtx;
    private ServiceContext(){
        appCtx = new ClassPathXmlApplicationContext("classpath*:application-*.xml","classpath*:/conf/application-*.xml");
    };
    public static ServiceContext getInstance(){
        if(sc==null){
            sc = new ServiceContext();
        }
        return sc;
    }
    public Object getService(String serviceId){
        return appCtx.getBean(serviceId);
    }
    public <T> T getService(String serviceId,Class<T> clazz){
        return (T)appCtx.getBean(serviceId,clazz);
    }
}
