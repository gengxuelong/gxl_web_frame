package com.httpserver.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author GengXuelong
 * @version 1.0
 * @time 2023/2/3 19:17
 * @email 3349495429@qq.com
 * @className com.httpserver.utils.MyCommonUtils
 * @description:
 */
public class GxlCommonUtils {

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     将List_Byte转变为byte[]
     *     start: 起始的索引，从零开始，包含此边界
     *     len: 要获取元素的长度/个数
     */
    public static byte[] convertListToLittleArrayFromByte(List<Byte> byteList,int start,int len){
	List<Byte> collect = byteList.stream().skip(start).limit(len).collect(Collectors.toList());
	byte[] res = new byte[collect.size()];
	for (int i = 0; i < byteList.size(); i++) {
	    res[i] = byteList.get(i);
	}
	return res;
    }

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     将byte[] 转换为List_Byte
     *     start: 起始的索引，从零开始，包含此边界
     *     len: 要获取元素的长度/个数
     */
    public static List<Byte> convertLittleArrayToListFromByte(byte[] bytes,int start,int len){
        List<Byte> byteList = new ArrayList<>();
	for (int i = start; i < start + len; i++) {
	    byteList.add(bytes[i]);
	}
	return byteList;

    }
}
