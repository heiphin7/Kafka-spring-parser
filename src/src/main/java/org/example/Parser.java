package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class Parser {
    public static void main(String[] args) {
        parseKolesa();
    }

    public static void parseKolesa() {
        // URL страницы, которую мы будем парсить
        String url = "https://kolesa.kz/";

        try {
            // Отправляем GET-запрос на указанный URL и получаем HTML-страницу
            Document doc = Jsoup.connect(url).get();

            // Находим все объявления о продаже автомобилей
            Elements carAds = doc.select("div.hot-item");

            // Проходимся по каждому объявлению и извлекаем информацию
            for (Element ad : carAds) {
                // Получаем название автомобиля
                Element titleElem = ad.selectFirst("span.bot");
                String title = (titleElem != null) ? titleElem.text().trim() : "Нет информации о названии";

                // Получаем цену автомобиля
                Element priceElem = ad.selectFirst("span.bot");
                String price = (priceElem != null) ? priceElem.text().trim() : "Нет информации о цене";

                // Получаем ссылку на объявление
                Element linkElem = ad.selectFirst("a");
                String link = (linkElem != null) ? linkElem.attr("href") : "Нет ссылки";

                // Выводим информацию
                System.out.println("Название: " + title);
                System.out.println("Цена: " + price);
                System.out.println("Ссылка: " + link);
                System.out.println("---");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке страницы: " + e.getMessage());
        }
    }
}