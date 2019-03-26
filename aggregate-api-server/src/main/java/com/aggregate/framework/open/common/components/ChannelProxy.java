package com.aggregate.framework.open.common.components;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Component
public class ChannelProxy  implements MethodInterceptor {

    public Object getInstance(Object obj){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(obj.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        Boolean isCallThird =Boolean.FALSE;

        Object obj = before();

        if(Objects.nonNull(obj)){
            obj = methodProxy.invokeSuper(o,objects);
            isCallThird = Boolean.TRUE;
        }

        if(isCallThird){
            after();
        }
        return obj;
    }

    private Object before(){
        //1.检查用户是否开通通道接口

        //2.检查用户金额是否满足本次操作

        //3.cache数据检查
        System.out.println("before方法");
        return null;
    }

    private void after(){
        //1.确认调用后扣款

        //2.调用次数 +1

        System.out.println("after方法");
    }
}
