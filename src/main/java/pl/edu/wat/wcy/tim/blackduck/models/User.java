package pl.edu.wat.wcy.tim.blackduck.models;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import pl.edu.wat.wcy.tim.blackduck.util.RandomString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String uuid;

    @NotBlank
    @Size(min = 3, max = 21)
    private String username;

    @NaturalId
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 128)
    private String password;

    @Size(max = 50)
    private String description;

    private Date creationDate = new Date();

    private String profilePhotoUrl;

    private String profileBacgroundUrl;

    private String fullName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Set<Folder> folders = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Post> posts;

    //FetchType.LAZY == LOAD DATA ON DEMAND
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    //followedUsers -> this user follows users listed in "followedUsers"
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "follower_of_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_user_id"))
    private Set<User> followedUsers = new HashSet<>();


    public User(String username, String fullName, String email, String password, String description, Date creationDate, String profilePhotoUrl, String profileBacgroundUrl) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.description = description;
        this.creationDate = creationDate;
        this.profilePhotoUrl = profilePhotoUrl;
        this.profileBacgroundUrl = profileBacgroundUrl;
        this.uuid=RandomString.generateUUID();
    }
}
