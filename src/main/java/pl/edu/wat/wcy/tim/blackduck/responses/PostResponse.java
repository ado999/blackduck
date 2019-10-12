package pl.edu.wat.wcy.tim.blackduck.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private int id;

    private String title;

    private String contentUrl;

    private ContentTypeResponse contentType; //video/photo

    private UserShortResponse author;

    private Date creationDate;

    private String description;

    private List<HashtagResponse> hashtags = new ArrayList<>();

    private Double rate;

    private List<CommentResponse> comments = new ArrayList<>();

    private List<RateResponse> rates = new ArrayList<>();
}