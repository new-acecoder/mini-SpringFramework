package com.ace.service;

import com.ace.spring.annotation.Autowired;
import com.ace.spring.annotation.Component;

/**
 * @Author Ace
 * @Date 2025/5/22 22:55
 */
@Component("employeeService")
public class EmployeeService {
    @Autowired
    private OrderService orderService;

    public void test(){
        orderService.add();
    }
}
