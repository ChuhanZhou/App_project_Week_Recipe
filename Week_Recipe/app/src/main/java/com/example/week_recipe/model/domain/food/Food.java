package com.example.week_recipe.model.domain.food;

import com.example.week_recipe.R;

public class Food {
    private String name;
    private FoodType type;
    private int imageId;
    private IngredientsList ingredientsList;

    public Food(String name,FoodType type,IngredientsList ingredientsList,int imageId)
    {
        this.name = name;
        this.type = type;
        this.ingredientsList = ingredientsList;
        this.imageId = imageId;
    }

    public Food(String name,FoodType type,IngredientsList ingredientsList)
    {
        this.name = name;
        this.type = type;
        this.ingredientsList = ingredientsList;
        this.imageId = R.drawable.ic_action_food;
    }

    public void update(Food newFood)
    {
        name = newFood.name;
        type = newFood.type;
        ingredientsList = newFood.ingredientsList;
        imageId = newFood.imageId;
    }

    public String getName() {
        return name;
    }

    public FoodType getType() {
        return type;
    }

    public int getImageId() {
        return imageId;
    }

    public IngredientsList getIngredientsList() {
        return ingredientsList;
    }

    public Food copy()
    {
        return new Food(name,type,ingredientsList.copy(),imageId);
    }
}
