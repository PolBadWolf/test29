package org.example.test29;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class MainClass {

    public static void main(String[] args) {
        new MainClass().start();
    }

    private void defaultProperties(Properties properties, String fileName) {
        properties.clear();
        properties.setProperty("URL_Server", "127.0.0.1" );
        properties.setProperty("Name_Server", "SQLEX");
        properties.setProperty("Port_Server", "1433");
        properties.setProperty("DataBase", "spc1");
        properties.setProperty("User", "max");
        properties.setProperty("Password", "1122");
        try {
            properties.store(new BufferedWriter(new FileWriter(fileName)), "access base");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void start() {
        System.out.println("start programm");

        Properties properties = new Properties();
        String connectionString;
        Connection connection = null;
        // загрузка
        try {
            properties.load(new BufferedReader(new FileReader("z1.txt")));
        } catch (IOException e) {
            // файл отсутствует
            //defaultProperties(properties, "z1.txt");
        }

        {
            String URL_Server = properties.getProperty("URL_Server");
            String Name_Server = properties.getProperty("Name_Server");
            String Port_Server = properties.getProperty("Port_Server");
            String DataBase = properties.getProperty("DataBase");
            String User = properties.getProperty("User");
            String Password = properties.getProperty("Password");
            if ( (URL_Server == null)
            || (Name_Server == null)
            || (Port_Server == null)
            || (DataBase == null)
            || (User == null)
            || (Password == null)) {
                defaultProperties(properties, "z1.txt");
                URL_Server = properties.getProperty("URL_Server");
                Name_Server = properties.getProperty("Name_Server");
                Port_Server = properties.getProperty("Port_Server");
                DataBase = properties.getProperty("DataBase");
                User = properties.getProperty("User");
                Password = properties.getProperty("Password");
            }
            String connectionUrl = "jdbc:sqlserver://%1$s//%2$s:%3$s;databaseName=%4$s;user=%5$s;password=%6$s;";
            connectionString = String.format(connectionUrl
                    , URL_Server
                    , Name_Server
                    , Port_Server
                    , DataBase
                    , User
                    , Password);
        }

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            System.out.println("ошибка инициализации драйвера");
            System.exit(1);
        }

        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            //ex.printStackTrace();
            System.out.println("ошибка подключения к серверу");
            System.exit(1);
        }
        Statement stmt;
        ResultSet executeQuery;
        PreparedStatement statement;
        try {
            stmt = connection.createStatement();
            executeQuery = stmt.executeQuery("SELECT * FROM spc1.dbo.Table_1");

            while (executeQuery.next()) {
                int id_spec = executeQuery.getInt("id_spec");
                int ves = executeQuery.getInt("ves");
                Blob blob = executeQuery.getBlob("dis");

                System.out.print(id_spec + "\t" + ves);
                if (blob != null) {
                    System.out.print("\tdis len = " + blob.length());
                }
                System.out.println();
            }

            executeQuery.close();
            stmt.close();

            ArrayList<Ddt> tt = new ArrayList<>();
            for (int i = 1; i < 200_001; i++) {
                tt.add(new Ddt(i, i));
            }
            byte[] q = Ddt.toBytes( tt.toArray(new Ddt[tt.size()]));
            Blob blob = new MyBlob(q);

            statement = connection.prepareStatement("INSERT INTO Table_1(id_spec, ves, dis) VALUES (?, ?, ?)");
            statement.setInt(1, 66);
            statement.setInt(2, 77);
            statement.setBlob(3, blob);

            statement.executeUpdate();
            statement.close();
        }
        catch (java.lang.Throwable e) {
            e.printStackTrace();
        }

    }

}
