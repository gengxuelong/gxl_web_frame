package com.httpserver.process;

import com.httpserver.servletmapping.ServletConcurrentHashMap;
import com.httpserver.servlet.GxlHttpServlet;
import com.httpserver.twoHand.GxlHttpRequest;
import com.httpserver.twoHand.GxlHttpResponse;

/**
 * @author GengXuelong
 * @version 1.0
 * @time 2023/2/3 22:01
 * @email 3349495429@qq.com
 * @className com.httpserver.process.DynamicResourceProcess
 * @description:
 */
public class DynamicResourceProcess {
    public void process(GxlHttpRequest httpRequest, GxlHttpResponse httpResponse){
        String uri = httpRequest.getUri();
	String[] split = uri.split("\\?");
	GxlHttpServlet httpServlet = ServletConcurrentHashMap.map.get(split[0]);
	if(httpServlet==null){
	    httpResponse.write("<h1>404!not found the dynamic resource</h1>");
	}else{
	    httpServlet.service(httpRequest,httpResponse);
	}


    }
}
