package ru.javawebinar.topjava.web.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by taras on 2018-10-08.
 */

public class CyrillicFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
