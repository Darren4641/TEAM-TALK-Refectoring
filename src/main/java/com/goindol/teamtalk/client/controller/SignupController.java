package com.goindol.teamtalk.client.controller;

import com.goindol.teamtalk.Main;
import com.goindol.teamtalk.client.dao.UserDAO;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {
    UserDAO userDAO = UserDAO.getInstance();

    @FXML private Pane pane;
    @FXML private TextField Id;
    @FXML private PasswordField Password;
    @FXML private TextField Nickname;
    @FXML private ImageView goBackButton;

    public void goToBack(){
        try {
            Stage stage = (Stage) pane.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/InitialView.fxml"));
            Parent root = loader.load();
            LoginController login = loader.getController();
            stage.setScene(new Scene(root, 400, 600));
            stage.setTitle("Team Talk");
            stage.setOnCloseRequest(event -> {System.exit(0);});
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerUserAction() {
        String id = Id.getText();
        String password = Password.getText();
        String nickname = Nickname.getText();

        //TODO : 중복 회원가입 여부 확인


        if (!password.isBlank() && !nickname.isBlank() && !id.isBlank()) {

            if (id.matches("^[a-zA-Z0-9]{1,20}$") && password.matches("^[a-zA-Z0-9]{1,20}$")) {
                if (userDAO.validateSignUp(id, nickname) == 0) {
                    try {
                        userDAO.signUp(id, password, nickname);

                        Stage stage = (Stage) Id.getScene().getWindow();
                        Parent root = FXMLLoader.load(Main.class.getResource("views/InitialView.fxml"));
                        stage.setScene(new Scene(root, 400, 600));
                        stage.setTitle("Team Talk");
                        stage.setOnCloseRequest(event -> {
                            System.exit(0);
                        });
                        stage.setResizable(false);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (userDAO.validateSignUp(id, nickname) == 1) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("warning");
                    alert.setHeaderText("로그인 오류");
                    alert.setContentText("이미 존재하는 아이디입니다.");
                    alert.show();
                } else if (userDAO.validateSignUp(id, nickname) == 2) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("warning");
                    alert.setHeaderText("로그인 오류");
                    alert.setContentText("이미 존재하는 닉네임입니다.");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("warning");
                alert.setHeaderText("로그인 오류");
                alert.setContentText("아이디 및 비밀번호는 숫자와 영어만 사용 가능합니다.");
                alert.show();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("warning");
            alert.setHeaderText("로그인 오류");
            alert.setContentText("입력하지 않은 항목이 있습니다.");
            alert.show();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        goBackButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                goToBack();
            }
        });
    }
}