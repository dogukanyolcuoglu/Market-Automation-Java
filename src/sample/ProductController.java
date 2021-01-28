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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML
    private TableView<ProductModel> tblDataView;
    @FXML
    private TableColumn<ProductModel,Integer> idColumn;
    @FXML
    private TableColumn<ProductModel,String> productNameColumn;
    @FXML
    private TableColumn<ProductModel,Double> priceColumn;
    @FXML
    private TableColumn<ProductModel,String> saleColumn;
    @FXML
    private TableColumn<ProductModel,String> dateColumn;
    @FXML
    private TableColumn<ProductModel,Integer> stockColumn;
    @FXML
    private TableColumn<ProductModel,Integer> placeIDColumn;
    @FXML
    private TableColumn<ProductModel,String> categoryColumn;
    @FXML
    private TextField txtProductID;
    @FXML
    private TextField txtSale;
    @FXML
    private TextField txtStock;
    @FXML
    private TextField txtSearch;
    @FXML
    private ComboBox cmbCategory;
    @FXML
    private Button listButton;
    @FXML
    private Button confirmButton;
    @FXML
    private Button employeeButton;
    @FXML
    private Button tradeButton;
    @FXML
    private Button placesButton;
    @FXML
    private Button exitButton;
    @FXML
    private ImageView employeeImageView;
    @FXML
    private ImageView tradeImageView;
    @FXML
    private ImageView placeImageView;

    ObservableList<ProductModel> observableList = FXCollections.observableArrayList();

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar calendar = Calendar.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File employeeFile = new File("images/teamwork.png");
        Image employeeImage = new Image(employeeFile.toURI().toString());
        employeeImageView.setImage(employeeImage);

        File tradeFile = new File("images/business.png");
        Image tradeImage = new Image(tradeFile.toURI().toString());
        tradeImageView.setImage(tradeImage);

        File placeFile = new File("images/market.png");
        Image placeImage = new Image(placeFile.toURI().toString());
        placeImageView.setImage(placeImage);

        cmbCategory.setValue("Tüm Ürünler");
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Tüm Ürünler",
                        "Gıda Ürünleri",
                        "Temizlik Ürünleri",
                        "Haftalık Kampanya Ürünleri"
                );
        cmbCategory.setItems(options);

        getData("Tüm Ürünler");
    }

    public void exitButtonOnAction(ActionEvent event){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void getData(String type){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        String query = "";
        if (type == "Tüm Ürünler"){
            query = "SELECT *FROM tblUrunler";
        }else {
            query = "SELECT *FROM tblUrunler Where kategori='"+type+"'";
        }

        observableList.removeAll(observableList);

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(query);
            while (queryResult.next()){
                observableList.add(new ProductModel(
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

    private double x,y;
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


    public void listButtonOnAction(ActionEvent event){
        if (cmbCategory.getValue() != null){
            getData(cmbCategory.getValue().toString());
        }
    }

    public void tradeButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) tradeButton.getScene().getWindow();
        stage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("tradeView.fxml"));
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

    public void confirmButtonOnAction(ActionEvent event){
        if(txtSale.getText().isBlank() == false && txtStock.getText().isBlank() == true){
            updateDataPrice(Integer.parseInt(txtProductID.getText()));
        }else if(txtSale.getText().isBlank() == true && txtStock.getText().isBlank() == false){
            updateDataStock(Integer.parseInt(txtProductID.getText()));
        }else if(txtSale.getText().isBlank() == false && txtStock.getText().isBlank() == false){
            updateDataPrice(Integer.parseInt(txtProductID.getText()));
            updateDataStock(Integer.parseInt(txtProductID.getText()));
        }
        if (cmbCategory.getValue() != null){
            getData(cmbCategory.getValue().toString());
        }
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

    public int readDataStock(int id){

        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        String query = "SELECT *FROM tblUrunler";
        int result = 0;

        try {
            Statement statement = connectDB.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()){
                if (rs.getInt("id") == id){

                    result =  rs.getInt("stokDurumu");
                   break;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

        return result;
    }

    public void updateDataStock(int id){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        PreparedStatement statement = null;
        ResultSet rs;

        int newStock = 0;
        int oldStock = 0;

        oldStock = readDataStock(id);

        if(Integer.parseInt(txtStock.getText()) > 0){
            newStock = readDataStock(id) + Integer.parseInt(txtStock.getText());
        }else {
            System.out.println("HATA");
        }
        try {
            String query = "UPDATE tblUrunler SET stokDurumu=? WHERE id='"+ id +"'";
            statement = connectDB.prepareStatement(query);

            statement.setInt(1,newStock);

            int result = statement.executeUpdate();

            infoBox("Stok güncellendi.Ürün ID: "+id+"\nEski stok: "+oldStock+"\nGüncel stok: "
                    +newStock,"Bilgilendirme","Ürün Stok durumu");
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

        String query = "SELECT * FROM tblUrunler WHERE urunAdi LIKE CONCAT('%','"+text+"','%')";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(query);

            while (queryResult.next()){
                observableList.add(new ProductModel(
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
}
