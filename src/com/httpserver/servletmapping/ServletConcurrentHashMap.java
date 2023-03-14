package com.httpserver.servletmapping;

import com.httpserver.servlet.GxlHttpServlet;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author GengXuelong
 * @version 1.0
 * @time 2023/2/3 22:30
 * @email 3349495429@qq.com
 * @className com.httpserver.servletmapping.ServletConcurrentHashMap
 * @description:
 */
public interface ServletConcurrentHashMap {
    public static final ConcurrentHashMap<String, GxlHttpServlet> map = new ConcurrentHashMap<>();
}
