package com.goindol.teamtalk.client.dao.query;

public enum FriendQuery {

    ISFRIEND("SELECT * FROM db_team.friendInfo WHERE nickName = ? AND friendNickName = ?"),
    FRIEND_VALIDATE("SELECT * FROM db_team.user WHERE nickName = ?"),
    FRIEND_ADD("INSERT INTO `db_team`.`friendInfo`" +
            "(" +
            "`nickName`," +
            "`friendNickName`," +
            "`friendStatus` " +
            ")" +
            "VALUES" +
            "(" +
            "?, " +
            "?, " +
            "? " +
            ")"),
    GETFRIEND("SELECT * FROM `db_team`.`friendInfo` WHERE friendNickName = ? and nickName = ?"),
    GETFRIEND_LIST("SELECT `friendInfo`.`f_id`," +
            "`friendInfo`.`nickName`," +
            "`friendInfo`.`friendNickName`," +
            "`friendInfo`.`friendStatus`" +
            "FROM `db_team`.`friendInfo` WHERE `friendInfo`.`nickName` = ?"),
    GETFRIEND_NICKNAME_LIST("SELECT `friendInfo`.`f_id`," +
            "`friendInfo`.`nickName`," +
            "`friendInfo`.`friendNickName`," +
            "`friendInfo`.`friendStatus`" +
            "FROM `db_team`.`friendInfo` WHERE `friendInfo`.`nickName` = ?"),
    ISBOTHFRIEND("select * from friendInfo where nickName = ? and friendNickName=?");

    private String value;

    public String getValue() {
        return value;
    }

    private FriendQuery(String value) {
        this.value = value;
    }
}
