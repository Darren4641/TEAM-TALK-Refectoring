package com.goindol.teamtalk.client.dao.query;

public enum VoteQuery {

    CREATE_VOTE("INSERT INTO `db_team`.`vote` (chatRoom_id,title,isAnonymous,isOverLap) VALUES "+
            "(?,?,?,?)"),
    VOTE_INIT("UPDATE `db_team`.`chatRoomParticipants`" +
            "SET" +
            "`isVoted` = 1 " +
            "WHERE `chatRoom_id` = ? "),
    CREATE_VOTEVAR("INSERT INTO `db_team`.`voteVar` (vote_id,content) VALUES" +
            "(?,?)"),
    GETVOTEID("SELECT vote_id FROM db_team.vote WHERE chatRoom_id = ?"),
    READVOTEVAR("SELECT * FROM `db_team`.`voteVar` WHERE vote_id=?"),
    DELETE_VOTE("DELETE FROM `db_team`.`vote` WHERE vote_id =?"),
    DELETE_VOTEVAR("DELETE FROM `db_team`.`voteVar` WHERE vote_id =?"),
    DELETE_VOTERESULT("DELETE FROM `db_team`.`voteResult` WHERE vote_id =?"),
    CHOICEVOTE("INSERT INTO `db_team`.`voteResult` (vote_id,content,nickName) values (?,?,?)"),
    CHOICEVOTE_UPDATE("UPDATE `db_team`.`chatRoomParticipants`" +
            "SET `isVoted`=2 " +
            "WHERE `nickName`=? and chatRoom_id=?"),
    CHOICEVOTE_VALIDATE("SELECT * FROM `db_team`.`vote` WHERE `vote`.`vote_id` = ?"),
    FINDVOTEBYVOTEID("SELECT * FROM db_team.vote WHERE vote_id=? and chatRoom_id=?"),
    CHECKVOTE("SELECT * FROM db_team.vote WHERE chatRoom_id=?"),
    ISVOTE("SELECT" +
            "`voteResult`.`voteResult_id`," +
            "`voteResult`.`vote_id`," +
            "`voteResult`.`content`," +
            "`voteResult`.`nickName`" +
            "FROM `db_team`.`voteResult` WHERE vote_id = ? and nickName = ?"),
    ISALLVOTED_CHATROOMID("SELECT count(*) as count from `db_team`.`chatRoomParticipants` where chatRoom_id=?"),
    ISALLVOTED("SELECT count(*) as count from `db_team`.`chatRoomParticipants` where isVoted=? and chatRoom_id=?"),
    GETVOTER("SELECT * FROM `db_team`.`voteResult` WHERE vote_id=? and content=?"),
    GETVOTERESULT("select " +
            " p.content, " +
            " count(q.content) as count " +
            " from `db_team`.`voteVar` as p " +
            " left join " +
            " `db_team`.`voteResult` as q " +
            " on p.content = q.content " +
            " where p.vote_id = ? " +
            " group by p.content ");

    private String value;

    public String getValue() {
        return value;
    }

    private VoteQuery(String value) {
        this.value = value;
    }
}
