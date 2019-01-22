package com.yuhao.exception;

import com.alibaba.fastjson.JSON;
import com.yuhao.util.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统一异常处理
 *
 * CustomExceptionResolver这个类名可以自己根据业务定义，但是继承了HandlerExceptionResolver之后，
 * 需要重写父类的方法，ModelAndView resolveException
 */

@Component
public class CustomExceptionResolver implements HandlerExceptionResolver {

    private static Logger log = LoggerFactory.getLogger(CustomExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception ex) {

        log.error("系统异常", ex);

        //如果是自定义异常，则显示ex.getMessage ，否则显示  系统系统异常，请稍后再试
        String errorMsg = null;
        if (ex instanceof BizException) {
            errorMsg = ex.getMessage();
        } else {
            errorMsg = "系统异常，请稍后再试";
        }


        try {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(AjaxResult.fail(errorMsg)));
            return null;
        } catch (IOException e) {
            log.error("系统异常", ex);
        }

        return null;
    }
}
