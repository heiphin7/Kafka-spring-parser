package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Scanner;

public class Parser {
    private static final String BASE_URL = "https://kolesa.kz/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите марку машины:");
        String brand = scanner.nextLine().toLowerCase();

        if (brand.length() < 3) {
            System.out.println("Название марки/модели должно быть больше 3 символов");
            return; // Exit the program if brand name is too short
        }

        String[] parts = brand.split(" ");

        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i] + "/";
        }

        parseKolesa(parts, 10); // Parse 10 pages
    }

    public static void parseKolesa(String[] brand, int pageCount) {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL).append("cars/");

        for (String part : brand) {
            urlBuilder.append(part);
        }

        String baseUrl = urlBuilder.toString();

        try {
            for (int i = 2; i <= pageCount; i++) {
                String url = baseUrl + "?page=" + i; // Include "?" before the "page" parameter
                Document doc = Jsoup.connect(url).get();
                Elements carAds = doc.select("div.a-list__item");

                for (Element ad : carAds) {
                    Element titleElem = ad.selectFirst("h5.a-card__title a");
                    String title = (titleElem != null) ? titleElem.text().trim() : "Нет информации о названии";

                    Element priceElem = ad.selectFirst("span.a-card__price");
                    String price = (priceElem != null) ? priceElem.text().trim() : "Нет информации о цене";

                    Element linkElem = ad.selectFirst("a.a-card__link");
                    String link = (linkElem != null) ? BASE_URL + linkElem.attr("href") : "Нет ссылки";

                    System.out.println("Название: " + title);
                    System.out.println("Цена: " + price);
                    System.out.println("Ссылка: " + link);
                    System.out.println("---");
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке страницы: " + e.getMessage());
        }
    }
}
