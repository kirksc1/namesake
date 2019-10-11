package com.github.kirksc1.namesake.entity;

import javax.persistence.*;

@Entity
@Table(name="StandardTable")
public class Goo {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "StandardId")
    private Long id;

    @Column(name = "StandardField")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
