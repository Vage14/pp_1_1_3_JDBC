package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {

    private  static  final  String DB_URL = "jdbc:mysql://127.0.0.1:3306/pp_1_1_3bd";
    private  static  final  String DB_USERNAME = "root";
    private  static  final  String DB_PASSWORD = "v14ag93Kh";

    public static Connection getConnection() {
        Connection connection = null ;
        try {
            connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
            System.out.println("Connection OK");
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к БД");
        }
        return connection;
    }

}
