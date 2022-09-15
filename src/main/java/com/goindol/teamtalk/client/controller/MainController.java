package com.goindol.teamtalk.client.controller;

import com.goindol.teamtalk.Main;
import com.goindol.teamtalk.client.dao.ChatRoomDAO;
import com.goindol.teamtalk.client.dao.FriendDAO;
import com.goindol.teamtalk.client.dao.UserDAO;
import com.goindol.teamtalk.client.dto.ChatRoomDTO;
import com.goindol.teamtalk.client.dto.FriendDTO;
import com.goindol.teamtalk.client.dto.UserDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    PrintWriter out;
    BufferedReader in;
    Socket socket;
    String IP = "192.168.0.125";
    int port = 9600;
    @FXML public StackPane stackPane;
    @FXML public AnchorPane chatAnchor;
    @FXML public AnchorPane friendAnchor;
    @FXML public TabPane tabContainer;
    @FXML public Tab chatTab;
    @FXML public Tab friendTab;
    @FXML public Tab logoutTab;
    @FXML public ListView chatRoomList;
    @FXML public ListView friendList;
    @FXML public TextField searchFriend;
    @FXML public Button addFriendButton;
    @FXML public ImageView makeChatRoomButton;
    @FXML public ImageView chatRoomListTabImage;
    @FXML public ImageView friendListTabImage;
    @FXML public ImageView logoutTabImage;
    DropShadow dropShadow = new DropShadow();

    ChatRoomDAO chatRoomDAO = ChatRoomDAO.getInstance();
    FriendDAO friendDAO = FriendDAO.getInstance();
    UserDAO userDAO = UserDAO.getInstance();
    public UserDTO userDTO;

    public void startClient(String IP, int port) {

        Thread thread = new Thread() {
            public void run() {
                synchronized (this) {
                    try {
                        socket = new Socket(IP, port);
                        send(userDTO.getNickName());
                        receive();
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (!socket.isClosed()) {
                            stopClient();
                            System.out.println("Server Failed");
                        }

                    }
                }
            }
        };
        thread.start();
    }

    public void stopClient() {
        try {
            if(socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receive() {

        while(true) {
            try {
                InputStream in = socket.getInputStream();
                System.out.println("receiving");
                byte[] buffer = new byte[512];
                int length = in.read(buffer);
                String message = new String(buffer, 0, length, "UTF-8");
                if(message.equals("login")) {
                    showFriendList();
                }else if(message.equals("notice") || message.equals("vote") || message.equals("chatRoom")) {
                    showChatRoomList();
                }
            }catch(Exception e) {
                stopClient();
                break;
            }
        }
    }

    public void send(String message) {
        Thread thread = new Thread() {
            public void run() {
                try {

                    OutputStream out = socket.getOutputStream();
                    byte[] buffer = message.getBytes("UTF-8");
                    out.write(buffer);
                    out.flush();
                }catch(Exception e) {
                    stopClient();
                }
            }
        };
        thread.start();
    }

    public void showFriendList(){
        List<FriendDTO> strings = new ArrayList<>();

        if(userDTO != null) {
            ArrayList<FriendDTO> friends = friendDAO.getFriendList(userDTO.getNickName());
            if(friends != null) {
                for(int i = 0; i < friends.size(); i++) {
                    strings.add(friends.get(i));
                }
            }
        }
        ObservableList<FriendDTO> friendObervableList = FXCollections.observableList(strings);
        Platform.runLater(()->{
            friendList.setItems(friendObervableList);
            friendList.setCellFactory(param -> new colorListCell());

        });
    }

    public void showChatRoomList(){
        List<ChatRoomDTO> strings = new ArrayList<>();
        if(userDTO != null) {
            ArrayList<ChatRoomDTO> chatRoom = chatRoomDAO.getChatRoomNameList(userDTO.getNickName());
            if(chatRoom != null) {
                for(int i = 0; i < chatRoom.size(); i++) {
                    strings.add(chatRoom.get(i));
                }
            }
        }

        ObservableList<ChatRoomDTO> chatRoomObservableList = FXCollections.observableArrayList();

        chatRoomObservableList.addAll(strings);

        Platform.runLater(()->{
            chatRoomList.setItems(chatRoomObservableList);
            chatRoomList.setCellFactory(param -> new chatRoomListCell());
        });
    }

    public void openChatRoom(){
        ChatRoomDTO cr = (ChatRoomDTO) chatRoomList.getSelectionModel().getSelectedItem();
        if(cr==null)
            return;
        try {

            Stage stage = (Stage) stackPane.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/ChatView.fxml"));
            Parent root = (Parent) loader.load();
            ChatController chatController = loader.getController();
            chatController.setuserDTO(userDTO);
            chatController.setChatRoomId(cr.getChatRoom_id());
            chatController.setMainController(this);
            chatController.setChatRoomTitle();
            chatController.initialChat();
            stage.setScene(new Scene(root, 400, 600));
            stage.setTitle("Team Talk");
            stage.setOnCloseRequest(event -> {
                userDAO.logout(userDTO.getUserId(), userDTO.getNickName());
                send("login/roomId/" + userDTO.getNickName());
                System.exit(0);});
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeChatroom(){
        //TODO : DB에 채팅방 저장

        try {
            Stage stage = (Stage) stackPane.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/MakeChatRoomView.fxml"));
            Parent root = (Parent) loader.load();
            MakeChatRoomController chatRoomTitleController = loader.getController();
            chatRoomTitleController.setUserDTO(userDTO);
            stage.setScene(new Scene(root, 400, 600));
            stage.setTitle("Team Talk");
            stage.setOnCloseRequest(event -> {System.exit(0);});
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFriend(){
        if(userDTO.getNickName().equals(searchFriend.getText())) {
            searchFriend.setText("");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("warning");
            alert.setHeaderText("친구 추가 오류");
            alert.setContentText("자기 자신은 친구추가 할 수 없습니다. ");
            alert.show();
        }else {
            int status = friendDAO.addFriend(userDTO.getNickName(), searchFriend.getText());

            if(status == 1) {
                searchFriend.setText("");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("warning");
                alert.setHeaderText("친구 추가 오류");
                alert.setContentText("이미 추가된 사용자입니다. ");
                alert.show();
            }else if(status == 2){
                searchFriend.setText("");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("warning");
                alert.setHeaderText("친구 추가 오류");
                alert.setContentText("존재하지 않은 사용자입니다. ");
                alert.show();
            } else {
                ObservableList<FriendDTO> friendListItems = friendList.getItems();
                FriendDTO friend = friendDAO.getFriend(userDTO.getNickName(), searchFriend.getText());
                friendListItems.add(friend);
                Platform.runLater(()-> {
                    friendList.setItems(friendListItems);
                    friendList.setCellFactory(param -> new colorListCell());
                });
                searchFriend.setText("");
                send("login/roomId/" + userDTO.getNickName());
            }
        }





    }

    public void logOut(){
        userDAO.logout(userDTO.getUserId(), userDTO.getNickName());


        try {
            Stage stage = (Stage) stackPane.getScene().getWindow();
            Parent root = FXMLLoader.load(Main.class.getResource("views/InitialView.fxml"));
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
        logoutTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                logOut();
                send("login/roomId/" + userDTO.getNickName());
            }
        });

        addFriendButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                addFriend();
                send("login/roomId/" + userDTO.getNickName());
            }
        });

        makeChatRoomButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                makeChatroom();
            }
        });
        chatRoomList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                openChatRoom();
            }
        });
        try {
            InetAddress ia = InetAddress.getLocalHost();
            String ip_str = ia.toString();
            String ip = ip_str.substring(ip_str.indexOf("/") + 1);
            startClient(ip, port);
        }catch (IOException e) {
            e.printStackTrace();

        }


    }

    private class chatRoomListCell extends ListCell<ChatRoomDTO> {
        HBox hbox = new HBox();
        Label label1 = new Label("label1");
        Label label2 = new Label("새 공지");
        Label label3 = new Label("새 투표");
        Pane pane = new Pane();
        public chatRoomListCell() {
            super();
            hbox.getChildren().addAll(label1,label2,label3);
            label1.setTextFill(Color.valueOf("#d7d6dc"));
            label1.setPrefWidth(180);
            label2.setTextFill(Color.valueOf("#eba576"));
            label3.setTextFill(Color.valueOf("#eba576"));
            label2.setPrefWidth(65);
            label2.setVisible(false);
            label3.setPrefWidth(65);
            label3.setVisible(false);
            HBox.setHgrow(pane, Priority.ALWAYS);

        }
        @Override
        public void updateItem(ChatRoomDTO obj, boolean empty) {
            super.updateItem(obj, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                label1.setText(obj.getChatRoomName());
                if(obj.isNoticeRead()==1)
                    label2.setVisible(true);
/*                VoteDAO voteDAO = VoteDAO.getInstance();
                int voteid = voteDAO.getVoteId(obj.getChatRoom_id());
                boolean isVoted = voteDAO.checkOverLap(voteid, userDTO.getNickName());*/
                if(obj.isVoted()==1) {
                    label3.setVisible(true);
                }
                setGraphic(hbox);
            }
        }
    }
    private class colorListCell extends ListCell<FriendDTO> {
        @Override
        public void updateItem(FriendDTO obj, boolean empty) {
            super.updateItem(obj, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                Label label = new Label(obj.getFriendNickName());
                //#TODO 친구 온라인일시 Color.GREEN, 오프라인이면 Color.BLACK
                if(obj.isFriendStatus()){
                    label.setTextFill(Color.valueOf("#33ff33"));
                } else {
                    label.setTextFill(Color.valueOf("#d7d6dc"));
                }
                setGraphic(label);
            }
        }
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}