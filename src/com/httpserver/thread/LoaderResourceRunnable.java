package com.httpserver.thread;

import com.httpserver.config.AnnoParseServletConfig;
import com.httpserver.config.ParseServletConfig;
import com.httpserver.config.PropertiesParseServletConfig;
import com.httpserver.config.XmlParseServletConfig;
import com.httpserver.global.ProjectConfigLoader;
import org.apache.log4j.Logger;

/**
 * @author GengXuelong
 * @version 1.0
 * @time 2023/2/3 22:42
 * @email 3349495429@qq.com
 * @className com.httpserver.thread.LoaderResourceRunable
 * @description:
 */
public class LoaderResourceRunnable implements Runnable{
    @Override
    public void run() {
        ProjectConfigLoader projectConfigLoader = new ProjectConfigLoader();
        ParseServletConfig parseServletConfig = new AnnoParseServletConfig();
        projectConfigLoader.parse();
        parseServletConfig.parse();
        Logger logger = Logger.getLogger(this.getClass());
        logger.info("project starter successfully");

        System.out.println("              .__      _____                            ._.\n" +
                "   _______  __|  |   _/ ____\\___________    _____   ____| |\n" +
                "  / ___\\  \\/  /  |   \\   __\\\\_  __ \\__  \\  /     \\_/ __ \\ |\n" +
                " / /_/  >    <|  |__  |  |   |  | \\// __ \\|  Y Y  \\  ___/\\|\n" +
                " \\___  /__/\\_ \\____/  |__|   |__|  (____  /__|_|  /\\___  >_\n" +
                "/_____/      \\/                         \\/      \\/     \\/\\/\n" +
                "                project starter successfully\n" +
                "                author gengxuelong");
    }
}
