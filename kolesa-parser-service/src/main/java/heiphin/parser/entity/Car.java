package heiphin.parser.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
public class Car {
    private String name;
    private String price;
    private String link; // to page
    private String description;
}
