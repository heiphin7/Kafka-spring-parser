package heiphin.kafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import heiphin.kafka.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
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
    private KafkaTemplate<String, String> kafkaTemplateMessage; // Заменить на <String, String>

    private CompletableFuture<List<Car>> future = new CompletableFuture<>();

    @PostMapping("/api/{carName}")
    public ResponseEntity<List<Car>> parseWithMicroservice(@PathVariable String carName) throws ExecutionException, InterruptedException {
        kafkaTemplateMessage.send("kolesa-parser-topic", carName);
        return ResponseEntity.ok().body(future.get());
    }

    @KafkaListener(groupId = "kolesaGroupId", topics = "kolesa-parser-response")
    public void kolesaListener(String carListJson) throws JsonProcessingException { // Изменить тип на String
        List<Car> carList = new ObjectMapper().readValue(carListJson, new TypeReference<List<Car>>() {}); // Десериализовать из JSON
        future.complete(carList);
    }
}
