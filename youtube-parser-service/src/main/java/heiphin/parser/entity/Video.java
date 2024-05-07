package heiphin.parser.entity;

import lombok.Data;

@Data // For setters & getters
public class Video {
    private String title;
    private String views;
    private String date;
    private String previewLink;
    private String linkToVideo;
}
