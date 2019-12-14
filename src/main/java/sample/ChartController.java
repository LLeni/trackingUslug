package sample;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.HashMap;

public class ChartController {
    @FXML
    CategoryAxis xAxis;
    @FXML
    NumberAxis yAxis;
    @FXML
    BarChart<String, Number> barChart;

    //                      X,       Y
    private XYChart.Series<String, Number> dataChart;
    private HashMap<String, Integer> data;


    public void initialize(){
        dataChart = new XYChart.Series<>();

        xAxis.setLabel("Даты");
        yAxis.setLabel("Количество оказанных услуг");
        yAxis.setTickUnit(1);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(25);
        yAxis.setAutoRanging(false);

        barChart.getData().add(dataChart);
        barChart.setBarGap(3);
        barChart.setCategoryGap(20);



    }

    public void setData(HashMap<String, Integer> data){
        this.data = data;
        for (String date :
                data.keySet()) {
            dataChart.getData().add(new XYChart.Data<>(date, data.get(date)));
        }
    }
}
