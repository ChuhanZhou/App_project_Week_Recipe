package com.example.week_recipe.model.domain.food;

import com.example.week_recipe.utility.MyString;

import java.util.ArrayList;
//@Entity(tableName = "foodList_table")
public class FoodList {
    //@PrimaryKey(autoGenerate = true)
    private int id;
    //@Ignore
    private ArrayList<Food> foodList;
    //@Ignore
    public FoodList()
    {
        foodList = new ArrayList<>();
    }

    public FoodList(int id)
    {
        this.id = id;
        foodList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean hasFood(Food food)
    {
        return getByName(food.getName())!=null;
    }

    public String add(Food newFood)
    {
        if (newFood!=null)
        {
            if (!hasFood(newFood))
            {
                foodList.add(newFood);
                return null;
            }
            return "The food [" + newFood.getName() + "] is in the list.";
        }
        return "Can't input null";
    }

    public Food getByName(String name)
    {
        for (int x=0;x<foodList.size();x++)
        {
            if (foodList.get(x).getName().equals(name))
            {
                return foodList.get(x);
            }
        }
        return null;
    }

    public FoodList getListByName(String name)
    {
        FoodList searchList = new FoodList();
        for (int x=0;x<foodList.size();x++)
        {
            if (MyString.haveOrInside(name,foodList.get(x).getName()))
            {
                searchList.add(foodList.get(x));
            }
        }
        return searchList;
    }

    public Food getByIndex(int index)
    {
        if (index>=0&&index<foodList.size())
        {
            return foodList.get(index);
        }
        return null;
    }

    public FoodList getByType(FoodType foodType)
    {
        FoodList searchList = new FoodList();
        for (int x=0;x<foodList.size();x++)
        {
            if (foodList.get(x).getType()==foodType)
            {
                searchList.add(foodList.get(x));
            }
        }
        return searchList;
    }

    public String update(Food oldFood,Food newFood)
    {
        if (oldFood!=null&&newFood!=null)
        {
            if (hasFood(oldFood))
            {
                if (!hasFood(newFood)||oldFood.getName().equals(newFood.getName()))
                {
                    for (int x=0;x<foodList.size();x++)
                    {
                        if (foodList.get(x).getName().equals(oldFood.getName()))
                        {
                            foodList.set(x,newFood);
                            return null;
                        }
                    }
                }
                return "The new food name [" + newFood.getName() + "] is used";
            }
            return "Can't find old food [" + oldFood.getName() + "]";
        }
        return "Input null!";
    }

    public void remove(Food food)
    {
        for (int x=0;x<foodList.size();x++)
        {
            if (foodList.get(x).getName().equals(food.getName()))
            {
                foodList.remove(x);
                break;
            }
        }
    }

    public void remove(int index)
    {
        foodList.remove(index);
    }

    public FoodList copy()
    {
        FoodList copy = new FoodList(id);
        for (int x=0;x<foodList.size();x++)
        {
            copy.add(foodList.get(x).copy());
        }
        return copy;
    }

    public int getSize()
    {
        return foodList.size();
    }

    @Override
    public String toString() {
        String toString = "";
        for (int x=0;x<foodList.size();x++)
        {
            if (x==foodList.size()-1)
            {
                toString += foodList.get(x).getName();
            }
            else
            {
                toString += foodList.get(x).getName() + "\n";
            }
        }
        return toString;
    }
}
