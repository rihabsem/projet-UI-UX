package org.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.example.demo.session.Session;
import org.example.demo.DB.DBUtil;
import org.example.demo.models.User;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    @FXML
    private TextField email;  // Assuming fx:id="email"

    @FXML
    private TextField mot_de_passe;  // Assuming fx:id="mot_de_passe"

    @FXML
    public void handleLoginRedirection(MouseEvent event) {
        String UserEmail = email.getText();
        String UserPassword = mot_de_passe.getText();

        // Validate user credentials
        User user = validateUser(UserEmail, UserPassword);
        if (user != null) {
            // Set the current user in the session
            Session.setCurrentUser(user);

            // Redirect to the next screen
            try {
                Parent root = FXMLLoader.load(getClass().getResource("transaction_sort_date.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Show error message if login fails
            showError("Invalid email or password.");
        }
    }

    private User validateUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                // If user is found, create a User object and return it
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));  // Even though not necessary for the session, you can store it for debugging

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return null if no user found
        return null;
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
