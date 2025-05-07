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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditController {

    @FXML private TextField date_edit;
    @FXML private TextField amont_edit;
    @FXML private ComboBox<String> categories_edit;
    @FXML private TextField note_edit;
    @FXML private Label edit;
    @FXML private Label delete;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private long transactionId;

    // Assuming the transaction ID is passed when navigating to this view
    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
        loadTransactionDetails();
    }

    private void loadTransactionDetails() {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT date, amount, main_category, note FROM transactions WHERE id = ? AND user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, transactionId);
                stmt.setLong(2, Session.userId);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    LocalDate date = rs.getDate("date").toLocalDate();
                    double amount = rs.getDouble("amount");
                    String category = rs.getString("main_category");
                    String note = rs.getString("note");

                    // Populate the fields with the current transaction details
                    date_edit.setText(date.format(formatter));
                    amont_edit.setText(String.valueOf(amount));
                    categories_edit.setValue(category);
                    note_edit.setText(note);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading transaction details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        edit.setOnMouseClicked(this::handleEditTransaction);
        //delete.setOnMouseClicked(this::handleDeleteTransaction);

        // Populate the categories combo box
        categories_edit.getItems().addAll("Groceries", "Transportation", "Family", "Other");
    }

    private void handleEditTransaction(MouseEvent event) {
        String dateInput = date_edit.getText();
        String amountInput = amont_edit.getText();
        String category = categories_edit.getValue();
        String noteInput = note_edit.getText();

        if (dateInput.isEmpty() || amountInput.isEmpty() || category == null) {
            System.out.println("Please fill all required fields.");
            return;
        }

        try {
            LocalDate parsedDate = LocalDate.parse(dateInput, formatter); // Parsing with updated format
            double amount = Double.parseDouble(amountInput);

            try (Connection conn = DBUtil.getConnection()) {
                String sql = "UPDATE transactions SET date = ?, amount = ?, main_category = ?, note = ? WHERE id = ? AND user_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setDate(1, java.sql.Date.valueOf(parsedDate));
                    stmt.setDouble(2, amount);
                    stmt.setString(3, category);
                    stmt.setString(4, noteInput);
                    stmt.setLong(5, transactionId);
                    stmt.setLong(6, Session.userId);

                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        System.out.println("Transaction updated successfully.");
                        // After editing, we can clear the form or navigate back
                        clearForm();
                    } else {
                        System.out.println("Failed to update transaction.");
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error while updating transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*private void handleDeleteTransaction(MouseEvent event) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "DELETE FROM transactions WHERE id = ? AND user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, transactionId);
                stmt.setLong(2, Session.userId);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("Transaction deleted successfully.");
                    // Clear the form or navigate to another page
                    clearForm();
                } else {
                    System.out.println("Failed to delete transaction.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }*/

    private void clearForm() {
        date_edit.clear();
        amont_edit.clear();
        categories_edit.setValue(null);
        note_edit.clear();
    }

    @FXML
    public void handleEdit(MouseEvent event) {
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
    @FXML
    public void handleDelete(MouseEvent event) {
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

    @FXML
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

    @FXML
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
}
