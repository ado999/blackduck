package pl.edu.wat.wcy.tim.blackduck.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "folders")
@Getter
@Setter
@NoArgsConstructor
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @ManyToOne
    @JoinColumn(name = "users")
    private User owner; // obvious

    private String folderName; // eg. "Yummy"

    @Size(max = 100)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rootFolder")
    @JsonManagedReference
    private List<Post> contentList; // files inside dir

    public Folder(User owner, String folderName, String description) {
        this.owner = owner;
        this.folderName = folderName;
        this.description=description;
    }
}
