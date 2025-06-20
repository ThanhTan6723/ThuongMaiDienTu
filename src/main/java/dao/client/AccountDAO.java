package dao.client;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import model.Log;
import model.Account;
import model.Role;

public class AccountDAO extends AbsDAO<Account> {
    Connection connection = JDBCUtil.getConnection();

//    @Override
//    public Account login(Account model) {
//        Account account = null;
//        String sql = "SELECT Accounts.id, Accounts.name, Accounts.password, Accounts.email, " +
//                "Accounts.phonenumber, Accounts.role_id, Role.role_name, Accounts.failed, Accounts.isLocked " +
//                "FROM Accounts " +
//                "JOIN Role ON Accounts.role_id = Role.id " +
//                "WHERE Accounts." + field + "= ?" + " AND Accounts.password = ?";
//
//        try (Connection conn = JDBCUtil.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, value);
//            ps.setString(2, password);
//            ResultSet rs = ps.executeQuery();
//
//            if (rs.next()) {
//                account = new Account();
//                account.setId(rs.getInt("id"));
//                account.setName(rs.getString("name"));
//                account.setPassword(rs.getString("password"));
//                account.setEmail(rs.getString("email"));
//                account.setTelephone(rs.getString("phonenumber"));
//                account.setRole(new Role(rs.getInt("role_id"), rs.getString("role_name")));
//                account.setFailed(rs.getInt("failed"));
//                account.setLocked(rs.getBoolean("isLocked"));
//
//                return super.login(model);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return account;
//    }

