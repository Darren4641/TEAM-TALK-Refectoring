
create database db_team;


use db_team;

CREATE TABLE `user` (
  `userId` varchar(100) NOT NULL COMMENT '회원 아이디',
  `userPassword` varchar(100) NOT NULL COMMENT '회원 비밀번호',
  `nickName` varchar(100) NOT NULL COMMENT '닉네임',
  `status` tinyint(1) DEFAULT NULL COMMENT '접속 상태',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `friendinfo` (
  `f_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '친구목록 index',
  `nickName` varchar(100) NOT NULL COMMENT '닉네임',
  `friendNickName` varchar(100) NOT NULL COMMENT '해당 회원 닉네임의 친구 닉네임',
  `friendStatus` tinyint(1) NOT NULL COMMENT '친구 접속 상태',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `chatRoom` (
  `chatRoom_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '채팅방 번호',
  `chatRoomName` varchar(100) NOT NULL COMMENT '채팅방 이름',
  `nickName` varchar(100) NOT NULL COMMENT '닉네임',
  `isNoticeRead` int(11) NOT NULL default 0 COMMENT '읽음여부',
  `isVoted` int(11) NOT NULL default 0 COMMENT '투표 여부',
  PRIMARY KEY (`chatRoom_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `chatRoomParticipants` (
  `chatRoomParticipants_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '해당 채팅방 회원목록',
  `chatRoom_id` int(11) NOT NULL COMMENT '채팅방 번호',
  `nickName` varchar(100) NOT NULL COMMENT '닉네임',
  `isNoticeRead` int(11) NOT NULL COMMENT '공지 읾음 유무',
  `isVoted` int(11) NOT NULL COMMENT '투표 여부',
  PRIMARY KEY (`chatRoomParticipants_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `chatlog` (
  `chatLog_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '채팅 로그 일렬번호',
  `chatRoom_id` int(11) NOT NULL COMMENT '채팅방 일렬번호',
  `content` text NOT NULL COMMENT '채팅 내용',
  `regDate` varchar(100) NOT NULL COMMENT '채팅 시간',
  PRIMARY KEY (`chatLog_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `notice` (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '공지 일렬번호',
  `nickName` varchar(100) NOT NULL COMMENT '사용자 닉네임',
  `chatRoom_id` int(11) NOT NULL COMMENT '채팅방 일렬번호',
  `title` varchar(30) NOT NULL COMMENT '공지 제목',
  `content` text COMMENT '공지 내용',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `vote` (
  `vote_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '투표 일렬번호',
  `chatRoom_id` int(11) NOT NULL COMMENT '채팅방 일렬번호',
  `title` varchar(30) NOT NULL COMMENT '투표 제목',
  `isAnonymous` tinyint(1) NOT NULL DEFAULT '0' COMMENT '익명 여부',
  `isOverLap` tinyint(1) NOT NULL DEFAULT '0' COMMENT '중복 투표 여부',
  PRIMARY KEY (`vote_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `votevar` (
  `voteVar_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '투표 항목 일렬번호',
  `vote_id` int(11) NOT NULL COMMENT '투표 일렬번호',
  `content` text COMMENT '투표 항목',
  PRIMARY KEY (`voteVar_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `voteresult` (
  `voteResult_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '투표 결과 일렬번호',
  `vote_id` int(11) NOT NULL COMMENT '투표 일렬번호',
  `content` text COMMENT '투표 항목',
  `nickName` varchar(100) NOT NULL COMMENT '회원 아이디',
  PRIMARY KEY (`voteResult_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;






