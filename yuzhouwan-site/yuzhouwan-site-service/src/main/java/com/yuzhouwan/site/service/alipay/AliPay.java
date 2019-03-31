package com.yuzhouwan.site.service.alipay;

import com.yuzhouwan.common.util.PropUtils;

//import static com.alipay.api.AlipayConstants.APP_ID;

//import com.alipay.api.AlipayApiException;
//import com.alipay.api.AlipayClient;
//import com.alipay.api.DefaultAlipayClient;
//import com.alipay.api.FileItem;
//import com.alipay.api.request.AlipayOfflineMaterialImageUploadRequest;
//import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
//import com.alipay.api.request.AlipayUserUserinfoShareRequest;
//import com.alipay.api.response.AlipayOfflineMaterialImageUploadResponse;
//import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
//import com.alipay.api.response.AlipayUserUserinfoShareResponse;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: AliPay
 *
 * @author Benedict Jin
 * @since 2016/8/12
 */
public class AliPay {

    private static final String APP_PRIVATE_KEY = PropUtils.getInstance().getProperty("APP_PRIVATE_KEY");
    private static final String ALIPAY_PUBLIC_KEY = PropUtils.getInstance().getProperty("ALIPAY_PUBLIC_KEY");

//    void request() throws AlipayApiException {
//
//        //实例化客户端
//        AlipayClient client = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
//                APP_ID, APP_PRIVATE_KEY, "json", "GBK", ALIPAY_PUBLIC_KEY);
//
//        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：
//          alipay.open.public.template.message.industry.modify
//        AlipayOpenPublicTemplateMessageIndustryModifyRequest request =
//          new AlipayOpenPublicTemplateMessageIndustryModifyRequest();
//
//        //SDK已经封装掉了公共参数，这里只需要传入业务参数
//        //此次只是参数展示，未进行字符串转义，实际情况下请转义
//        Map<String, Object> params = new HashMap<>();
//        params.put("primary_industry_name", "IT科技/IT软件与服务");
//        params.put("primary_industry_code", "10001/20102");
//        params.put("secondary_industry_code", "10001/20102");
//        params.put("secondary_industry_name", "IT科技/IT软件与服务");
//        request.setBizContent(JSON.toJSONString(params));
//
//        AlipayOpenPublicTemplateMessageIndustryModifyResponse response = client.execute(request);
//        //调用成功，则处理业务逻辑
//        if (response.isSuccess()) {
//            //.....
//            System.out.println(response.toString());
//        }
//    }
//
//    void uploadImage() throws AlipayApiException {
//        AlipayClient client = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
//                APP_ID, APP_PRIVATE_KEY, "json", "GBK", ALIPAY_PUBLIC_KEY);
//
//        // 实例化具体API对应的request类,类名称和接口名称对应，当前调用接口名称：alipay.offline.material.image.upload
//        AlipayOfflineMaterialImageUploadRequest request = new AlipayOfflineMaterialImageUploadRequest();
//        request.setImageName("test");
//        FileItem item = new FileItem("C:/Downloads/ooopic_963991_7eea1f5426105f9e6069/16365_1271139700.jpg");
//        request.setImageType("JPG");
//        request.setImageContent(item);
//
//        //执行API请求
//        AlipayOfflineMaterialImageUploadResponse response = client.execute(request);
//
//        //调用成功，则处理业务逻辑
//        if (response.isSuccess()) {
//            //获取图片访问地址
//            String imageUrl = response.getImageUrl();
//            //.....
//            System.out.println(imageUrl);
//        }
//    }
//
//    void auth() throws AlipayApiException {
//        AlipayClient client = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
//                APP_ID, APP_PRIVATE_KEY, "json", "GBK", ALIPAY_PUBLIC_KEY);
//        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.user.userinfo.share
//        AlipayUserUserinfoShareRequest request = new AlipayUserUserinfoShareRequest();
//        //授权类接口执行API调用时需要带上accessToken
//        AlipayUserUserinfoShareResponse response = client.execute(request, "accessToken");
//        //业务处理
//        //...
//        System.out.println(response.isSuccess());
//    }
}
