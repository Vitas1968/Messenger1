package Server;

import java.sql.*;

public class AuthService
{
    private static Connection connection;
    private static Statement stmt;

    //установка соединения с БД
    public static void connect() throws SQLException
    {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

//        public static String setNewUsers(String login, String pass) {
//        int hash = pass.hashCode();
//        String sql = String.format("INSERT INTO main (id, login, pass, nick) VALUES (1,'log1', ) FROM main where login = '%s' and password = '%s'", login, hash);
//
//        try {
//            ResultSet rs = stmt.executeQuery(sql);
//
//            if (rs.next()) {
//                String str = rs.getString(1);
//                return rs.getString(1);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    // получение из БД ника по логину и паролю
    public static String getNickByLoginAndPass(String login, String pass) throws SQLException {

        int passInt = pass.hashCode();
        String qry = String.format("SELECT nickname FROM main where login = '%s' and password = '%d'", login, passInt);
        ResultSet rs = stmt.executeQuery(qry);

        if (rs.next()) {
            return rs.getString(1);
        }

        return null;
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
