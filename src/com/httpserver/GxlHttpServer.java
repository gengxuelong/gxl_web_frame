package com.httpserver;

import com.httpserver.process.DynamicResourceProcess;
import com.httpserver.thread.LoaderResourceRunnable;
import com.httpserver.twoHand.GxlHttpRequest;
import com.httpserver.twoHand.GxlHttpResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author GengXuelong
 * @version 1.0
 * @time 2023/2/3 19:14
 * @email 3349495429@qq.com
 * @className com.httpserver.HttpServer
 * @description:
 */
public class GxlHttpServer {

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     start a server
     */
    public static void start(int port){
        Logger logger = Logger.getLogger(GxlHttpServer.class);
        GxlHttpServer gxlHttpServer = new GxlHttpServer();
        try {
            gxlHttpServer.startServer(port);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     启动服务器
     */
    public void startServer(int port) throws IOException {
        new Thread(new LoaderResourceRunnable()).start();
        Selector selector = this.openAServerSocketChanel(port);
        this.startWatching(selector);
    }

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     开启针对用户访问的监听与接受
     */
    private void startWatching(Selector selector) throws IOException {
        while(true){
            int count;
            count = selector.select();
            if(count!=0){//接受到一个客户端请求
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
                while(selectionKeyIterator.hasNext()){
                    SelectionKey next = selectionKeyIterator.next();
                    if(next.isAcceptable()){
                        this.buildAChannelWithClient(next);
                    }else if(next.isReadable()){
                        GxlHttpRequest httpRequest = new GxlHttpRequest(next);
                        httpRequest.parseAndStoreMsgFromRequest();
                        GxlHttpResponse httpResponse = new GxlHttpResponse(next);
                        httpResponse.setRequest(httpRequest);//加载好数据
                        SocketChannel socketChannel = (SocketChannel) next.channel();
                        if(!httpRequest.isBrowserReq()){
                            socketChannel.close();
                            System.out.println("请求并非来自浏览器，不予处理");
                            continue;
                        }
                        if(httpRequest.getUri().startsWith("/servlet/")){
                            System.out.println("动态资源");
                            DynamicResourceProcess dynamicResourceProcess = new DynamicResourceProcess();
                            dynamicResourceProcess.process(httpRequest,httpResponse);
                        }else{
                            httpResponse.sendStaticResourceToClient();
                        }
                        socketChannel.close();
                    }
                    selectionKeyIterator.remove();
                }
            }
        }
    }

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     构建一个由客户端伸向服务端的通道
     */
    private void buildAChannelWithClient(SelectionKey next) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) next.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        Selector selector = next.selector();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ);
    }

    /**
     * @author GengXuelong
     * <p> 函数功能描述如下:
     * @description:
     *     开启一个服务端通道
     */
    private Selector openAServerSocketChanel(int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(port));
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        return selector;
    }
}
