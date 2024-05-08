package heiphin.kolesa.controller;

import heiphin.kolesa.entity.Car;
import heiphin.kolesa.service.KolesaParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final KolesaParserService parserService;

    @PostMapping("/parse/{carName}") // parsing by car name
    public List<Car> getCarsByName (@PathVariable String carName) {
        return parserService.parseKolesa(carName);
    }
}
