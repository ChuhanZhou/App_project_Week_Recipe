package com.example.week_recipe.model.domain.food;

import com.example.week_recipe.utility.MyString;

import java.util.ArrayList;

public class IngredientsList {
    private ArrayList<Ingredients> ingredientsList;

    public IngredientsList()
    {
        ingredientsList = new ArrayList<>();
    }

    private boolean hasIngredients(Ingredients ingredients)
    {
        return getByName(ingredients.getName())!=null;
    }

    public boolean isEmpty()
    {
        return ingredientsList.isEmpty();
    }

    public String add(Ingredients ingredients)
    {
        if (ingredients!=null)
        {
            if (!hasIngredients(ingredients))
            {
                ingredientsList.add(ingredients);
                return null;
            }
            return "The ingredients [" + ingredients.getName() + "] is in the list.";
        }
        return "Can't input null";
    }

    public Ingredients getByName(String name)
    {
        for (int x=0;x<ingredientsList.size();x++)
        {
            if (ingredientsList.get(x).getName().equals(name))
            {
                return ingredientsList.get(x);
            }
        }
        return null;
    }

    public IngredientsList getListByName(String name)
    {
        IngredientsList searchList = new IngredientsList();
        for (int x=0;x<ingredientsList.size();x++)
        {
            if (MyString.haveOrInside(name,ingredientsList.get(x).getName()))
            {
                searchList.add(ingredientsList.get(x));
            }
        }
        return searchList;
    }

    public Ingredients getByIndex(int index)
    {
        if (index>=0&&index<ingredientsList.size())
        {
            return ingredientsList.get(index);
        }
        return null;
    }

    public String set(Ingredients oldIngredients,Ingredients newIngredients)
    {
        if (hasIngredients(oldIngredients))
        {
            for (int x=0;x<ingredientsList.size();x++)
            {
                if (ingredientsList.get(x).getName().equals(oldIngredients.getName()))
                {
                    ingredientsList.set(x,newIngredients);
                    return null;
                }
            }
        }
        return "Can't find old ingredients [" + oldIngredients.getName() + "].";
    }

    public void remove(Ingredients ingredients)
    {
        for (int x=0;x<ingredientsList.size();x++)
        {
            if (ingredientsList.get(x).getName().equals(ingredients.getName()))
            {
                ingredientsList.remove(x);
                break;
            }
        }
    }

    public IngredientsList copy()
    {
        IngredientsList copy = new IngredientsList();
        for (int x=0;x<ingredientsList.size();x++)
        {
            copy.add(ingredientsList.get(x).copy());
        }
        return copy;
    }

    public int getSize()
    {
        return ingredientsList.size();
    }
}
