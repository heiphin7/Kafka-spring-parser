package heiphin.parser.controller;

import heiphin.parser.entity.Car;
import heiphin.parser.service.ParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ParserController {

    private final ParserService parserService;

    @PostMapping("/parse/{carName}")
    public List<Car> getCarsByName (@PathVariable String carName) {
        System.out.println(carName);
        return parserService.parseKolesa(carName);
    }
}
