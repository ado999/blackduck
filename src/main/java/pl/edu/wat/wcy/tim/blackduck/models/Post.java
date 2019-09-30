package pl.edu.wat.wcy.tim.blackduck.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public Post(String title, String contentUrl, ContentType contentType, User author, Date creationDate, String description, Folder rootFolder) {
        this.title = title;
        this.contentUrl = contentUrl;
        this.contentType = contentType;
        this.author = author;
        this.creationDate = creationDate;
        this.description = description;
        this.rootFolder = rootFolder;
    }

}
