package com.yuhao.service.impl;


import com.yuhao.entity.User;
import com.yuhao.mapper.UserMapper;
import com.yuhao.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private  UserMapper userMapper;



    public void mailSignUp (@RequestParam String email,
                            @RequestParam String password){


        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        userMapper.insertUserByEmail(user);


    }


    public User getUserByLoginName(String loginName){
        return userMapper.getUserByLoginName(loginName);
    }

    public User selectByEmail(String email){
        return userMapper.selectByEmail(email);
    }



}
