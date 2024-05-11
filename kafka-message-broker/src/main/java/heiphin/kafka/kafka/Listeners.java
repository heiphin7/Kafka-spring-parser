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
    private KafkaTemplate<String, String> kafkaTemplateMessage;

    @Autowired
    public Listeners(KafkaTemplate<String, String> kafkaTemplateMessage) {
        this.kafkaTemplateMessage = kafkaTemplateMessage;
    }
    CompletableFuture<List<Car>> kolesaFuture;
    CompletableFuture<List<Listing>> olxFuture;
    CompletableFuture<List<Video>> youtubeFuture;


    public CompletableFuture<List<Car>> sendToKolesaParser(String carName) {
        kolesaFuture = new CompletableFuture<>();
        kafkaTemplateMessage.send("kolesa-parser-topic", carName);
        return kolesaFuture;
    }

    public CompletableFuture<List<Listing>> sendToOlxParser(String thingName) {
        olxFuture = new CompletableFuture<>();
        kafkaTemplateMessage.send("olx-parser-topic", thingName);
        return olxFuture;
    }

    public CompletableFuture<List<Video>> sendToYoutubeParser(String channelName) {
        youtubeFuture = new CompletableFuture<>();
        kafkaTemplateMessage.send("youtube-parser-topic", channelName);
        return youtubeFuture;
    }

    @KafkaListener(groupId = "kolesaGroupId", topics = "kolesa-parser-response")
    public void kolesaListener(String carListJson) throws JsonProcessingException {
        logger.info("Получено сообщение в kafka-message-broker, от: kolesa-parser-service");
        List<Car> carList = new ObjectMapper().readValue(carListJson, new TypeReference<List<Car>>() {});
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
}
