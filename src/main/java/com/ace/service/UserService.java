package com.ace.service;

import com.ace.spring.annotation.Component;
import com.ace.spring.annotation.Scope;

/**
 * @Author Ace
 * @Date 2025/5/20 19:33
 */
@Component("userService")
//@Scope("prototype")
public class UserService {
    public void add(){
        System.out.println("add user");
    }
}
