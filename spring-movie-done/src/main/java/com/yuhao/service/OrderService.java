package com.yuhao.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.yuhao.alipay.AlipayConfig;
import com.yuhao.entity.Movie;
import com.yuhao.entity.Order;
import com.yuhao.entity.OrderInfo;
import com.yuhao.exception.BizException;
import com.yuhao.mapper.*;
import com.yuhao.util.DecimalCalculate;
import com.yuhao.util.MyRandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderService {

    private static Logger log = LoggerFactory.getLogger(OrderService.class);


    @Resource
    private MovieMapper movieMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderInfoMapper orderInfoMapper;

    /**
     * 添加订单业务
     *
     * @param showTimeId
     * @param movieId
     * @param selectedSeats
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    public Order addOrder(@RequestParam("showTimeId") Integer showTimeId, @RequestParam("movieId") Integer movieId, @RequestParam("selectedSeats[]") List<String> selectedSeats, Integer userId) throws BizException {

        //校验座位是否可买
        for (String seat : selectedSeats) {
            int count = orderMapper.countBySeat(showTimeId,seat);
            if (count>0){
                throw new BizException("票已售出，请重新选座");
            }
        }

        Movie movie = movieMapper.selectByPrimaryKey(movieId);
        Double totalPrice = DecimalCalculate.mul(Double.parseDouble(movie.getPrice()), selectedSeats.size());

        //插入主表
        Order order = new Order();
        order.setUserId(userId);
        order.setShowTimeId(showTimeId);
        order.setTotalPrice(totalPrice.toString());
        order.setOrderNo(System.currentTimeMillis() + MyRandomUtil.getRandom(6));
        order.setSeatCode(MyRandomUtil.getRandom(6));
        orderMapper.insertOrder(order);

        //Mapper.xml文件中配置，<selectKey keyProperty="id" order="AFTER" resultType="java.lang.Integer">
        //      SELECT LAST_INSERT_ID()
        //    </selectKey>   或
        //  keyProperty="id" useGeneratedKeys="true". 可以在插入数据库数据的时候返回随着这条数据新增的ID。


        //插入info从表
        for (String seat : selectedSeats) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(order.getId());
            orderInfo.setPrice(movie.getPrice());
            orderInfo.setSeat(seat);
            orderInfoMapper.insertOderInfo(orderInfo);
        }

        return order;
    }


    /**
     * 获取支付form表单
     *
     * @param orderNo
     * @param money
     * @return
     * @throws AlipayApiException
     */
    public String toPay(String orderNo, String money) throws AlipayApiException {
        //调用支付宝支付接口
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = orderNo;
        //付款金额，必填
        String total_amount = money;
        //订单名称，必填
        String subject = "于武聪国际影城";
        //商品描述，可空
        String body = "电影票";

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        return result;

    }


    /**
     * 用于异步通知时，更新数据库里订单状态并且加上trade_no。
     *
     * @param out_trade_no
     * @param trade_no
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderStatus(String out_trade_no, String trade_no) throws Exception {

        //锁订单
        Order order = orderMapper.selectOrderForLock(out_trade_no);
        if (order == null) {
            throw new BizException("订单有误");
        }

        //幂等，同一件事情只能执行一次
        if (order.getStatus() == 2) {
            return;
        }

        //更新充值数据
        orderMapper.updateOrderStatus(out_trade_no, trade_no);

    }


    /**
     * 取消订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder() {

        //查询超过十分钟未支付的订单列表
        List<Integer> ids = orderMapper.listCancelOrders();


        for (Integer id : ids){
            //锁住
            orderMapper.selectByPrimaryKeyForLock(id);
            //修改状态为3，已取消
            orderMapper.updateStatusById(id);
            log.info("取消订单，id={}", id);
        }

    }


}
