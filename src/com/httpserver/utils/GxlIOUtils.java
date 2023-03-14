package com.httpserver.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GengXuelong
 * @version 1.0
 * @time 2023/2/3 19:16
 * @email 3349495429@qq.com
 * @className com.httpserver.utils.IOUtils
 * @description:
 */
public class GxlIOUtils {

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     将资源流转换为byte[]并返回
     */
    public static byte[] getLittleByteArrayFromInputStream(InputStream inputStream) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        List<Byte> byteList = new ArrayList<>();
        byte[] bytes = new byte[1024];
        int len ;
        while((len=bufferedInputStream.read(bytes))!=-1){
            byteList.addAll(GxlCommonUtils.convertLittleArrayToListFromByte(bytes,0,len));
        }
        return GxlCommonUtils.convertListToLittleArrayFromByte(byteList,0,byteList.size());
    }
}
