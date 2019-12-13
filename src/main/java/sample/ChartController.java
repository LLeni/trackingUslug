package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private ObservableList<Usluga> data;


    public void initialize(){
        dataChart = new XYChart.Series<>();

        xAxis.setLabel("Даты");
        yAxis.setLabel("Количество заявок");
        yAxis.setTickUnit(1);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(20);
        yAxis.setAutoRanging(false);

        barChart.getData().add(dataChart);
        barChart.setBarGap(3);
        barChart.setCategoryGap(20);



    }

//    public void setData(ObservableList<CountPerDate> data){
//        this.data = data;
//        for (int i = 0; i < data.size(); i++) {
//            dataChart.getData().add(new XYChart.Data<>(data.get(i).getDate(), data.get(i).getCount()));
//        }
//    }
}
