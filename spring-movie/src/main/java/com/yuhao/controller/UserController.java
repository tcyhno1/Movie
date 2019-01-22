package com.yuhao.controller;


import com.yuhao.entity.User;
import com.yuhao.mapper.UserMapper;
import com.yuhao.service.UserService;
import com.yuhao.util.AjaxResult;
import com.yuhao.util.MD5Tools;
import com.yuhao.util.MailUtil;
import com.yuhao.util.MyRandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("api/user")
public class UserController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;

    @PostMapping("login.do")
    @ResponseBody
    public AjaxResult login(HttpSession session,
                            @RequestParam(value = "loginName") String loginName,
                            @RequestParam(value = "password") String psw) {

        String a = psw+loginName;
        String password = MD5Tools.MD5(MD5Tools.MD5(a));

        log.info("登录账号：{}；密码：{}", loginName, password);

        //参数校验
        if (loginName == null || "".equals(loginName) || password == null || "".equals(password)) {
            return AjaxResult.fail("账号或密码错误"); //这里可以统一异常处理
        }

        //查询数据库判断用户名和密码是否正确
        User user = userMapper.getUserByLoginName(loginName);
        if (user == null) {
            return AjaxResult.fail("账号或密码错误");
        }

        //校验密码
        String password1 = user.getPassword();
        if (!(password1 != null && password1.equals(password))) {
            return AjaxResult.fail("账号或密码错误");
        }

        session.setAttribute("loginName", loginName);
        session.setAttribute("id", user.getId());

        return AjaxResult.success();
    }


    /**
     * 获取用户信息
     *
     * @param session
     * @return
     */
    @PostMapping(value = "getUser.do")
    @ResponseBody
    public AjaxResult getUser(HttpSession session) {

        String loginName = (String) session.getAttribute("loginName");

        User user = userMapper.getUserByLoginName(loginName);

        return AjaxResult.success(user);
    }


    /**
     * 发送验证码
     *
     * @param session
     * @param email
     * @return
     */
    @PostMapping(value = "sendCode.do")
    @ResponseBody
    public AjaxResult sendCode(HttpSession session,
                               @RequestParam String email) {
        try {
            String validateCode = MyRandomUtil.getRandom(6);

            MailUtil.sendTextMail(email, "大猿影城邮箱验证", "验证码:" + validateCode + ",有效时间1分钟");

            String validateCode_time = validateCode + "_" + System.currentTimeMillis();

            session.setAttribute("validateCode_time", validateCode_time);
            session.setAttribute("email", email);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AjaxResult.fail("邮件发送失败");
        }

        return AjaxResult.success();
    }


    @PostMapping(value = "signUp.do")
    @ResponseBody
    public AjaxResult signUp(HttpSession session,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String validateCode) {

        System.out.println();
        try {

            //从session中拿到的是Object类，需要转型
            String validateCode_time = (String) session.getAttribute("validateCode_time");

            //如果validateCode_time为空的话，说明sendCode方法没有被执行，那么用户直接signUp传过来的验证码肯定是不存在的。
            if (validateCode_time == null) {
                return AjaxResult.fail("验证码不正确");
            }

            String validateCode_session = validateCode_time.split("_")[0];
            String time_session = validateCode_time.split("_")[1];

            Long oldTime = Long.valueOf(time_session);
            Long newTime = System.currentTimeMillis();

            if ((newTime - oldTime) > 60 * 1000) {
                return AjaxResult.fail(601, "验证码已失效");
            }


            String email_session = (String) session.getAttribute("email");
            if (email_session == null || !email_session.equals(email)) {
                return AjaxResult.fail("邮箱不正确");
            }

            if (!validateCode.equals(validateCode_session)) {
                return AjaxResult.fail("验证码不正确");
            }

            //todo 注册业务

            User user = userMapper.selectByEmail(email);
            if (user != null) {
                return AjaxResult.fail("该邮箱已经注册");
            }

            userService.mailSignUp(email, password);


        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AjaxResult.fail("注册失败");
        }


        return AjaxResult.success();
    }


}
