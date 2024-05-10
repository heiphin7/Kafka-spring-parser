package heiphin.kafka.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import heiphin.kafka.entity.Car;
import heiphin.kafka.entity.Listing;
import heiphin.kafka.entity.Video;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class Listeners {

    private static final Logger logger = LoggerFactory.getLogger(Listeners.class);
    private final CompletableFuture<List<Car>> kolesaFuture = new CompletableFuture<>();
    private final CompletableFuture<List<Listing>> olxFuture = new CompletableFuture<>();
    private final CompletableFuture<List<Video>> youtubeFuture = new CompletableFuture<>();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplateMessage;

    public void sendToKolesaParser(String carName) {
        kafkaTemplateMessage.send("kolesa-parser-topic", carName);
    }

    public void sendToOlxParser(String thingName) {
        kafkaTemplateMessage.send("olx-parser-topic", thingName);
    }

    public void sendToYoutubeParser(String channelName) {
        kafkaTemplateMessage.send("youtube-parser-topic", channelName);
    }

    @KafkaListener(groupId = "kolesaGroupId", topics = "kolesa-parser-response") // for kolesa parser service
    public void kolesaListener(String carListJson) throws JsonProcessingException { // Изменить тип на String
        logger.info("Получено сообщение в kafka-message-broker, от: kolesa-parser-service");
        List<Car> carList = new ObjectMapper().readValue(carListJson, new TypeReference<List<Car>>() {}); // Десериализовать из JSON
        kolesaFuture.complete(carList);
    }

    @KafkaListener(groupId = "olxGroupId", topics = "olx-parser-response")
    public void olxListener(String liningListJson) throws JsonProcessingException {
        logger.info("Получено сообщение в kafka-message-broker, от: olx-parser-service");
        List<Listing> listings = new ObjectMapper().readValue(liningListJson, new TypeReference<List<Listing>>() {});
        olxFuture.complete(listings);
    }

    @KafkaListener(groupId = "youtubeGroupId", topics = "youtube-parser-response")
    public void youtubeListener(String videoListJson) throws JsonProcessingException {
        logger.info("Получено сообщение в kafka-message-broker, от: youtube-parser-service");
        List<Video> videoList = new ObjectMapper().readValue(videoListJson, new TypeReference<List<Video>>() {});
        youtubeFuture.complete(videoList);
    }

    public CompletableFuture<List<Car>> getKolesaFuture() {
        return kolesaFuture;
    }

    public CompletableFuture<List<Listing>> getOlxFuture() {
        return olxFuture;
    }

    public CompletableFuture<List<Video>> getYoutubeFuture() {
        return youtubeFuture;
    }
}

