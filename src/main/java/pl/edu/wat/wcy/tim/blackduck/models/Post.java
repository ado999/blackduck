package pl.edu.wat.wcy.tim.blackduck.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String title;

    private String contentUrl;

    private ContentType contentType; //video/photo

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    private Date creationDate = new Date();

    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "rootPost")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "rootPost")
    private List<Rate> rates = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder rootFolder;

    private double rate;

    public Post(String title, String contentUrl, ContentType contentType, User author, Date creationDate, String description, Folder rootFolder, double rate) {
        this.title = title;
        this.contentUrl = contentUrl;
        this.contentType = contentType;
        this.author = author;
        this.creationDate = creationDate;
        this.description = description;
        this.rootFolder = rootFolder;
        this.rate = rate;
    }

    public double getRate() {
        int sum = 0;
        double frate = 0.0;
        for(Rate rate: rates){
            sum += rate.getRate();
        }
        frate = sum/(rates.size());
        return frate;
    }

}
