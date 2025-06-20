package dao.client;

import model.Category;
import model.Product;
import model.Voucher;
import model.DiscountType;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherDAO {
    public static List<Voucher> getAllVouchers() {
        List<Voucher> vouchers = new ArrayList<>();
        String query = "SELECT v.*, dt.type as discount_type_name,p.id as product_id, p.name as product_name,c.id as category_id, c.name as category_name FROM Vouchers v " +
                "LEFT JOIN DiscountType dt ON v.discount_type = dt.id " +
                "LEFT JOIN Products p ON v.product_id = p.id " +
                "LEFT JOIN Category c ON v.category_id = c.id "+
                "WHERE v.end_date > CURRENT_DATE";
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Voucher voucher = new Voucher();
                voucher.setId(resultSet.getInt("id"));
                voucher.setCode(resultSet.getString("code"));
                voucher.setDiscountType(new DiscountType(resultSet.getInt("discount_type"), resultSet.getString("discount_type_name")));
                voucher.setDiscountPercentage(resultSet.getBigDecimal("discount_percentage"));

                if (resultSet.getInt("product_id") != 0) {
                    Product product = new Product();
                    product.setId(resultSet.getInt("product_id"));
                    product.setName(resultSet.getString("product_name"));
                    voucher.setProduct(product);
                }
                if (resultSet.getInt("category_id") != 0) {
                    Category category = new Category();
                    category.setId(resultSet.getInt("category_id"));
                    category.setName(resultSet.getString("category_name"));
                    voucher.setCategory(category);
                }
                voucher.setQuantity(resultSet.getInt("quantity"));
                voucher.setStartDate(resultSet.getDate("start_date"));
                voucher.setEndDate(resultSet.getDate("end_date"));
                voucher.setMinimumOrderValue(resultSet.getDouble("minimum_order_value"));
                voucher.setMaximumDiscount(resultSet.getDouble("maximum_discount"));
                voucher.setActive(resultSet.getBoolean("is_active"));
                vouchers.add(voucher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vouchers;
    }

    public static boolean saveEVoucher(int accountId, int voucherId) {
        String queryInsert = "INSERT INTO eVouchers (account_id, voucher_id) VALUES (?, ?)";
        String queryUpdate = "UPDATE Vouchers SET quantity = quantity - 1 WHERE id = ? AND quantity > 0";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatementInsert = connection.prepareStatement(queryInsert);
             PreparedStatement preparedStatementUpdate = connection.prepareStatement(queryUpdate)) {

            // Start transaction
            connection.setAutoCommit(false);

            // Update voucher quantity
            preparedStatementUpdate.setInt(1, voucherId);
            int updateResult = preparedStatementUpdate.executeUpdate();
            if (updateResult == 0) {
                connection.rollback();
                return false; // No rows updated, probably voucher out of stock
            }

            // Insert eVoucher
            preparedStatementInsert.setInt(1, accountId);
            preparedStatementInsert.setInt(2, voucherId);
            int insertResult = preparedStatementInsert.executeUpdate();

            // Commit transaction
            connection.commit();
            return insertResult > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection connection = JDBCUtil.getConnection()) {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        }
    }

    public static List<Integer> getSavedVouchers(int accountId) {
        List<Integer> savedVouchers = new ArrayList<>();
        String query = "SELECT voucher_id FROM eVouchers WHERE account_id = ?";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, accountId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    savedVouchers.add(resultSet.getInt("voucher_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return savedVouchers;
    }

    public static int checkUsageVoucher(int accountId, int voucherId) {
        int check = 0;
        String query = "SELECT usage_limit FROM eVouchers WHERE account_id = ? and voucher_id = ?";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, accountId);
            preparedStatement.setInt(2, voucherId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    check = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return check;
    }

    public static Voucher getVoucherById(int voucherId) {
        Voucher voucher = null;
        String query = "SELECT v.*, dt.type as discount_type_name, p.id as product_id, p.name as product_name, c.id as category_id, c.name as category_name " +
                "FROM Vouchers v " +
                "LEFT JOIN DiscountType dt ON v.discount_type = dt.id " +
                "LEFT JOIN Products p ON v.product_id = p.id " +
                "LEFT JOIN Category c ON v.category_id = c.id " +
                "WHERE v.id = ?";
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, voucherId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    voucher = new Voucher();
                    voucher.setId(resultSet.getInt("id"));
                    voucher.setCode(resultSet.getString("code"));
                    voucher.setDiscountType(new DiscountType(resultSet.getInt("discount_type"), resultSet.getString("discount_type_name")));
                    voucher.setDiscountPercentage(resultSet.getBigDecimal("discount_percentage"));

                    if (resultSet.getInt("product_id") != 0) {
                        Product product = new Product();
                        product.setId(resultSet.getInt("product_id"));
                        product.setName(resultSet.getString("product_name"));
                        voucher.setProduct(product);
                    }

                    if (resultSet.getInt("category_id") != 0) {
                        Category category = new Category();
                        category.setId(resultSet.getInt("category_id"));
                        category.setName(resultSet.getString("category_name"));
                        voucher.setCategory(category);
                    }

                    voucher.setQuantity(resultSet.getInt("quantity"));
                    voucher.setStartDate(resultSet.getDate("start_date"));
                    voucher.setEndDate(resultSet.getDate("end_date"));
                    voucher.setMinimumOrderValue(resultSet.getDouble("minimum_order_value"));
                    voucher.setMaximumDiscount(resultSet.getDouble("maximum_discount"));
                    voucher.setActive(resultSet.getBoolean("is_active"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voucher;
    }

    public static Voucher getVoucherByCode(String code) {
        Voucher voucher = null;
        String query = "SELECT v.*, dt.type as discount_type_name, p.id as product_id, p.name as product_name, c.id as category_id, c.name as category_name " +
                "FROM Vouchers v " +
                "LEFT JOIN DiscountType dt ON v.discount_type = dt.id " +
                "LEFT JOIN Products p ON v.product_id = p.id " +
                "LEFT JOIN Category c ON v.category_id = c.id " +
                "WHERE v.code = ?";
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    voucher = new Voucher();
                    voucher.setId(resultSet.getInt("id"));
                    voucher.setCode(resultSet.getString("code"));
                    voucher.setDiscountType(new DiscountType(resultSet.getInt("discount_type"), resultSet.getString("discount_type_name")));
                    voucher.setDiscountPercentage(resultSet.getBigDecimal("discount_percentage"));

                    if (resultSet.getInt("product_id") != 0) {
                        Product product = new Product();
                        product.setId(resultSet.getInt("product_id"));
                        product.setName(resultSet.getString("product_name"));
                        voucher.setProduct(product);
                    }

                    if (resultSet.getInt("category_id") != 0) {
                        Category category = new Category();
                        category.setId(resultSet.getInt("category_id"));
                        category.setName(resultSet.getString("category_name"));
                        voucher.setCategory(category);
                    }

                    voucher.setQuantity(resultSet.getInt("quantity"));
                    voucher.setStartDate(resultSet.getDate("start_date"));
                    voucher.setEndDate(resultSet.getDate("end_date"));
                    voucher.setMinimumOrderValue(resultSet.getDouble("minimum_order_value"));
                    voucher.setMaximumDiscount(resultSet.getDouble("maximum_discount"));
                    voucher.setActive(resultSet.getBoolean("is_active"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voucher;
    }


    // Get saved vouchers for a specific user
    public List<Voucher> getVoucherForAccount(int accountId) {
        List<Voucher> savedVouchers = new ArrayList<>();
        String query = "SELECT v.*, dt.type as discount_type_name, p.id as product_id, p.name as product_name, c.id as category_id, c.name as category_name FROM eVouchers ev " +
                "JOIN Vouchers v ON ev.voucher_id = v.id " +
                "LEFT JOIN DiscountType dt ON v.discount_type = dt.id " +
                "LEFT JOIN Products p ON v.product_id = p.id " +
                "LEFT JOIN Category c ON v.category_id = c.id " +
                "WHERE ev.account_id = ?";
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, accountId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Voucher voucher = new Voucher();
                    voucher.setId(resultSet.getInt("id"));
                    voucher.setCode(resultSet.getString("code"));
                    voucher.setDiscountType(new DiscountType(resultSet.getInt("discount_type"), resultSet.getString("discount_type_name")));
                    voucher.setDiscountPercentage(resultSet.getBigDecimal("discount_percentage"));

                    if (resultSet.getInt("product_id") != 0) {
                        Product product = new Product();
                        product.setId(resultSet.getInt("product_id"));
                        product.setName(resultSet.getString("product_name"));
                        voucher.setProduct(product);
                    }
                    if (resultSet.getInt("category_id") != 0) {
                        Category category = new Category();
                        category.setId(resultSet.getInt("category_id"));
                        category.setName(resultSet.getString("category_name"));
                        voucher.setCategory(category);
                    }
                    voucher.setQuantity(resultSet.getInt("quantity"));
                    voucher.setStartDate(resultSet.getDate("start_date"));
                    voucher.setEndDate(resultSet.getDate("end_date"));
                    voucher.setMinimumOrderValue(resultSet.getDouble("minimum_order_value"));
                    voucher.setMaximumDiscount(resultSet.getDouble("maximum_discount"));
                    voucher.setActive(resultSet.getBoolean("is_active"));
                    savedVouchers.add(voucher);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return savedVouchers;
    }

    public static List<Voucher> getVoucherByType(String type) {
        List<Voucher> vouchers = new ArrayList<>();
        String query = "SELECT v.*, dt.type as discount_type_name, p.id as product_id, p.name as product_name, p.image as product_image, c.id as category_id, c.name as category_name " +
                "FROM Vouchers v " +
                "LEFT JOIN DiscountType dt ON v.discount_type = dt.id " +
                "LEFT JOIN Products p ON v.product_id = p.id " +
                "LEFT JOIN Category c ON v.category_id = c.id " +
                "WHERE dt.type = ?";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, type);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Voucher voucher = new Voucher();
                    voucher.setId(resultSet.getInt("id"));
                    voucher.setCode(resultSet.getString("code"));
                    voucher.setDiscountType(new DiscountType(resultSet.getInt("discount_type"), resultSet.getString("discount_type_name")));
                    voucher.setDiscountPercentage(resultSet.getBigDecimal("discount_percentage"));
                    voucher.setQuantity(resultSet.getInt("quantity"));
                    voucher.setStartDate(resultSet.getDate("start_date"));
                    voucher.setEndDate(resultSet.getDate("end_date"));
                    voucher.setMinimumOrderValue(resultSet.getDouble("minimum_order_value"));
                    voucher.setMaximumDiscount(resultSet.getDouble("maximum_discount"));
                    voucher.setActive(resultSet.getBoolean("is_active"));

                    int productId = resultSet.getInt("product_id");
                    if (productId != 0) {
                        Product product = new Product();
                        product.setId(productId);
                        product.setName(resultSet.getString("product_name"));
                        product.setImage(resultSet.getString("product_image"));
                        voucher.setProduct(product);
                    }

                    int categoryId = resultSet.getInt("category_id");
                    if (categoryId != 0) {
                        Category category = new Category();
                        category.setId(categoryId);
                        category.setName(resultSet.getString("category_name"));
                        voucher.setCategory(category);
                    }

                    vouchers.add(voucher);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vouchers;
    }

    public static boolean insertVoucher(Voucher voucher) {
        String query = "INSERT INTO Vouchers (code, discount_type, discount_percentage, product_id, category_id, quantity, start_date, end_date,minimum_order_value,maximum_discount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, voucher.getCode());
            preparedStatement.setInt(2, voucher.getDiscountType().getId());
            preparedStatement.setBigDecimal(3, voucher.getDiscountPercentage());

            // Set product_id or category_id based on voucher type
            if (voucher.getProduct() != null) {
                preparedStatement.setInt(4, voucher.getProduct().getId());
                preparedStatement.setNull(5, java.sql.Types.INTEGER);
            } else if (voucher.getCategory() != null) {
                preparedStatement.setNull(4, java.sql.Types.INTEGER);
                preparedStatement.setInt(5, voucher.getCategory().getId());
            } else {
                preparedStatement.setNull(4, java.sql.Types.INTEGER);
                preparedStatement.setNull(5, java.sql.Types.INTEGER);
            }

            preparedStatement.setInt(6, voucher.getQuantity());
            preparedStatement.setDate(7, new java.sql.Date(voucher.getStartDate().getTime()));
            preparedStatement.setDate(8, new java.sql.Date(voucher.getEndDate().getTime()));
            preparedStatement.setDouble(9, voucher.getMinimumOrderValue());
            preparedStatement.setDouble(10, voucher.getMaximumDiscount());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {
        Voucher voucher = new Voucher();
        voucher.setCode("all");
        voucher.setCategory(new Category(1, "Rau củ"));
        voucher.setDiscountType(new DiscountType(2, "Category"));
        voucher.setDiscountPercentage(BigDecimal.valueOf(25));
        voucher.setQuantity(10);
        voucher.setStartDate(new java.util.Date(10 / 10 / 2020));
        voucher.setEndDate(new java.util.Date(17 / 10 / 2020));
        System.out.println(insertVoucher(voucher));
    }
}
