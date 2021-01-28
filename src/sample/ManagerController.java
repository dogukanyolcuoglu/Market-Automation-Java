package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ManagerController implements Initializable {
    @FXML
    private Button exitButton;
    @FXML
    private Button confirmButton;
    @FXML
    private TextField txtProductID;
    @FXML
    private TextField txtSearch;
    @FXML
    private TextField txtSale;
    @FXML
    private Label loginLabel;
    @FXML
    private TableView<ManagerModel> tblDataView;
    @FXML
    private TableColumn<ManagerModel,Integer> idColumn;
    @FXML
    private TableColumn<ManagerModel,String> productNameColumn;
    @FXML
    private TableColumn<ManagerModel,Double> priceColumn;
    @FXML
    private TableColumn<ManagerModel,String> saleColumn;
    @FXML
    private TableColumn<ManagerModel,String> dateColumn;
    @FXML
    private TableColumn<ManagerModel,Integer> stockColumn;
    @FXML
    private TableColumn<ManagerModel,Integer> placeIDColumn;
    @FXML
    private TableColumn<ManagerModel,String> categoryColumn;


    ObservableList<ManagerModel> observableList = FXCollections.observableArrayList();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar calendar = Calendar.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        getData();
    }

    public void confirmButtonOnAction(ActionEvent event){
        if(txtSale.getText().isBlank() == false){
            updateDataPrice(Integer.parseInt(txtProductID.getText()));
            getData();
        }
    }

    public void exitButtonOnAction(ActionEvent event){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void getData(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        String query = "SELECT *FROM tblUrunler WHERE kategori='Haftalık Kampanya Ürünleri'";

        observableList.removeAll(observableList);

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(query);
            while (queryResult.next()){
                observableList.add(new ManagerModel(
                        queryResult.getInt("id"),
                        queryResult.getInt("stokDurumu"),
                        queryResult.getInt("subeID"),
                        queryResult.getDouble("fiyat"),
                        queryResult.getString("urunAdi"),
                        queryResult.getString("indirimYuzdesi"),
                        queryResult.getString("kategori"),
                        queryResult.getString("indirimTarihi")));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        updateTable();
    }

    public void updateTable(){

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        saleColumn.setCellValueFactory(new PropertyValueFactory<>("sale"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        placeIDColumn.setCellValueFactory(new PropertyValueFactory<>("placeID"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        tblDataView.setItems(observableList);
    }

    public double readDataPrice(int id){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectionDB = connection.getConnection();
        double result = 0.0;

        String query = "SELECT *FROM tblUrunler";
        try {
            Statement statement = connectionDB.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()){
                if (rs.getInt("id") == id){
                    result = rs.getDouble("fiyat");
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

        return result;
    }

    public void updateDataPrice(int id){

        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();

        PreparedStatement statement = null;
        ResultSet rs;

        double newPrice = 0.0;
        double oldPrice = 0.0;

        oldPrice = readDataPrice(id);
        newPrice = readDataPrice(id) - readDataPrice(id) * (Double.parseDouble(txtSale.getText()) / 100) ;

        try {
            String query = "UPDATE tblUrunler SET fiyat=?, indirimYuzdesi=?, indirimTarihi=? WHERE id='"+id+"'";

            statement = connectDB.prepareStatement(query);
            statement.setDouble(1,Math.round(newPrice * 100.0) / 100.0);
            statement.setString(2,"%"+txtSale.getText());
            statement.setString(3,formatter.format(calendar.getTime()));

            int result = statement.executeUpdate();

            infoBox("İndirim gerçekleşti.Ürün ID: "+id+"\nEski fiyat: "+oldPrice+"\nGüncel fiyat: "+
                    Math.round(newPrice * 100.0) / 100.0,"Bilgilendirme","Ürün İndirimi");
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void findButtonOnAction(ActionEvent event){
        findProduct(txtSearch.getText());
    }

    public void findProduct(String text){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();

        observableList.removeAll(observableList);

        String query = "SELECT * FROM tblUrunler WHERE urunAdi LIKE CONCAT('%','"+text+"','%') AND kategori='Haftalık Kampanya Ürünleri'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(query);

            while (queryResult.next()){
                observableList.add(new ManagerModel(
                        queryResult.getInt("id"),
                        queryResult.getInt("stokDurumu"),
                        queryResult.getInt("subeID"),
                        queryResult.getDouble("fiyat"),
                        queryResult.getString("urunAdi"),
                        queryResult.getString("indirimYuzdesi"),
                        queryResult.getString("kategori"),
                        queryResult.getString("indirimTarihi")));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        updateTable();
    }

    public static void infoBox(String infoMessage, String titleBar, String headerMessage)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }

    public void setLoginLabel(String name) {
        loginLabel.setText(name);
    }
}
