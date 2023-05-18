package org.launchcode.LiftoffRecipeProject.models;

import jakarta.persistence.*;

@Entity
public class Ingredient extends AbstractEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    private String name;

    public Ingredient(){}

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}