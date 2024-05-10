package heiphin.kafka.entity;

import lombok.Data;

@Data
public class Video {
    private String title;
    private String views;
    private String date;
    private String previewLink;
    private String linkToVideo;
}

