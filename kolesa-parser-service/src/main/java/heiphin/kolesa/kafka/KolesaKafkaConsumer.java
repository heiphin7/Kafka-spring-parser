package heiphin.kolesa.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import heiphin.kolesa.entity.Car;
import heiphin.kolesa.service.KolesaParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KolesaKafkaConsumer {

    private final Logger logger = LoggerFactory.getLogger(KolesaKafkaConsumer.class);
    private final KolesaParserService kolesaParserService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KolesaKafkaConsumer(KolesaParserService kolesaParserService, KafkaTemplate<String, String> kafkaTemplate) {
        this.kolesaParserService = kolesaParserService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(id = "kolesa-microservice", topics = "kolesa-parser-topic")
    public void parseByCarName(String carName) throws JsonProcessingException {
        logger.info("Получено сообщение в topic: kolesa-parser-topic");
        List<Car> carList = kolesaParserService.parseKolesa(carName);
        String carListJson = new ObjectMapper().writeValueAsString(carList); // Сериализовать в JSON
        kafkaTemplate.send("kolesa-parser-response", carListJson); // Отправить как JSON строку
    }
}
