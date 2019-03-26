package com.aggregate.framework.open.common.components;

import com.aggregate.framework.exception.BusinessException;
import com.aggregate.framework.exception.ExceptionCode;
import com.aggregate.framework.open.annotations.ServiceChannel;
import com.aggregate.framework.open.annotations.ServiceMethod;
import com.aggregate.framework.open.bean.dto.CreditQualityDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CreditQualityDispatcher {


    @Value("${base.root.scanPackage}")
    private String baseRootScanPackage;

    /**
     * 保存扫描的所有的类名
     */
    private List<String> classNames = new ArrayList<String>();

    /**
     * 自定义注册IOC容器
     */
    private Map<String,Object> ioc = new HashMap<String,Object>();

    /**
     * 方法集合
     */
    private List<Handler> handlerMapping = new ArrayList<Handler>();


    @PostConstruct
    private void  init(){
        //扫描相关的类
        doScanner(baseRootScanPackage);

        //初始化扫描到的类，并且将它们放入到ICO容器之中
        doInstance();

        //初始化HandlerMapping
        initHandlerMapping();

        log.debug("Spring framework is init.");
    }


    private void initHandlerMapping() {
        if(ioc.isEmpty()){ return; }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();

            if(!clazz.isAnnotationPresent(ServiceChannel.class)){continue;}

            String serverName = "";
            if(clazz.isAnnotationPresent(ServiceChannel.class)){
                ServiceChannel channelMapping = clazz.getAnnotation(ServiceChannel.class);
                serverName = channelMapping.channelName();
            }

            //默认获取所有的public方法
            for (Method method : clazz.getMethods()) {
                if(!method.isAnnotationPresent(ServiceMethod.class)){continue;}

                ServiceMethod requestMapping = method.getAnnotation(ServiceMethod.class);
                String methodName = requestMapping.methodName();

                //通道.服务 名称
                serverName = serverName.concat(".").concat(methodName);
                this.handlerMapping.add(new Handler(serverName,entry.getValue(),method));
            }
        }
    }

    private void doInstance() {
        //初始化，为DI做准备
        if(classNames.isEmpty()){return;}

        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                if(!clazz.isAnnotationPresent(ServiceChannel.class)){continue;}

                //如果扫描到 @ServiceChannel 标签，则类为新的服务通道
                if(clazz.isAnnotationPresent(ServiceChannel.class)){
                    ServiceChannel requestMapping = clazz.getAnnotation(ServiceChannel.class);
                    String channelName = requestMapping.channelName();

                    Method method = clazz.getMethod("getInstance",null);
                    ioc.put(channelName,method.invoke(clazz,null));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void doScanner(String baseRootScanPackage) {
        File rootFile = null;
        try {
            rootFile = ResourceUtils.getFile("classpath:"+baseRootScanPackage.replaceAll("\\.","/"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        File classPath = rootFile;
        for (File file : classPath.listFiles()) {
            if(file.isDirectory()){
                doScanner(baseRootScanPackage + "." + file.getName());
            }else{
                if(!file.getName().endsWith(".class")){ continue;}
                String className = (baseRootScanPackage + "." + file.getName().replace(".class",""));
                classNames.add(className);
            }
        }
    }

    /**
     * 服务分发
     * @param creditQualityDto
     * @return
     */
    public Object doDispatch(CreditQualityDto creditQualityDto) throws BusinessException {
        Handler handler = getHandler(creditQualityDto.getServerName());
        if(handler == null){
            throw new BusinessException(ExceptionCode.CHANNEL_FAIL);
        }

        try {
            Object returnValue = handler.method.invoke(handler.controller,creditQualityDto);
            return returnValue;
        } catch (Exception e) {
            throw new BusinessException(ExceptionCode.CHANNEL_FAIL);
        }
    }

    private Handler getHandler(String serverName) {
        if(handlerMapping.isEmpty()){return null;}

        for (Handler handler : this.handlerMapping) {
            boolean isMatched  = StringUtils.equals(handler.getPattern(),serverName);
            if(!isMatched){ continue;}
            return handler;
        }
        return null;
    }


    public class Handler {

        private String pattern;
        private Method method;
        private Object controller;
        private Class<?> [] paramTypes;

        public String getPattern() {
            return pattern;
        }

        public Method getMethod() {
            return method;
        }

        public Object getController() {
            return controller;
        }

        public Class<?>[] getParamTypes() {
            return paramTypes;
        }

        private Map<String,Integer> paramIndexMapping;

        public Handler(String pattern, Object controller, Method method) {
            this.pattern = pattern;
            this.method = method;
            this.controller = controller;

            paramTypes = method.getParameterTypes();

            paramIndexMapping = new HashMap<String, Integer>();
            putParamIndexMapping(method);
        }

        private void putParamIndexMapping(Method method){
            //提取方法中的参数
            Class<?> [] paramsTypes = method.getParameterTypes();
            for (int i = 0; i < paramsTypes.length ; i ++) {
                Class<?> type = paramsTypes[i];
                paramIndexMapping.put(type.getName(),i);
            }

        }
    }
}
