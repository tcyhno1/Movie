package com.yuhao.service;


import com.yuhao.entity.User;
import com.yuhao.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Service
public class UserService {

    private static Logger log = LoggerFactory.getLogger(OrderService.class);

    @Resource
    private  UserMapper userMapper;



    public void mailSignUp (@RequestParam String email,
                            @RequestParam String password){


        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        userMapper.insertUserByEmail(user);


    }


}
