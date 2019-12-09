package pl.edu.wat.wcy.tim.blackduck.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "postId", nullable = false)
    private Post rootPost;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    private String content;

    private Date creationDate;

    private Long videoTime;

    public Comment(Post rootPost, User author, String content, Date creationDate, Long videoTime) {
        this.rootPost = rootPost;
        this.author = author;
        this.content = content;
        this.creationDate = creationDate;
        this.videoTime = videoTime;
    }
}
