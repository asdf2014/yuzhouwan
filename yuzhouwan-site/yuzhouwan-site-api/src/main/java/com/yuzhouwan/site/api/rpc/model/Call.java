package com.yuzhouwan.site.api.rpc.model;

import java.io.Serializable;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Call
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
public class Call implements Serializable {

    private Class<?> interfaces;
    private Object[] params;
    private Object result;  //这是存储服务端的计算结果的
    private String methodName;
    private Class<?>[] parameterTypes;

    public Class<?> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(Class<?> interfaces) {
        this.interfaces = interfaces;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}
