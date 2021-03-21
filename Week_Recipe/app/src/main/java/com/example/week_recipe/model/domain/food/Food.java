package com.example.week_recipe.model.domain.food;

public class Food {
    private String name;
    private FoodType type;
    private IngredientsList ingredientsList;

    public Food(String name,FoodType type,IngredientsList ingredientsList)
    {
        this.name = name;
        this.type = type;
        this.ingredientsList = ingredientsList;
    }

    public void update(Food newFood)
    {
        name = newFood.name;
        type = newFood.type;
        ingredientsList = newFood.ingredientsList;
    }

    public String getName() {
        return name;
    }

    public FoodType getType() {
        return type;
    }

    public IngredientsList getIngredientsList() {
        return ingredientsList;
    }

    public Food copy()
    {
        return new Food(name,type,ingredientsList.copy());
    }
}
