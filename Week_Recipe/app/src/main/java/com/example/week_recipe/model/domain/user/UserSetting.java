package com.example.week_recipe.model.domain.user;

public class UserSetting {
    private boolean useOnlineDatabase;

    public UserSetting()
    {
        useOnlineDatabase = true;
    }

    public void setUseOnlineDatabase(boolean useOnlineDatabase) {
        this.useOnlineDatabase = useOnlineDatabase;
    }

    public boolean isUseOnlineDatabase() {
        return useOnlineDatabase;
    }
}
