package by.bsu.dekrat.enrolleeservice.bean;

import by.bsu.dekrat.enrolleeservice.entity.User;

/**
 * Created by USER on 17.11.2016.
 */

public class UserInfoProvider {

    private static final UserInfoProvider INSTANCE = new UserInfoProvider();

    private User currentUser;

    public static UserInfoProvider getInstance() {
        return INSTANCE;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    private UserInfoProvider() {
    }
}
