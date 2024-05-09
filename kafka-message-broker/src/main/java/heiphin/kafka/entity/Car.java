package heiphin.kafka.entity;

import lombok.Data;

@Data
public class Car {
    private String name;
    private String price;
    private String link; // to page
    private String description;
}