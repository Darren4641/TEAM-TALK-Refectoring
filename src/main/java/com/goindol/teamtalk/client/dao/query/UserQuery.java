package com.goindol.teamtalk.client.dao.query;

public enum UserQuery {

    SIGNUP("INSERT INTO `db_team`.`user`" +
            "(" +
            "`userId`," +
            "`userPassword`," +
            "`nickName`," +
            "`status`" +
            ")" +
            "VALUES" +
            "(" +
            "?," +
            "?," +
            "?," +
            "?" +
            ")"),
    VALIDATESIGNUP_ID("SELECT " +
                              "`user`.`userId`," +
                              "`user`.`userPassword`," +
                              "`user`.`nickName`," +
                              "`user`.`status`" +
                              "FROM `db_team`.`user` WHERE userId = ? "),
    VALIDATESIGNUP_NICKNAME("SELECT " +
            "`user`.`userId`," +
            "`user`.`userPassword`," +
            "`user`.`nickName`," +
            "`user`.`status`" +
            "FROM `db_team`.`user` WHERE nickName = ?"),
    LOGIN_OVERLAP("SELECT * FROM `db_team`.`user` WHERE userId=?"),
    LOGIN("SELECT " +
            "`user`.`userId`," +
            "`user`.`userPassword`," +
            "`user`.`nickName`," +
            "`user`.`status`" +
            "FROM `db_team`.`user` WHERE userId = ? AND userPassword = ?"),
    LOGIN_STATUS("UPDATE `db_team`.`user`" +
            "SET " +
            "`status` = true " +
            "WHERE `userId` = ?"),
    LOGIN_FRIEND("UPDATE `db_team`.`friendInfo` " +
            "SET " +
            "`friendStatus` = true " +
            "WHERE `friendNickName` = ?"),
    GETUSER("SELECT * FROM user WHERE userId = ? AND userPassword = ?"),
    LOGOUT("UPDATE `db_team`.`user`" +
            "SET" +
            "`status` = false " +
            "WHERE `userId` = ?"),
    LOGOUT_FRIEND("UPDATE `db_team`.`friendInfo` " +
            "SET " +
            "`friendStatus` = false " +
            "WHERE `friendNickName` = ?");

    private String value;

    public String getValue() {
        return value;
    }

    private UserQuery(String value) {
        this.value = value;
    }
}
