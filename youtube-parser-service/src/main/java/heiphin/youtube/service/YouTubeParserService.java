package heiphin.youtube.service;

import heiphin.youtube.entity.Video;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class YouTubeParserService {

    private static final String BASIC_URL = "https://www.youtube.com/@";
    private static final String SECOND_PART = "/videos";

    public List<Video> parseYoutubeChannel(String channelName) {
        List<Video> videosInChannel = new ArrayList<>();

        String channelUrl = BASIC_URL + channelName + SECOND_PART;
        WebDriver webDriver = new ChromeDriver();

        try {
            webDriver.get(channelUrl);

            // Получаем заголовок страницы до получения HTML-кода и скролла
            String pageTitle = webDriver.getTitle();

            // Проверяем заголовок страницы на наличие текста ошибки 404
            if (pageTitle.contains("404 Not Found")) {
                return videosInChannel; // Возвращаем пустой список
            }

            // Для прокрутки вниз страницы
            JavascriptExecutor js = (JavascriptExecutor) webDriver;

            // Цикл для скролла вниз
            for (int i = 0; i < 30; i++) {
                js.executeScript("window.scrollBy(0,500)");
                Thread.sleep(10);
            }

            Thread.sleep(2000);

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

                Video video = new Video();
                video.setDate(uploadDate);
                video.setTitle(title);
                video.setLinkToVideo(videoLink);
                video.setViews(viewCount);
                video.setPreviewLink(previewImageLink);
                videosInChannel.add(video);
            }

        } catch (NoSuchElementException e) {
            // Если элемент title не найден, обработка ошибки
            e.printStackTrace();
            return videosInChannel;

        } catch (Exception e) {
            // Ловим остальные исключения
            e.printStackTrace();
            return videosInChannel;

        } finally {
            webDriver.quit();
        }

        return videosInChannel;
    }
}
