package dao.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.client.AccountDAO;
import dao.client.JDBCUtil;
import model.Account;

public class AccountsDAO {
    Connection connection = JDBCUtil.getConnection();

    public static List<Account> getListAccountOnRole(int roleId) {
        ArrayList<Account> list = new ArrayList<>();
        String query = null;
        if(roleId ==1){
            query = "select * from Accounts where role_id in (0,2,3,4,5)";
        }else if(roleId == 2){
            query = "select * from Accounts where role_id in(0,3,4,5)";
        }
        try {
            Connection con = JDBCUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Account(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        AccountDAO.getRole(rs.getInt(6)), rs.getInt(7), rs.getBoolean(8)));
            }
        } catch (Exception e) {

        }
        return list;
    }

    /*public static List<Account> getListAccount(int offset, int limit) {
        ArrayList<Account> list = new ArrayList<>();
        String query = "SELECT * FROM Accounts WHERE isAdmin = 0 LIMIT ?, ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String password = rs.getString(3);
                String email = rs.getString(4);
                String telephone = rs.getString(5);
                int isAdmin = rs.getInt(6);

                list.add(new Account(id, name, password, email, telephone, isAdmin));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return list;
    }*/
    public static int getNumberOfRecords() {
        int totalRecords = 0;
        String query = "SELECT COUNT(*) AS total FROM Accounts WHERE isAdmin = 0";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalRecords;
    }

    public static Account getAccountById(String uid) {
        String query = "select * from Accounts where id = ? ";
        try {
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, uid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new Account(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        AccountDAO.getRole(rs.getInt(6)), rs.getInt(7), rs.getBoolean(8));
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static void removeAccount(int id) {
        String query = "DELETE FROM Accounts Where id = ?";
        try {
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {

        }
    }

    public static void updateAccount(Account account) {
        String query = "update Accounts set name=?,password=?,email=?,phonenumber=?  where id =?";
        try {
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, account.getName());
            ps.setString(2, account.getPassword());
            ps.setString(3, account.getEmail());
            ps.setString(4, account.getTelephone());
            ps.setInt(5, account.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AccountsDAO a = new AccountsDAO();
        System.out.println(getListAccountOnRole(2));
    }
}
