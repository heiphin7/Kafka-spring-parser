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
    private static final String BASE_URL = "https://www.youtube.com/@";
    private static final String SECOND_PART_URL = "/videos"; // for parse videos

    public void parseYoutube(String channelName) {
        String FULL_URL = BASE_URL + channelName + SECOND_PART_URL;
        System.out.println(FULL_URL);
        try {
            System.out.println("start parsing");

            // Лишь после инициализации ссылки для текущей страницы инициализируем остальные переменные
            Document doc = Jsoup.connect(FULL_URL).get();
            Elements videoItems = doc.select("ytd-rich-item-renderer.style-scope ytd-rich-grid-row");

            for (Element ad : videoItems) {
                System.out.println("цикл сработал");
                    Element titleElem = ad.selectFirst("span.style-scope yt-formatted-string");
                    String title = (titleElem != null) ? titleElem.text().trim() : "Нет информации о названии";

                    Element linkElem = ad.selectFirst("a.video-title-link");
                    String link = (linkElem != null) ? BASE_URL + linkElem.attr("href") : "Нет ссылки";

                    Element descriptionElem = ad.selectFirst("span.inline-metadata-item style-scope ytd-video-meta-block");
                    String description = (descriptionElem != null) ? descriptionElem.text().trim() : "Нет описания";

                    if ( // Если нет информации, то ничего не выводим
                            title.equals("Нет информации о названии") &&
                            link.equals("Нет ссылки") &&
                            description.equals("Нет описания")
                    ) {
                        continue;
                    }

                    System.out.println(title);
                    System.out.println(link);
                    System.out.println(description);

                }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
