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

public class EmployeeController implements Initializable {

    @FXML
    private TableView<EmployeeModel> tblDataView;
    @FXML
    private TableColumn<EmployeeModel,Integer> idColumn;
    @FXML
    private TableColumn<EmployeeModel,String> nameSurnameColumn;
    @FXML
    private TableColumn<EmployeeModel,String> addressColumn;
    @FXML
    private TableColumn<EmployeeModel,Double> salaryColumn;
    @FXML
    private TableColumn<EmployeeModel,Integer> placeidColumn;
    @FXML
    private TableColumn<EmployeeModel,String> positionColumn;
    @FXML
    private TableColumn<EmployeeModel,Integer> useridColumn;
    @FXML
    private TableColumn<EmployeeModel,String> dateColumn;
    @FXML
    private TableColumn<EmployeeModel,Integer> permissionColumn;
    @FXML
    private ComboBox cmbPlaceID;
    @FXML
    private ComboBox cmbPosition;
    @FXML
    private ComboBox cmbCategory;
    @FXML
    private ImageView productImageView;
    @FXML
    private ImageView tradeImageView;
    @FXML
    private ImageView placesImageView;
    @FXML
    private Button productButton;
    @FXML
    private Button tradeButton;
    @FXML
    private Button placesButton;
    @FXML
    private Button pageOneButton;
    @FXML
    private Button exitButton;
    @FXML
    private TextField txtEmployeeID;
    @FXML
    private TextField txtSalary;
    @FXML
    private TextField txtSearch;


    ObservableList<EmployeeModel> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        cmbPlaceID.setItems(placesID);

        ObservableList<String> position = FXCollections.observableArrayList(
                "Müdür Yardımcısı","Müdür"
        );
        cmbPosition.setItems(position);

        cmbCategory.setValue("Tüm Çalışanlar");
        ObservableList<String> category = FXCollections.observableArrayList(
                "Tüm Çalışanlar","Kasiyer","Müdür Yardımcısı","Müdür"
        );
        cmbCategory.setItems(category);
        
