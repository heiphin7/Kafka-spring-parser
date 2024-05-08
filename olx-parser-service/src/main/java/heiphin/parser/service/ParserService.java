package heiphin.parser.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ParserService {
    private static final String BASE_URL = "https://www.olx.kz/list/q-";

    public void parseKolesa(String query) {
        String FULL_URL = BASE_URL + query + "/";

        try {
            // Лишь после инициализации ссылки для текущей страницы инициализируем остальные переменные
            Document doc = Jsoup.connect(FULL_URL).get();
            Elements carAds = doc.select("div.css-1sw7q4x");


            for (Element ad : carAds) {
                Element titleElem = ad.selectFirst("h6.css-16v5mdi er34gjf0");
                String title = (titleElem != null) ? titleElem.text().trim() : "Нет информации о названии";

                Element linkElem = ad.selectFirst("a.css-z3gu2d");
                String link = (linkElem != null) ? BASE_URL + linkElem.attr("href") : "Нет ссылки";

                Element descriptionElem = ad.selectFirst("p.css-tyui9s er34gjf0");
                String price = (descriptionElem != null) ? descriptionElem.text().trim() : "Нет описания";

                Element locationElem = ad.selectFirst("p.css-1a4brun er34gjf0");
                String locationInfo = (locationElem != null) ? locationElem.text().trim() : "Нет местонахождение";

                if ( // Если нет информации, то ничего не выводим
                        title.equals("Нет информации о названии") &&
                                link.equals("Нет ссылки") &&
                                    price.equals("Нет описания")
                ) {
                    continue;
                }

                System.out.println(title);
                System.out.println(price);
                System.out.println(link);
                System.out.println(locationInfo);
                System.out.println("====================================");

                }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}