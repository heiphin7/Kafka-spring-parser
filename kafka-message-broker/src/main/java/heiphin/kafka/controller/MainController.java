package heiphin.kafka.controller;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
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

    @PostMapping("/api/{carName}")
    public ResponseEntity<?> parseWithMicroservice(@PathVariable String carName) {
        // Отправляем сообщение в Kafka и ожидаем ответа
        CompletableFuture<SendResult<String, String>> future = kafkaTemplateMessage.send("kolesa-parser-topic", carName);

        try {
            // Получаем результат выполнения операции
            SendResult<String, String> result = future.get();
            // Получаем ответ из результата
            RecordMetadata carList = result.getRecordMetadata();
            // Возвращаем ответ клиенту
            return ResponseEntity.ok(carList);
        } catch (InterruptedException | ExecutionException e) {
            // В случае ошибки возвращаем соответствующий статус
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
