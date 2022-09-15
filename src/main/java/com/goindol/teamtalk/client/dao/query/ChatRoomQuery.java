package com.goindol.teamtalk.client.dao.query;

public enum ChatRoomQuery {

    CREATE_CHATROOM("INSERT INTO `db_team`.`chatRoom` (`chatRoomName`, `nickName`) VALUES ( ?, ? ) "),
    GETCHATROOMID("SELECT " +
            "`chatRoom`.`chatRoom_id`" +
            "FROM `db_team`.`chatRoom` WHERE chatRoomName = ? and nickName = ?"),
    INVITE_CHATROOM("INSERT INTO `db_team`.`chatRoomParticipants`" +
            "(" +
            "`chatRoom_id`," +
            "`nickName`," +
            "`isNoticeRead`," +
            "`isVoted`" +
            ")" +
            "VALUES" +
            "(" +
            "?," +
            "?," +
            "?," +
            "?" +
            ")"),
    NOTICE_INIT("SELECT * FROM db_team.notice WHERE chatRoom_id = ?"),
    VOTE_INIT("SELECT * FROM vote WHERE chatRoom_id = ?"),
    GETVOTEID("select vote_id from db_team.vote where chatRoom_id = ?"),
    VOTECHECK("SELECT * FROM db_team.voteResult WHERE nickName = ? and vote_id = ?"),
    GETCURRENT_CHATROOM_NAME("SELECT chatRoomName FROM db_team.chatRoom WHERE chatRoom_id = ?"),
    GETCHATROOMNAME_LIST("select p.chatRoom_id, " +
            "   p.chatRoomName, " +
            "   q.isNoticeRead, " +
            "       q.isVoted "  +
            "from chatRoom as p " +
            "join chatRoomParticipants as q " +
            "on p.chatRoom_id = q.chatRoom_id " +
            "where q.nickName = ?");

    private String value;

    public String getValue() {
        return value;
    }

    private ChatRoomQuery(String value) {
        this.value = value;
    }
}
