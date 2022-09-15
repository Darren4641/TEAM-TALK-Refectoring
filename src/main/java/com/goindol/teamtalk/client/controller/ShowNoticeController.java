package com.goindol.teamtalk.client.controller;

import com.goindol.teamtalk.client.dao.NoticeDAO;
import com.goindol.teamtalk.client.dto.NoticeDTO;
import com.goindol.teamtalk.client.dto.UserDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ShowNoticeController implements Initializable {
    @FXML private BorderPane noticeCheckContainer;
    @FXML private Label noticeTitle;
    @FXML private TextArea noticeContent;

    @FXML private ListView readUserList;
    public int chatid;
    public UserDTO userDTO;
    public NoticeDTO noticeDTO;


    private static NoticeDAO noticeDAO = NoticeDAO.getInstance();

    public void showReadUser() {
        List<String> strings = new ArrayList<String>();
        ArrayList<String> readUser = noticeDAO.readNoticeUserList(chatid);
        if(readUser != null) {
            for(String users : readUser) {
                strings.add(users);
            }
        }
        ObservableList<String> readUserObservableList = FXCollections.observableArrayList();

        readUserObservableList.addAll(strings);

        Platform.runLater(()->{
            readUserList.setItems(readUserObservableList);
        });

    }

    public boolean showNoticeContent(){
//        만약 공지사항이 있다면 checkNotice에서 true값이 나옴
         if(noticeDAO.hasNotice(chatid)) {
             noticeDTO = noticeDAO.showNoticeContent(chatid, userDTO.getNickName());
             noticeTitle.setText("<" + noticeDTO.getTitle() + ">");
             noticeContent.setText(noticeDTO.getContent());
             return true;
         }else {
           return false;
         }
//        else  {}
//        공지 제목, 내용
//        noticeDAO.showNotice(chatid);
//        공지 눌렀을 때 그 사람 이름 체킹
//        noticeDAO.readNotice(userDTO.getNickName(), chatid);
//        공지 읽은 유저 리스트들
//        List<String> users = noticeDAO.readNoticeList(chatid);

//        Text t1 = new Text("공지내용");
//
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        noticeContent.setEditable(false);
    }

    public void setChatRoomId(int chatid) {
        this.chatid = chatid;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
