package com.goindol.teamtalk.client.dao.query;

public enum ChatRoomParticipantsQuery {

    ISPARTICIPANTS("SELECT * FROM db_team.chatRoomParticipants WHERE chatRoom_id=? and nickName=?"),
    GETPARTICIPANTS("SELECT nickName FROM db_team.chatRoomParticipants where chatRoom_id = ?"),
    EXIT_CURRENT_ROOM("DELETE FROM `db_team`.`chatRoomParticipants` " +
            "WHERE chatRoom_id = ? and nickName = ?");
    private String value;

    public String getValue() {
        return value;
    }

    private ChatRoomParticipantsQuery(String value) {
        this.value = value;
    }

}
