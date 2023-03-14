package com.httpserver.config;

import com.httpserver.exception.NotImplementedHttpServletException;
import com.httpserver.servlet.GxlHttpServlet;
import com.httpserver.servletmapping.ServletConcurrentHashMap;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GengXuelong
 * @version 1.0
 * @time 2023/2/4 13:45
 * @email 3349495429@qq.com
 * @className com.httpserver.config.XmlParseServletConfig
 * @description:
 */
public class XmlParseServletConfig implements ParseServletConfig{
    private static final String WEB_XML_PATH = "config_file/web.xml";
    @Override
    public void parse() {
	try {
	    FileInputStream fileInputStream = new FileInputStream(WEB_XML_PATH);
	    SAXReader saxReader = new SAXReader();
	    Document read = saxReader.read(fileInputStream);
	    Element rootElement = read.getRootElement();
	    List<Element> elements = rootElement.elements("servlet");
	    Map<String, GxlHttpServlet> httpServletMap = new HashMap<>();
	    for (Element element : elements) {
		Element servletName = element.element("servlet-name");
		String name = servletName.getText();
		Element element1 = element.element("servlet-class");
		Class<?> clazz =  Class.forName(element.getText());
		GxlHttpServlet httpServlet = null;
		if(ParseServletConfig.isImplHttpServletInterface(clazz)){
		    httpServlet = (GxlHttpServlet) clazz.getConstructor().newInstance();
		}else{
		    throw new NotImplementedHttpServletException(clazz.getName()+"not implement HttpServlet");
		}
		httpServletMap.put(name,httpServlet);
	    }
	    List<Element> elements1 = rootElement.elements("servlet-mapping");
	    for (Element element : elements1) {
		Element element1 = element.element("servlet-name");
		String servletName = element1.getText();
		Element element2 = element.element("url-pattern");
		String url = element2.getText();
		ServletConcurrentHashMap.map.put(url,httpServletMap.get(servletName));
	    }
	} catch (FileNotFoundException | DocumentException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | NotImplementedHttpServletException e) {
	    e.printStackTrace();
	}
    }
}
