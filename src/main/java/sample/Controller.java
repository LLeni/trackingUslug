package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Controller {
    @FXML
    TextField fieldFIO, fieldAddress, fieldContacts, fieldExecutor, fieldName;
    @FXML
    TextArea areaDescription;
    @FXML
    TableView<Usluga> tableView;
    @FXML
    TableColumn<Usluga, String> columnDate;
    @FXML
    TableColumn<Usluga, String> columnName;
    @FXML
    TableColumn<Usluga, CheckBox> columnCondition;

    private final String SQL_STATEMENT_GET_DATA = "SELECT * FROM uslugi;";
    private final String SQL_STATEMENT_INSERT = "INSERT INTO uslugi(FIO, address, contacts, executor, name, dataBegin, dataEnd, description, condition) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private final String SQL_STATEMENT_UPDATE = "UPDATE uslugi SET condition = ? WHERE name = ?";

    private ObservableList<Usluga> data = FXCollections.observableArrayList();
    private Connection con;
    private boolean hasData = false;
    private Stage mainStage;

    public void initialize(){
        getConnection();

        columnDate.setCellValueFactory(new PropertyValueFactory<Usluga, String>("dateBegin"));
        columnName.setCellValueFactory(new PropertyValueFactory<Usluga, String>("name"));
        columnCondition.setCellValueFactory(new PropertyValueFactory<Usluga, CheckBox>("checkBox"));

        areaDescription.setPromptText("Необязательное поле");

        tableView.setItems(data);
    }

    private void getConnection(){
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:controlUslug.db");
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

            PreparedStatement state = con.prepareStatement(SQL_STATEMENT_GET_DATA);
            ResultSet rs = state.executeQuery();
            while(rs.next()){
                data.add(new Usluga(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10)));
            }

        }
    }

    public void addUslugu(){
        if(fieldFIO.getText().equals("") || fieldAddress.getText().equals("") || fieldContacts.getText().equals("") || fieldExecutor.getText().equals("") || fieldName.getText().equals("")  ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Вы не ввели данные во всех необходимых полях");
            alert.setContentText("Для продолжения нажмите кнопку 'ОК'");
            alert.show();
        } else {
            clear();
            Usluga usluga = new Usluga(fieldFIO.getText(), fieldAddress.getText(), fieldContacts.getText(), fieldExecutor.getText(), fieldName.getText(),
                    areaDescription.getText(), getCurrentDate(), "", "Невыполнено");
            try {

                PreparedStatement state = con.prepareStatement(SQL_STATEMENT_INSERT);
                state.setString(1, usluga.getFIO());
                state.setString(2, usluga.getAddress());
                state.setString(3, usluga.getContacts());
                state.setString(4, usluga.getExecutor());
                state.setString(5, usluga.getName());
                state.setString(6, usluga.getDescription());
                state.setString(7, usluga.getDateBegin());
                state.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

//            boolean isChange = false;
//            for (int i = 0; i < data.size(); i++) {
//                if(data.get(i).getDate().equals(usluga.getDate())){
//                    int newCount = data.get(i).getCount() + 1;
//                    data.set(i, new CountPerDate(usluga.getDate(), newCount));
//                    isChange = true;
//                }
//            }
//            if(!isChange){
//                data.add(new CountPerDate(usluga.getDate(), 1));
//            }
        }
    }

    public void updateUslugu(Usluga usluga){
        try {
            PreparedStatement state = con.prepareStatement(SQL_STATEMENT_UPDATE);
            state.setString(1, usluga.getCheckBox().toString());
            state.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clear(){
        fieldFIO.setText("");
        fieldAddress.setText("");
        fieldContacts.setText("");
        fieldExecutor.setText("");
        fieldName.setText("");
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

//        ChartController chartController = loader.getController();
//        chartController.setData(data);
//
//        Scene scene =  new Scene(root, 1200, 620);
//
//        Stage newWindow = new Stage();
//        newWindow.setMaxWidth(1200);
//        newWindow.setMaxHeight(620);
//        newWindow.setMinHeight(1200);
//        newWindow.setMinHeight(620);
//        newWindow.initModality(Modality.WINDOW_MODAL);
//        // Specifies the owner Window (parent) for new window
//        newWindow.initOwner(mainStage);
//        newWindow.setTitle("Услуги - Гистограмма");
//        newWindow.setScene(scene);
//
//        newWindow.show();
    }

    public void setDone(){
        for(Usluga usluga: tableView.getItems()){
            if(usluga.getCheckBox().isSelected()){
                Platform.runLater(() -> tableView.getItems().remove(usluga));
                updateUslugu(usluga);
            }
        }
    }

    public void setMainStage(Stage stage){
        this.mainStage = stage;
    }
}
