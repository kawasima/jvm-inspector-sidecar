package net.unit8.sidecar.inspector.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.unit8.sidecar.inspector.ThreadDumper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ThreadDumpEndpoint implements HttpHandler {
    private final ThreadDumper dumper;
    public ThreadDumpEndpoint(ThreadDumper dumper) {
        this.dumper = dumper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String dump = dumper.dump();

        byte[] resBody = dump.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, dump.getBytes().length);
        exchange.getResponseBody().write(resBody);
    }
}
