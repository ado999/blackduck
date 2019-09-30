package pl.edu.wat.wcy.tim.blackduck.models;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
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

    public Role(){}


}
