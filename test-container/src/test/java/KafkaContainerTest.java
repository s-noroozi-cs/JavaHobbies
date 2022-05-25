import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class KafkaContainerTest {
    private static final String zookeeperDockerImage = "zookeeper:3.7.0";
    private static final String kafkaDockerImage = "wurstmeister/kafka:2.13-2.8.1";

    private static final int kafkaPort = getLocalFreeRandomPort();
    private static GenericContainer zookeeperContainer;

    private static GenericContainer kafkaContainer;


    @BeforeAll
    static void init_container() {

        zookeeperContainer = new GenericContainer(DockerImageName.parse(zookeeperDockerImage))
                .withExposedPorts(2181);
        zookeeperContainer.start();

        kafkaContainer = new GenericContainer(DockerImageName.parse(kafkaDockerImage));
        kafkaContainer.withExposedPorts(kafkaPort);
        kafkaContainer.withEnv("KAFKA_ZOOKEEPER_CONNECT", getZookeeperAddress());
        kafkaContainer.withEnv("KAFKA_LISTENERS", "plaintext://:" + kafkaPort);
        kafkaContainer.start();
    }

    private static int getLocalFreeRandomPort() {
        try {
            ServerSocket server = new ServerSocket(0);
            int port = server.getLocalPort();
            server.close();
            return port;
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static String getZookeeperAddress() {
        return zookeeperContainer.getHost() + ":" + zookeeperContainer.getFirstMappedPort();
    }

    @Test
    void test_zookeeper_container_init() {
        Assertions.assertNotNull(zookeeperContainer);
        Assertions.assertNotNull(zookeeperContainer.getContainerId());
        Assertions.assertNotNull(zookeeperContainer.getHost());
        Assertions.assertNotNull(zookeeperContainer.getFirstMappedPort());
    }

    @Test
    void test_zookeeper_container_command() {
        try {
            Socket socket = new Socket(zookeeperContainer.getHost(), zookeeperContainer.getFirstMappedPort());
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();


            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            printWriter.print("srvr");
            printWriter.flush();

            while (is.available() == 0) {
                Thread.sleep(100);
            }

            String out = new String(is.readAllBytes(), StandardCharsets.US_ASCII);

            Assertions.assertTrue(out.contains("Zookeeper version: "));
            Assertions.assertTrue(out.contains("Latency min/avg/max: "));
            Assertions.assertTrue(out.contains("Received: "));
            Assertions.assertTrue(out.contains("Sent: "));
            Assertions.assertTrue(out.contains("Connections: "));
        } catch (Throwable ex) {
            Assertions.fail(ex.getMessage(), ex);
        }
    }


    @Test
    void test_kafka_container_init() {
        Assertions.assertNotNull(kafkaContainer);
        Assertions.assertNotNull(kafkaContainer.getContainerId());
        Assertions.assertNotNull(kafkaContainer.getHost());
        Assertions.assertNotNull(kafkaContainer.getFirstMappedPort());
    }

    @Test
    void test_kafka_container_port_access() {
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(),kafkaPort);
        } catch (Throwable e) {
            Assertions.fail(e.getMessage(), e);
        }
    }

}
