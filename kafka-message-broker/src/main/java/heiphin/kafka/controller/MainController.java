package heiphin.kafka.controller;

import heiphin.kafka.entity.Car;
import heiphin.kafka.entity.Listing;
import heiphin.kafka.entity.Video;
import heiphin.kafka.kafka.Listeners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class MainController {

    @Autowired
    private Listeners listeners;

    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    @PostMapping("/api/cars/{carName}")
    public ResponseEntity<List<Car>> parseWithMicroservice(@PathVariable String carName) throws ExecutionException, InterruptedException {
        listeners.sendToKolesaParser(carName);
        logger.info("Отправлено сообщение на: kolesa-parser-topic");
        return ResponseEntity.ok().body(listeners.getKolesaFuture().get());
    }

    @PostMapping("/api/olx/{thingName}")
    public ResponseEntity<List<Listing>> parserOlx(@PathVariable String thingName) throws ExecutionException, InterruptedException {
        listeners.sendToOlxParser(thingName);
        logger.info("Отправлено сообщение на: olx-parser-topic");
        return ResponseEntity.ok().body(listeners.getOlxFuture().get());
    }

    @PostMapping("/api/youtube/{channelName}")
    public ResponseEntity<List<Video>> parseYoutubeChannel(@PathVariable String channelName) throws ExecutionException, InterruptedException {
        listeners.sendToYoutubeParser(channelName);
        logger.info("Отправлено сообщение на: youtube-parser-topic");
        return ResponseEntity.ok().body(listeners.getYoutubeFuture().get());
    }
}
