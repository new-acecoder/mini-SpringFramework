package com.ace.spring.core;

/**
 * @Author Ace
 * @Date 2025/5/20 22:32
 */
public class BeanDefinition {
    private Class clazz;
    private String scope;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
