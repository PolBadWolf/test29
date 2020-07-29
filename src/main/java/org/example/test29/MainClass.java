package org.example.test29;

import java.sql.*;
import java.util.ArrayList;

public class MainClass {

    public static void main(String[] args) {
        new MainClass().start();
    }

    private void start() {
        System.out.println("start programm");

        try {
//            String instanceName = "127.0.0.1\\SQLEX";
            String instanceName = "127.0.0.1\\SQLEX:1433";
            String databaseName = "spc1";
            String userName = "max";
            String pass = "1122";
            String connectionUrl = "jdbc:sqlserver://%1$s;databaseName=%2$s;user=%3$s;password=%4$s;";
            String connectionString = String.format(connectionUrl, instanceName, databaseName, userName, pass);

            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Connection connection = DriverManager.getConnection(connectionString);

            Statement stmt = connection.createStatement();

            ResultSet executeQuery = stmt.executeQuery("SELECT * FROM spc1.dbo.Table_1");

            while (executeQuery.next()) {
                int id_spec = executeQuery.getInt("id_spec");
                int ves = executeQuery.getInt("ves");
                byte[] dis = executeQuery.getBytes("dis");
                System.out.println(id_spec + "\t" + ves);
            }

            executeQuery.close();
            stmt.close();

            PreparedStatement statement = connection.prepareStatement("INSERT INTO Table_1(id_spec, ves, dis) VALUES (?, ?, ?)");
            statement.setInt(1, 2);
            statement.setInt(2, 223);

            ArrayList<Ddt> tt = new ArrayList<>();
            for (int i = 16_380; i < 16_384; i++) {
                tt.add(new Ddt(i, i));
            }

            byte[] q = Ddt.toBytes( tt.toArray(new Ddt[tt.size()]));
            q = null;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
