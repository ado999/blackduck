package pl.edu.wat.wcy.tim.blackduck.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "folders")
@Data
@NoArgsConstructor
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users", referencedColumnName = "id")
    private User owner; // obvious

    private String folderName; // eg. "Yummy"

    @Size(max = 100)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "rootFolder")
    private List<Post> contentList; // files inside dir

    public Folder(User owner, String folderName, String description) {
        this.owner = owner;
        this.folderName = folderName;
        this.description=description;
    }
}
