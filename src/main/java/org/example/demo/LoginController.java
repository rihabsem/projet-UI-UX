package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.example.demo.dao.UserDAO;
import org.example.demo.models.User;

import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private TextField motDePasseField;
    @FXML
    private Label loginButton;

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        // Add an event listener to the "Login" label (acting as the login button)
        loginButton.setOnMouseClicked(this::handleLogin);
    }

    private void handleLogin(MouseEvent event) {
        String email = emailField.getText();
        // String motDePasse = motDePasseField.getText(); // Password logic skipped for now

        try {
            // Look up user by email in the database
            User user = userDAO.findByEmail(email);
            if (user == null) {
                System.out.println("User not found. Please check the email.");
            } else {
                // If user is found, print username for now
                System.out.println("Logged in: " + user.getName());
                // You can proceed to additional steps (session handling, transitioning to a new screen, etc.)
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
