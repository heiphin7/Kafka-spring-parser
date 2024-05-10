package heiphin.youtube.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import heiphin.youtube.entity.Video;
import heiphin.youtube.service.YouTubeParserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class YoutubeKafkaConsumer {

    private final Logger logger = (Logger) LoggerFactory.getLogger(YoutubeKafkaConsumer.class);
    private final YouTubeParserService youTubeParser;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(groupId = "youtubeGroupId", topics = "youtube-parser-topic")
    public void youTubeParser(String channelName) throws JsonProcessingException {
        logger.info("Получено сообщение в topic: youtube-parser-topic");
        List<Video> videoList = youTubeParser.parseYoutubeChannel(channelName);
        String videoListParser = new ObjectMapper().writeValueAsString(videoList);
        kafkaTemplate.send("youtube-parser-response", videoListParser);
    }

}
