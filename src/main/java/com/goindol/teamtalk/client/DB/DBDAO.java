package com.goindol.teamtalk.client.DB;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBDAO {
    private static DBDAO instance = null;

    public static DBDAO getInstance() {
        if (instance == null)
            instance = new DBDAO();

        return instance;
    }
    public static Connection getConnection() throws Exception {
        DataSource dataSource;
        //BasicDataSource ds = new BasicDataSource();
        Connection conn = null;
        Context ctx;

        ctx = new InitialContext();
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql:// 127.0.0.1:3306/db_team?useUnicode=true&serverTimezone=Asia/Seoul", "root", "3249");
        return conn;
    }
}
