package sample;

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
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;


public class LoginController implements Initializable {
    @FXML
    private ImageView iconImageView;
    @FXML
    private ImageView passwordImageView;
    @FXML
    private ImageView loginImageView;
    @FXML
    private ImageView screenImageView;
    @FXML
    private Button loginButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField loginText;
    @FXML
    private TextField passwordText;

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){
        File loginFile = new File("images/login.png");
        Image loginImage = new Image(loginFile.toURI().toString());
        loginImageView.setImage(loginImage);

        File passwordFile = new File("images/password.png");
        Image passwordImage = new Image(passwordFile.toURI().toString());
        passwordImageView.setImage(passwordImage);

        File screenFile = new File("images/monitor-screen.png");
        Image screenImage = new Image(screenFile.toURI().toString());
        screenImageView.setImage(screenImage);

        File iconFile = new File("images/icon.png");
        Image iconImage = new Image(iconFile.toURI().toString());
        iconImageView.setImage(iconImage);
    }

    public void loginButtonOnAction(ActionEvent event){
        if(loginText.getText().isBlank() == false && passwordText.getText().isBlank() == false)
        {
            validateLogin();
        }else {
            loginMessageLabel.setText("Please enter username and password");
        }
    }

    public void cancelButtonOnAction(ActionEvent event){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private double x,y;
    public void validateLogin(){

        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();

        String verifyLogin = "SELECT kullaniciAdi,parola,kullaniciTipi FROM tblKullanici";
        try {

            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);
            while (queryResult.next()){
                if (queryResult.getString("kullaniciAdi").equals(loginText.getText()) &&
                    queryResult.getString("parola").equals(passwordText.getText()) &&
                    queryResult.getString("kullaniciTipi").equals("Admin")){

                    Stage stage2 = (Stage) loginButton.getScene().getWindow();
                    stage2.close();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("productView.fxml"));
                    Parent root = loader.load();

                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(scene);
                    stage.show();


                    root.setOnMousePressed(mouseEvent -> {
                        x = mouseEvent.getSceneX();
                        y = mouseEvent.getSceneY();
                    });

                    root.setOnMouseDragged(mouseEvent -> {
                        stage.setX(mouseEvent.getScreenX() - x);
                        stage.setY(mouseEvent.getScreenY() - y);
                    });

                }else if(queryResult.getString("kullaniciAdi").equals(loginText.getText()) &&
                        queryResult.getString("parola").equals(passwordText.getText()) &&
                        queryResult.getString("kullaniciTipi").equals("Müdür")) {

                    Stage stage2 = (Stage) loginButton.getScene().getWindow();
                    stage2.close();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("managerView.fxml"));
                    Parent root = loader.load();

                    ManagerController managerController = loader.getController();
                    managerController.setLoginLabel(getName());

                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(scene);
                    stage.show();


                    root.setOnMousePressed(mouseEvent -> {
                        x = mouseEvent.getSceneX();
                        y = mouseEvent.getSceneY();
                    });

                    root.setOnMouseDragged(mouseEvent -> {
                        stage.setX(mouseEvent.getScreenX() - x);
                        stage.setY(mouseEvent.getScreenY() - y);
                    });

                }else {

                    loginMessageLabel.setText("Invalid login. Please try again.");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
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

            statement.close();
            resultSet.close();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return result;
    }

    public String getName(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();

        String result = "";
        try {
            String selectQuery = "SELECT * FROM TEST.tblCalisanlar";
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()){

                if (resultSet.getInt("kullaniciID") == readUserID(loginText.getText()) ){

                    result =  resultSet.getString("adSoyad");
                    break;
                }
            }
            statement.close();
            resultSet.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return result;
    }
}
