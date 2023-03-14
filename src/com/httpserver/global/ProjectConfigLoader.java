package com.httpserver.global;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author GengXuelong
 * @version 1.0
 * @time 2023/2/4 16:07
 * @email 3349495429@qq.com
 * @className com.httpserver.global.ProjectConfigLoader
 * @description:
 */
public class ProjectConfigLoader {
    private static final String GXL_FRAME_CONFIG_XML = "./src/gxlFrameConfig.xml";

    public void parse() {
	Logger logger = Logger.getLogger(this.getClass());
	try {
	    FileInputStream fileInputStream = new FileInputStream(GXL_FRAME_CONFIG_XML);
	    SAXReader saxReader = new SAXReader();
	    Document read = saxReader.read(fileInputStream);
	    Element rootElement = read.getRootElement();
	    Element resourceLocation = rootElement.element("resourceLocation");
	    Element staticFilePath = resourceLocation.element("staticFilePath");
	    Element servletPackage = resourceLocation.element("servletPackage");
	    ProjectConstant.SERVLET_PACKAGE = servletPackage.getText();
	    ProjectConstant.STATIC_FILE_PATH = staticFilePath.getText();
	} catch (FileNotFoundException | DocumentException e) {
	    logger.error(e.getMessage());
	    e.printStackTrace();
	}
    }
}
