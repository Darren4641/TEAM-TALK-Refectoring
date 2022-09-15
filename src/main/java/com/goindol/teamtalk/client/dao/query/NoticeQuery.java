package com.goindol.teamtalk.client.dao.query;

public enum NoticeQuery {

    CREATENOTICE("INSERT INTO `db_team`.`notice`" +
            "(" +
            "`nickName`," +
            "`chatRoom_id`," +
            "`title`," +
            "`content`" +
            ")" +
            "VALUES" +
            "(" +
            "?," +
            "?," +
            "?," +
            "?" +
            ")"),
    NOTICE_INIT("UPDATE `db_team`.`chatRoomParticipants`" +
            "SET" +
            "`isNoticeRead` = 1 " +
            "WHERE `chatRoom_id` = ? "),
    NOTICE_INIT_WRITER("UPDATE `db_team`.`chatRoomParticipants`" +
            "SET `isNoticeRead`=2 " +
            "WHERE `nickName`=? and chatRoom_id=?"),
    DELETE_NOTICE("DELETE FROM `db_team`.`notice` WHERE chatRoom_id =?"),
    DELETE_UPDATE("UPDATE `db_team`.`chatRoomParticipants`" +
            "SET `isNoticeRead`= 1 " +
            "WHERE chatRoom_id=?"),
    ISNOTICE("SELECT * FROM `db_team`.`notice` WHERE chatRoom_id=?"),
    GETNOTICE("SELECT * FROM `db_team`.`notice` WHERE chatRoom_id=?"),
    NOTICE_READ("UPDATE `db_team`.`chatRoomParticipants`" +
            "SET `isNoticeRead`=2 " +
            "WHERE `nickName`=? and chatRoom_id=?"),
    NOTICE_NON_READ("SELECT * FROM `db_team`.`chatRoomParticipants` WHERE chatRoom_id =? and isNoticeRead=?"),
    NOTICE_READ_COUNT("SELECT count(*) as count from `db_team`.`chatRoomParticipants` where chatRoom_id=?"),
    NOTICE_READ_ALL_COUNT("SELECT count(*) as count from `db_team`.`chatRoomParticipants` where isNoticeRead=? and chatRoom_id=?");


    private String value;

    public String getValue() {
        return value;
    }

    private NoticeQuery(String value) {
        this.value = value;
    }
}
