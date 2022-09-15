package com.goindol.teamtalk.client.controller;

import com.goindol.teamtalk.client.dao.VoteDAO;
import com.goindol.teamtalk.client.dto.UserDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MakeVoteController implements Initializable {

    @FXML public BorderPane borderPane;
    @FXML public TextField voteTitle;
    @FXML public TextField voteVar;
    @FXML public ListView voteVarList;
    @FXML public CheckBox isAnonoymous;
    @FXML public CheckBox isOverLap;
    @FXML public Button addVoteVarbutton;
    @FXML public Button addVoteButton;

    private static VoteDAO voteDAO = VoteDAO.getInstance();
    boolean annony = false;
    boolean overLap = false;
    public ObservableList voteVarItems = FXCollections.observableArrayList();
    public List<String> _voteVarList = new ArrayList<>();

    public int chatid;
    public UserDTO userDTO;
    public MainController mainController;

    public void addVoteVar() {
        if(!voteVar.getText().isBlank()) {
            _voteVarList.add(voteVar.getText());
            voteVarItems.add(voteVar.getText());
            voteVarList.setItems(voteVarItems);
            voteVar.clear();
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("warning");
            alert.setHeaderText("투표 오류");
            alert.setContentText("투표 항목을 입력해주세요.");
            alert.show();
        }
    }

    public void addVote() {
        System.out.println(chatid);
        if(!voteTitle.getText().isBlank() && !_voteVarList.isEmpty()) {
            int vote_id = voteDAO.getVoteId(chatid);
            if(voteDAO.checkVote(chatid)) {
                voteDAO.deleteVote(vote_id);
            }
            voteDAO.creatVote(chatid, voteTitle.getText(), annony, overLap);
            int voteId = voteDAO.getVoteId(chatid);
            for (String content : _voteVarList) {
                voteDAO.createVoteVar(content, voteId);
                Stage stage = (Stage) borderPane.getScene().getWindow();
                stage.close();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("warning");
            alert.setHeaderText("투표 오류");
            alert.setContentText("입력하지 않은 투표 제목이나 투표 항목이 있습니다.");
            alert.show();
        }
    }

    @FXML
    void selectAnonoymousCheckBox(ActionEvent event) {
        if(isAnonoymous.isSelected()) {
            annony = true;
        }else {
            annony = false;
        }
    }

    @FXML
    void selectOverLapCheckBox(ActionEvent event) {
        if(isOverLap.isSelected()) {
            overLap = true;
        }else {
            overLap = false;
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /*voteTitle.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                voteTitle.setText(keyEvent.getText());
            }
        });

        voteVar.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                voteVar.setText(keyEvent.getText());
            }
        });

        voteVar.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    addVoteVar();
                }
            }
        });*/

        addVoteVarbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                addVoteVar();
            }
        });

        addVoteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                addVote();
                mainController.send("vote/"+ chatid + "/" + voteTitle.getText());

            }
        });


    }

    public void setChatRoomId(int chatid) {
        this.chatid = chatid;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public void setMainController(MainController mainController) { this.mainController = mainController; }
}
