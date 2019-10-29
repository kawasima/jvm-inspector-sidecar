package net.unit8.sidecar.inspector.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class InspectorServer {
    private final HttpServer server;
    public InspectorServer(int port) throws IOException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(port), 0);
    }

    public void addContext(String contextPath, HttpHandler handler) {
        server.createContext(contextPath, handler);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}
