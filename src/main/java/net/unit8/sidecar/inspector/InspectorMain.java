package net.unit8.sidecar.inspector;

import net.unit8.sidecar.inspector.http.InspectorServer;
import net.unit8.sidecar.inspector.http.ThreadDumpEndpoint;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

public class InspectorMain {
    public static void main(String[] args) throws IOException {
        final ThreadDumper threadDumper = new ThreadDumper();

        Optional.ofNullable(System.getenv("SERVER_PORT"))
                .map(Integer::parseInt)
                .filter(p -> p > 0 && p < 65535)
                .ifPresent(serverPort -> {
                    try {
                        InspectorServer server = new InspectorServer(serverPort);
                        server.addContext("/thread_dump", new ThreadDumpEndpoint(threadDumper));
                        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.stop()));
                        server.start();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });

    }
}
