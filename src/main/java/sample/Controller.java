package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Controller {
    @FXML
    TabPane tabPane;
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
    private final String SQL_STATEMENT_INSERT = "INSERT INTO uslugi(FIO, address, contacts, executor, name, dateBegin, dateEnd, description, condition) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private final String SQL_STATEMENT_UPDATE = "UPDATE uslugi SET condition = ?, dateEnd = ? WHERE name = ?";

    private ObservableList<Usluga> data = FXCollections.observableArrayList();
    private Connection con;
    private boolean hasData = false;
    private Stage mainStage;

    public void initialize(){
        getConnection();

        for (int i = 0; i < data.size(); i++) {
            CheckBox checkBox = data.get(i).getCheckBox();
            Usluga usluga = data.get(i);
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if(!checkBox.isSelected()) {
                    checkBox.setSelected(true);
                    updateCondition(usluga);
                }
            });
        }

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

            Usluga usluga = new Usluga(fieldFIO.getText(), fieldAddress.getText(), fieldContacts.getText(), fieldExecutor.getText(), fieldName.getText(),
                     getCurrentDate(), "", areaDescription.getText(), "Невыполнено");

            try {

                PreparedStatement state = con.prepareStatement(SQL_STATEMENT_INSERT);
                state.setString(1, usluga.getFIO());
                state.setString(2, usluga.getAddress());
                state.setString(3, usluga.getContacts());
                state.setString(4, usluga.getExecutor());
                state.setString(5, usluga.getName());
                state.setString(6, usluga.getDateBegin());
                state.setString(7, "");
                state.setString(8, usluga.getDescription());
                state.setString(9, "Невыполнено");
                state.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            data.add(usluga);
            clear();
        }
    }

    public void updateCondition(Usluga usluga) {
        if (usluga.getCheckBox().isSelected()) {
            try {
                PreparedStatement state = con.prepareStatement(SQL_STATEMENT_UPDATE);
                state.setString(1, "Выполнено");
                state.setString(2, getCurrentDate());
                state.setString(3, usluga.getName());
                state.execute();
            } catch(SQLException e){
                e.printStackTrace();
            }
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
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormatter.format(calendar.getTime());
    }

    public void buildChart() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/chart.fxml"));
        Parent root = loader.load();

        HashMap<String, Integer> dataMap = new HashMap<>();
        ChartController chartController = loader.getController();
        for (Usluga usluga:
             data) {
            if(usluga.getCheckBox().isSelected()){
                if(dataMap.containsKey(usluga.getDateBegin())){
                    dataMap.put(usluga.getDateBegin(), dataMap.get(usluga.getDateBegin()) + 1);
                } else {
                    dataMap.put(usluga.getDateBegin(), 1);
                }
            }

        }
        chartController.setData(dataMap);

        Tab tab = new Tab();
        tab.setText("Гистограмма " + (tabPane.getTabs().size() - 2));
        tab.setContent(root);
        tabPane.getTabs().add(tab);
    }

    public void setMainStage(Stage stage){
        this.mainStage = stage;
    }
}
