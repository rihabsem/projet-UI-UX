package org.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.demo.session.Session;

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


}
