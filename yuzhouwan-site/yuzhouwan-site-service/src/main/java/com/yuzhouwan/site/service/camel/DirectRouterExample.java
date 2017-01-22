package com.yuzhouwan.site.service.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Apache Camel Example about Direct Router
 *
 * @author Benedict Jin
 * @since 2016/8/7
 */
public class DirectRouterExample {

    public static void main(String[] args) throws Exception {

        // 这是camel上下文对象，整个路由的驱动全靠它了
        ModelCamelContext camelContext = new DefaultCamelContext();
        // 启动route
        camelContext.start();
        // 首先将两个完整有效的路由注册到Camel服务中
        camelContext.addRoutes(new DirectRouterExample().new DirectRouteA());
        camelContext.addRoutes(new DirectRouterExample().new DirectRouteB());

        // 通用没有具体业务意义的代码，只是为了保证主线程不退出
        synchronized (DirectRouterExample.class) {
            DirectRouterExample.class.wait();
        }
    }

    /**
     * DirectRouteA 其中使用direct 连接到 DirectRouteB
     */
    private class DirectRouteA extends RouteBuilder {

        /* (non-Javadoc)
         * @see org.apache.camel.builder.RouteBuilder#configure()
         *
         * 2016-08-07 23:20:08.626 | INFO | Exchange[Id: ID-DESKTOP-P0UR2J5-52315-1470583152904-0-7, ExchangePattern: InOut, BodyType: org.apache.camel.converter.stream.InputStreamCache, Body: [Body is instance of org.apache.camel.StreamCache]] | DirectRouteB.log | CamelLogger.java:180
         */
        @Override
        public void configure() throws Exception {
            from("jetty:http://0.0.0.0:8282/directCamel")
                    // 连接路由：DirectRouteB
                    .to("direct:directRouteB")
                    //2016-08-07 23:20:08.628 | INFO | Exchange[Id: ID-DESKTOP-P0UR2J5-52315-1470583152904-0-7, ExchangePattern: InOut, BodyType: org.apache.camel.converter.stream.InputStreamCache, Body: [Body is instance of org.apache.camel.StreamCache]] | DirectRouteA.log | CamelLogger.java:180
                    .to("log:DirectRouteA?showExchangeId=true");
        }
    }

    private class DirectRouteB extends RouteBuilder {

        /* (non-Javadoc)
         * @see org.apache.camel.builder.RouteBuilder#configure()
         */
        @Override
        public void configure() throws Exception {
            from("direct:directRouteB")
                    .to("log:DirectRouteB?showExchangeId=true");
        }
    }
}