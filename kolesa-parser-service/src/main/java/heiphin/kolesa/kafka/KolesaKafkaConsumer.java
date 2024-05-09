package heiphin.kolesa.kafka;

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
    private final KafkaTemplate<String, List<Car>> kafkaTemplate;

    @Autowired
    public KolesaKafkaConsumer(KolesaParserService kolesaParserService, KafkaTemplate<String, List<Car>> kafkaTemplate) {
        this.kolesaParserService = kolesaParserService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(id = "kolesaGroupId", topics = "kolesa-parser-topic")
    public void parseByCarName(String carName) {
        List<Car> carList = kolesaParserService.parseKolesa(carName);
        kafkaTemplate.send("kolesa-parser-response", carList);
    }
}
