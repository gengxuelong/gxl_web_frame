package com.httpserver.servlet;



import com.httpserver.anno.GxlWebServlet;
import com.httpserver.twoHand.GxlHttpRequest;
import com.httpserver.twoHand.GxlHttpResponse;

import java.util.List;

/**
 * @author GengXuelong
 * @version 1.0
 * @time 2023/2/4 17:36
 * @email 3349495429@qq.com
 * @className com.code.servlet.StudentServlet
 * @description:
 */
@GxlWebServlet(urlPatterns = "/servlet/user")
public class StudentServlet implements GxlHttpServlet {
//    private final StudentService studentService = new StudentService();
    @Override
    public void service(GxlHttpRequest gxlHttpRequest, GxlHttpResponse gxlHttpResponse) {
	String method = gxlHttpRequest.getParams().get("method");
	System.out.println(method);
	System.out.println(gxlHttpRequest.getParams());
	if("showall".equals(method)){

	}else{
	    gxlHttpResponse.write("<h2>sorry , this method is developing now and it can not work still</h2>");
	}
    }
}
