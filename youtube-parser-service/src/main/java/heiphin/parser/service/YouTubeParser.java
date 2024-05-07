package heiphin.parser.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class YouTubeParser {

    public static void main(String[] args) {
        String channelUrl = "https://www.youtube.com/@java/videos";
        try {
            // Получаем HTML-код страницы канала
            Document doc = Jsoup.connect(channelUrl).get();

            // Извлекаем блоки с видео
            Elements videoElements = doc.select("div.yt-lockup-video");

            // Проверяем, сколько элементов найдено
            System.out.println("Найдено видео: " + videoElements.size());

            // Перебираем каждый элемент с видео и извлекаем информацию
            for (Element videoElement : videoElements) {
                System.out.println("цикл начался");
                // Извлекаем заголовок видео
                Element titleElement = videoElement.selectFirst("h3.yt-lockup-title a");
                String title = titleElement.text();

                // Извлекаем ссылку на видео
                String videoUrl = "https://www.youtube.com" + titleElement.attr("href");

                // Извлекаем количество просмотров
                Element viewCountElement = videoElement.selectFirst("ul.yt-lockup-meta-info li");
                String viewCount = viewCountElement.text();

                // Выводим информацию о видео
                System.out.println("Title: " + title);
                System.out.println("URL: " + videoUrl);
                System.out.println("Views: " + viewCount);
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


