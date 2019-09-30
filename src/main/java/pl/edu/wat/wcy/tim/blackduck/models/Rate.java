package pl.edu.wat.wcy.tim.blackduck.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "rates")
@Data
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private int rate; // int in 1..5

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post rootPost;

}