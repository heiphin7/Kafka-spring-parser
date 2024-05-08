package heiphin.parser.controller;

import heiphin.parser.entity.Listing;
import heiphin.parser.service.ParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ParserController {

    private final ParserService parserService;

    @PostMapping("/parse/{query}")
    public ResponseEntity<?> parserAds (@PathVariable String query) {
        List<Listing> listings =  parserService.parseOLX(query);

        if (listings.isEmpty()) {
            return new ResponseEntity<>("По запросу ничего не найдено!", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(listings);
    }
}
