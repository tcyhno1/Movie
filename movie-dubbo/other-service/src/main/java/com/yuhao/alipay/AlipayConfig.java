package com.yuhao.alipay;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2018082822242264868";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCEOMlkWUjcInhnyzPAZZm2PrLP OHmQh+iy9VNHImTLbp4S2ox4mw6F1Y5cMunZjkl+tzdNNmpmtnPp/fdRk1mmE2pPPxUcFgF1RHED ueDRdBqhO3AQsYdrlAO0B3rOzazzzvpY2yDo9mJ4RJO7QvoB2i2pxZUJlyGK4NITG2cTmL7644xk B5wb8vqmVH3sGk1Rx1D2qDHgkggJzokUhoEz6NkSjp/obMxhU+aGlTlzz1d/xk/E4jHBGybg0KU6 C2ZG/hSYXfrJlgPp0AfMPWClKhjc8iEirKiWOBORjNglN8wBrI9S4IgfBo3xCYNaYkDQSycRFXnk gUZpYPl/Gt7FAgMBAAECggEAeOGh3YhzQC6TffEc4y0AFsnDmAe9vBFFATegZ0xeck4zXnFIM4su /9R6TniNq0SWlK7UdFbnd/geiTKSxZW9RERD0SePGRpVQuzLleh+TX+kSPuaPOjRMd0kdZbRA/it wkjcQBg2QkXfSK0Jkw5smvzo+mmVFCuDkwBvjuBioFqLGEG04slCgIe0xKGBtJgQD7c3XyrSy44j foHdZ4KvWrfWCY6epUaeVY/eIwMPQgiIuyOoHdWMRh2Ink99CxhxZzUMRZb+U3oUYCwm3WUlwIKv WbOYUBeyCSuFNPEZ68cri4bCy09TBJZMsGs/7VlVahk0tbbL3Vkh4L0e7i7GEQKBgQD08iMPM+sF 3NmPSf/9oUjSWwwQMDuP2Z0clxwT2oho+t/4HuFCs9qAPFZIShVNYiHqCvnxUGY7UNhRCH/T/Akg DWpVA+Mp8ySIQAH8zQgHGWkL663rzTvJrY6JWcizE6zeREPd6g9Y8TPGmP4Aw+gTgK52yNFbDxar Ndv7ijC7gwKBgQCKMFjupnjRv49SY85Y2vPqQjlEEDqvkeHVZf40LIH4cgSndAH2LwdaQrOrHD6g WmG4tSAq1cYcFy1bnR0JHg9qM70gfU7b752ycjT3dKwHz8XWJfp0LV0JW7FBNHDVQG9DC2rsBXXg W4j53oOWTss+MuxAGrAGEZrxFzAE3UECFwKBgQCFXqJAUOCTHKFJJ92+1+aywuhamCiWNsMZIh4j oUXNA2yXyRiWHR/H2xKP4eafb+NPcBKPlltTyL0/wgT4A69kSGmxSKCT4tPtbAyo/Wp1kxxjZTwZ vHHlah7GIqDpALmLfFgHqL4ai5rJc9w5xnHCzLox6z4y+pA5QOY6nA/UyQKBgD1ibJ7WZCUIKuo2 W5ss59zPvhB2BRuoeeEJhXo3qyRpwZeA+L4a9fEqYBVKXqdsfqxGCRv+ChrAUO25RQSfzcknYoPk Jfuc3Tg/czfZI/H0YbVW+YJSBFn72SlV2ilgjgwCLmTZqBsZjicwqNND/RSMGlX/hOGtQF6IJlSC 1DXRAoGASKVxDkyrjkhR8BJoFHS0M2PDt12jSMM7924iRqA7L4JPUSu6x1McpJG3SjZPfExeCvlQ W1ZO4o1883MH/KpF7Mu/qlgK+JyvCN5bMhd5pmjz666Lyz7J5BYAmVDva/N5RPaaauEI0ye7aWaN 5/Lun+8OpSpuKCFSpBhxzxhuE/w=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4SZgs0ZcIyvfS4hv2WEY PtoLpTCmza2IQKsLWxXHWwIFgPsnDB61jvNG9Oij7pmxx/+JMOWkcdVj1ltoRShH Dy3HAelIYxXnpKnHI3x9c6PyYUIX+szZ/dlkCEEbJPQ0tQqvkauK2ue3gDTBeWw/ frSbRUh0sQu47IUX8XO0MoUZDo62/5mnRnq4S+BKltL08eV7Cb5U+d/sIoce/IuR rDe9IKzdRk+efsSGK52nttuMa5NBOXpZrOg4MdLlhepc/g5xuZAIHkn+MAlW/gRD NfKvfOHh49ghpkO8ilNCQEC3KTn6uccl1DfxWcbgm0Slk5d/4XJ0dJWkarcsTcr2 nQIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问（支付宝通知商户，商户通过接口获取）
    public static String notify_url = "http://taiyuanyijingywcymovie.frp3.chuantou.org/api/order/notifyUrl.do";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问（支付宝通知买家，买家通过页面获取）
    public static String return_url = "http://taiyuanyijingywcymovie.frp3.chuantou.org/index.html";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "http://payhub.dayuanit.com/gateway/alipay/web.do";

    // 支付宝网关
    public static String log_path = "C:\\";

}
