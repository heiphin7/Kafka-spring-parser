package heiphin.kafka.controller;

import heiphin.kafka.entity.Car;
import heiphin.kafka.entity.Listing;
import heiphin.kafka.entity.Video;
import heiphin.kafka.kafka.Listeners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
public class MicroservicesController {

    @Autowired
    private Listeners listeners;

    private final Logger logger = LoggerFactory.getLogger(MicroservicesController.class);

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

    @PostMapping("/api/youtube/")
    public String parseYoutubeChannel(@RequestParam("channelName") String channelName, Model model) throws ExecutionException, InterruptedException {

        CompletableFuture<List<Video>> list = listeners.sendToYoutubeParser(channelName)
                .thenApplyAsync(videoList -> {
                    logger.info("Отправлено сообщение на: youtube-parser-topic");
                    return videoList;
                });

        // Для отображения (видео, где есть превью)
        List<Video> videoList = new ArrayList<>();

        // Проверяем и сохраняем в новый список
        for (Video video: list.get()) {
            // Сохраняем видео, ТОЛЬКО если у него есть ссылка на превью-изображение
            if (video.getPreviewLink() != null || !video.getPreviewLink().isEmpty() || !video.getPreviewLink().isBlank()){
                videoList.add(video);
            }
        }
        model.addAttribute("videos", videoList);
        return "index";
    }

}