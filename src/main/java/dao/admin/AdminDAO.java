package dao.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dao.client.JDBCUtil;

public class AdminDAO {
	public static String getSumOrder() {
		String sql = "SELECT COUNT(*) FROM Orders";
		try {
			Connection connect = JDBCUtil.getConnection();
			PreparedStatement ps = connect.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	public static String getSumPrice() {
		String sql ="SELECT SUM(totalMoney) FROM Orders";
		try {
			Connection connect = JDBCUtil.getConnection();
			PreparedStatement ps = connect.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return rs.getString(1);
			}
		}catch(Exception e) {
			
		}
		return null;
	}
	public static String getSumType(String id) {
		String sql = "SELECT SUM(od.quantity * p.price)\r\n"
				+ "FROM OrderDetails od\r\n"
				+ "INNER JOIN Products p ON od.product_id = p.id\r\n"
				+ "INNER JOIN Orders o ON od.order_id = o.id\r\n"
				+ "WHERE p.category_id = ?;\r\n";
		try {
			Connection connect = JDBCUtil.getConnection();
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	public static String getRatio(String id) {
		String sql = "SELECT ROUND((SUM(od.quantity * p.price) / (SELECT SUM(totalMoney) FROM Orders)) * 100, 2) AS Percentage\r\n"
				+ "FROM OrderDetails od\r\n"
				+ "INNER JOIN Products p ON od.product_id = p.id\r\n"
				+ "INNER JOIN Orders o ON od.order_id = o.id\r\n"
				+ "WHERE p.category_id = ?;";
		try {
			Connection connect = JDBCUtil.getConnection();
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1,id);

			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	public static void main(String[] args) {
		System.out.println(getSumOrder());
		System.out.println(getSumPrice());
		System.out.println(getSumType("2"));
		System.out.println(getRatio("3"));
	}

}
