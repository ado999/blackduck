package pl.edu.wat.wcy.tim.blackduck.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "hashtags")
@Data
@NoArgsConstructor
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String name;

    @ManyToMany
    private List<Post> rootPosts;

    public Hashtag(String name) {
        this.name = name;
    }
}
