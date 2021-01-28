package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class TradeController implements Initializable {

    @FXML
    private TextField txtProductID;
    @FXML
    private TextField txtStock;
    @FXML
    private TextField txtSearch;
    @FXML
    private Button listButton;
    @FXML
    private Button findButton;
    @FXML
    private Button buyButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button productButton;
    @FXML
    private Button employeeButton;
    @FXML
    private Button placesButton;
    @FXML
    private TableView<TradeModel> tblDataView;
    @FXML
    private TableColumn<TradeModel,Integer> idColumn;
    @FXML
    private TableColumn<TradeModel,Integer> stockColumn;
    @FXML
    private TableColumn<TradeModel,Double> priceColumn;
    @FXML
    private TableColumn<TradeModel,String> tradeNameColumn;
    @FXML
    private TableColumn<TradeModel,String> tradeAddressColumn;
    @FXML
    private TableColumn<TradeModel,String> productNameColumn;
    @FXML
    private TableColumn<TradeModel,String> categoryColumn;
    @FXML
    private ImageView productImageView;
    @FXML
    private ImageView employeeImageView;
    @FXML
    private ImageView placesImageView;
    @FXML
    private ComboBox cmbCategory;
    @FXML
    private ComboBox cmbPlacesID;

    ObservableList<TradeModel> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File employeeFile = new File("images/teamwork.png");
        Image employeeImage = new Image(employeeFile.toURI().toString());
        employeeImageView.setImage(employeeImage);

        File productFile = new File("images/stock.png");
        Image productImage = new Image(productFile.toURI().toString());
        productImageView.setImage(productImage);

        File placeFile = new File("images/market.png");
        Image placeImage = new Image(placeFile.toURI().toString());
        placesImageView.setImage(placeImage);

        cmbCategory.setValue("Tüm Ürünler");
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Tüm Ürünler",
                        "Gıda Ürünleri",
                        "Temizlik Ürünleri",
                        "Haftalık Kampanya Ürünleri"
                );
        cmbCategory.setItems(options);


        ObservableList<String> places =
                FXCollections.observableArrayList("1", "2", "3", "4","5","6","7","8","9","10");
        cmbPlacesID.setItems(places);

        getData("Tüm Ürünler");
    }

    public void exitButtonOnAction(ActionEvent event){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void listButtonOnAction(ActionEvent event){

        if (cmbCategory.getValue() != null){
            getData(cmbCategory.getValue().toString());
        }
    }

    public void findButtonOnAction(ActionEvent event){
        findProduct(txtSearch.getText());
    }

    public void getData(String type){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        String query = "";
        if (type == "Tüm Ürünler"){
                query = "SELECT *FROM tblTedarikciler";
        }else {
            query = "SELECT *FROM tblTedarikciler Where kategori='"+type+"'";
        }

        observableList.removeAll(observableList);

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(query);

            while (queryResult.next()){
                observableList.add(new TradeModel(
                        queryResult.getInt("id"),
                        queryResult.getInt("stok"),
                        queryResult.getString("tedarikciAdi"),
                        queryResult.getString("tedarikciAdres"),
                        queryResult.getString("urunAdi"),
                        queryResult.getString("kategori"),
                        queryResult.getDouble("fiyat")));
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
        tradeNameColumn.setCellValueFactory(new PropertyValueFactory<>("tradeName"));
        tradeAddressColumn.setCellValueFactory(new PropertyValueFactory<>("tradeAddress"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        tblDataView.setItems(observableList);
    }

    private double x,y;

    public void productButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) productButton.getScene().getWindow();
        stage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("productView.fxml"));
        Parent root = loader.load();

        Stage stage2 = new Stage();
        Scene scene = new Scene(root);
        stage2.initStyle(StageStyle.UNDECORATED);
        stage2.setScene(scene);
        stage2.show();

        root.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        root.setOnMouseDragged(mouseEvent -> {
            stage2.setX(mouseEvent.getScreenX() - x);
            stage2.setY(mouseEvent.getScreenY() - y);
        });
    }

    public void employeeButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) employeeButton.getScene().getWindow();
        stage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("employeeSaveView.fxml"));
        Parent root = loader.load();

        Stage stage2 = new Stage();
        Scene scene = new Scene(root);

        stage2.initStyle(StageStyle.UNDECORATED);
        stage2.setScene(scene);
        stage2.show();

        root.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        root.setOnMouseDragged(mouseEvent -> {
            stage2.setX(mouseEvent.getScreenX() - x);
            stage2.setY(mouseEvent.getScreenY() - y);
        });
    }

    public void placesButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) placesButton.getScene().getWindow();
        stage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("placesView.fxml"));
        Parent root = loader.load();

        Stage stage2 = new Stage();
        Scene scene = new Scene(root);
        stage2.initStyle(StageStyle.UNDECORATED);
        stage2.setScene(scene);
        stage2.show();

        root.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        root.setOnMouseDragged(mouseEvent -> {
            stage2.setX(mouseEvent.getScreenX() - x);
            stage2.setY(mouseEvent.getScreenY() - y);
        });
    }

    public void buyButtonOnAction(ActionEvent event){
        buyProduct();
        if (cmbCategory.getValue() != null){
            getData(cmbCategory.getValue().toString());
        }
    }


    public void buyProduct(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        PreparedStatement statement = null;
        ResultSet rs;

        try {
            if (txtStock.getText().isBlank() == false && cmbPlacesID.getSelectionModel().isEmpty() == false){
                if (Integer.parseInt(txtStock.getText()) < readStock()){

                    String query = "INSERT INTO tblUrunler(urunAdi,fiyat,subeID,stokDurumu,kategori) VALUES(?,?,?,?,?)";
                    statement = connectDB.prepareStatement(query);

                    statement.setString(1,readProductname());
                    statement.setDouble(2,readPrice());
                    statement.setInt(3,Integer.parseInt(cmbPlacesID.getValue().toString()));
                    statement.setInt(4,Integer.parseInt(txtStock.getText()));
                    statement.setString(5,readCategory());

                    int result = statement.executeUpdate();

                    downStock();

                } else {
                    infoBox2("Tedarikçinin stoğundan fazla değer girdiniz.","Uyarı","Geçersiz stok!");

                }
            }

        }catch (SQLException e){
            e.printStackTrace();
            e.getMessage();
        }
    }

    public double readPrice(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        String query = "SELECT *FROM tblTedarikciler";

        double result = 0;

        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getInt("id") == Integer.parseInt(txtProductID.getText())){
                    result = resultSet.getDouble("fiyat");
                    break;
                }
            }

            statement.close();
            resultSet.close();

        }catch (SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    public String readProductname(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        String query = "SELECT *FROM tblTedarikciler";

        String str = "";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getInt("id") == Integer.parseInt(txtProductID.getText())){
                    str = resultSet.getString("urunAdi");
                    break;
                }
            }

            statement.close();
            resultSet.close();

        }catch (SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
        return str;
    }

    public String readCategory(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        String query = "SELECT *FROM tblTedarikciler";

        String str = "";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getInt("id") == Integer.parseInt(txtProductID.getText())){
                    str = resultSet.getString("kategori");
                    break;
                }
            }

            statement.close();
            resultSet.close();

        }catch (SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
        return str;
    }

    public int readStock(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        String query = "SELECT *FROM tblTedarikciler";

        int stock = 0;

        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getInt("id") == Integer.parseInt(txtProductID.getText())){
                    stock = resultSet.getInt("stok");
                    break;
                }
            }

            statement.close();
            resultSet.close();

        }catch (SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
        return stock;
    }


    public void downStock(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        PreparedStatement statement = null;
        ResultSet rs;

        try {
            String query = "UPDATE tblTedarikciler SET stok=? WHERE id='"+Integer.parseInt(txtProductID.getText())+"'";

            statement = connectDB.prepareStatement(query);

            statement.setInt(1,readStock() - Integer.parseInt(txtStock.getText()));

            int result = statement.executeUpdate();

        }catch (SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
    }

    public void findProduct(String text){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();

        observableList.removeAll(observableList);
        String query = "SELECT * FROM tblTedarikciler WHERE urunAdi LIKE CONCAT('%','"+text+"','%')";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(query);

            while (queryResult.next()){
                observableList.add(new TradeModel(
                        queryResult.getInt("id"),
                        queryResult.getInt("stok"),
                        queryResult.getString("tedarikciAdi"),
                        queryResult.getString("tedarikciAdres"),
                        queryResult.getString("urunAdi"),
                        queryResult.getString("kategori"),
                        queryResult.getDouble("fiyat")));
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

    public static void infoBox2(String infoMessage, String titleBar, String headerMessage)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }

}
