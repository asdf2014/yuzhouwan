package com.yuzhouwan.site.service.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Error Filter
 *
 * @author Benedict Jin
 * @since 2015/11/9
 */
public class ErrorFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("WEB-INF/views/error.html").forward(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}
