package com.github.kirksc1.namesake.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="${foo.name:TableFoo}")
public class Foo {

    @Id
    @Column(name = "${foo.field.name:ColumnName}")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
