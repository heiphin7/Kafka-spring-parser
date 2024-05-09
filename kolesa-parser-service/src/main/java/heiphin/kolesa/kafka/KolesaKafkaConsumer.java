package heiphin.kolesa.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import heiphin.kolesa.entity.Car;
import heiphin.kolesa.service.KolesaParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KolesaKafkaConsumer {

    private final KolesaParserService kolesaParserService;
    private final KafkaTemplate<String, String> kafkaTemplate; // Заменить на <String, String>

    @Autowired
    public KolesaKafkaConsumer(KolesaParserService kolesaParserService, KafkaTemplate<String, String> kafkaTemplate) { // Изменить тип KafkaTemplate
        this.kolesaParserService = kolesaParserService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(id = "kolesaGroupId", topics = "kolesa-parser-topic")
    public void parseByCarName(String carName) throws JsonProcessingException {
        List<Car> carList = kolesaParserService.parseKolesa(carName);
        String carListJson = new ObjectMapper().writeValueAsString(carList); // Сериализовать в JSON
        kafkaTemplate.send("kolesa-parser-response", carListJson); // Отправить как JSON строку
    }
}
