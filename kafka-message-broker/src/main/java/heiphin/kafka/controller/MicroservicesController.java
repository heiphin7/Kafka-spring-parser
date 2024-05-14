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

    private Listeners listeners;

    public MicroservicesController(Listeners listeners) {
        this.listeners = listeners;
    }

    private final Logger logger = LoggerFactory.getLogger(MicroservicesController.class);

    @PostMapping("/api/cars/")
    public String parseWithMicroservice(@RequestParam(name = "carName") String carName, Model model) throws ExecutionException, InterruptedException {
        CompletableFuture<List<Car>> list = listeners.sendToKolesaParser(carName)
                .thenApplyAsync(
                        Cars -> {
                            logger.info("Отправлено сообщение на kolesa-parser-topic");
                            return Cars;
                        }
                );

        model.addAttribute("cars", list.get());
        return "kolesa-results";
    }

    @PostMapping("/api/olx/")
    public String parserOlx(@RequestParam("thingName") String thingName, Model model) throws ExecutionException, InterruptedException {
        CompletableFuture<List<Listing>> list = listeners.sendToOlxParser(thingName)
                .thenApply(
                        listings -> {
                            logger.info("Отправлено сообщение на olx-parser-topic");
                            return listings;
                        }
                );
        model.addAttribute("listingList", list.get());
        return "olx-results";
    }

    @PostMapping("/api/youtube/")
    public String parseYoutubeChannel(@RequestParam("channelName") String channelName, Model model) throws ExecutionException, InterruptedException {
        CompletableFuture<List<Video>> list = listeners.sendToYoutubeParser(channelName)
                .thenApplyAsync(videoList -> {
                    logger.info("Отправлено сообщение на: youtube-parser-topic");
                    return videoList;
                });

        // Список для отсортированного списка (Где есть превью-изображение)
        List<Video> videoList = new ArrayList<>();

        // Проверяем и сохраняем в новый список
        for (Video video: list.get()) {
            // Сохраняем видео, ТОЛЬКО если у него есть ссылка на превью-изображени
            if (video.getPreviewLink() != null && !video.getPreviewLink().isEmpty() && !video.getPreviewLink().isBlank()){
                videoList.add(video);
            }
        }
        model.addAttribute("videos", videoList);
        return "youtube-results";
    }

}