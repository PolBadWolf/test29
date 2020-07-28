package org.example.test29;

import java.sql.*;

public class MainClass {

    public static void main(String[] args) {
        new MainClass().start();
    }

    private void start() {
        System.out.println("start programm");

        try {
            String instanceName = "127.0.0.1\\SQLEX";
            String databaseName = "spc1";
            String userName = "max";
            String pass = "1122";
            String connectionUrl = "jdbc:sqlserver://%1$s;databaseName=%2$s;user=%3$s;password=%4$s;";
            String connectionString = String.format(connectionUrl, instanceName, databaseName, userName, pass);

            Connection connection = DriverManager.getConnection(connectionString);

            Statement stmt = connection.createStatement();

            ResultSet executeQuery = stmt.executeQuery("SELECT * FROM spc1.dbo.Table_1");

            while (executeQuery.next()) {
                int id_spec = executeQuery.getInt("id_spec");
                int ves = executeQuery.getInt("ves");
                byte[] dis = executeQuery.getBytes("dis");
                System.out.println(id_spec + "\t" + ves);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
