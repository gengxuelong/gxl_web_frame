package com.httpserver.exception;

/**
 * @author GengXuelong
 * @version 1.0
 * @time 2023/2/4 14:19
 * @email 3349495429@qq.com
 * @className com.httpserver.exception.ProjectException
 * @description:
 */
public class ProjectException extends Exception {
    public ProjectException(){}
    public ProjectException(String msg){
        super(msg);
    }
}
