package com.goindol.teamtalk.client.controller;

import com.goindol.teamtalk.Main;
import com.goindol.teamtalk.client.dao.ChatRoomDAO;
import com.goindol.teamtalk.client.dto.UserDTO;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MakeChatRoomController implements Initializable {
    public ChatRoomDAO chatRoomDAO = ChatRoomDAO.getInstance();
    public UserDTO userDTO;
    @FXML private Pane pane;
    @FXML private TextField chatRoomTitle;
    @FXML private Button complete;

    public void setChatRoomTitle(){
        if(chatRoomTitle.getText().length()>10){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("warning");
            alert.setHeaderText("채팅방 제목 오류");
            alert.setContentText("채팅창 제목은 10글자 이내로 입력해주세요.");
            alert.show();
        }else {
            chatRoomDAO.createChatRoom(chatRoomTitle.getText(), userDTO.getNickName());
            int chatId = chatRoomDAO.getChatRoomId(chatRoomTitle.getText(), userDTO.getNickName());
            chatRoomDAO.inviteChatRoom(chatId, userDTO.getNickName());
            chatRoomTitle.setText("");
            //
            String s = chatRoomTitle.getText();
            System.out.println(s);
        }
    }
    public void goToBack(){
        try {
            Stage stage = (Stage) chatRoomTitle.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/MainView.fxml"));
            Parent root = loader.load();
            MainController main = loader.getController();
            main.setUserDTO(userDTO);
            main.showChatRoomList();
            stage.setScene(new Scene(root, 400, 600));
            stage.setTitle("Team Talk");
            stage.setOnCloseRequest(event -> {System.exit(0);});
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        complete.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setChatRoomTitle();
                try {
                    Stage stage = (Stage) pane.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/MainView.fxml"));
                    Parent root = loader.load();
                    MainController main = loader.getController();
                    main.setUserDTO(userDTO);
                    main.showChatRoomList();
                    stage.setScene(new Scene(root, 400, 600));
                    stage.setTitle("Team Talk");
                    stage.setOnCloseRequest(event -> {System.exit(0);});
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
