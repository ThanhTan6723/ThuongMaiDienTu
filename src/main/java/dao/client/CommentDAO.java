package dao.client;

import java.sql.*;
import java.util.*;
import model.Comment;

public class CommentDAO {
    public static void addComment(int accountId, int productId, String content) {
        String sql = "INSERT INTO Comments (account_id, product_id, content) VALUES (?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ps.setInt(2, productId);
            ps.setString(3, content);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Comment> getCommentsByProduct(int productId) {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT c.*, a.name FROM Comments c " +
                "JOIN Accounts a ON c.account_id = a.id " +
                "WHERE c.product_id = ? ORDER BY c.created_at DESC";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Comment c = new Comment();
                c.setId(rs.getInt("id"));
                c.setAccountId(rs.getInt("account_id"));
                c.setProductId(rs.getInt("product_id"));
                c.setContent(rs.getString("content"));
                c.setReply(rs.getString("reply"));
                c.setCreatedAt(rs.getTimestamp("created_at"));
                c.setReplyDate(rs.getTimestamp("reply_at"));
                c.setName(rs.getString("name"));
                comments.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments;
    }

}
