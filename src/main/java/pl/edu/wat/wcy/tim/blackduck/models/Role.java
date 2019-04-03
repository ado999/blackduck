package pl.edu.wat.wcy.tim.blackduck.models;

import org.hibernate.annotations.NaturalId;
import pl.edu.wat.wcy.tim.blackduck.models.RoleName;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //Enumerated == DELARE THAT VALUE SHOULD BE CONVERTED FROM STRING IN DATABASE
    @Enumerated(EnumType.STRING)
    //NaturalId == IDENTIFIES A DATABASE RECORD AND AN OBJECT IN REAL LIFE
    @NaturalId
    @Column(length = 60)
    private RoleName name;

    public Role(RoleName name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }
}
