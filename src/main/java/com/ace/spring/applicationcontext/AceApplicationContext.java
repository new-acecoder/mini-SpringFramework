package com.ace.spring.applicationcontext;

import com.ace.spring.annotation.Autowired;
import com.ace.spring.annotation.Component;
import com.ace.spring.annotation.ComponentScan;
import com.ace.spring.annotation.Scope;
import com.ace.spring.core.BeanDefinition;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Ace
 * @Date 2025/5/20 19:26
 */
public class AceApplicationContext {

    private Class configClass;

    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    public AceApplicationContext(Class configClass) {
        this.configClass = configClass;

        //解析配置类 ComponentScan注解-->扫描路径-->
        //扫描有@Component注解的类-->BeanDefinition-->beanDefinitionMap
        scanComponent(configClass);

        //创建单例bean
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if (beanDefinition.getScope().equals("singleton")) {
                //创建bean对象
                Object bean = createBean(beanDefinition);
                //放入单例池
                singletonObjects.put(beanName, bean);
            }
        }
    }

    private Object createBean(BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getClazz();
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();

            //简单的依赖注入
            //TODO 需要解决循环依赖问题
            for (Field declaredField : clazz.getDeclaredFields()) {
                //判断类中属性是否有@Autowired注解
                if(declaredField.isAnnotationPresent(Autowired.class)) {
                    //根据属性名调用getBean方法获取bean
                    Object bean = getBean(declaredField.getName());
                    //给属性赋值
                    declaredField.setAccessible(true);
                    declaredField.set(instance,bean);
                }
            }

            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
            
        }
    }
    private void scanComponent(Class configClass) {
        ComponentScan componentScanAnnotation = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
        String path = componentScanAnnotation.value();
        path = path.replace(".", "/");
        ClassLoader classLoader = AceApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile());
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File f : files){
                String fileName = f.getAbsolutePath();
                if(fileName.endsWith(".class")){
                    String className = fileName.substring(fileName.indexOf("com"),fileName.indexOf(".class"));
                    className = className.replace("\\",".");
                    try {
                        Class<?> clazz = classLoader.loadClass(className);
                        if(clazz.isAnnotationPresent(Component.class)){
                            //解析类，判断当前bean是singleton bean,还是prototype bean
                            Component componentAnnotation = clazz.getDeclaredAnnotation(Component.class);
                            String beanName = componentAnnotation.value();

                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setClazz(clazz);
                            if(clazz.isAnnotationPresent(Scope.class)){
                                Scope scopeAnnotation = clazz.getDeclaredAnnotation(Scope.class);
                                beanDefinition.setScope(scopeAnnotation.value());
                            }else{
                                beanDefinition.setScope("singleton");
                            }
                            beanDefinitionMap.put(beanName,beanDefinition);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public Object getBean(String beanName){
        if(beanDefinitionMap.containsKey(beanName)){
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if(beanDefinition.getScope().equals("singleton")){
                //singleton
                if(singletonObjects.containsKey(beanName)){
                    //如果单例池有，从单例池中获取
                    Object o = singletonObjects.get(beanName);
                    return o;
                }else{
                    //如果没有，创建bean对象加入到单例池中
                    Object bean = createBean(beanDefinition);
                    singletonObjects.put(beanName,bean);
                    return bean;
                }
            }else{
                //prototype
                //创建Bean对象
                Object bean = createBean(beanDefinition);
                return bean;
            }
        }else{
            throw new RuntimeException("No such bean");
        }
    }
}
