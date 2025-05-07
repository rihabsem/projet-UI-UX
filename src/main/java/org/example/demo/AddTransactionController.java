package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.example.demo.DB.DBUtil;
import org.example.demo.session.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddTransactionController {

    @FXML private TextField date;
    @FXML private TextField amont;
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
        String amountInput = amont.getText();
        String category = categories.getValue();
        String noteInput = note.getText();

        if (dateInput.isEmpty() || amountInput.isEmpty() || category == null) {
            System.out.println("Please fill all required fields.");
            return;
        }

        try {
            LocalDate parsedDate = LocalDate.parse(dateInput, formatter); // Parsing with updated format
            double amount = Double.parseDouble(amountInput);

            try (Connection conn = DBUtil.getConnection()) {
                String sql = "INSERT INTO transactions (user_id, date, amount, main_category, sub_category, note) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setLong(1, Session.userId);
                    stmt.setDate(2, java.sql.Date.valueOf(parsedDate));
                    stmt.setDouble(3, amount);
                    stmt.setString(4, category);
                    stmt.setNull(5, java.sql.Types.VARCHAR);
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
        amont.clear();
        categories.setValue(null);
        note.clear();
    }
}
