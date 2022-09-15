package com.goindol.teamtalk.client.controller;

import com.goindol.teamtalk.Main;
import com.goindol.teamtalk.client.dao.VoteDAO;
import com.goindol.teamtalk.client.dto.UserDTO;
import com.goindol.teamtalk.client.dto.VoteDTO;
import com.goindol.teamtalk.client.dto.VoteResultDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ShowVoteResultController implements Initializable {

    @FXML
    public BorderPane borderPane;

    @FXML
    public ListView voteResultList;
    @FXML
    public Label voteTitle;
    private ToggleGroup group = new ToggleGroup();

    public int chatid;
    public VoteDTO voteDTO;
    public VoteDAO voteDAO = VoteDAO.getInstance();
    public UserDTO userDTO;
    public boolean anonymous;

    public void initialVoteList() {
        //TODO DB에서 해당 채팅방 투표의 투표 항목 불러오기
        voteTitle.setText("< " + voteDTO.getTitle() + " > " + "투표 현황");
        anonymous = voteDAO.findVoteByVoteId(voteDTO.getVote_id(), chatid).isAnonoymous();
        ObservableList names = FXCollections.observableArrayList();
        List<VoteResultDTO> voteVarDTOList = voteDAO.showVoteList(voteDTO.getVote_id());
        for(VoteResultDTO voteResult : voteVarDTOList) {
            names.add(voteResult);
        }

        voteResultList.setItems(names);
        //voteDAO.checkAnnony(voteId);  <-- 이용해서 익명 여부 확인 후 닉네임 안띄워주기
        voteResultList.setCellFactory(param -> new listCell());

        //투표결과닉네임
//        List<String> resultNickname = voteDAO.ShowVoteList(vote_id);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    public void setChatRoomId(int chatid) {
        this.chatid = chatid;
    }
    public void setVote(VoteDTO vote) { this.voteDTO = vote; }
    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    private class listCell extends ListCell<VoteResultDTO> {
        HBox hbox = new HBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();
        Button button = new Button("투표자 현황");
        String lastItem;

        public listCell() {
            super();
            hbox.getChildren().addAll(label, pane, button);
            HBox.setHgrow(pane, Priority.ALWAYS);
            button.setPrefWidth(120);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (!anonymous) {
                        try {
                            Stage curStage = (Stage) borderPane.getScene().getWindow();
                            Stage stage = new Stage();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Main.class.getResource("views/VoterListView.fxml"));
                            Parent root = (Parent) loader.load();
                            VoterListController voterListController = (VoterListController) loader.getController();

                            voterListController.setVote(voteDTO);
                            voterListController.setVoteVarContent(lastItem);
                            voterListController.showVoterList();
                            stage.setScene(new Scene(root, 200, 250));
                            stage.setTitle("Team Talk");
                            stage.setOnCloseRequest(event -> stage.close());
                            stage.setResizable(false);
                            stage.setX(curStage.getX() + 400);
                            stage.setY(curStage.getY());
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }

        @Override
        public void updateItem(VoteResultDTO obj, boolean empty) {
            super.updateItem(obj, empty);
            if (empty) {
                lastItem = null;
                setGraphic(null);
            } else {
                if(anonymous) {
                    ImageCursor imageCursor = new ImageCursor(new Image(Main.class.getResourceAsStream("images/x.png")));
                    button.setCursor(imageCursor);
                }
                else
                    button.setCursor(Cursor.HAND);
                lastItem=obj.getContent();
                button.setText("투표자 현황 "+"(" + String.valueOf(obj.getCount()) + " 명)");
                label.setText(obj.getContent()!=null ? obj.getContent() : "<null>");
                setGraphic(hbox);
            }
        }
    }
}
