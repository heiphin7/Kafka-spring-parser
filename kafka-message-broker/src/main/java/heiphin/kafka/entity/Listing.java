package heiphin.kafka.entity;

import lombok.Data;

@Data
public class Listing { // "Объявление"
    private String title;
    private String price;
    private String link;
    private String locationInfo;
    private String previewImage;
}
