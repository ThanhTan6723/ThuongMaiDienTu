package dao.client;

import model.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;

public class Logging {

    public static void login(Log log) {
        try (Connection conn = JDBCUtil.getConnection()) {
            String sql = "INSERT INTO Log (times_tamp, log_level, module, action_type,user_id, log_content, source_ip,user_agent, affected_table,beforeData, afterData,national) VALUES (?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, LocalDateTime.now());
            ps.setString(2, log.getLogLevel().toString());
            ps.setString(3, log.getModule());
            ps.setString(4, log.getActionType());
            if(log.getAccount()!=null){
                ps.setInt(5,log.getAccount().getId());
            }else{
                ps.setNull(5, Types.INTEGER);
            }
            ps.setString(6, log.getLogContent());
            ps.setString(7,log.getSourceIP());
            ps.setString(8,log.getUserAgent());
            ps.setString(9, "Users");
            if(log.getBeforeData()!=null){
                ps.setString(10,log.getBeforeData());
            }else{
                ps.setNull(10, Types.INTEGER);
            }
            if(log.getAffterData()!=null){
                ps.setString(11,log.getAffterData());
            }else{
                ps.setNull(11, Types.INTEGER);
            }
            ps.setString(12,log.getNational());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(IModel model) {
        System.out.println("Table?->" + model.getTable()
                + "\n before: " + model.getBeforeData()
                + "\n after: " + model.getAfterData());
    }

    public static void insert(IModel model) {
        System.out.println("Table?->" + model.getTable()
                + "\n before: " + model.getBeforeData()
                + "\n after: " + model.getAfterData());
    }

    public static void delete(IModel model) {
        System.out.println("Table?->" + model.getTable()
                + "\n before: " + model.getBeforeData()
                + "\n after: " + model.getAfterData());
    }
}
