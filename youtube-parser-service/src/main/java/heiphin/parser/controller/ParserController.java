package heiphin.parser.controller;

import heiphin.parser.service.ParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ParserController {

    private final ParserService parserService;

    @PostMapping("/parse/{channelName}")
    public void getCarsByName (@PathVariable String channelName) {
        parserService.parseYoutube(channelName);
    }
}
