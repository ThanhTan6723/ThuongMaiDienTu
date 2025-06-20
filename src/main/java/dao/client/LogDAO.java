package dao.client;

import model.Log;
import model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LogDAO {
    static Connection connection = JDBCUtil.getConnection();

    public static List<Log> getLogsByActionType(String actionType) {
        List<Log> logs = new ArrayList<>();
        String query = "SELECT log_id, times_tamp, log_level, module, action_type, user_id, log_content, source_ip, user_agent, affected_table, beforeData, afterData, national " +
                "FROM Log WHERE action_type = ? ORDER BY times_tamp DESC";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, actionType);
            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("log_id");
                    String timesTamp = rs.getString("times_tamp");
                    String logLevel = rs.getString("log_level");
                    String module = rs.getString("module");
                    String action_type = rs.getString("action_type");
                    int userId = rs.getInt("user_id");
                    String logContent = rs.getString("log_content");
                    String sourceIP = rs.getString("source_ip");
                    String userAgent = rs.getString("user_agent");
                    String affectedTable = rs.getString("affected_table");
                    String beforeData = rs.getString("beforeData");
                    String afterData = rs.getString("afterData");
                    String national = rs.getString("national");

                    // Lấy thông tin account
                    Account account = AccountDAO.getAccountById(userId);
                    // Khởi tạo đối tượng Log và thêm vào danh sách
                    Log log = new Log(id, timesTamp, LOG_LEVEL.valueOf(logLevel), module, action_type, account, logContent, sourceIP, userAgent, affectedTable, beforeData, afterData, national);
                    logs.add(log);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    public static void main(String[] args) {
        System.out.println(getLogsByActionType("UPDATE ROLE"));
    }
}
