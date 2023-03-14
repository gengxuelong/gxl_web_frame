package com.httpserver.config;


import com.httpserver.anno.GxlWebServlet;
import com.httpserver.global.ProjectConstant;
import com.httpserver.exception.NotImplementedHttpServletException;
import com.httpserver.exception.ProjectException;
import com.httpserver.servlet.GxlHttpServlet;
import com.httpserver.servletmapping.ServletConcurrentHashMap;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * @author GengXuelong
 * @version 1.0
 * @time 2023/2/4 14:13
 * @email 3349495429@qq.com
 * @className com.httpserver.config.AnnoParseServletConfig
 * @description:
 */
public class AnnoParseServletConfig implements ParseServletConfig{
    private  String servletFilePath;
    private  String servletPackagePath;
    @Override
    public void parse() {
        loadPathData();
	Logger logger = Logger.getLogger(this.getClass());
	File directory = new File(servletFilePath);
	if(directory.exists()&&directory.isDirectory()){
	    File[] files = directory.listFiles();
	    assert files != null;
	    for (File file : files) {
	        if(file.isFile()&&file.getName().endsWith(".java")){
		    String fileName = file.getName();
		    String[] split = fileName.split(".java");
		    String className = split[0];
		    try {
			Class<?> clazz = Class.forName(servletPackagePath +className);
			if(clazz.isAnnotationPresent(GxlWebServlet.class)){//因为父类并没有实现该接口，可以将其排除，从而避免报父类没有实现自己的错误
			    if(ParseServletConfig.isImplHttpServletInterface(clazz)){
				GxlHttpServlet httpServlet = (GxlHttpServlet) clazz.getConstructor().newInstance();
				GxlWebServlet gxlWebServlet = clazz.getAnnotation(GxlWebServlet.class);
				String uri = gxlWebServlet.urlPatterns();
				ServletConcurrentHashMap.map.put(uri,httpServlet);
			    }else{
				throw new NotImplementedHttpServletException(clazz.getName()+"not implement HttpServlet");
			    }
			}
		    } catch (ClassNotFoundException | NotImplementedHttpServletException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			logger.error(e.getMessage());
		        e.printStackTrace();
		    }
		}else if(file.isFile()){
		    try {
			throw new ProjectException(file.getName()+"不是Java文件，请移除servlet专属文件加");
		    } catch (ProjectException e) {
		        logger.error(e.getMessage());
			e.printStackTrace();
		    }
		}
	    }
	}else{
	    try {
		throw new ProjectException("servlet文件不存在或者路径错误");
	    } catch (ProjectException e) {
	        logger.error(e.getMessage());
		e.printStackTrace();
	    }
	}
    }

    private void loadPathData() {
        this.servletPackagePath = ProjectConstant.SERVLET_PACKAGE+".";
	String[] split = ProjectConstant.SERVLET_PACKAGE.split("\\.");
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append("./src");
	for (String s : split) {
	    stringBuilder.append("/").append(s);
	}
	this.servletFilePath = stringBuilder.toString();
    }
}
