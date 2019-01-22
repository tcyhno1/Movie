package com.yuhao.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.yuhao.alipay.AlipayConfig;
import com.yuhao.entity.Movie;
import com.yuhao.entity.Order;
import com.yuhao.entity.OrderInfo;
import com.yuhao.exception.BizException;
import com.yuhao.mapper.*;
import com.yuhao.service.OrderService;
import com.yuhao.util.AjaxResult;
import com.yuhao.util.DecimalCalculate;
import com.yuhao.util.MyRandomUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("api/order")
public class OrderController {

    @Resource
    private MovieMapper movieMapper;
    @Resource
    private ShowDayMapper showDayMapper;
    @Resource
    private ShowTimeMapper showTimeMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderService orderService;


    @PostMapping("listSaledSeats.do")
    @ResponseBody
    public AjaxResult listSaledSeats(@RequestParam("showTimeId") Integer showTimeId) {
        return AjaxResult.success(orderMapper.listSaledSeats(showTimeId));
    }


    //进入addOrder之前，会先被拦截器过滤一遍，看看有没有登陆，有登陆就正常传进来，没登陆就从拦截器那里向前端传送一个600
    @PostMapping("addOrder.do")
    @ResponseBody
    public AjaxResult addOrder(HttpSession session,
                               @RequestParam("showTimeId") Integer showTimeId,
                               @RequestParam("movieId")  Integer movieId,
                               @RequestParam("selectedSeats[]") List<String> selectedSeats) {

        Integer userId = (Integer) session.getAttribute("id");
//        Integer x = (Integer) session.getAttribute("xx");
//        System.out.println(x);  -->null

        Order order = null;
        try {
            order = orderService.addOrder(showTimeId, movieId, selectedSeats, userId);
        } catch (BizException e) {
            return AjaxResult.fail(e.getMessage());
        }

        //请求支付宝获取表单
        try {
            String result = orderService.toPay(order.getOrderNo(), order.getTotalPrice());
            return AjaxResult.success(result);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            AjaxResult.fail("支付失败");
        }


        return null;
    }



    @RequestMapping(value = "notifyUrl.do",method = {RequestMethod.GET,RequestMethod.POST})
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {


        System.out.println("成功进入异步通知");

        //接收报文
        /* *
         * 功能：支付宝服务器异步通知页面
         * 日期：2017-03-30
         * 说明：
         * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
         * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。


         *************************页面功能说明*************************
         * 创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。
         * 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
         * 如果没有收到该页面返回的 success
         * 建议该页面只做支付成功的业务逻辑处理，退款的处理请以调用退款查询接口的结果为准。
         */

        //获取支付宝POST过来的反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = false; //调用SDK验证签名
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        //——请在这里编写您的程序（以下代码仅作参考）——

	/* 实际验证过程建议商户务必添加以下校验：
	1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
	3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
	4、验证app_id是否为该商户本身。
	*/
        if (signVerified) {//验证成功
            //商户订单号
            String out_trade_no = request.getParameter("out_trade_no");//我们给支付宝的orderId,被支付宝拿到简单处理再传回来

            //支付宝交易号，用于对账
            String trade_no = request.getParameter("trade_no");//支付宝自动生成的

            //交易状态
            String trade_status = request.getParameter("trade_status");//这个状态是支付宝的，表现的是支付宝是否操作成功

            String app_id = request.getParameter("app_id");

            String total_amount = request.getParameter("total_amount");

            Order order = orderMapper.selectOrderForLock(out_trade_no);

            System.out.println("asdf");

            //验证appid是否正确
            if (!(app_id.equals(AlipayConfig.app_id) && out_trade_no.equals(order.getOrderNo()) && total_amount.equals(order.getTotalPrice()))) {
                try {
                    response.getWriter().println("fail");
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {



                //异步通知是支付宝告诉商家的，只有收到异步通知，才进行数据库数据状态的更新
                orderService.updateOrderStatus(out_trade_no, trade_no);

            }

            try {
                //给支付宝返回一个success，如果不返回，支付宝就会持续发送异步通知
                response.getWriter().println("success");
            } catch (IOException e) {
                e.printStackTrace();
            }

//            out.println("success");

        } else {//验证失败
//            out.println("fail");

            //调试用，写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);

            try {
                response.getWriter().println("fail");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //——请在这里编写您的程序（以上代码仅作参考）——


    }

}
