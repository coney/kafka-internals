package com.lemonconey.spike.kafkainternals;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

@Slf4j
public class KafkaTestInteceptor implements ProducerInterceptor {
    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        log.info("sending record {}:{} to partition {}", record.key(), record.value(), record.partition());

        // return null to abandon this message
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (metadata != null) {
            log.info("ack {}", SpikeUtils.dumpPublicFields(metadata));
        } else {
            log.info("ack exception:", exception);
        }
    }

    @Override
    public void close() {
        log.info("on close");
    }

    @Override
    public void configure(Map<String, ?> configs) {
        log.info("on configure {}", configs);
    }
}
