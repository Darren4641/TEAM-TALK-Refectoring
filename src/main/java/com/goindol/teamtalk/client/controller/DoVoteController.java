package com.goindol.teamtalk.client.controller;

import com.goindol.teamtalk.client.dao.VoteDAO;
import com.goindol.teamtalk.client.dto.UserDTO;
import com.goindol.teamtalk.client.dto.VoteDTO;
import com.goindol.teamtalk.client.dto.VoteVarDTO;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class DoVoteController implements Initializable {

    @FXML
    public BorderPane borderPane;
    @FXML
    public ListView voteList;
    @FXML
    public Button voteButton;
    @FXML
    public Label voteTitle;
    private ToggleGroup group = new ToggleGroup();
    public VoteDAO voteDAO = VoteDAO.getInstance();

    public int chatid;
    public int voteVarId;
    public boolean isOverLap;
    List<String> tempList = new ArrayList<String>();
    public UserDTO userDTO;
    public VoteDTO voteDTO;

    public void saveVoteResult() {
        if (isOverLap) {
            for (String temp : tempList) {
                voteDAO.choiceVote(voteDTO.getVote_id(), chatid, temp, userDTO.getNickName());
                Stage stage = (Stage) borderPane.getScene().getWindow();
                stage.close();
            }
        } else {
            String temp = group.getSelectedToggle().toString();
            String value = temp.substring(temp.indexOf("'") + 1, temp.length() - 1);
            voteDAO.choiceVote(voteDTO.getVote_id(), chatid, value, userDTO.getNickName());
        }

        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
    }

    public void initialVoteList() {
        //TODO DB에서 해당 채팅방 투표의 투표 항목 불러오기
        isOverLap = voteDTO.isOverLap();
        voteTitle.setText("< " + voteDTO.getTitle() + " > " + "투표 하기");
        ObservableList names = FXCollections.observableArrayList();
        List<VoteVarDTO> voteVarDTOList = voteDAO.readVoteVar(voteDTO.getVote_id());
        if (voteVarDTOList != null) {
            for (VoteVarDTO voteVar : voteVarDTOList) {
                voteVarId = voteVar.getVoteVar_id();
                names.add(voteVar);
            }
            voteList.setItems(names);
            voteList.setCellFactory(param -> new RadioListCell());
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        voteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                saveVoteResult();

            }
        });
    }

    public void setChatRoomId(int chatid) {
        this.chatid = chatid;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public void setVote(VoteDTO voteDTO) {
        this.voteDTO = voteDTO;
    }

    private class RadioListCell extends ListCell<VoteVarDTO> {
        @Override
        public void updateItem(VoteVarDTO obj, boolean empty) {
            super.updateItem(obj, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isOverLap) {
                    CheckBox checkBox = new CheckBox(obj.getContent());
                    checkBox.setText(obj.getContent());
                    checkBox.selectedProperty().addListener(new InvalidationListener() {
                        @Override
                        public void invalidated(Observable observable) {
                            if (checkBox.isSelected()) {
                                tempList.add(obj.getContent());
                            } else {
                                tempList.remove(obj.getContent());
                            }

                        }
                    });
                    setGraphic(checkBox);
                } else {
                    RadioButton radioButton = new RadioButton(obj.getContent());
                    radioButton.setToggleGroup(group);
                    setGraphic(radioButton);
                }
            }
        }
    }
}
