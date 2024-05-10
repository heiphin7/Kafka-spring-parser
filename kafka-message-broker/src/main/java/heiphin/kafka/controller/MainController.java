package heiphin.kafka.controller;

import heiphin.kafka.entity.Car;
import heiphin.kafka.entity.Listing;
import heiphin.kafka.entity.Video;
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
    private KafkaTemplate<String, String> kafkaTemplateMessage;

    private final Logger logger =  LoggerFactory.getLogger(MainController.class);

    private final CompletableFuture<List<Car>> kolesaFuture = new CompletableFuture<>();
    private final CompletableFuture<List<Listing>> olxFuture = new CompletableFuture<>();
    private final CompletableFuture<List<Video>> youtubeFuturue = new CompletableFuture<>();

    @PostMapping("/api/cars/{carName}")
    public ResponseEntity<List<Car>> parseWithMicroservice(@PathVariable String carName) throws ExecutionException, InterruptedException {
        kafkaTemplateMessage.send("kolesa-parser-topic", carName);
        logger.info("Отправлено сообщение на: kolesa-parser-topic");
        return ResponseEntity.ok().body(kolesaFuture.get());
    }

    @PostMapping("/api/olx/{thingName}")
    public ResponseEntity<List<Listing>> parserOlx(@PathVariable String thingName) throws ExecutionException, InterruptedException {
        kafkaTemplateMessage.send("olx-parser-topic", thingName);
        logger.info("Отправлено сообщение на: olx-parser-topic");
        return ResponseEntity.ok().body(olxFuture.get());
    }

    @PostMapping("/api/youtube/{channelName}")
    public ResponseEntity<List<Video>> parseYoutubeChannel(@PathVariable String channelName) throws ExecutionException, InterruptedException {
        kafkaTemplateMessage.send("youtube-parser-topic", channelName);
        logger.info("Отправлено сообщение на: youtube-parser-topic");
        return ResponseEntity.ok().body(youtubeFuturue.get());
    }

}
