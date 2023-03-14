package com.httpserver.config;

import com.httpserver.servlet.GxlHttpServlet;

/**
 * @author GengXuelong
 * @version 1.0
 * @time 2023/2/3 22:31
 * @email 3349495429@qq.com
 * @className com.httpserver.config.ParseServletConfig
 * @description:
 */
public interface ParseServletConfig {
    void parse();
    static boolean isImplHttpServletInterface(Class<?> clazz){
        boolean res = false;
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            if(anInterface== GxlHttpServlet.class){
                res = true;
                break;
            }
        }
        return res;
    }
}
