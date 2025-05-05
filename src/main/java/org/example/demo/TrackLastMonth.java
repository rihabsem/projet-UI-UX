package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class TrackLastMonth {

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    public void initialize() {
        if (xAxis != null) {
            xAxis.setLabel("Days of the Month");
        } else {
            System.out.println("xAxis is not initialized!");
        }

        if (yAxis != null) {
            yAxis.setLabel("Value");
        } else {
            System.out.println("yAxis is not initialized!");
        }

        // Example data for the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Last Month Data");
        series.getData().add(new XYChart.Data<>("1", 25));
        series.getData().add(new XYChart.Data<>("2", 40));
        series.getData().add(new XYChart.Data<>("3", 30));

        lineChart.getData().add(series);
    }
}
