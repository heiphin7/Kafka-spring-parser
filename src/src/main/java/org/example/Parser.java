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
                        break;
                    } else {
                        System.out.println("Название: " + title);
                        System.out.println("Цена: " + price);
                        System.out.println("Ссылка: " + link);
                        System.out.println("Описание: " + description);
                        System.out.println("---");
                    }
                }
            }
        } catch (IOException e) {
            return;
        }
    }
}
