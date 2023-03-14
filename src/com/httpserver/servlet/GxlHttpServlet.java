package com.httpserver.servlet;

import com.httpserver.twoHand.GxlHttpRequest;
import com.httpserver.twoHand.GxlHttpResponse;

/**
 * @author GengXuelong
 * @version 1.0
 * @time 2023/2/3 21:53
 * @email 3349495429@qq.com
 * @className com.httpserver.servlet.BaseServlet
 * @description:
 */
public interface GxlHttpServlet {
    void service(GxlHttpRequest httpRequest, GxlHttpResponse httpResponse);
}
