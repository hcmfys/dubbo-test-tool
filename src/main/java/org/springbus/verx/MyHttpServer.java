package org.springbus.verx;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

import java.lang.ref.ReferenceQueue;

public class MyHttpServer {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        // 创建一个HttpServer
        HttpServer server = vertx.createHttpServer();
        Router router= Router.router( vertx);
        router.route("/index").handler(request->{
            request.response().end("index page");
        });
        server.requestHandler(router::accept);

        server.listen(8888); // 监听8888端口
    }


}
