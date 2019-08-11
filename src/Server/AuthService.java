package Server;

import java.sql.*;

public class AuthService
{
    private static Connection connection;
    private static Statement stmt;

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
