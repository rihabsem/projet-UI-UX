package org.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.demo.DB.DBUtil;
import org.example.demo.session.Session;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddTransactionController {

    @FXML private TextField date;
    @FXML private TextField amount; // Fixed typo here (amont -> amount)
    @FXML private ComboBox<String> categories;
    @FXML private TextField note;
    @FXML private Label create_button;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    private void initialize() {
        create_button.setOnMouseClicked(this::handleCreateTransaction);
    }

    private void handleCreateTransaction(MouseEvent event) {
        String dateInput = date.getText();
        String amountInput = amount.getText(); // Fixed typo here
        String category = categories.getValue();
        String noteInput = note.getText();

        if (dateInput.isEmpty() || amountInput.isEmpty() || category == null) {
            System.out.println("Please fill all required fields.");
            return;
        }

        try {
            LocalDate parsedDate = LocalDate.parse(dateInput, formatter); // Parsing with updated format
            double amountValue = Double.parseDouble(amountInput); // Fixed variable name to match usage

            try (Connection conn = DBUtil.getConnection()) {
                String sql = "INSERT INTO transactions (user_id, date, amount, main_category, sub_category, note) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setLong(1, Session.getUserId()); // Updated to use Session.getUserId()
                    stmt.setDate(2, java.sql.Date.valueOf(parsedDate));
                    stmt.setDouble(3, amountValue);
                    stmt.setString(4, category);
                    stmt.setNull(5, Types.VARCHAR); // Corrected to match SQL NULL type
                    stmt.setString(6, noteInput);

                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        System.out.println("Transaction added successfully.");
                        clearForm();
                    } else {
                        System.out.println("Failed to add transaction.");
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error while saving transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearForm() {
        date.clear();
        amount.clear(); // Fixed typo here
        categories.setValue(null);
        note.clear();
    }

    public void handleMenu(MouseEvent event) {
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

    public void handleTrack(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("last_week_transaction.fxml"));

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleCreate(MouseEvent event) {
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
