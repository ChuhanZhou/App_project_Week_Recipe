package com.example.week_recipe.model.domain.food;

public class Ingredients {
    private String name;

    public Ingredients(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Ingredients copy()
    {
        return new Ingredients(name);
    }
}
