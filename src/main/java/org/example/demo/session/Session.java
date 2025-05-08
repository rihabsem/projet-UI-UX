package org.example.demo.session;

import org.example.demo.models.User;

public class Session {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static Long getUserId() {
        return currentUser != null ? currentUser.getId() : null;
    }
}
