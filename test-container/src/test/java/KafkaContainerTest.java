import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class KafkaContainerTest {
    private static final String zookeeperDockerImage = "zookeeper:3.7.0";

    private static GenericContainer zookeeperContainer;

    private static GenericContainer kafkaContainer;

    @BeforeAll
    static void init_container(){

        zookeeperContainer = new GenericContainer(DockerImageName.parse(zookeeperDockerImage))
                .withExposedPorts(2181);
        zookeeperContainer.start();

    }

    @Test
    void test_zookeeper_container_init(){
        Assertions.assertNotNull(zookeeperContainer);
        Assertions.assertNotNull(zookeeperContainer.getContainerId());
        Assertions.assertNotNull(zookeeperContainer.getHost());
        Assertions.assertNotNull(zookeeperContainer.getFirstMappedPort());
    }

    @Test
    void test_zookeeper_container_command(){
        try {
            Socket socket = new Socket(zookeeperContainer.getHost(), zookeeperContainer.getFirstMappedPort());
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();


            PrintWriter printWriter = new PrintWriter( new OutputStreamWriter(socket.getOutputStream()));
            printWriter.print("srvr");
            printWriter.flush();

            while (is.available() == 0){
                Thread.sleep(100);
            }

            String out = new String(is.readAllBytes(),StandardCharsets.US_ASCII);

            Assertions.assertTrue(out.contains("Zookeeper version: "));
            Assertions.assertTrue(out.contains("Latency min/avg/max: "));
            Assertions.assertTrue(out.contains("Received: "));
            Assertions.assertTrue(out.contains("Sent: "));
            Assertions.assertTrue(out.contains("Connections: "));
        }catch (Throwable ex){
            Assertions.fail(ex.getMessage(),ex);
        }
    }


}
