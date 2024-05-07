package heiphin.parser.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParserService {
    private static final String BASE_URL = "https://kolesa.kz/";

    public List<Car> parseKolesa(String brand) {
        int pages = 0;
        int pageCount = 3; // Дефолтное количество страниц, которые парсит парсер.
        List<Car> carList = new ArrayList<>();

        StringBuilder urlBuilder = new StringBuilder(BASE_URL).append("cars/");
        String[] carNameParts = brand.split(" ");

        for (int i = 0; i < carNameParts.length; i++) {
            carNameParts[i] = carNameParts[i] + "/";
        }


        for (String part : carNameParts) {
            urlBuilder.append(part);
        }

        String baseUrl = urlBuilder.toString();

        try {
            for (int i = 1; i <= pageCount; i++) {
                String url;

            /*
               Если текущая страница - первая, тогда у нее не должно быть
               атрибута ?page = number, так как первая страница будет
               поэтому делаем проверка на первую страницу
            */

                if (i == 1) {
                    url = baseUrl;
                } else {
                    url = baseUrl + "?page=" + i; // i -
                }
                // Лишь после инициализации ссылки для текущей страницы инициализируем остальные переменные
                Document doc = Jsoup.connect(url).get();
                Elements carAds = doc.select("div.a-list__item");

                for (Element ad : carAds) {
                    Element titleElem = ad.selectFirst("h5.a-card__title a");
                    String title = (titleElem != null) ? titleElem.text().trim() : "Нет информации о названии";

                    Element priceElem = ad.selectFirst("span.a-card__price");
                    String price = (priceElem != null) ? priceElem.text().trim() : "Нет информации о цене";

                    Element linkElem = ad.selectFirst("a.a-card__link");
                    String link = (linkElem != null) ? BASE_URL + linkElem.attr("href") : "Нет ссылки";

                    Element descriptionElem = ad.selectFirst("p.a-card__description");
                    String description = (descriptionElem != null) ? descriptionElem.text().trim() : "Нет описания";

                    if ( // Если нет информации, то ничего не выводим
                            title.equals("Нет информации о названии") &&
                            price.equals("Нет информации о цене") &&
                            link.equals("Нет ссылки") &&
                            description.equals("Нет описания")
                    ) {
                        continue;
                    }
                    Car car = new Car();
                    car.setName(title);
                    car.setPrice(price);
                    car.setLink(link);
                    car.setDescription(description);

                    carList.add(car);
                }
            }
        } catch (IOException e) {
            return carList;
        }

        return carList;
    }
}
