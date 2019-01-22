package com.yuhao.service;

import com.yuhao.entity.User;

public interface UserService {

    public void mailSignUp(String email, String password);

    public User getUserByLoginName(String loginName);

    public User selectByEmail(String email);


}
