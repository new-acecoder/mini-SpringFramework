package com.ace.test;

import com.ace.service.OrderService;
import com.ace.service.UserService;
import com.ace.spring.applicationcontext.AceApplicationContext;
import com.ace.test.config.AppConfig;

/**
 * @Author Ace
 * @Date 2025/5/20 19:29
 */
public class Test {
    public static void main(String[] args) {

        AceApplicationContext applicationContext = new AceApplicationContext(AppConfig.class);

        OrderService orderService = (OrderService) applicationContext.getBean("orderService");
        orderService.add();
    }
}
