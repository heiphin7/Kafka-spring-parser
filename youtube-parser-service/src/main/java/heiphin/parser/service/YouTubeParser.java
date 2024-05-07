package heiphin.parser.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class YouTubeParser {

    public static void main(String[] args) {
        String channelUrl = "https://www.youtube.com/@requ2093/videos";
        WebDriver webDriver = new ChromeDriver();

        try {
            webDriver.get(channelUrl);

            // Ждем, пока элемент с определенным селектором не станет видимым
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofMillis(10000)); // Максимальное время ожидания в секундах
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".style-scope.ytd-rich-grid-row")));

            // После того, как элемент станет видимым, продолжаем выполнение кода
            String html = webDriver.getPageSource();

            // Создаем объект Document из HTML-кода страницы
            Document doc = Jsoup.parse(html);

            // Извлекаем блоки с видео
            Elements videoElements = doc.select("ytd-rich-grid-renderer #contents ytd-rich-item-renderer");

            // Перебираем каждый элемент с видео и извлекаем информацию
            for (Element videoElement : videoElements) {
                // Извлекаем заголовок видео
                Element titleElement = videoElement.selectFirst("#video-title");
                String title = titleElement != null ? titleElement.text() : "Нет заголовка";

                // Извлекаем ссылку на видео
                Element thumbnailElement = videoElement.selectFirst("a.yt-simple-endpoint.inline-block.style-scope.ytd-thumbnail");
                String videoLink = thumbnailElement != null ? "https://www.youtube.com" + thumbnailElement.attr("href") : "Нет ссылки на видео";

                // Извлекаем превью видео
                Element previewImageElement = videoElement.selectFirst("img.yt-core-image");
                String previewImageLink = previewImageElement != null ? previewImageElement.attr("src") : "Нет превью";

                // Извлекаем количество просмотров
                Element viewCountElement = videoElement.select("span.inline-metadata-item.style-scope.ytd-video-meta-block").first();
                String viewCount = viewCountElement != null ? viewCountElement.text() : "Нет данных о просмотрах";

                // Извлекаем дату загрузки видео
                Element uploadDateElement = videoElement.select("span.inline-metadata-item.style-scope.ytd-video-meta-block").get(1);
                String uploadDate = uploadDateElement != null ? uploadDateElement.text() : "Нет данных о дате загрузки";

                // Выводим информацию о видео
                System.out.println("Title: " + title);
                System.out.println("Views: " + viewCount);
                System.out.println("Upload Date: " + uploadDate);
                System.out.println("Link: " + videoLink);
                System.out.println("Preview image link:" + previewImageLink);
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webDriver.quit();
        }
    }
}
