package heiphin.kafka.controller;

import heiphin.kafka.entity.Car;
import heiphin.kafka.entity.Listing;
import heiphin.kafka.entity.Video;
import heiphin.kafka.kafka.Listeners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class MainController {

    @Autowired
    private Listeners listeners;

    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    @PostMapping("/api/cars/{carName}")
    public CompletableFuture<ResponseEntity<List<Car>>> parseWithMicroservice(@PathVariable String carName) {
        return listeners.sendToKolesaParser(carName)
                .thenApplyAsync(carList -> {
                    logger.info("Отправлено сообщение на: kolesa-parser-topic");
                    return ResponseEntity.ok().body(carList);
                });
    }

    @PostMapping("/api/olx/{thingName}")
    public CompletableFuture<ResponseEntity<List<Listing>>> parserOlx(@PathVariable String thingName) {
        return listeners.sendToOlxParser(thingName)
                .thenApplyAsync(listings -> {
                    logger.info("Отправлено сообщение на: olx-parser-topic");
                    return ResponseEntity.ok().body(listings);
                });
    }

    @PostMapping("/api/youtube/{channelName}")
    public CompletableFuture<ResponseEntity<List<Video>>> parseYoutubeChannel(@PathVariable String channelName) {
        return listeners.sendToYoutubeParser(channelName)
                .thenApplyAsync(videoList -> {
                    logger.info("Отправлено сообщение на: youtube-parser-topic");
                    return ResponseEntity.ok().body(videoList);
                });
    }
}