package org.springbus.verx;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

public class MyTcpClient {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();
        // 创建一个TCP客户端
        NetClient client = vertx.createNetClient();
        // 连接服务器
        client.connect(5555, "localhost", conn -> {
            if (conn.succeeded()) {
                NetSocket s = conn.result();
                // 向服务器写数据
                s.write(Buffer.buffer("hello"));

                // 读取服务器的响应数据
                s.handler(buffer -> {
                    System.out.println(buffer.toString());
                });
            } else {
                System.out.println("连接服务器异常");
            }

        });
    }
}
