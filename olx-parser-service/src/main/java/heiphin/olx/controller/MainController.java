package heiphin.olx.controller;

import heiphin.olx.entity.Listing;
import heiphin.olx.service.OlxParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final OlxParserService parserService;

    @PostMapping("/parse/{query}")
    public ResponseEntity<?> parserAds (@PathVariable String query) throws MalformedURLException {
        List<Listing> listings =  parserService.parseOLX(query);

        if (listings.isEmpty()) {
            return new ResponseEntity<>("По запросу ничего не найдено!", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(listings);
    }
}
