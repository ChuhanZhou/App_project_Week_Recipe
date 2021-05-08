package com.example.week_recipe.model;

public interface LoginModel {
    String login(String email,String password);
    String register(String email,String password);
    void addAccountInfo(String email,String password,String userName);
}
