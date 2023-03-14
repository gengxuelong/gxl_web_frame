package com.httpserver.twoHand;

import com.httpserver.utils.GxlCommonUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author GengXuelong
 * @version 1.0
 * @time 2023/2/3 19:15
 * @email 3349495429@qq.com
 * @className com.httpserver.twoHand.MyHttpRequest
 * @description:
 */
public class GxlHttpRequest {

    private String method;
    private String uri;
    private String httpVersion;
    private final Map<String,String> params = new HashMap<>();

    private final Map<String,String> headMap = new HashMap<>();
    private boolean isBrowserReq;
    private final SelectionKey selectionKey;

    public GxlHttpRequest(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
        this.isBrowserReq = true;
    }

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     将从请求中获取的信息进行解析，并将解析的结果存储到本类中
     */
    public void parseAndStoreMsgFromRequest() throws IOException {
        String msgFromReq = this.obtainStringFromReq();
        this.parseAndStoreTheStringFromReq(msgFromReq);
    }

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     解析并存储从浏览器请求中获取的字符串信息
     */
    private void parseAndStoreTheStringFromReq(String msgFromReq) {
        String[] split = msgFromReq.split("\r\n\r\n");
        String body = null;
        if(split.length>1){
             body = split[1];
            msgFromReq = split[0];
        }
        String[] rowsArray = msgFromReq.split("\r\n");
        if (rowsArray.length<2){
            this.isBrowserReq = false;
            return;
        }
        String reqRow = rowsArray[0];
        this.parseAndStoreParseReqRow(reqRow);
        this.parseAndStoreReqHeaders(rowsArray);
        this.parseAndStoreParam(body);
    }

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     解析并存储从浏览器请求中获取的param
     */
    private void parseAndStoreParam(String requestBody) {
        String[] split = this.getUri().split("\\?");
        if(split.length>1){
            String paramMaps = split[1];
            String s1 = paramMaps.replaceAll("%20", "");
            String[] split1 = s1.split("&");
            for (String s : split1) {
                String[] split2 = s.split("=");
                if(split2.length>1){
                    String paramKey = split2[0].trim();
                    String paramValue = split2[1].trim();
                    this.params.put(paramKey,paramValue);
                }else{
                    String paramKey = split2[0].trim();
                    this.params.put(paramKey,"");
                }
            }
        }
        if(requestBody!=null){
            System.out.println("body : "+requestBody);
        }
    }

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     解析并存储请求头
     */
    private void parseAndStoreReqHeaders(String[] rowsArray) {
        for (int i = 1; i < rowsArray.length; i++) {
            String header = rowsArray[i];
            String[] split = header.split(": ");
            this.headMap.put(split[0],split[1]);
        }
    }

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     将请求行解析并存储到字段中
     */
    private void parseAndStoreParseReqRow(String reqRow) {
        String[] rows = reqRow.split(" ");
        if(rows.length!=3 || !rows[1].startsWith("/") || !rows[2].startsWith("HTTP")){
            this.isBrowserReq = false;
            return;
        }
        this.method = rows[0];
        this.uri = rows[1];
        this.httpVersion = rows[2];
    }

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     获取请求发送来的字符串信息
     */
    private String obtainStringFromReq() throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        List<Byte> byteList = new ArrayList<>();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int len;
        while((len=socketChannel.read(byteBuffer))>0){
            byteList.addAll(GxlCommonUtils.convertLittleArrayToListFromByte(byteBuffer.array(),0,len));
        }
        return new String(GxlCommonUtils.convertListToLittleArrayFromByte(byteList,0,byteList.size()), StandardCharsets.UTF_8);
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getHeadMap() {
        return headMap;
    }

    public boolean isBrowserReq() {
        return isBrowserReq;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
