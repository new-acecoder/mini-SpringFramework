package com.ace.service;

import com.ace.spring.annotation.Autowired;
import com.ace.spring.annotation.Component;
import com.ace.spring.annotation.Scope;

/**
 * @Author Ace
 * @Date 2025/5/20 19:33
 */
@Component("userService")
@Scope("singleton")
public class UserService {

//    @Autowired
//    private OrderService orderService;

    public void add(){
        System.out.println("UserService add...");
    }
}
