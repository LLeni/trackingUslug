package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Controller {
    @FXML
    TextField fieldFIO, fieldAddress, fieldContacts, fieldExecutor, fieldCondition;
    @FXML
    TextArea areaDescription;
    @FXML
    TableView<CountPerDate> tableView;
    @FXML
    TableColumn<CountPerDate, String> columnDate;
    @FXML
    TableColumn<CountPerDate, Integer> columnCount;

    //private final String SQL_STATEMENT_GET_DATA = "SELECT * FROM requests;";
    private final String SQL_STATEMENT_INSERT = "INSERT INTO requests(FIO, address, contacts, executor, condition, description, date) VALUES(?, ?, ?, ?, ?, ?, ?);";
    private final String SQL_STATEMENT_COUNT = "SELECT date, Count(*) As count_requests FROM requests GROUP BY date;";

    private ObservableList<CountPerDate> data = FXCollections.observableArrayList();
    private Connection con;
    private boolean hasData = false;
    private Stage mainStage;

    public void initialize(){
        getConnection();

        columnDate.setCellValueFactory(new PropertyValueFactory<CountPerDate, String>("date"));
        columnCount.setCellValueFactory(new PropertyValueFactory<CountPerDate, Integer>("count"));

        tableView.setItems(data);
    }

    private void getConnection(){
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:tracking.db");
            getData();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getData() throws SQLException {
        if(!hasData){
            hasData = true;

            PreparedStatement state = con.prepareStatement(SQL_STATEMENT_COUNT);
            ResultSet rs = state.executeQuery();
            while(rs.next()){
                data.add(new CountPerDate(rs.getString(1), rs.getInt(2)));
            }

        }
    }

    public void addRequest(){
        if(fieldFIO.getText().equals("") || fieldAddress.getText().equals("") || fieldContacts.getText().equals("") || fieldExecutor.getText().equals("") || fieldCondition.getText().equals("")  ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Вы не ввели данные во всех необходимых полях");
            alert.setContentText("Для продолжения нажмите кнопку 'ОК'");
            alert.show();
        } else {
            clear();
            Request request = new Request(fieldFIO.getText(), fieldAddress.getText(), fieldContacts.getText(), fieldExecutor.getText(), fieldCondition.getText(),
                    areaDescription.getText(), getCurrentDate());
            try {

                PreparedStatement state = con.prepareStatement(SQL_STATEMENT_INSERT);
                state.setString(1, request.getFIO());
                state.setString(2, request.getAddress());
                state.setString(3, request.getContacts());
                state.setString(4, request.getExecutor());
                state.setString(5, request.getCondition());
                state.setString(6, request.getDescription());
                state.setString(7, request.getDate());
                state.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            boolean isChange = false;
            for (int i = 0; i < data.size(); i++) {
                if(data.get(i).getDate().equals(request.getDate())){
                    int newCount = data.get(i).getCount() + 1;
                    data.set(i, new CountPerDate(request.getDate(), newCount));
                    isChange = true;
                }
            }
            if(!isChange){
                data.add(new CountPerDate(request.getDate(), 1));
            }
        }
    }

    private void clear(){
        fieldFIO.setText("");
        fieldAddress.setText("");
        fieldContacts.setText("");
        fieldExecutor.setText("");
        fieldCondition.setText("");
        areaDescription.setText("");

    }

    private String getCurrentDate(){
        Calendar calendar = new GregorianCalendar();
        //System.out.println(calendar.getTime().toString());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormatter.format(calendar.getTime());
    }

    public void buildChart() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/chart.fxml"));
        Parent root = loader.load();

        ChartController chartController = loader.getController();
        chartController.setData(data);

        Scene scene =  new Scene(root, 1200, 620);

        Stage newWindow = new Stage();
        newWindow.setMaxWidth(1200);
        newWindow.setMaxHeight(620);
        newWindow.setMinHeight(1200);
        newWindow.setMinHeight(620);
        newWindow.initModality(Modality.WINDOW_MODAL);
        // Specifies the owner Window (parent) for new window
        newWindow.initOwner(mainStage);
        newWindow.setTitle("Отслеживание заявок - Гистограмма");
        newWindow.setScene(scene);

        newWindow.show();
    }

    public void setMainStage(Stage stage){
        this.mainStage = stage;
    }
}
