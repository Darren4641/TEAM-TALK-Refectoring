package com.goindol.teamtalk.client.dao.query;

public enum ChatLogQuery {

    WRITE_LOG("INSERT INTO `db_team`.`chatLog`" +
            "(" +
            "`chatRoom_id`," +
            "`content`," +
            "`regDate`" +
            ")" +
            "VALUES" +
            "(" +
            "?," +
            "?," +
            "?" +
            ")"),
    GETCHATLOG("SELECT * FROM db_team.chatLog WHERE chatRoom_id = ? order by regDate asc");

    private String value;

    public String getValue() {
        return value;
    }

    private ChatLogQuery(String value) {
        this.value = value;
    }
}