        getData("Tüm Çalışanlar");
    }

    public void exitButtonOnAction(ActionEvent event){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
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

    public void pageOneButtonOnAction(ActionEvent event) throws IOException {

        Stage stage = (Stage) pageOneButton.getScene().getWindow();
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

    public void findButtonOnAction(ActionEvent event){
        findEmployee(txtSearch.getText());
    }
    public void getData(String type){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        String query = "";
        if (type == "Tüm Çalışanlar"){
            query = "SELECT *FROM tblCalisanlar";
        }else {
            query = "SELECT *FROM tblCalisanlar Where pozisyon='"+type+"'";
        }

        observableList.removeAll(observableList);

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(query);
            while (queryResult.next()){
                observableList.add(new EmployeeModel(
                        queryResult.getString("adSoyad"),
                        queryResult.getString("adres"),
                        queryResult.getString("pozisyon"),
                        queryResult.getString("iseBaslamaTarihi"),
                        queryResult.getInt("izinHakki"),
                        queryResult.getInt("kullaniciID"),
                        queryResult.getInt("subeID"),
                        queryResult.getInt("id"),
                        queryResult.getDouble("maas")));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        updateTable();
    }

    public void updateTable(){

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("nameSurname"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        placeidColumn.setCellValueFactory(new PropertyValueFactory<>("placesID"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        useridColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        permissionColumn.setCellValueFactory(new PropertyValueFactory<>("permissionDay"));

        tblDataView.setItems(observableList);
    }

    public void listButtonOnAction(ActionEvent event){
        if (cmbCategory.getValue() != null){
            getData(cmbCategory.getValue().toString());
        }
    }

    public void updateButtonOnAction(ActionEvent event){
        updateEmployee();
        if (cmbCategory.getValue() != null){
            getData(cmbCategory.getValue().toString());
        }
    }

    public void updateEmployee(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        PreparedStatement statement = null;
        ResultSet rs;

        try {
           if (txtSalary.getText().isBlank() == false && cmbPlaceID.getSelectionModel().isEmpty() == false
                   && cmbPosition.getSelectionModel().isEmpty() == false){

               if (readSalary() < Double.parseDouble(txtSalary.getText())){

                   if (!readPosition().equals("Müdür") && !(cmbPosition.getValue().equals("Müdür"))){

                       String query = "UPDATE tblCalisanlar SET maas=?, subeID=?, pozisyon=? WHERE id='"+txtEmployeeID.getText()+"'";

                       statement = connectDB.prepareStatement(query);

                       statement.setDouble(1,Double.parseDouble(txtSalary.getText()));
                       statement.setInt(2,Integer.parseInt(cmbPlaceID.getValue().toString()));
                       statement.setString(3,cmbPosition.getValue().toString());

                       int result = statement.executeUpdate();

                   }else if(!readPosition().equals("Müdür") && cmbPosition.getValue().equals("Müdür")){

                       String query = "UPDATE tblCalisanlar SET maas=?, subeID=?, pozisyon=?,kullaniciID=? WHERE id='"+txtEmployeeID.getText()+"'";

                       statement = connectDB.prepareStatement(query);

                       statement.setDouble(1,Double.parseDouble(txtSalary.getText()));
                       statement.setInt(2,Integer.parseInt(cmbPlaceID.getValue().toString()));
                       statement.setString(3,cmbPosition.getValue().toString());
                       statement.setInt(4,readUserID(addManagerAccount(readUserNameSurname())));

                       int result = statement.executeUpdate();

                   } else {
                       infoBox("Detay: Çalışanın terfi düşürülmesi gerçekleştirilemez!","Hatırlatma","Çalışan Terfi");
                   }

               }else {
                   infoBox("Detay: Çalışanın maaş düşürülmesi gerçekleştirilemez!","Hatırlatma","Çalışan Maaş");
               }
           }if(txtSalary.getText().isBlank() == false && cmbPlaceID.getSelectionModel().isEmpty() == true
                    && cmbPosition.getSelectionModel().isEmpty() == true){

               if (readSalary() < Double.parseDouble(txtSalary.getText())){

                   String query = "UPDATE tblCalisanlar SET maas=? WHERE id='"+txtEmployeeID.getText()+"'";

                   statement = connectDB.prepareStatement(query);

                   statement.setDouble(1,Double.parseDouble(txtSalary.getText()));

                   int result = statement.executeUpdate();

               }else {
                   infoBox("Detay: Çalışanın maaş düşürülmesi gerçekleştirilemez!","Hatırlatma","Çalışan Maaş");
               }

           }if (cmbPlaceID.getSelectionModel().isEmpty() == false && txtSalary.getText().isBlank() == true
                    && cmbPosition.getSelectionModel().isEmpty() == true){

                   String query = "UPDATE tblCalisanlar SET subeID=? WHERE id='"+txtEmployeeID.getText()+"'";

                   statement = connectDB.prepareStatement(query);

                   statement.setInt(1,Integer.parseInt(cmbPlaceID.getValue().toString()));

                   int result = statement.executeUpdate();

           }if (cmbPosition.getSelectionModel().isEmpty() == false && txtSalary.getText().isBlank() == true
                    && cmbPlaceID.getSelectionModel().isEmpty() == true){
               if (!readPosition().equals("Müdür") && !(cmbPosition.getValue().equals("Müdür"))) {

                   String query = "UPDATE tblCalisanlar SET pozisyon=? WHERE id='"+txtEmployeeID.getText()+"'";

                   statement = connectDB.prepareStatement(query);

                   statement.setString(1,cmbPosition.getValue().toString());

                   int result = statement.executeUpdate();

               }else if (!readPosition().equals("Müdür") && cmbPosition.getValue().equals("Müdür")){

                   String query = "UPDATE tblCalisanlar SET pozisyon=?, kullaniciID=? WHERE id='"+txtEmployeeID.getText()+"'";

                   statement = connectDB.prepareStatement(query);

                   statement.setString(1,cmbPosition.getValue().toString());
                   statement.setInt(2,readUserID(addManagerAccount(readUserNameSurname())));

                   int result = statement.executeUpdate();

               }else {
                   infoBox("Detay: Çalışanın terfi düşürülmesi gerçekleştirilemez!","Hatırlatma","Çalışan Terfi");
               }

           }if (txtSalary.getText().isBlank() == false && cmbPlaceID.getSelectionModel().isEmpty() == false
                    && cmbPosition.getSelectionModel().isEmpty() == true){

                if (readSalary() < Double.parseDouble(txtSalary.getText())){

                    String query = "UPDATE tblCalisanlar SET maas=?, subeID=? WHERE id='"+txtEmployeeID.getText()+"'";

                    statement = connectDB.prepareStatement(query);

                    statement.setDouble(1,Double.parseDouble(txtSalary.getText()));
                    statement.setInt(2,Integer.parseInt(cmbPlaceID.getValue().toString()));

                    int result = statement.executeUpdate();
                }else {
                    infoBox("Detay: Çalışanın maaş düşürülmesi gerçekleştirilemez!","Hatırlatma","Çalışan Maaş");
                }

           }if (txtSalary.getText().isBlank() == false && cmbPosition.getSelectionModel().isEmpty() == false
                    && cmbPlaceID.getSelectionModel().isEmpty() == true){

               if (readSalary() < Double.parseDouble(txtSalary.getText())){

                   if (!readPosition().equals("Müdür") && !(cmbPosition.getValue().equals("Müdür"))) {

                       String query = "UPDATE tblCalisanlar SET maas=?, pozisyon=? WHERE id='"+txtEmployeeID.getText()+"'";

                       statement = connectDB.prepareStatement(query);

                       statement.setDouble(1,Double.parseDouble(txtSalary.getText()));
                       statement.setString(2,cmbPosition.getValue().toString());

                       int result = statement.executeUpdate();

                   }else if (!readPosition().equals("Müdür") && cmbPosition.getValue().equals("Müdür")){
                       String query = "UPDATE tblCalisanlar SET maas=?, pozisyon=?, kullaniciID=? WHERE id='"+txtEmployeeID.getText()+"'";

                       statement = connectDB.prepareStatement(query);

                       statement.setDouble(1,Double.parseDouble(txtSalary.getText()));
                       statement.setString(2,cmbPosition.getValue().toString());
                       statement.setInt(3,readUserID(addManagerAccount(readUserNameSurname())));

                       int result = statement.executeUpdate();
                   }else {
                       infoBox("Detay: Çalışanın terfi düşürülmesi gerçekleştirilemez!","Hatırlatma","Çalışan Terfi");
                   }
               }else {
                   infoBox("Detay: Çalışanın maaş düşürülmesi gerçekleştirilemez!","Hatırlatma","Çalışan Maaş");
               }

           }if (cmbPlaceID.getSelectionModel().isEmpty() == false && cmbPosition.getSelectionModel().isEmpty() == false
                    && txtSalary.getText().isBlank() == true){
               if (!readPosition().equals("Müdür") && !(cmbPosition.getValue().equals("Müdür"))) {

                   String query = "UPDATE tblCalisanlar SET subeID=?, pozisyon=? WHERE id='"+txtEmployeeID.getText()+"'";

                   statement = connectDB.prepareStatement(query);

                   statement.setInt(1,Integer.parseInt(cmbPlaceID.getValue().toString()));
                   statement.setString(2,cmbPosition.getValue().toString());

                   int result = statement.executeUpdate();

               }else if (!readPosition().equals("Müdür") && cmbPosition.getValue().equals("Müdür")){

                   String query = "UPDATE tblCalisanlar SET subeID=?, pozisyon=?, kullaniciID=? WHERE id='"+txtEmployeeID.getText()+"'";

                   statement = connectDB.prepareStatement(query);

                   statement.setInt(1,Integer.parseInt(cmbPlaceID.getValue().toString()));
                   statement.setString(2,cmbPosition.getValue().toString());
                   statement.setInt(3,readUserID(addManagerAccount(readUserNameSurname())));

                   int result = statement.executeUpdate();

               }else {
                   infoBox("Detay: Çalışanın terfi düşürülmesi gerçekleştirilemez!","Hatırlatma","Çalışan Terfi");
               }
           }

        }catch (SQLException e){
            e.getMessage();
            e.printStackTrace();
        }

    }

    public double readSalary(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        String query = "SELECT *FROM tblCalisanlar";

        double result = 0;

        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getInt("id") == Integer.parseInt(txtEmployeeID.getText())){
                    result = resultSet.getDouble("maas");
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

    public String readPosition(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        String query = "SELECT *FROM tblCalisanlar";

        String result = "";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getInt("id") == Integer.parseInt(txtEmployeeID.getText())){
                    result = resultSet.getString("pozisyon");
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

    public void findEmployee(String text){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();

        observableList.removeAll(observableList);

        String query = "SELECT * FROM tblCalisanlar WHERE adSoyad LIKE CONCAT('%','"+text+"','%')";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(query);

            while (queryResult.next()){
                observableList.add(new EmployeeModel(
                        queryResult.getString("adSoyad"),
                        queryResult.getString("adres"),
                        queryResult.getString("pozisyon"),
                        queryResult.getString("iseBaslamaTarihi"),
                        queryResult.getInt("izinHakki"),
                        queryResult.getInt("kullaniciID"),
                        queryResult.getInt("subeID"),
                        queryResult.getInt("id"),
                        queryResult.getDouble("maas")));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        updateTable();
    }

    public String addManagerAccount(String input){

        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        PreparedStatement statement = null;
        ResultSet rs;

        String username = input.toLowerCase().replace(" ", "");

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

    public String readUserNameSurname(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();

        String str = "";

        try {
        String query = "SELECT *FROM tblCalisanlar";
        Statement statement = connectDB.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            if (resultSet.getInt("id") == Integer.parseInt(txtEmployeeID.getText())){
                str = resultSet.getString("adSoyad");
                break;
            }
        }

        }catch (SQLException e){
            e.printStackTrace();
            e.getMessage();
        }
        return  str;
    }

    public static void infoBox(String infoMessage, String titleBar, String headerMessage)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }
}
