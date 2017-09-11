package com.lemonconey.spike.kafkainternals;

import com.google.common.util.concurrent.SettableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/kafka")
public class KafkaTestController {

    @Autowired
    private KafkaTestClient kafkaTestClient;

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public Map sendSync() {
        return SpikeUtils.dumpPublicFields(kafkaTestClient.sendSync("hello"));
    }

    @RequestMapping(value = "/send-async", method = RequestMethod.GET)
    public Map sendAsync() throws ExecutionException, InterruptedException {
        SettableFuture<Map> result = SettableFuture.create();
        SpikeUtils.dumpPublicFields(kafkaTestClient.sendAsync("hello", (metadata, exception) -> {
            if (exception != null) {
                result.setException(exception);
            } else {
                result.set(SpikeUtils.dumpPublicFields(metadata));
            }
        }));
        return result.get();
    }

}
