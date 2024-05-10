package heiphin.youtube.controller;

import heiphin.youtube.entity.Video;
import heiphin.youtube.service.YouTubeParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final YouTubeParserService parserService;

    @PostMapping("/parse/{channelName}")
    public ResponseEntity<?> getVideosInChannel (@PathVariable String channelName) {
        List<Video> videoList = parserService.parseYoutubeChannel(channelName);

        if(videoList.isEmpty()) {
            return new ResponseEntity<>("Канал не найден или на нем нету видео", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(videoList);
    }
}
