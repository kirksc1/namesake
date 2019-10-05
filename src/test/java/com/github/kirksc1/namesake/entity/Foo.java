package com.github.kirksc1.namesake.entity;

import javax.persistence.*;

@Entity
@Table(name="${foo.name:TableFoo}")
public class Foo {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "${foo.field.id}")
    private Long id;

    @Column(name = "${foo.field.name:ColumnName}")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
