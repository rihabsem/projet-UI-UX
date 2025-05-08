package org.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo.DB.DBUtil;
import org.example.demo.session.Session;

import java.io.IOException;
import java.sql.*;

public class MenuFilterController {
    @FXML
    private VBox sorted_transactions;

    @FXML
    private Label name;

    @FXML
    public void initialize() {
        if (Session.getCurrentUser() == null) return;

        try (Connection conn = DBUtil.getConnection()) {
            loadUserInfo(conn);
            loadTransactionsSortedByCategory(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUserInfo(Connection conn) throws SQLException {
        String sql = "SELECT name FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, Session.getUserId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    name.setText(rs.getString("name"));
                }
            }
        }
    }

    private void loadTransactionsSortedByCategory(Connection conn) throws SQLException {
        String sql = """
        SELECT main_category, date, amount, note
        FROM transactions
        WHERE user_id = ?
        ORDER BY main_category ASC, date DESC
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, Session.getUserId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String display = rs.getString("main_category") + " | " +
                            rs.getDate("date") + " - " +
                            rs.getBigDecimal("amount") + " DH - " +
                            rs.getString("note");

                    Label label = new Label(display);
                    label.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                    sorted_transactions.getChildren().add(label);
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
    public void handleSort(MouseEvent event) {
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
