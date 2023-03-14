package com.httpserver.config;

import com.httpserver.exception.NotImplementedHttpServletException;
import com.httpserver.servlet.GxlHttpServlet;
import com.httpserver.servletmapping.ServletConcurrentHashMap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author GengXuelong
 * @version 1.0
 * @time 2023/2/3 22:31
 * @email 3349495429@qq.com
 * @className com.httpserver.config.PropertiesParseServletConfig
 * @description:
 */
public class PropertiesParseServletConfig implements ParseServletConfig{
    private static final String WEB_PROPERTIES_PATH = "./src/com/httpserver/config_file/servletConfig.properties";

    @Override
    public void parse() {
	try {
	    FileInputStream fileInputStream = new FileInputStream(WEB_PROPERTIES_PATH);
	    Properties properties = new Properties();
	    properties.load(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
	    fileInputStream.close();
	    String value = properties.getProperty("servlet-info");
	    String[] split = value.split(";");
	    for (String s : split) {
		String[] split1 = s.split(",");
		String uri = split1[0];
		String className = split1[1];
		Class<?> aClass = Class.forName(className);
		Class<?>[] interfaces = aClass.getInterfaces();
		if(ParseServletConfig.isImplHttpServletInterface(aClass)){
		    Constructor<?> constructor = aClass.getConstructor();
		    GxlHttpServlet httpServlet = (GxlHttpServlet) constructor.newInstance();
		    ServletConcurrentHashMap.map.put(uri,httpServlet);
		}else{
		    throw new NotImplementedHttpServletException(aClass.getName()+"not implements HttpServlet");
		}
	    }
	} catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
	    System.out.println("数据加载异常");
	    e.printStackTrace();
	} catch (NotImplementedHttpServletException e) {
	    System.out.println(e.getMessage());
	}
    }
}
