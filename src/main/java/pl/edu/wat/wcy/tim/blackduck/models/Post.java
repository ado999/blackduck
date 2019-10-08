package pl.edu.wat.wcy.tim.blackduck.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String title;

    private String contentUrl;

    private ContentType contentType; //video/photo

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    private Date creationDate = new Date();

    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "rootPost")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "rootPost")
    private List<Rate> rates = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder rootFolder;

    private double rate = 0d;

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
