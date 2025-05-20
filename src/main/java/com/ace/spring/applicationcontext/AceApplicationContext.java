package com.ace.spring.applicationcontext;

/**
 * @Author Ace
 * @Date 2025/5/20 19:26
 */
public class AceApplicationContext {

    private Class configClass;

    public AceApplicationContext(Class configClass) {
        this.configClass = configClass;
    }

    public Object getBean(String beanName){
        return null;
    }
}
