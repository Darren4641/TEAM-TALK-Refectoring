package com.goindol.teamtalk.client.controller;

import com.goindol.teamtalk.Main;
import com.goindol.teamtalk.client.dao.ChatRoomDAO;
import com.goindol.teamtalk.client.dao.ChatRoomParticipantsDAO;
import com.goindol.teamtalk.client.dao.FriendDAO;
import com.goindol.teamtalk.client.dao.UserDAO;
import com.goindol.teamtalk.client.dto.UserDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatRoomInfoController implements Initializable {

    public UserDTO userDTO;
    public UserDAO userDAO = UserDAO.getInstance();
    public int chatId;
    public MainController mainController;
    public ChatRoomDAO chatRoomDAO = ChatRoomDAO.getInstance();
    public FriendDAO friendDAO = FriendDAO.getInstance();
    public ChatRoomParticipantsDAO chatRoomParticipantsDAO = ChatRoomParticipantsDAO.getInstance();
    @FXML private ListView chatRoomParticipants;
    @FXML private TextField userInput;
    @FXML private Button invite;
    @FXML private Button exitRoom;


    public void showChatRoomParticipants() {
       List<String> strings = new ArrayList<>();
        ArrayList<String> chatRoomParticipant = chatRoomParticipantsDAO.getChatRoomParticipants(chatId);
        for(String users : chatRoomParticipant) {
            strings.add(users);
        }
        ObservableList<String> chatRoomObservableUserList = FXCollections.observableList(strings);

        chatRoomParticipants.setItems(chatRoomObservableUserList);
    }

    public void inviteFriend() {
        System.out.println("chatId = " + chatId);
        if(chatRoomParticipantsDAO.isParticipants(chatId,userInput.getText())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("warning");
            alert.setHeaderText("채팅방 친구 오류");
            alert.setContentText("이미 채팅방에 존재하는 친구입니다.");
            alert.show();
        }else {
            if(friendDAO.isFriend(userDTO.getNickName(),userInput.getText())) {
                chatRoomDAO.inviteChatRoom(chatId, userInput.getText());
                ObservableList<String> chatRoomParticipantsItems = chatRoomParticipants.getItems();
                chatRoomParticipantsItems.add(userInput.getText());
                chatRoomParticipants.setItems(chatRoomParticipantsItems);
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("warning");
                alert.setHeaderText("친구 오류");
                alert.setContentText("친구 목록에 존재하지 않습니다.");
                alert.show();
            }
        }
    }

    public void exitRoom() {
        chatRoomParticipantsDAO.exitCurrentRoom(chatId, userDTO.getNickName());

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatRoomParticipants.setEditable(false);
        invite.setOnMouseClicked(event-> {
            inviteFriend();
            mainController.send("chatRoom/"+ chatId + "/" + userInput.getText());
            userInput.setText("");
        });

        exitRoom.setOnMouseClicked(event-> {
            exitRoom();
            //TO DO 현재 화면 닫아주기
            try {
                Stage curStage = (Stage) userInput.getScene().getWindow();
                curStage.close();
                Stage parentStage = (Stage) curStage.getOwner();
                parentStage.close();
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/MainView.fxml"));
                Parent root = loader.load();
                MainController main = loader.getController();
                main.setUserDTO(userDTO);
                main.showChatRoomList();
                stage.setScene(new Scene(root, 400, 600));
                stage.setTitle("Team Talk");
                stage.setResizable(false);
                stage.show();
                stage.setOnCloseRequest(event1 -> {
                    userDAO.logout(userDTO.getUserId(), userDTO.getNickName());
                    main.send("login/roomId/value");
                    //System.exit(0);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    public void setChatRoomId(int chatId) {
        this.chatId = chatId;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public void setMainController(MainController mainController) { this.mainController = mainController; }

}
