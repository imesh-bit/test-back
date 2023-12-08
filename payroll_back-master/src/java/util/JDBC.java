/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Nimesha
 */
public class JDBC {

//    static String IpAddress = "payroll.c9adroeuc57q.us-east-1.rds.amazonaws.com";
//    static String user = "admin";
//    static String password = "AWS.rk7891";
    
//    static String IpAddress = "54.251.96.127";
//    static String user = "nimesha";
//    static String password = "Nimesha@123";
    
     static String IpAddress = "127.0.0.1:3306";
    static String user = "root";
    static String password = "";

    public static Connection con_payroll() throws Exception {
        String url;
        url = "jdbc:mysql://" + IpAddress + "/payroll?zeroDateTimeBehavior=convertToNull&noAccessToProcedureBodies=true&autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection con = DriverManager.getConnection(url, user, password);
        return con;
    }

}
