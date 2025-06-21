package dao.client;

import java.sql.*;

public class JDBCUtil {
    private static final String servername = "localhost";
    private static final int port = 3306;
    private static final String databaseName = "handmades";
    private static final String username = "root";
    private static final String password = "6723";


    public static Connection getConnection() {
        Connection c = null;

        try {
            // Đăng ký Driver cho MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Tạo URL kết nối
            String url = "jdbc:mysql://" + servername + ":" + port + "/" + databaseName + "?useSSL=false";
            // Tạo kết nối
            c = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Kết nối thất bại");
        }

        return c;
    }

    public static void closeConnection(Connection c) {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Connection c = getConnection();
        System.out.println(c);

    }

}