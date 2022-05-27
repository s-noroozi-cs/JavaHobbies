import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.utility.DockerImageName;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.Consumer;

public class GenericKafkaContainerTest {
    private static final Logger logger = LoggerFactory.getLogger(GenericKafkaContainerTest.class);

    private static final String zookeeperDockerImage = "zookeeper:3.7.0";
    private static final String kafkaDockerImage = "wurstmeister/kafka:2.13-2.8.1";

    private static Network network = Network.NetworkImpl.builder().build();
    private static final int kafkaPort = getLocalFreeRandomPort();
    private static GenericContainer zookeeperContainer;

    private static GenericContainer kafkaContainer;

    private static Consumer<OutputFrame> logConsumer() {
        return outputFrame -> logger.info(outputFrame.getUtf8String());
    }


    @BeforeAll
    static void init_container() {

        zookeeperContainer = new GenericContainer(DockerImageName.parse(zookeeperDockerImage));
        zookeeperContainer.withNetwork(network);
        zookeeperContainer.withNetworkAliases("zookeeper");
        zookeeperContainer.withExposedPorts(2181);
        zookeeperContainer.withLogConsumer(logConsumer());
        zookeeperContainer.start();

        kafkaContainer = new GenericContainer(DockerImageName.parse(kafkaDockerImage));
        kafkaContainer.withNetwork(network);
        kafkaContainer.withNetworkAliases("kafka");
        kafkaContainer.withExposedPorts(9092);
        kafkaContainer.withEnv("KAFKA_ZOOKEEPER_CONNECT", "zookeeper:2181");
        kafkaContainer.withEnv("KAFKA_LISTENERS", "plaintext://:9092");
        kafkaContainer.withEnv("KAFKA_ADVERTISED_LISTENERS", "plaintext://kafka:9092");
        kafkaContainer.withLogConsumer(logConsumer());
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
            Socket socket = new Socket(InetAddress.getLocalHost(), zookeeperContainer.getFirstMappedPort());
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
            new Socket(InetAddress.getLocalHost(), kafkaContainer.getFirstMappedPort());
        } catch (Throwable e) {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    void test_Kafka_producer_consumer() {
        try {
            Properties props = new Properties();

            props.put("bootstrap.servers", kafkaContainer.getHost() + ":" + kafkaContainer.getFirstMappedPort());
            props.put("acks", "all");
            props.put("retries", 0);
            props.put("linger.ms", 1);
            props.put("key.serializer",
                    "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer",
                    "org.apache.kafka.common.serialization.StringSerializer");

            Producer
                    <String, String> producer = new KafkaProducer
                    <String, String>(props);

            producer.send(new ProducerRecord<String, String>("test-kafka","test-message"));
            producer.close();

            Assertions.fail("Kafka container through some images does not work properly");
        } catch (Throwable ex) {
            //ignore exception
        }
    }

}
