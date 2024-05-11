package heiphin.kafka.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import heiphin.kafka.entity.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaService {
    private Logger logger = LoggerFactory.getLogger(KafkaService.class);
    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public List<Car> parseCar(String carName) throws ExecutionException, InterruptedException {
        CompletableFuture<List<Car>> future = new CompletableFuture<>();
        sendToKolesaParser(carName);
        return future.get();
    }

    @KafkaListener(groupId = "kolesaGroupId", topics = "kolesa-parser-response")
    public void kolesaListener(String carListJson) throws JsonProcessingException {
        List<Car> carList = new ObjectMapper().readValue(carListJson, new TypeReference<List<Car>>() {});
    }

    private void sendToKolesaParser(String carName) {
        System.out.println("al;df");
    }

}
