package com.lemonconey.spike.kafkainternals;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class KafkaTestClient {

    private final static String topic = "kafka-test";

    private final AtomicInteger counter = new AtomicInteger();

    private KafkaProducer<Integer, String> producer;

    public KafkaTestClient() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("client.id", "test");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("interceptor.classes", "com.lemonconey.spike.kafkainternals.KafkaTestInteceptor");
        producer = new KafkaProducer<>(properties);

    }

    @SneakyThrows
    public RecordMetadata sendSync(String content) {
        Future<RecordMetadata> recordMetadata = producer.send(createRecord(content));
        return recordMetadata.get();
    }

    @SneakyThrows
    public RecordMetadata sendAsync(String content, Callback callback) {
        return producer.send(createRecord(content), callback).get();
    }

    private ProducerRecord<Integer, String> createRecord(String content) {
        return new ProducerRecord<>(topic, counter.getAndIncrement(), content);
    }
}
