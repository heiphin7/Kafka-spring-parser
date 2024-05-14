package heiphin.olx.service;

import heiphin.olx.entity.Listing;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OlxParserService {
    private static final String BASE_URL = "https://www.olx.kz/list/q-";
    private static final String LISTGIN_URL = "https://www.olx.kz";

    public List<Listing> parseOLX(String query) {
        List<Listing> listForReturn = new ArrayList<>();

        String FULL_URL = BASE_URL + query + "/";

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        // Создаем экземпляр WebDriver
        WebDriver webDriver = new ChromeDriver(options);

        try {
            // Устанавливаем максимальное время ожидания элементов на странице
            webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            // Загружаем страницу
            webDriver.get(FULL_URL);

            // Выполняем скрипты на странице для прокрутки вниз
            JavascriptExecutor js = (JavascriptExecutor) webDriver;

            // Ждем загрузки всех изображений на странице
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(60));
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("img.css-8wsg1m")));

            Thread.sleep(1000);

            // Цикл для скролла вниз
            for (int i = 0; i < 15; i++) { // увеличиваем количество скроллов
                Thread.sleep(250);
                js.executeScript("window.scrollBy(0,600)");
            }

            // Ждем некоторое время для полной загрузки страницы
            Thread.sleep(1000); // Увеличили время ожидания до 5 секунд

            // Получаем HTML-код страницы после полной загрузки изображений
            String html = webDriver.getPageSource();

            // Создаем объект Document из HTML-кода страницы
            Document doc = Jsoup.parse(html);

            // Измените код, который ищет изображение в каждом объявлении
            Elements listings = doc.select("div[data-cy=\"l-card\"]");

            // Перебираем каждое объявление и извлекаем информацию
            for (Element ad : listings) {
                Element titleElem = ad.selectFirst("h6.css-16v5mdi.er34gjf0");
                String title = (titleElem != null) ? titleElem.text().trim() : "Нет информации о названии";

                Element linkElem = ad.selectFirst("a.css-z3gu2d");
                String link = (linkElem != null) ? LISTGIN_URL + linkElem.attr("href") : "Нет ссылки";

                Element descriptionElem = ad.selectFirst("p.css-tyui9s.er34gjf0");
                String price = (descriptionElem != null) ? descriptionElem.text().trim() : "Нет описания";

                // Проверяем, что изображение найдено и получаем ссылку на него
                String previewImageLink = "Нет превью";
                Element previewImageElement = ad.selectFirst("div#" + ad.id() + " img.css-gwhqbt");
                if (previewImageElement != null && previewImageElement.hasAttr("src")) {
                    previewImageLink = previewImageElement.attr("src");
                }

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
                }else if (previewImageLink.equals("Нет превью") || previewImageLink.startsWith("/app/static")) {
                    continue;
                }

                Listing listing = new Listing();
                listing.setTitle(title);
                listing.setPrice(price);
                listing.setLink(link);
                listing.setLocationInfo(locationInfo);
                listing.setPreviewImage(previewImageLink);

                listForReturn.add(listing);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Выводим информацию об ошибке
        } finally {
            // Закрываем браузер после завершения работы
            webDriver.quit();
        }
        return listForReturn;
    }
}
