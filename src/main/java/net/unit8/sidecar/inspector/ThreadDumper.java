package net.unit8.sidecar.inspector;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class ThreadDumper {
    MBeanServerConnection conn;

    public ThreadDumper() {
        String port = Objects.toString(System.getenv("JMX_PORT"), "10080");
        String host = Objects.toString(System.getenv("JMX_HOST"), "localhost");
        String url = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi";
        try {
            JMXServiceURL serviceUrl = new JMXServiceURL(url);
            final JMXConnector connector = JMXConnectorFactory.connect(serviceUrl, null);
            conn = connector.getMBeanServerConnection();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String dump() {
        try {
            final ThreadMXBean mxBean = ManagementFactory.newPlatformMXBeanProxy(conn, "java.lang:type=Threading", ThreadMXBean.class);
            return Arrays.stream(mxBean.dumpAllThreads(true, true))
                    .map(threadInfo -> formatStackTrace(threadInfo))
                    .collect(Collectors.joining());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String formatStackTrace(ThreadInfo threadInfo) {
        final StringBuilder dump = new StringBuilder();
        dump.append('"');
        dump.append(threadInfo.getThreadName());
        dump.append("\" ");
        final Thread.State state = threadInfo.getThreadState();
        dump.append("\n   java.lang.Thread.State: ");
        dump.append(state);
        final StackTraceElement[] stackTraceElements = threadInfo.getStackTrace();
        for (final StackTraceElement stackTraceElement : stackTraceElements) {
            dump.append("\n        at ");
            dump.append(stackTraceElement);
        }
        dump.append("\n\n");
        return dump.toString();
    }
}
