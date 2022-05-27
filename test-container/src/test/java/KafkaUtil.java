import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.*;

public class KafkaUtil {
    public static void produceMessage(String bootstrapServer, String topic, String message) {
        try {
            Properties props = new Properties();

            props.put("bootstrap.servers", bootstrapServer);
            props.put("acks", "all");
            props.put("retries", 0);
            props.put("linger.ms", 1);
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

            Producer<String, String> producer = new KafkaProducer<String, String>(props);

            producer.send(new ProducerRecord<String, String>(topic, message));
            producer.close();
        } catch (Throwable ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public static List<String> consumeOnlyValues(String bootstrapServer, String topic, Duration pollTimeout) {
        try {

            List<String> out = new ArrayList<>();

            Properties props = new Properties();

            props.put("bootstrap.servers", bootstrapServer);
            props.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("group.id", topic + "_group_id");
            props.put("auto.offset.reset","earliest");

            KafkaConsumer kafkaConsumer = new KafkaConsumer(props);
            kafkaConsumer.subscribe(Collections.singleton(topic));
            ConsumerRecords<Integer, String> records = kafkaConsumer.poll(pollTimeout);

            for (ConsumerRecord<Integer, String> record : records) {
                out.add(record.value());
            }

            return out;
        } catch (Throwable ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}
