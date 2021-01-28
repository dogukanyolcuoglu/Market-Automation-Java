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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class EmployeeSave implements Initializable {

    @FXML
    private ImageView productImageView;
    @FXML
    private ImageView tradeImageView;
    @FXML
    private ImageView placesImageView;
    @FXML
    private Button registerButton;
    @FXML
    private Button productButton;
    @FXML
    private Button tradeButton;
    @FXML
    private Button placesButton;
    @FXML
    private Button pageTwoButton;
    @FXML
    private Button exitButton;
    @FXML
    private TextField txtSalary;
    @FXML
    private TextField txtNameSurname;
    @FXML
    private TextArea txtAddress;
    @FXML
    private ComboBox cmbPosition;
    @FXML
    private ComboBox cmbPlacesID;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        File productFile = new File("images/stock.png");
        Image productImage = new Image(productFile.toURI().toString());
        productImageView.setImage(productImage);

        File tradeFile = new File("images/business.png");
        Image tradeImage = new Image(tradeFile.toURI().toString());
        tradeImageView.setImage(tradeImage);

        File placesFile = new File("images/market.png");
        Image placesImage = new Image(placesFile.toURI().toString());
        placesImageView.setImage(placesImage);

        ObservableList<String> placesID = FXCollections.observableArrayList(
                "1","2","3","4","5","6","7","8","9","10"
        );
        cmbPlacesID.setItems(placesID);

        ObservableList<String> position = FXCollections.observableArrayList(
                "Kasiyer","Müdür Yardımcısı","Müdür"
        );
        cmbPosition.setItems(position);

    }

    public void exitButtonOnAction(ActionEvent event){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
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

    public void registerButtonOnAction(ActionEvent event){
        if (txtAddress.getText().isBlank() == false && txtNameSurname.getText().isBlank() == false && cmbPosition.getValue() != null
            && cmbPlacesID.getValue() != null && txtSalary.getText().isBlank() == false){

            saveEmployee();
            clear();

        } else {
            infoBox2("Lütfen boş alan bırakmayınız!","Uyarı","Boş alan hatası");
        }
    }

    public void clear(){
        txtNameSurname.setText("");
        txtAddress.setText("");
        txtSalary.setText("");
        cmbPosition.setValue("");
        cmbPlacesID.setValue("");
    }

    private double x,y;
    public void pageTwoButtonOnAction(ActionEvent event) throws IOException {

        Stage stage = (Stage) pageTwoButton.getScene().getWindow();
        stage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("employeeView.fxml"));
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


    //ÇALIŞANLARIN DATABASE İŞLEMLERİ....
    public void saveEmployee(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        PreparedStatement statement = null;
        ResultSet rs;

        try {
            if (cmbPosition.getValue() == "Müdür"){

                String query = "INSERT INTO tblCalisanlar(adSoyad,adres,maas,subeID,pozisyon,kullaniciID) VALUES(?,?,?,?,?,?)";
                statement = connectDB.prepareStatement(query);

                statement.setString(1,txtNameSurname.getText().toUpperCase());
                statement.setString(2,txtAddress.getText());
                statement.setDouble(3,Double.parseDouble(txtSalary.getText()));
                statement.setInt(4,Integer.parseInt(cmbPlacesID.getValue().toString()));
                statement.setString(5,cmbPosition.getValue().toString());
                statement.setInt(6,readUserID(addManagerAccount()));

                int result = statement.executeUpdate();

            }else {

                String query = "INSERT INTO tblCalisanlar(adSoyad,adres,maas,subeID,pozisyon) VALUES(?,?,?,?,?)";
                statement = connectDB.prepareStatement(query);

                statement.setString(1,txtNameSurname.getText().toUpperCase());
                statement.setString(2,txtAddress.getText());
                statement.setDouble(3,Double.parseDouble(txtSalary.getText()));
                statement.setInt(4,Integer.parseInt(cmbPlacesID.getValue().toString()));
                statement.setString(5,cmbPosition.getValue().toString());

                int result = statement.executeUpdate();

                infoBox("Yeni bir çalışan kaydedildi.\nAd Soyad: "+
                        txtNameSurname.getText().toUpperCase()+"\nÇalışacağı Mevki: "+cmbPosition.getValue().toString(),"Bilgilendirme","Çalışan Kayıt");
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public String addManagerAccount(){

        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        PreparedStatement statement = null;
        ResultSet rs;

        String username = txtNameSurname.getText().toLowerCase().replace(" ", "");

        if (username.contains("ğ") || username.contains("ı") || username.contains("ö") || username.contains("ü") || username.contains("ç"))
        {

            username = username.replace("ğ", "g");
            username = username.replace("ı", "i");
            username = username.replace("ö", "o");
            username = username.replace("ü", "u");
            username = username.replace("ç", "c");

            if (controlUsername(username)){

                username = newUsername(username);
            }
        }else{
            if (controlUsername(username)){

                username = newUsername(username);
            }
        }
        try {

            String query = "INSERT INTO tblKullanici(kullaniciAdi,parola,kullaniciTipi) VALUES(?,?,?)";
            statement = connectDB.prepareStatement(query);

            statement.setString(1,username);
            statement.setString(2,username);
            statement.setString(3,cmbPosition.getValue().toString());

            int result = statement.executeUpdate();

            infoBox("Yeni bir çalışan kaydedildi.\nAd Soyad: "+txtNameSurname.getText().toUpperCase()+"\nÇalışacağı mevki: Müdür\nKullanıcı adı: "+username+"\nParola: "
                    +username,"Bilgilendirme","Çalışan Kayıt");
        }catch (SQLException e){
                e.printStackTrace();
                e.getCause();
        }
        return username;
    }

    public String newUsername(String getUsername)
    {
        while(true)
        {
            int rnd = 1+(int)(Math.random()* 100);
            getUsername += String.valueOf(rnd);
            if (!controlUsername(getUsername)) {
                break;
            }

        }
        return getUsername;
    }

    public boolean controlUsername(String input){

        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        boolean isThere = false;

        try {
            String query = "SELECT * FROM TEST.tblKullanici";
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){

                if (resultSet.getString("kullaniciAdi").equals(input)){
                    isThere = true;
                    break;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return isThere;
    }

    public int readUserID(String username){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();

        int result = 0;

        try {
            String selectQuery = "SELECT * FROM TEST.tblKullanici";
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()){

                if (resultSet.getString("kullaniciAdi").equals(username)){

                    result =  resultSet.getInt("kullaniciID");
                    break;
                }
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return result;
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
