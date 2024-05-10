package heiphin.olx.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import heiphin.olx.entity.Listing;
import heiphin.olx.service.OlxParserService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OlxKafkaListener {
    private final OlxParserService olxParserService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public OlxKafkaListener(OlxParserService olxParserService, KafkaTemplate<String, String> kafkaTemplate) {
        this.olxParserService = olxParserService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(groupId = "olxGroupId", topics = "olx-parser-topic")
    public void parserThingByName(String thingName) throws JsonProcessingException {
        List<Listing> thingsList = olxParserService.parseOLX(thingName);
        String thingsListJson = new ObjectMapper().writeValueAsString(thingsList);
        kafkaTemplate.send("olx-parser-response", thingsListJson);
    }

}
