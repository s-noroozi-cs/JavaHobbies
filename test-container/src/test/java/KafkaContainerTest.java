import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.List;

public class KafkaContainerTest {

    private static KafkaContainer kafkaContainer;

    @BeforeAll
    static void init_container() {
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));
        kafkaContainer.start();
    }


    @Test
    void produce_and_consume_message() {
        try {
            String bootstrapServer = kafkaContainer.getBootstrapServers();
            String topic = "test-topic";
            String message = "test-message";
            KafkaUtil.produceMessage(bootstrapServer,topic,message);
            List<String> values =
                    KafkaUtil.consumeOnlyValues(bootstrapServer,topic, Duration.ofSeconds(1));
            Assertions.assertEquals(1,values.size());
            Assertions.assertEquals(message,values.get(0));
        } catch (Throwable ex) {
            Assertions.fail(ex.getMessage(), ex);
        }
    }

}
