package dao.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Account;
import model.Category;
import model.Product;
import model.Provider;

public class AccessDAO {
	public static List<Product> searchByName(String txtSearch) {
		List<Product> list = new ArrayList<>();
		String query = "SELECT * FROM Products WHERE name LIKE ?";
		try {
			Connection conn = JDBCUtil.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, "%" + txtSearch + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3),rs.getDouble(4), rs.getString(5), rs.getString(6),
						new Category(rs.getInt(7)),rs.getInt(8)));
            }
		} catch (Exception e) {
		}

		return list;
	}
	public static List<Product> paddingProductSearch(int cid, String txtSearch,String sort,String type,int index) {
		List<Product> list = new ArrayList<>();
		String orderByClause = " ORDER BY " + sort + " " + type;
		int ind = (index-1)*8;
		String query="SELECT * FROM Products WHERE name LIKE ? AND category_id = ?" + orderByClause + " LIMIT 8 OFFSET " + ind;
		try {
			Connection con = JDBCUtil.getConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, "%" + txtSearch + "%");
			ps.setInt(2, cid);
			ResultSet rs = ps.executeQuery();
            while (rs.next()) {
				list.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getString(5),
						new Category(rs.getInt(6))));


            }
		} catch (Exception e) {
		}
		return list;
	}

	public static int getTotalProductSearch(String txtSearch) {
		String query = " select count(*) from products where  [name] like ?";
		try {
			Connection conn = JDBCUtil.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, "%" + txtSearch + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {

		}

		return 0;
	}

public static List<Product> pagingProductSearch(int index, String txtSearch, String sort, String type) {
		List<Product> list = new ArrayList<>();
		String query = "select * from products  where  [name] like ? order by " + sort + " " + type
				+ " offset ? rows fetch next 12 rows only";

		try {
			Connection conn = JDBCUtil.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, "%" + txtSearch + "%");
			ps.setInt(2, (index - 1) * 12);
			ResultSet rs = ps.executeQuery();
            while (rs.next()) {
				list.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getString(5),
						new Category(rs.getInt(6))));


            }
		} catch (Exception e) {

		}
		return list;
	}


	public static void main(String[] args) {
		AccessDAO a = new AccessDAO();
		List<Product> list = paddingProductSearch(1,"noo","price","ASC",1);
		List<Product> list1 = searchByName("A");
		for (Product product : list) {
			System.out.println(product.toString());
		}

        System.out.println(AccessDAO.searchByName("a"));
	}
}
