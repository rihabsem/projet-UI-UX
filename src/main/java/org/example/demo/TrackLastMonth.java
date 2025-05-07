package org.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo.session.Session;

import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.example.demo.DB.DBUtil;


public class TrackLastMonth implements Initializable {

    @FXML private Label name;
    @FXML private Label Acc_tracker;
    @FXML private Label month_tracker;
    @FXML private LineChart<String, Number> graph;
    @FXML private VBox latest_transaction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Session.userId == null) return;

        try (Connection conn = DBUtil.getConnection()) {
            loadUserInfo(conn);
            loadMonthlyChartData(conn);
            loadLatestTransactions(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void loadUserInfo(Connection conn) throws SQLException {
        String sql = "SELECT name FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, Session.userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    name.setText(rs.getString("name"));
                }
            }
        }
    }


    private void loadLatestTransactions(Connection conn) throws SQLException {
        String sql = """
            SELECT date, amount, note
            FROM transactions
            WHERE user_id = ?
            ORDER BY date DESC
            LIMIT 5
        """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, Session.userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Label tx = new Label(rs.getDate("date") + ": " +
                            rs.getBigDecimal("amount") + " - " +
                            rs.getString("note"));
                    tx.setStyle("-fx-text-fill: white;");
                    latest_transaction.getChildren().add(tx);
                }
            }
        }
    }


    private void loadMonthlyChartData(Connection conn) throws SQLException {
        String sql = """
        SELECT TO_CHAR(date, 'YYYY-MM-DD') AS day, SUM(amount) AS total
        FROM transactions
        WHERE user_id = ?
          AND date_trunc('month', date) = date_trunc('month', CURRENT_DATE)
        GROUP BY day
        ORDER BY day
    """;

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Daily Spending");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, Session.userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String day = rs.getString("day");
                    BigDecimal total = rs.getBigDecimal("total");
                    series.getData().add(new XYChart.Data<>(day, total));
                }
            }
        }

        graph.getData().clear();
        graph.getData().add(series);
    }
    @FXML
    private void handleMenu(MouseEvent event) {
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

    public void handleAddTransaction(MouseEvent event) {
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

    public void handleLastWeekClicked(MouseEvent event) {
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

    public void handleLastYearClicked(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("last_year_clicked.fxml"));

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
