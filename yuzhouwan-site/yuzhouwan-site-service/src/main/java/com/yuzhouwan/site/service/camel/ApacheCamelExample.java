package com.yuzhouwan.site.service.camel;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpMessage;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Apache Camel Example
 *
 * @author Benedict Jin
 * @since 2016/8/5
 */
public class ApacheCamelExample extends RouteBuilder {

    public static void main(String... args) throws Exception {

        // 这是camel上下文对象，用来驱动所有路由
        ModelCamelContext camelContext = new DefaultCamelContext();
        // 启动route
        camelContext.start();
        // 将我们编排的一个完整消息路由过程，加入到上下文中
        camelContext.addRoutes(new ApacheCamelExample());

        /*
         * ==========================
         * 为什么我们先启动一个Camel服务
         * 再使用addRoutes添加编排好的路由呢？
         * 这是为了告诉各位读者，Apache Camel支持 动态加载/卸载编排 的路由
         * 这很重要，因为后续设计的Broker需要依赖这种能力
         * ==========================
         */

        // 通用没有具体业务意义的代码，只是为了保证主线程不退出
        synchronized (ApacheCamelExample.class) {
            ApacheCamelExample.class.wait();
        }
    }

    @Override
    public void configure() throws Exception {
        // 在本代码段之下随后的说明中，会详细说明这个构造的含义
        // http://127.0.0.1:8282/doHelloWorld
        from("jetty:http://0.0.0.0:8282/doHelloWorld")
                .process(new HttpProcessor())
                // ndPoint控制端点的URI描述（log:helloworld?showExchangeId=true
                // 表明它是一个Log4j的实现，所以消息最终会以Log日志的方式输出到控制台上
                .to("log:helloworld?showExchangeId=true");
    }

    /**
     * 这个处理器用来完成输入的json格式的转换.
     */
    private class HttpProcessor implements Processor {

        /* (non-Javadoc)
         * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
         */
        @Override
        public void process(Exchange exchange) throws Exception {
            // 因为很明确消息格式是http的，所以才使用这个类
            // 否则还是建议使用org.apache.camel.Message这个抽象接口
            HttpMessage message = (HttpMessage) exchange.getIn();
            InputStream bodyStream = (InputStream) message.getBody();
            String inputContext = this.analysisMessage(bodyStream);
            bodyStream.close();

            // 存入到 exchange的 out区域
            if (exchange.getPattern() == ExchangePattern.InOut) {
                Message outMessage = exchange.getOut();
                outMessage.setBody(inputContext + " || out");
            }
        }

        /**
         * 从stream中分析字符串内容.
         *
         * @param bodyStream
         * @return
         */
        private String analysisMessage(InputStream bodyStream) throws IOException {
            try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
                byte[] contextBytes = new byte[4096];
                int realLen;
                while ((realLen = bodyStream.read(contextBytes, 0, 4096)) != -1) {
                    outStream.write(contextBytes, 0, realLen);
                }

                // 返回从Stream中读取的字串
                return new String(outStream.toByteArray(), "UTF-8");
            }
        }
    }
}
