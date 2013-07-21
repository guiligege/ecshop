package com.guili.dbtest;

import java.sql.Connection;
import java.sql.DriverManager;


public class ConMysql {
    public ConMysql() {

    }

    private Connection conn = null;
    private String url = "jdbc:sqlserver://192.168.0.103:1433;databaseName=ecshop";
    private String user = "sa";

    public Connection getconn() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            conn = DriverManager.getConnection(url, user, "123456");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(String[] args){
    	ConMysql conMysql=new ConMysql();
    	conMysql.getconn();
    	System.out.println("success!");
    }
}