    public static boolean isUserLocked(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isLocked = false;

        try {
            conn = JDBCUtil.getConnection(); // Lấy kết nối tới cơ sở dữ liệu
            String sql = "SELECT isLocked FROM Accounts WHERE id = ?"; // Truy vấn để lấy trạng thái khóa
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                isLocked = rs.getBoolean("locked"); // Lấy trạng thái khóa từ kết quả truy vấn
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isLocked;
    }

    public static boolean updateFailed(int accountId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "UPDATE Accounts SET isLocked = 1 WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    // Khóa tài khoản
    public static boolean lockAccount(String field, Object value) {
        boolean success = false;
        try (Connection conn = JDBCUtil.getConnection()) {
            String sql = "UPDATE Accounts SET isLocked = TRUE, lock_time = NOW() WHERE " + field + " = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, value);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean lockAccountIndefinite(String field, Object value) {
        boolean success = false;
        try (Connection conn = JDBCUtil.getConnection()) {
            String sql = "UPDATE Accounts SET isLocked = 1, lock_time = Indefinite WHERE " + field + " = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, value);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean unlockAccount(String field, Object value) {
        boolean success = false;
        try (Connection conn = JDBCUtil.getConnection()) {
            String sql = "UPDATE Accounts SET isLocked = FALSE, lock_time = NULL WHERE " + field + " = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, value);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean isAccountLocked(String field, String value) {
        try (Connection conn = JDBCUtil.getConnection()) {
            String sql = "SELECT lock_time FROM Accounts WHERE " + field + " = ? AND isLocked = TRUE";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, value);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Timestamp lockTime = rs.getTimestamp("lock_time");
                LocalDateTime lockDateTime = lockTime.toLocalDateTime();
                LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.systemDefault());

                if (ChronoUnit.MINUTES.between(lockDateTime, currentDateTime) >= 15) {
                    unlockAccount(field, value);
                    return false;
                } else {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Mở khóa tài khoản
//    public static boolean unlockAccount(int userId) {
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        boolean success = false;
//        try {
//            conn = JDBCUtil.getConnection();
//            String sql = "UPDATE accounts SET isLocked = 0 WHERE id = ?";
//            stmt = conn.prepareStatement(sql);
//            stmt.setInt(1, userId);
//            int rowsAffected = stmt.executeUpdate();
//            if (rowsAffected > 0) {
//                success = true;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            JDBCUtil.closeObject(conn);
//        }
//        return success;
//    }

    public static Account findByEmail(String email) {
        String sql = "SELECT * FROM Accounts WHERE email = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Account account = new Account();
                account.setId(rs.getInt("id"));
                account.setName(rs.getString("name"));
                account.setEmail(rs.getString("email"));
                account.setPassword(rs.getString("password"));
                account.setTelephone(rs.getString("phonenumber"));
                account.setRole(AccountDAO.getRole(rs.getInt("role_id")));
                account.setFailed(rs.getInt("failed"));
                account.setLocked(rs.getBoolean("isLocked"));
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Role getRole(int roleId) {
        Role role = null;
        String sql = "Select * from Role where id=?";
        try {
            Connection connect = JDBCUtil.getConnection();
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, roleId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                role = new Role(rs.getInt(1), rs.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return role;
    }

    public ArrayList<Account> selectAll() {
        ArrayList<Account> result = new ArrayList<Account>();
        try {
            // 1.connect to database
            Connection connect = JDBCUtil.getConnection();

            // 2.create object statement
            String sql = "Select * From Accounts";
            PreparedStatement preSt = connect.prepareStatement(sql);

            // 3.excute function sql
            ResultSet rs = preSt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String telephone = rs.getString("phonenumber");
                Role role = AccountDAO.getRole(rs.getInt("role_id"));
                int failed = rs.getInt("failed");
                boolean isLocked = rs.getBoolean("isLocked");

                Account cus = new Account(id, name, password, email, telephone, role, failed, isLocked);
                result.add(cus);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public Account selectByName(Account t) {
        Account res = null;
        try {
            // 1.connect to database
            Connection connect = JDBCUtil.getConnection();

            // 2.create object statement
            String sql = "SELECT Accounts.id, Accounts.name, Accounts.password, Accounts.email, " +
                    "Accounts.phonenumber, Accounts.role_id, Role.role_name,Accounts.failed, Accounts.isLocked " +
                    "FROM Accounts " +
                    "JOIN Role ON Accounts.role_id = Role.id " + "where Accounts.name=?";
            PreparedStatement preSt = connect.prepareStatement(sql);
            preSt.setString(1, t.getName());
            ;

            // 3.excute function sql
            ResultSet rs = preSt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String telephone = rs.getString("phonenumber");
                int roleId = rs.getInt("role_id");
                String roleName = rs.getString("role_name");
                int failed = rs.getInt("failed");
                boolean isLocked = rs.getBoolean("isLocked");

                Account cus = new Account(id, name, password, email, telephone,
                        new Role(roleId, roleName), failed, isLocked);
                System.out.println(cus);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public Account login(String field, String value, String password, Log log) {
        Account account = null;
        String sql = "SELECT Accounts.id, Accounts.name, Accounts.password, Accounts.email, " +
                "Accounts.phonenumber, Accounts.role_id, Role.role_name, Accounts.failed, Accounts.isLocked " +
                "FROM Accounts " +
                "JOIN Role ON Accounts.role_id = Role.id " +
                "WHERE Accounts." + field + "= ?" + " AND Accounts.password = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                account = new Account();
                account.setId(rs.getInt("id"));
                account.setName(rs.getString("name"));
                account.setPassword(rs.getString("password"));
                account.setEmail(rs.getString("email"));
                account.setTelephone(rs.getString("phonenumber"));
                account.setRole(new Role(rs.getInt("role_id"), rs.getString("role_name")));
                account.setFailed(rs.getInt("failed"));
                account.setLocked(rs.getBoolean("isLocked"));

                return super.login(field, value, password, log);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return account;

    }

    public static Account getAccountByField(String field, String value, String password) {
        Account account = null;
        String sql = "SELECT Accounts.id, Accounts.name, Accounts.password, Accounts.email, " +
                "Accounts.phonenumber, Accounts.role_id, Role.role_name, Accounts.failed, Accounts.isLocked " +
                "FROM Accounts " +
                "JOIN Role ON Accounts.role_id = Role.id " +
                "WHERE Accounts." + field + "= ?" + " AND Accounts.password = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                account = new Account();
                account.setId(rs.getInt("id"));
                account.setName(rs.getString("name"));
                account.setPassword(rs.getString("password"));
                account.setEmail(rs.getString("email"));
                account.setTelephone(rs.getString("phonenumber"));
                account.setRole(new Role(rs.getInt("role_id"), rs.getString("role_name")));
                account.setFailed(rs.getInt("failed"));
                account.setLocked(rs.getBoolean("isLocked"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Logging.login(account);
        return account;
    }


    public static int insertAccount(Account t) {
        int res = 0;
        try {
            Connection connection = JDBCUtil.getConnection();

            String sql = "INSERT INTO Accounts(name,password,email,phonenumber,role_id)" + "VALUES(?,?,?,?,?)";

            PreparedStatement prSt = connection.prepareStatement(sql);

            prSt.setString(1, t.getName());
            prSt.setString(2, t.getPassword());
            prSt.setString(3, t.getEmail());
            prSt.setString(4, t.getTelephone());
            prSt.setInt(5, t.getRole().getId());
//			prSt.setInt(6, t.getIsDeleted());

            res = prSt.executeUpdate();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return res;
    }

    public int delete(Account t) {
        int res = 0;
        try {
            Connection connect = JDBCUtil.getConnection();

            String sql = "DETELE From " + "Where name=?";

            PreparedStatement prSt = connect.prepareStatement(sql);
            prSt.setString(1, t.getName());

            res = prSt.executeUpdate();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return res;
    }

    public int update(Account t) {
        int res = 0;
        try {
            Connection connect = JDBCUtil.getConnection();

            String sql = "UPDATE Accounts " + "SET" + " password=?" + "WHERE email=?";

            PreparedStatement prSt = connect.prepareStatement(sql);
            prSt.setString(1, t.getPassword());
            prSt.setString(2, t.getEmail());

            res = prSt.executeUpdate();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return res;
    }

    public static int updateProfile(Account t) {
        int res = 0;
        try {
            Connection connect = JDBCUtil.getConnection();

            String sql = "UPDATE Accounts " + "SET" + " name=?, email=?, phonenumber=? " + "WHERE id=?";

            PreparedStatement prSt = connect.prepareStatement(sql);
            prSt.setString(1, t.getName());
            prSt.setString(2, t.getEmail());
            prSt.setString(3, t.getTelephone());
            prSt.setInt(4, t.getId());

            res = prSt.executeUpdate();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return res;
    }

    public static int updateRole(int id, int roleId) {
        int res = 0;
        try {
            Connection connect = JDBCUtil.getConnection();

            String sql = "UPDATE Accounts " + "SET" + " role_id = ? " + "WHERE id=?";

            PreparedStatement prSt = connect.prepareStatement(sql);
            prSt.setInt(1, roleId);
            prSt.setInt(2, id);
            res = prSt.executeUpdate();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return res;
    }

    public static boolean checkUserName(String userName) {
        boolean res = false;

        try {
            Connection connect = JDBCUtil.getConnection();

            String sql = "SELECT * FROM Accounts WHERE name=?";
            PreparedStatement prSt = connect.prepareStatement(sql);
            prSt.setString(1, userName);
//			prSt.setString(2, email);
//			prSt.setString(3, telephone);

            ResultSet rs = prSt.executeQuery();
//			System.out.println(rs);
            while (rs.next()) {
                res = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return res;
    }

    public static boolean checkFieldExists(String field, String value) {
        String sql = "SELECT COUNT(*) FROM Accounts WHERE " + field + " = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void resetFailedAttempts(int accountId) {
        String sql = "UPDATE Accounts SET failed = 0 WHERE id = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void incrementFailedAttempts(String field, String identifier) {
        String sql = "UPDATE Accounts SET failed = failed + 1 WHERE " + field + " = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, identifier);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getFailedAttempts(String field, String identifier) {
        String sql = "SELECT failed FROM Accounts WHERE " + field + " = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, identifier);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static boolean checkPass(String pass) {
        boolean res = false;
        try {
            Connection connect = JDBCUtil.getConnection();

            String sql = "SELECT * FROM Accounts WHERE password=?";
            PreparedStatement prSt = connect.prepareStatement(sql);
            prSt.setString(1, pass);

            System.out.println(sql);
            ResultSet rs = prSt.executeQuery();

            while (rs.next()) {
                res = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return res;

    }

    public static Account getAccountById(int id) {
        try {
            // 1.connect to database
            Connection connect = JDBCUtil.getConnection();

            // 2.create object statement
            String sql = "SELECT Accounts.id, Accounts.name, Accounts.password, Accounts.email, " +
                    "Accounts.phonenumber, Accounts.role_id, Role.role_name,Accounts.failed, Accounts.isLocked " +
                    "FROM Accounts " +
                    "JOIN Role ON Accounts.role_id = Role.id " + " Where Accounts.id=?";
            PreparedStatement preSt = connect.prepareStatement(sql);
            preSt.setInt(1, id);

            // 3.excute function sql
            ResultSet rs = preSt.executeQuery();

            while (rs.next()) {
                int accId = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String phonenumber = rs.getString("phonenumber");
                int roleId = rs.getInt("role_id");
                String roleName = rs.getString("role_name");
                int failed = rs.getInt("failed");
                boolean isLocked = rs.getBoolean("isLocked");

                return new Account(accId, name, password, email, phonenumber, new Role(roleId, roleName), failed, isLocked);
            }
//			JDBCUtil.closeObject(connect);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static Account getAccountByEmail(String email) {
        try {
            // 1.connect to database
            Connection connect = JDBCUtil.getConnection();

            // 2.create object statement
            String sql = "SELECT Accounts.id, Accounts.name, Accounts.password, Accounts.email, " +
                    "Accounts.phonenumber, Accounts.role_id, Role.role_name,Accounts.failed, Accounts.isLocked " +
                    "FROM Accounts " +
                    "JOIN Role ON Accounts.role_id = Role.id " + " Where Accounts.email=?";
            PreparedStatement preSt = connect.prepareStatement(sql);
            preSt.setString(1, email);

            // 3.excute function sql
            ResultSet rs = preSt.executeQuery();

            while (rs.next()) {
                int accId = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String mail = rs.getString("email");
                String phonenumber = rs.getString("phonenumber");
                int roleId = rs.getInt("role_id");
                String roleName = rs.getString("role_name");
                int failed = rs.getInt("failed");
                boolean isLocked = rs.getBoolean("isLocked");

                return new Account(accId, name, password, mail, phonenumber, new Role(roleId, roleName), failed, isLocked);
            }
//			JDBCUtil.closeObject(connect);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

//    public static Account getAccount(String name, String pass) {
//        String sql = "select * from Accounts where name=? and password=?";
//        try {
//            Connection connect = JDBCUtil.getConnection();
//
//            PreparedStatement st = connect.prepareStatement(sql);
//            st.setString(1, name);
//            st.setString(2, pass);
//
//            ResultSet rs = st.executeQuery();
//
//            if (rs.next()) {
//                int id = rs.getInt("id");
//                String username = rs.getString("name");
//                String password = rs.getString("password");
//                String email = rs.getString("email");
//                String telephone = rs.getString("phonenumber");
//                int isAdmin = rs.getInt("isAdmin");
//
//                return new Account(id, username, password, email, telephone, isAdmin);
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            System.out.println(e);
//        }
//        return null;
//    }

    public static void main(String[] args) {
        System.out.println(checkUserName("thanhtan"));
        AccountDAO acc = new AccountDAO();
        System.out.println(acc.selectAll());
        System.out.println(getAccountById(13));
        System.out.println(getAccountByEmail("anhtuan542100@gmail.com"));
        System.out.println(findByEmail("anhtuan542100@gmail.com"));
    }
}