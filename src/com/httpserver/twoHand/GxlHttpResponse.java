package com.httpserver.twoHand;

import com.httpserver.global.ProjectConstant;
import com.httpserver.utils.GxlIOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GengXuelong
 * @version 1.0
 * @time 2023/2/3 19:15
 * @email 3349495429@qq.com
 * @className com.httpserver.twoHand.MyHttpResponse
 * @description:
 */
public class GxlHttpResponse {
    private static final Map<String,String> TYPEMAPPING = new HashMap<>();
    static{
        TYPEMAPPING.put("java","html/text;charset=UTF-8");
        TYPEMAPPING.put("txt","text/plain;charset=UTF-8");
        TYPEMAPPING.put("properties","text/properties;charset=UTF-8");
        TYPEMAPPING.put("jpg","image/jpg");
        TYPEMAPPING.put("png","image/png");
        TYPEMAPPING.put("ico","image/x-icon");
    }

    private final SelectionKey selectionKey;
    private GxlHttpRequest httpRequest;
    private String contentType;
    private String code;
    private String msg;
    private String httpVersion;
    private final Map<String,String> header = new HashMap<>();

    public GxlHttpResponse(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
        this.httpVersion = "HTTP/1.1";
        this.code = "200";
        this.msg = "OK";
        this.contentType="html/text;charset=UTF-8";
    }

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     发送静态资源给浏览器
     */
    public void sendStaticResourceToClient() throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        System.out.println("sendStaticResourceToClient");
        String uri = this.httpRequest.getUri();
        String url = ProjectConstant.STATIC_FILE_PATH+uri;
        File file = new File(url);
        if(!file.exists()){
            this.setCode("404");
            this.setMsg("not-found");
            this.write("<h1>404 not found this static resource<//h1>");
        }else if(file.isDirectory()){
            this.setContentType("text/html;charset=UTF-8");
            String navigationPage = this.buildNavigationPageByUri(uri,file);
            assert navigationPage != null;
            this.sendDataFromString(navigationPage);
        }else if(file.isFile()){
            this.setContentType(this.getContentTypeByUriOrUrl(uri));
            this.sendDataFromFile(url);
        }
    }

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     发送动态资源
     */
    public void write(String string){
        try {
            this.sendDataFromString(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     发送动态资源,
     */
    public void writeWithOutHtml(String string){
        try {
            String s = string.replaceAll("<", "&lt;");
            String ss = s.replaceAll(">", "&gt;");
            this.setContentType("text/html;charset=UTF-8");
            this.sendDataFromString(ss);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     发送动态资源,追加书写
     */
    public void moreWrite(String string) {
        SocketChannel socketChannel = (SocketChannel) this.selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.wrap(string.getBytes(StandardCharsets.UTF_8));
        try {
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     将字符串信息发送给浏览器
     */
    private void sendDataFromString(String string) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        this.sendFormatInfo(socketChannel);
        ByteBuffer byteBuffer = ByteBuffer.wrap(string.getBytes(StandardCharsets.UTF_8));
        socketChannel.write(byteBuffer);
    }
    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     从文件中获取数据并发送给浏览器
     */
    private void sendDataFromFile(String url) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        this.sendFormatInfo(socketChannel);
        FileInputStream fileInputStream = new FileInputStream(url);
        byte[] bytes = GxlIOUtils.getLittleByteArrayFromInputStream(fileInputStream);
        fileInputStream.close();
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        socketChannel.write(byteBuffer);
    }


    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     向浏览器发送基本格式性信息，即响应头和相应行
     */
    private void sendFormatInfo(SocketChannel socketChannel) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.httpVersion).append(" ").append(this.code).append(" ").append(this.msg).append("\r\n");
        stringBuilder.append("ContentType: ").append(this.contentType).append("\r\n");
        for (Map.Entry<String, String> stringStringEntry : this.header.entrySet()) {
            stringBuilder.append(stringStringEntry.getKey()).append(": ").append(stringStringEntry.getValue()).append("\r\n");
        }
        stringBuilder.append("\r\n");
        ByteBuffer byteBuffer = ByteBuffer.wrap((stringBuilder.toString()).getBytes(StandardCharsets.UTF_8));
        socketChannel.write(byteBuffer);
    }


    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     构建动态展示文件列表的页面
     */
    private String buildNavigationPageByUri(String uri,File directory) {
        if(!directory.isDirectory())return null;
        String[] filesName = directory.list();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<h1>this is ").append(uri).append(" directory,please select the files you wanted<h1>");
        assert filesName != null;
        for (String name : filesName) {
            String string;
            if(name.contains(".")){//是文件
                string = "<h3 style=\"color:red;\">文件："+name+"</h3>";
            }else{
                string = "<h3 style=\"color:blue;\">目录："+name+"</h3>";
            }
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     获取欲发送文件对应的响应头ContentType字段
     */
    private String getContentTypeByUriOrUrl(String uri) {
        String[] split = uri.split("\\.");
        String fileType = split[split.length-1];
        String contentType = TYPEMAPPING.get(fileType);
        if(contentType==null){
            contentType = "text/"+fileType+";charset=UTF-8";
        }
        return contentType;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public void setRequest(GxlHttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }
    public void setContentType(String contentType){
        this.contentType = contentType;
    }

    public void setHeader(String key,String value){
        this.header.put(key,value);
    }

}
