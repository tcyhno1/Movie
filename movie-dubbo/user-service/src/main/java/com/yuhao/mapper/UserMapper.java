package com.yuhao.mapper;

import com.yuhao.entity.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);



    User selectByPrimaryKey(Integer id);

    User getUserByLoginName(String loginName);

    int insertUserByEmail(User user);

    User selectByEmail(String email);

}