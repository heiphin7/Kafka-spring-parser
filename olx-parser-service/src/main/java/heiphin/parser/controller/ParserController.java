package heiphin.parser.controller;

import heiphin.parser.service.ParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParserController {

    private final ParserService parserService;

    @PostMapping("/parse/{query}")
    public void parserAds (@PathVariable String query) {
        parserService.parseKolesa(query);
    }
}
