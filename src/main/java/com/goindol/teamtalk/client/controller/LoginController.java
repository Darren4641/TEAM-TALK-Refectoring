package com.goindol.teamtalk.client.controller;

import com.goindol.teamtalk.Main;
import com.goindol.teamtalk.client.dao.UserDAO;
import com.goindol.teamtalk.client.dto.UserDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.Socket;


public class LoginController {

    @FXML public Pane pane;
    @FXML public TextField Id;
    @FXML public PasswordField Password;
    @FXML public Button loginButton;
    @FXML public Button signupButton;

    Socket socket;
    public UserDTO userDTO;
    public UserDAO userDAO = UserDAO.getInstance();

    public void showScene() throws IOException {
        Platform.runLater(() -> {
            Stage stage = (Stage) Id.getScene().getWindow();
            stage.setResizable(false);
            stage.setWidth(400);
            stage.setHeight(600);
            stage.setOnCloseRequest((WindowEvent e) -> {
                Platform.exit();
                System.exit(0);
            });
            stage.setScene(this.Id.getScene());
        });
    }

    public void loginButtonAction() {
        String id = Id.getText();
        String password = Password.getText();
        System.out.println("id = " + id);
        System.out.println("password = " + password);
        //TODO : 디비랑 아이디 비번 비교
        if(!id.equals("") && !password.equals("")) {
            int status = userDAO.login(id, password);
            if (status == 3) {
                try {

                    this.userDTO = userDAO.getUser(id, password);

                    Stage stage = (Stage) Id.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/MainView.fxml"));
                    Parent root = loader.load();
                    MainController main = loader.getController();
                    main.setUserDTO(userDTO);
                    main.showChatRoomList();
                    main.send("login/roomId/" + userDTO.getNickName());
                    stage.setScene(new Scene(root, 400, 600));
                    stage.setTitle("Team Talk");
                    stage.setOnCloseRequest(event -> {
                        userDAO.logout(userDTO.getUserId(), userDTO.getNickName());
                        main.send("login/roomId/" + userDTO.getNickName());
                        System.exit(0);
                    });
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(status == 1){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("warning");
                alert.setHeaderText("로그인 오류");
                alert.setContentText("잘못된 아이디나 비밀번호를 입력했습니다.");
                alert.show();
            } else if(status == 2) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("warning");
                alert.setHeaderText("로그인 오류");
                alert.setContentText("이미 로그인 되어 있는 계정입니다.");
                alert.show();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("warning");
            alert.setHeaderText("로그인 오류");
            alert.setContentText("입력하지 않은 항목이 있습니다.");
            alert.show();
        }


    }

    public void signupButtonAction() {
        try {
            Stage stage = (Stage) Id.getScene().getWindow();
            Parent root = FXMLLoader.load(Main.class.getResource("views/SignUpView.fxml"));
            stage.setScene(new Scene(root, 400, 600));
            stage.setTitle("Team Talk");
            stage.setOnCloseRequest(event -> {System.exit(0);});
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
