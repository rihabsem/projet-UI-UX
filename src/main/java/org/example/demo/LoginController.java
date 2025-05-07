package org.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.demo.dao.UserDAO;
import org.example.demo.models.User;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    /*@FXML
    private TextField email_field;
    @FXML
    private TextField mot_de_passe;
    @FXML
    private Label login_button;

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        // Add an event listener to the "Login" label (acting as the login button)
        login_button.setOnMouseClicked(this::handleLogin);
    }

    private void handleLogin(MouseEvent event) {
        String email = email_field.getText();
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
    */
    @FXML
    public void handleLoginRedirection(MouseEvent event) {
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
    }

}
