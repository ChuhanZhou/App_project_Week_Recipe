package com.example.week_recipe.model.domain.user;

public class Account {
    private String email;
    private String password;

    public Account(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public boolean checkPassword(String inputPassword)
    {
        return password.equals(inputPassword);
    }

    public String changePassword(String oldPassword,String newPassword)
    {
        if (checkPassword(oldPassword))
        {
            if (newPassword!=null)
            {
                password = newPassword;
                return null;
            }
            return "The new password can't be null!";
        }
        return "The old password is wrong!";
    }
}
