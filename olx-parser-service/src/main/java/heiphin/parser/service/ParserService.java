package heiphin.parser.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ParserService {
    private static final String BASE_URL = "https://www.olx.kz/list/q-";

    public void parseKolesa(String query) {
        String FULL_URL = BASE_URL + query + "/";

        // Создаем экземпляр WebDriver
        WebDriver webDriver = new ChromeDriver();

        try {
            // Устанавливаем максимальное время ожидания элементов на странице
            webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            // Загружаем страницу
            webDriver.get(FULL_URL);

            // Выполняем скрипты на странице для прокрутки вниз
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

            // Ждем некоторое время для полной загрузки страницы
            Thread.sleep(2000);

            // Получаем HTML-код страницы
            String html = webDriver.getPageSource();

            // Создаем объект Document из HTML-кода страницы
            Document doc = Jsoup.parse(html);

            // Извлекаем объявления
            Elements carAds = doc.select("div.css-1sw7q4x");

            // Перебираем каждое объявление и извлекаем информацию
            for (Element ad : carAds) {
                Element titleElem = ad.selectFirst("h6.css-16v5mdi.er34gjf0");
                String title = (titleElem != null) ? titleElem.text().trim() : "Нет информации о названии";

                Element linkElem = ad.selectFirst("a.css-z3gu2d");
                String link = (linkElem != null) ? BASE_URL + linkElem.attr("href") : "Нет ссылки";

                Element descriptionElem = ad.selectFirst("p.css-tyui9s.er34gjf0");
                String price = (descriptionElem != null) ? descriptionElem.text().trim() : "Нет описания";

                // Исключаем объявления с ценой "Договорная"
                if (price.contains("Договорная")) {
                    continue;
                }

                Element locationElem = ad.selectFirst("p.css-1a4brun.er34gjf0");
                String locationInfo = (locationElem != null) ? locationElem.text().trim() : "Нет местонахождения";

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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            // Закрываем браузер после завершения работы
            webDriver.quit();
        }
    }
}
