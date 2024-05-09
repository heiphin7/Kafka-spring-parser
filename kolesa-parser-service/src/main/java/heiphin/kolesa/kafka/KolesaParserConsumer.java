package heiphin.kolesa.kafka;

import heiphin.kolesa.entity.Car;
import heiphin.kolesa.service.KolesaParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KolesaParserConsumer {

    private final KolesaParserService kolesaParserService;

    @KafkaListener(id = "kolesaGroupId" ,topics = "kolesa-parser-topic")
    public List<Car> parseByCarName(String carName) {
        System.out.println("kolesa consumer get message: " + carName);
        return kolesaParserService.parseKolesa(carName);
    }

}
