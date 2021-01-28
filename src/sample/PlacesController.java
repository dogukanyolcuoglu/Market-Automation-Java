package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class PlacesController implements Initializable {

    @FXML
    private TableView<PlacesModel> tblDataView;
    @FXML
    private TableColumn<PlacesModel,Integer> idColumn;
    @FXML
    private TableColumn<PlacesModel,String> cityColumn;
    @FXML
    private TableColumn<PlacesModel,String> townColumn;
    @FXML
    private TableColumn<PlacesModel,String> addressColumn;
    @FXML
    private TableColumn<PlacesModel,String> marketNameColumn;
    @FXML
    private TextField txtSearch;
    @FXML
    private Button productButton;
    @FXML
    private Button employeeButton;
    @FXML
    private Button tradeButton;
    @FXML
    private Button findButton;
    @FXML
    private Button exitButton;
    @FXML
    private ImageView productImageView;
    @FXML
    private ImageView employeeImageView;
    @FXML
    private ImageView tradeImageView;


    ObservableList<PlacesModel> observableList = FXCollections.observableArrayList();
    private double x,y;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File productFile = new File("images/stock.png");
        Image productImage = new Image(productFile.toURI().toString());
        productImageView.setImage(productImage);

        File employeeFile = new File("images/teamwork.png");
        Image employeeImage = new Image(employeeFile.toURI().toString());
        employeeImageView.setImage(employeeImage);

        File tradeFile = new File("images/business.png");
        Image tradeImage = new Image(tradeFile.toURI().toString());
        tradeImageView.setImage(tradeImage);

        getData();
    }

    public void findButtonOnAction(ActionEvent event){
        findPlaces(txtSearch.getText());
    }

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

    public void exitButtonOnAction(ActionEvent event){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void getData(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();


        observableList.removeAll(observableList);

        try {
            String query = "SELECT *FROM tblSubeler";
            Statement statement = connectDB.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                observableList.add(new PlacesModel(
                        rs.getInt("id"),
                        rs.getString("il"),
                        rs.getString("ilce"),
                        rs.getString("adres"),
                        rs.getString("marketIsmi")
                ));
            }

        } catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }

        updateTable();
    }


    public void updateTable(){

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        townColumn.setCellValueFactory(new PropertyValueFactory<>("town"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        marketNameColumn.setCellValueFactory(new PropertyValueFactory<>("marketName"));

        tblDataView.setItems(observableList);

    }

    public void findPlaces(String input){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();

        observableList.removeAll(observableList);
        try {
            String query = "SELECT *FROM tblSubeler WHERE il LIKE CONCAT('%','"+ input +"','%') ||  ilce LIKE CONCAT('%','"+ input +"','%')";
            Statement statement = connectDB.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()){
                observableList.add(new PlacesModel(
                        rs.getInt("id"),
                        rs.getString("il"),
                        rs.getString("ilce"),
                        rs.getString("adres"),
                        rs.getString("marketIsmi")
                ));
            }

        }catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }
        updateTable();
    }

}
