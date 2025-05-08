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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo.session.Session;
import org.example.demo.DB.DBUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TrackLastWeek implements Initializable {

    @FXML private Label name;
    @FXML private Label Acc_tracker;
    @FXML private Label week_tracker;
    @FXML private LineChart<String, Number> graph;
    @FXML private VBox latest_transaction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Long userId = Session.getUserId();  // Updated to use Session.getUserId()

        if (userId == null) return;

        try (Connection conn = DBUtil.getConnection()) {
            loadUserInfo(conn, userId);
            loadWeeklyChartData(conn, userId);
            loadLatestTransactions(conn, userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUserInfo(Connection conn, Long userId) throws SQLException {
        String sql = "SELECT name FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    name.setText(rs.getString("name"));
                }
            }
        }
    }

    private void loadWeeklyChartData(Connection conn, Long userId) throws SQLException {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue()); // Sunday
        LocalDate endOfWeek = startOfWeek.plusDays(6); // Saturday

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Modify the query to use explicit type casting for the date comparison
        String sql = """
        SELECT TO_CHAR(date, 'YYYY-MM-DD') AS day, SUM(amount) AS total
        FROM transactions
        WHERE user_id = ?
          AND date >= CAST(? AS DATE) AND date <= CAST(? AS DATE)
        GROUP BY day
        ORDER BY day
    """;

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Weekly Spending");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Pass the formatted date string as parameters
            stmt.setLong(1, userId);
            stmt.setString(2, startOfWeek.format(formatter));  // Pass start of the week as String
            stmt.setString(3, endOfWeek.format(formatter));    // Pass end of the week as String
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String day = rs.getString("day");
                    BigDecimal total = rs.getBigDecimal("total");
                    series.getData().add(new XYChart.Data<>(day, total));
                }
            }
        }

        // Clear previous chart data and add the new series
        graph.getData().clear();
        graph.getData().add(series);
    }

    private void loadLatestTransactions(Connection conn, Long userId) throws SQLException {
        String sql = """
            SELECT date, amount, note
            FROM transactions
            WHERE user_id = ?
            ORDER BY date DESC
            LIMIT 5
        """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
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
    public void handleLastMonth(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("last_month_clicked.fxml"));

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
    public void handleLastYear(MouseEvent event) {
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
