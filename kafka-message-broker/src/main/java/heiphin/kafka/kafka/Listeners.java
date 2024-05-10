package heiphin.kafka.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import heiphin.kafka.entity.Car;
import heiphin.kafka.entity.Listing;
import heiphin.kafka.entity.Video;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class Listeners {

    private final CompletableFuture<List<Car>> kolesaFuture = new CompletableFuture<>();
    private final CompletableFuture<List<Listing>> olxFuture = new CompletableFuture<>();
    private final CompletableFuture<List<Video>> youtubeFuture = new CompletableFuture<>();

    @KafkaListener(groupId = "kolesaGroupId", topics = "kolesa-parser-response") // for kolesa parser service
    public void kolesaListener(String carListJson) throws JsonProcessingException { // Изменить тип на String
        List<Car> carList = new ObjectMapper().readValue(carListJson, new TypeReference<List<Car>>() {}); // Десериализовать из JSON
        kolesaFuture.complete(carList);
    }

    @KafkaListener(groupId = "olxGroupId", topics = "olx-parser-response")
    public void olxListener(String liningListJson) throws JsonProcessingException {
        List<Listing> listings = new ObjectMapper().readValue(liningListJson, new TypeReference<List<Listing>>() {});
        olxFuture.complete(listings);
    }

    @KafkaListener(groupId = "youtubeGroupId", topics = "youtube-parser-response")
    public void youtubeListener(String videoListJson) throws JsonProcessingException {
        List<Video> videoList = new ObjectMapper().readValue(videoListJson, new TypeReference<List<Video>>() {});
        youtubeFuture.complete(videoList);
    }
}
