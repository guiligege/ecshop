package com.guili.dbtest;

import java.sql.Connection;
import java.sql.DriverManager;


public class ConMysql {
    public ConMysql() {

    }

    private Connection conn = null;
    private String url = "jdbc:mysql://127.0.0.1:3306/ecshop";
    private String user = "root";

    public Connection getconn() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
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
