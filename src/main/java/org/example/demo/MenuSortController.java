package org.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.demo.DB.DBUtil;
import org.example.demo.session.Session;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuSortController {

    @FXML private Label name;  // Reference to the name label in FXML
    @FXML private VBox sorted_transactions;  // Reference to the VBox where transactions will be displayed

    @FXML
    public void initialize() {
        if (Session.userId == null) return; // Ensure user is logged in

        try (Connection conn = DBUtil.getConnection()) {
            loadUserInfo(conn); // Load user info
            loadTransactionsSortedByDate(conn); // Load and display transactions sorted by date
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to fetch and display the user's information
    private void loadUserInfo(Connection conn) throws SQLException {
        String sql = "SELECT name FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, Session.userId); // Set user ID
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    name.setText(rs.getString("name")); // Set the user's name in the label
                }
            }
        }
    }

    // Method to fetch and display the user's transactions sorted by date
    private void loadTransactionsSortedByDate(Connection conn) throws SQLException {
        String sql = """
            SELECT date, amount, note
            FROM transactions
            WHERE user_id = ?
            ORDER BY date DESC
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, Session.userId); // Set user ID
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String display = rs.getDate("date") + " - " + rs.getBigDecimal("amount") + " DH - " + rs.getString("note");
                    Label label = new Label(display); // Create a label for each transaction
                    label.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                    sorted_transactions.getChildren().add(label); // Add the label to the VBox
                }
            }
        }
    }

    @FXML
    public void handleAdd(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("add.fxml"));

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

    @FXML
    public void handleFilter(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("transaction_filter_category.fxml"));

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
