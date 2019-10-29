# jvm-inspector-sidecar

jvm-inspector-sidecar is a sidecar for the inspection of the JVM.

## Usage

Add options to the target container as follows:

```yaml
target:
  environment:
    JAVA_TOOL_OPTIONS: -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9000 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false
  ports:
    - 9000

inspector:
  image: dcr.io/kawasima/jvm-inspector-sidecar
  environment:
    JMX_HOST: target
    JMX_PORT: 9000
    SERVER_PORT: 5000
  ports:
    - "5000:5000"
``` 

In the example, you can show the thread dump at `http://inspector:5000/thread_dump`. 