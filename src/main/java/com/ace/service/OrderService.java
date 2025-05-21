package com.ace.service;

import com.ace.spring.annotation.Autowired;
import com.ace.spring.annotation.Component;

/**
 * @Author Ace
 * @Date 2025/5/21 14:58
 */
@Component("orderService")
public class OrderService {

    @Autowired
    private UserService userService;

    public void add(){
        userService.add();
    }
}
