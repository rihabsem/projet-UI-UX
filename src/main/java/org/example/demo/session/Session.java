package org.example.demo.session;

import org.example.demo.models.User;

public class Session {
    private static User currentUser;

    // Getter and Setter for currentUser
    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    // If you still want to access userId directly, you can create a getter for userId
    public static Long getUserId() {
        return currentUser != null ? currentUser.getId() : null;
    }
}
