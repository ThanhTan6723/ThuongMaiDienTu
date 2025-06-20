package dao.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import model.*;

public class ProductDAO {
    static Connection connection = JDBCUtil.getConnection();
    private static final String INSERT_REVIEW_SQL = "INSERT INTO Reviews (name_commenter, phonenumber_commenter, product_id, rating, comment,image, date_created,isAccept) VALUES (?, ?, ?, ?, ?, ?,?,?)";

    public static void saveReview(Review review) {
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_REVIEW_SQL)) {
            preparedStatement.setString(1, review.getNameCommenter());
            preparedStatement.setString(2, review.getPhoneNumberCommenter());
            preparedStatement.setInt(3, review.getProductEvaluated().getId());
            preparedStatement.setInt(4, review.getRating());
            preparedStatement.setString(5, review.getComment());
            preparedStatement.setString(6, review.getImage());
            preparedStatement.setString(7, review.getDateCreated());
            preparedStatement.setBoolean(8,review.isAccept());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, Integer> getReviewStatistics(int productId) {
        Map<Integer, Integer> statistics = new HashMap<>();
        String query = "SELECT rating, COUNT(*) AS count FROM reviews WHERE product_id = ? GROUP BY rating";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    statistics.put(rs.getInt("rating"), rs.getInt("count"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statistics;
    }
    // Method để cập nhật reply và ngày giờ reply của review dựa trên ID
    public static boolean updateReviewReply(int reviewId, String replyText, String replyDateTime) {
        String query = "UPDATE Reviews SET response = ?, date_reply = ? WHERE id = ?";

        try (
                PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, replyText);
            ps.setString(2, replyDateTime);
            ps.setInt(3, reviewId);

            // Thực thi câu lệnh SQL để cập nhật
            int rowsUpdated = ps.executeUpdate();

            // Trả về true nếu cập nhật thành công, false nếu không thành công
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean acceptReview(int reviewId) {
        String query = "UPDATE Reviews SET isAccept = ? WHERE id = ?";

        try (
                PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setBoolean(1, true);
            ps.setInt(2, reviewId);

            // Thực thi câu lệnh SQL để cập nhật
            int rowsUpdated = ps.executeUpdate();

            // Trả về true nếu cập nhật thành công, false nếu không thành công
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<Review> getListReviewsByProductId(int productId) {
        List<Review> list = new ArrayList<>();
        String query = "SELECT * FROM reviews WHERE product_id = ? and isAccept = true ORDER BY date_created DESC";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Review(rs.getInt(1), rs.getString(2), rs.getString(3), ProductDAO.getProductById(rs.getInt(4)), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8),rs.getString(9), rs.getString(10),rs.getBoolean(11)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Review> getListReviews() {
        List<Review> list = new ArrayList<>();
        String query = "SELECT * FROM reviews ORDER BY date_created DESC";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Review(rs.getInt(1), rs.getString(2), rs.getString(3), ProductDAO.getProductById(rs.getInt(4)), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),rs.getBoolean(11)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Product> getSellProduct() {
        List<Product> list = new ArrayList<>();
        try {
            Connection connect = JDBCUtil.getConnection();

            String sql = "select top 4 * from Products order by price ";
            PreparedStatement st = connect.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getString(5),
                        new Category(rs.getInt(6))));
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }
        return list;
    }

    public static Product getProductById(int pid) {
        String sql = "select * from Products where id=?";
        try {
            Connection connect = JDBCUtil.getConnection();
            PreparedStatement st = connect.prepareStatement(sql);
            st.setInt(1, pid);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3),rs.getDouble(4), rs.getString(5), rs.getString(6),
                        new Category(rs.getInt(7)),ProductDAO.getListBatchById(pid),rs.getInt(8));
            }
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e);
        }

        return null;
    }

    public static List<Product> getAllProduct() {
        String sql = "select * from Products";
        List<Product> list = new ArrayList<>();
        try {
            Connection connect = JDBCUtil.getConnection();
            PreparedStatement st = connect.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3),rs.getDouble(4), rs.getString(5), rs.getString(6),
                        new Category(rs.getInt(7)),rs.getInt(8)));
            }
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e);
        }
        return list;
    }

    public int getTotalProduct(int cid) {
        String query = "SELECT COUNT(*) FROM Products ";
        switch (cid) {
            case 0:
                break;
            case 1:
                query += "WHERE category_id = 1";
                break;
            case 2:
                query += "WHERE category_id = 2";
                break;
            case 3:
                query += "WHERE category_id = 3";
                break;

            default:
        }

        try {
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
        }
        return 0;
    }


    public static List<Category> getListCategory() {
        ArrayList<Category> list = new ArrayList<>();
        String query = "select* from Category ";
        try {
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Category(rs.getInt(1), rs.getString(2)));
            }
        } catch (Exception e) {
        }
        return list;

    }


    public static void insertProduct(Product product) {
        String query = "insert INTO Products (id, name, price, image,description,category_id)\r\n"
                + "values( ?, ?,?,?, ?,?)";
        try {
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, product.getId());
            ps.setString(2, product.getName());
            ps.setDouble(3, product.getPrice());
            ps.setString(4, product.getImage());
            ps.setString(5, product.getDescription());
            ps.setInt(6, product.getCategory().getId());
            ps.executeUpdate();
        } catch (Exception e) {
        }
    }

    public static List<Product> relativeProduct(int id) {
        List<Product> list = new ArrayList<>();
        String query = "select * from Products where category_id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getString(5),
                        new Category(rs.getInt(6))));
            }
        } catch (Exception e) {

        }
        return list;
    }

    public static List<Image> listImageProduct(int id) {
        List<Image> list = new ArrayList<>();
        String sql = "SELECT * FROM Images " +
                "INNER JOIN ProductImages ON Images.id = ProductImages.imageId WHERE ProductImages.productId = ?";
        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int imageId = rs.getInt("id");
                String imageUrl = rs.getString("imgAssetId");
                list.add(new Image(imageId, imageUrl));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ một cách thích hợp, ví dụ: ghi log và/hoặc thông báo cho người dùng
        }
        return list;
    }

    public static String getCategoryById(int id) {
        String sql = "select name from Category where id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (Exception e) {

        }
        return null;
    }

    public static Category getCategoryById1(int id) {
        String sql = "select * from Category where id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new Category(rs.getInt(1), rs.getString(2));
            }
        } catch (SQLException ex) {
        }

        return null;
    }

    public static Provider getInforByIdProvider(int id) {
        String sql = "select * from Providers where id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new Provider(rs.getInt(1), rs.getString(2), rs.getString(3));
            }

        } catch (Exception e) {

        }
        return null;
    }


    public static List<Provider> getListProvider() {
        List<Provider> providerList = new ArrayList<>();
        String sql = "select * from Providers";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                providerList.add(new Provider(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        } catch (Exception e) {

        }
        return providerList;
    }


    public static List<Batch> getListBatchById(int id) {
        List<Batch> list = new ArrayList<>();
        String sql = "SELECT b.id, b.name, b.manufacturingDate, b.expiryDate, b.dateOfImporting, " +
                "b.quantity,b.currentQuantity, b.priceImport, p.id AS provider_id, p.name AS provider_name, p.address AS provider_address, " +
                "a.id AS admin_id, a.name AS admin_name " +
                "FROM Batch b " +
                "JOIN Providers p ON b.provider_id = p.id " +
                "JOIN Accounts a ON b.adminCreate_id = a.id " +
                "WHERE b.product_id = ? AND b.expiryDate > CURDATE()";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Provider provider = new Provider(rs.getInt("provider_id"), rs.getString("provider_name"), rs.getString("provider_address"));
                Account adminCreate = new Account(); // Giả sử Account có constructor với các tham số này
                adminCreate.setId(rs.getInt("admin_id"));
                adminCreate.setName(rs.getString("admin_name"));
                Batch batch = new Batch(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getDate(3),
                        rs.getDate(4),
                        rs.getDate(5),
                        rs.getInt(6),
                        rs.getInt(7),
                        rs.getDouble(8),
                        provider,
                        adminCreate
                );
                list.add(batch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Batch getNearestExpiryBatchById(int id) {
        Batch nearestExpiryBatch = null;
        String sql = "SELECT b.id, b.name, b.manufacturingDate, b.expiryDate, b.dateOfImporting, " +
                "b.quantity, b.currentQuantity, b.priceImport, p.id AS provider_id, p.name AS provider_name, p.address AS provider_address, " +
                "a.id AS admin_id, a.name AS admin_name " +
                "FROM Batch b " +
                "JOIN Providers p ON b.provider_id = p.id " +
                "JOIN Accounts a ON b.adminCreate_id = a.id " +
                "WHERE b.product_id = ? AND b.expiryDate > CURDATE() " +
                "ORDER BY b.expiryDate ASC LIMIT 1";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Provider provider = new Provider(rs.getInt("provider_id"), rs.getString("provider_name"), rs.getString("provider_address"));
                Account adminCreate = new Account(); // Giả sử Account có constructor với các tham số này
                adminCreate.setId(rs.getInt("admin_id"));
                adminCreate.setName(rs.getString("admin_name"));
                nearestExpiryBatch = new Batch(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getDate(3),
                        rs.getDate(4),
                        rs.getDate(5),
                        rs.getInt(6),
                        rs.getInt(7),
                        rs.getDouble(8),
                        provider,
                        adminCreate
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nearestExpiryBatch;
    }

    public static void removeProduct(int pid) {
        String query = "DELETE FROM Products WHERE id = ?";
        try {
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, pid);
            ps.executeUpdate();
        } catch (Exception e) {

        }
    }

    public static Batch getBatchById(int id) {
        String sql = "select * from Batch where id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Batch(rs.getInt(1), rs.getString(2), rs.getDate(3), rs.getDate(4), rs.getDate(5), rs.getInt(6), rs.getInt(7), rs.getDouble(8),
                        new Provider(rs.getInt(9)), new Account(rs.getInt(10)));
            }
        } catch (Exception e) {

        }
        return null;
    }


    public static Provider getProviderById(int providerId) {
        String sql = "SELECT * FROM Providers WHERE id = ?";
        Provider provider = null;

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, providerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                provider = new Provider(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return provider;
    }

    public static Product getProductWithBatchesById(int id) {
        Product product = null;
        String productSql = "SELECT * FROM Products WHERE id=?";
        String batchSql = "SELECT b.id, b.name, b.manufacturingDate, b.expiryDate, b.dateOfImporting, " +
                "b.quantity, b.currentQuantity, b.priceImport, p.id AS provider_id, p.name AS provider_name, p.address AS provider_address, " +
                "a.id AS admin_id, a.name AS admin_name " +
                "FROM Batch b " +
                "JOIN Providers p ON b.provider_id = p.id " +
                "JOIN Accounts a ON b.adminCreate_id = a.id " +
                "WHERE b.product_id = ? AND b.expiryDate > CURDATE()";

        try (
                Connection conn = JDBCUtil.getConnection();
                PreparedStatement productStatement = conn.prepareStatement(productSql);
                PreparedStatement batchStatement = conn.prepareStatement(batchSql);
        ) {
            // Lấy thông tin sản phẩm từ bảng Products
            productStatement.setInt(1, id);
            try (ResultSet productResultSet = productStatement.executeQuery()) {
                if (productResultSet.next()) {
                    // Tạo đối tượng Product từ dữ liệu trong ResultSet
                    product = new Product(
                            productResultSet.getInt("id"),
                            productResultSet.getString("name"),
                            productResultSet.getDouble("price"),
                            productResultSet.getString("image"),
                            productResultSet.getString("description"),
                            new Category(productResultSet.getInt("category_id")),
                            new ArrayList<>(),
                            new ArrayList<>()
                    );

                    // Lấy danh sách các Batch từ bảng Batch
                    batchStatement.setInt(1, id);
                    try (ResultSet batchResultSet = batchStatement.executeQuery()) {
                        while (batchResultSet.next()) {
                            // Tạo đối tượng Batch và thêm vào danh sách của Product
                            Provider provider = new Provider(
                                    batchResultSet.getInt("provider_id"),
                                    batchResultSet.getString("provider_name"),
                                    batchResultSet.getString("provider_address")
                            );
                            Account adminCreate = new Account();
                            adminCreate.setId(batchResultSet.getInt("admin_id"));
                            adminCreate.setName(batchResultSet.getString("admin_name"));
                            int quantity = batchResultSet.getInt("currentQuantity");
                            Batch batch = new Batch(
                                    batchResultSet.getInt("id"),
                                    batchResultSet.getString("name"),
                                    batchResultSet.getDate("manufacturingDate"),
                                    batchResultSet.getDate("expiryDate"),
                                    batchResultSet.getDate("dateOfImporting"),
                                    batchResultSet.getInt("quantity"),
                                    batchResultSet.getInt("currentQuantity"),
                                    batchResultSet.getDouble("priceImport"),
                                    provider,
                                    adminCreate
                            );
                            product.addBatch(batch);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public static List<Product> pagingProduct(int cid, String sort, String type) {
        List<Product> list = new ArrayList<>();
        String query = "";
        String orderByClause = " ORDER BY " + sort + " " + type + " ";
        switch (cid) {
            case 0:
                query = "SELECT * FROM Products" + orderByClause;
                break;
            case 1:
                query = "SELECT * FROM Products WHERE category_id = 1" + orderByClause;
                break;
            case 2:
                query = "SELECT * FROM Products WHERE category_id = 2" + orderByClause;
                break;
            case 3:
                query = "SELECT * FROM Products WHERE category_id = 3" + orderByClause;
                break;
            case 4:
                query = "SELECT * FROM Products WHERE category_id = 4" + orderByClause;
                break;
            case 5:
                query = "SELECT * FROM Products WHERE oldPrice > price";
                break;
            default:
                break;
        }
        try {
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3),rs.getDouble(4), rs.getString(5), rs.getString(6),
                        new Category(rs.getInt(7)),rs.getInt(8)));
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ
            System.out.println("không lấy được sản phẩm");
        }
        return list;
    }

    public static List<Product> getFilteredProducts(Integer categoryId, Integer idProvider, Double startPrice, Double endPrice) {
        List<Product> list = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT DISTINCT p.* FROM Products p ");
        query.append("LEFT JOIN Batch b ON p.id = b.product_id ");
        query.append("LEFT JOIN Providers pr ON b.provider_id = pr.id ");
        query.append("LEFT JOIN Category c ON p.category_id = c.id ");
        query.append("WHERE 1=1 "); // Bắt đầu phần điều kiện với "WHERE 1=1" để dễ dàng thêm điều kiện động

        if (categoryId != null) {
            query.append("AND p.category_id = ? ");
        }
        if (idProvider != null) {
            query.append("AND pr.id = ? ");
        }
        if (startPrice != null && endPrice != null) {
            query.append("AND p.price BETWEEN ? AND ? ");
        }

        query.append("ORDER BY p.id"); // Đảm bảo có từ khóa ORDER BY

        try {
            PreparedStatement ps = connection.prepareStatement(query.toString());

            int parameterIndex = 1;

            if (categoryId != null) {
                ps.setInt(parameterIndex++, categoryId);
            }
            if (idProvider != null) {
                ps.setInt(parameterIndex++, idProvider);
            }
            if (startPrice != null && endPrice != null) {
                ps.setDouble(parameterIndex++, startPrice);
                ps.setDouble(parameterIndex++, endPrice);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"), // Giả định rằng cột "id" là cột đầu tiên
                        rs.getString("name"), // Giả định rằng cột "name" là cột thứ hai
                        rs.getDouble("price"), // Giả định rằng cột "price" là cột thứ ba
                        rs.getString("image"), // Giả định rằng cột "image" là cột thứ tư
                        rs.getString("description"), // Giả định rằng cột "description" là cột thứ năm
                        new Category(rs.getInt("category_id")) // Giả định rằng cột "category_id" là cột thứ sáu
                ));
            }
        } catch (Exception e) {
            // Xử lý exception
            e.printStackTrace();
        }
        return list;
    }


    public static List<Product> getListProducts() {
        List<Product> list = new ArrayList<>();
        String query = "       SELECT *\n" +
                "FROM Products\n" +
                "WHERE id IN (\n" +
                "    SELECT DISTINCT product_id\n" +
                "    FROM Batch\n" +
                "    WHERE expiryDate > CURDATE()\n" +
                ");";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getString(5),
                        new Category(rs.getInt(6))));
            }
        } catch (Exception e) {

        }
        return list;
    }

    public static void updateProductAndBatches(Product product, int id) throws SQLException {
        String updateProductSQL = "UPDATE Products SET name = ?, price = ?, image = ?, description = ?, category_id = ?  WHERE id = ?";

        try {
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement psProduct = conn.prepareStatement(updateProductSQL);
            psProduct.setString(1, product.getName());
            psProduct.setDouble(2, product.getPrice());
            psProduct.setString(3, product.getImage());
            psProduct.setString(4, product.getDescription());
            psProduct.setInt(5, product.getCategory().getId());
            psProduct.setInt(6, product.getId());
            psProduct.executeUpdate();



        } catch (SQLException e) {
            e.printStackTrace();
            // Rollback trong trường hợp có lỗi xảy ra
        }
    }

    public static List<Provider> getListProviderByIdP(int id) {
        List<Provider> list = new ArrayList<>();
        String sql = "SELECT DISTINCT p.id, p.name, p.address " +
                "FROM Providers p " +
                "JOIN Batch b ON p.id = b.provider_id " +
                "WHERE b.product_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Provider(rs.getInt("id"), rs.getString("name"), rs.getString("address")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //	public static Discount getDiscountByCode(String code) {
//		String sql = "SELECT * FROM Discounts WHERE code=?";
//		try {
//			Connection con = JDBCUtil.getConnection();
//			PreparedStatement ps = connectionprepareStatement(sql);
//			ps.setString(1, code);
//			ResultSet rs = ps.executeQuery();
//			if (rs.next()) {
//				Discount discount = new Discount();
//				discount.setId(rs.getInt("id"));
//				discount.setCode(rs.getString("code"));
//				discount.setDiscountType(rs.getString("discount_type"));
//				discount.setProductId(rs.getInt("product_id"));
//				discount.setCategoryId(rs.getInt("category_id"));
//				discount.setDiscountValue(rs.getDouble("discount_value"));
//				discount.setStartDate(rs.getDate("start_date"));
//				discount.setEndDate(rs.getDate("end_date"));
//				return discount;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
    public static void incrementViewCount(int id){
        String sql = "UPDATE Products SET viewCount = viewCount + 1 WHERE id = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
        }catch (Exception e){

        }
    }
    public static List<Product> getListProductByIdCategory(int id){
        List<Product> productList = new ArrayList<>();
        String sql = "Select * from Products where category_id=?";
        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                productList.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3),rs.getDouble(4), rs.getString(5), rs.getString(6),
                        new Category(rs.getInt(7)),rs.getInt(8)));
            }
        }catch (Exception e){

        }
        return productList;
    }
    public static void insertFavoriteProduct(int user_id, int product_id){
        String sql ="INSERT INTO FavoriteProducts (user_id, product_id) VALUES (?,?)";
        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,user_id);
            ps.setInt(2,product_id);
            ps.executeUpdate();
        }catch (Exception e){

        }
    }
    public static void deleteFavoriteProduct(int user_id, int product_id){
        String sql = "delete from FavoriteProducts where user_id=? and product_id=?";
        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,user_id);
            ps.setInt(2,product_id);
            ps.executeUpdate();
        }catch (Exception e){

        }
    }
    public static boolean isProductFavorited(int user_id, int product_id) {
        String sql = "SELECT COUNT(*) FROM FavoriteProducts WHERE user_id = ? AND product_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, user_id);
            ps.setInt(2, product_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static List<Product> listFavoriteProducts(int user_id) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.price, p.weight, p.image, p.description, p.category_id, p.viewCount " +
                "FROM FavoriteProducts fp " +
                "JOIN Products p ON fp.product_id = p.id " +
                "WHERE fp.user_id = ?";

        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3),rs.getDouble(4), rs.getString(5), rs.getString(6),
                        new Category(rs.getInt(7)),rs.getInt(8)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }
    public static int getTotalQuantityP(int id) {
        String sql = "SELECT SUM(currentQuantity) AS totalCurrentQuantity " +
                "FROM Batch " +
                "WHERE product_id = ? " +
                "AND expiryDate > CURDATE()";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("totalCurrentQuantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<Product> getListProductBatch() {
        List<Product> listProduct = new ArrayList<>();
        String sql = "SELECT " +
                "    p.id AS product_id, " +
                "    p.name AS product_name, " +
                "    p.price, " +
                "    p.image, " +
                "    b.dateOfImporting, " +
                "    b.quantity, " +
                "    b.id AS batch_id, " +
                "    b.name AS batch_name " +
                "FROM " +
                "    Products p " +
                "JOIN " +
                "    Batch b ON p.id = b.product_id " +
                "WHERE " +
                "    b.dateOfImporting >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
                "    AND b.dateOfImporting < CURDATE() " +
                "ORDER BY " +
                "    b.dateOfImporting DESC;";

        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery(); // Thực thi truy vấn

            while (rs.next()) {
                // Tạo đối tượng Product
                Product product = new Product();
                product.setId(rs.getInt("product_id"));
                product.setName(rs.getString("product_name"));
                product.setPrice(rs.getDouble("price"));
                product.setImage(rs.getString("image"));

                // Khởi tạo danh sách batches nếu chưa được khởi tạo
                if (product.getBatches() == null) {
                    product.setBatches(new ArrayList<>());
                }

                // Tạo đối tượng Batch và thiết lập các thuộc tính
                Batch batch = new Batch();
                batch.setDateOfImporting(rs.getDate("dateOfImporting"));
                batch.setQuantity(rs.getInt("quantity"));
                batch.setId(rs.getInt("batch_id"));
                batch.setName(rs.getString("batch_name"));

                // Thêm Batch vào Product
                product.getBatches().add(batch); // Giả sử có phương thức getBatches() trong Product

                // Thêm sản phẩm vào danh sách
                listProduct.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }

        return listProduct; // Trả về danh sách sản phẩm
    }
    public static List<Product> getListProductBatch1() {
        List<Product> listProduct = new ArrayList<>();
        String sql = "SELECT " +
                "    p.id AS product_id, " +
                "    p.name AS product_name, " +
                "    p.price, " +
                "    p.image, " +
                "    b.dateOfImporting, " +
                "    b.quantity, " +
                "    b.id AS batch_id, " +
                "    b.name AS batch_name " +
                "FROM " +
                "    Products p " +
                "JOIN " +
                "    Batch b ON p.id = b.product_id " +
                "WHERE " +
                "    b.dateOfImporting = CURDATE() " +
                "ORDER BY " +
                "    b.dateOfImporting DESC;";

        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery(); // Thực thi truy vấn

            while (rs.next()) {
                // Tạo đối tượng Product
                Product product = new Product();
                product.setId(rs.getInt("product_id"));
                product.setName(rs.getString("product_name"));
                product.setPrice(rs.getDouble("price"));
                product.setImage(rs.getString("image"));

                // Khởi tạo danh sách batches nếu chưa được khởi tạo
                if (product.getBatches() == null) {
                    product.setBatches(new ArrayList<>());
                }

                // Tạo đối tượng Batch và thiết lập các thuộc tính
                Batch batch = new Batch();
                batch.setDateOfImporting(rs.getDate("dateOfImporting"));
                batch.setQuantity(rs.getInt("quantity"));
                batch.setId(rs.getInt("batch_id"));
                batch.setName(rs.getString("batch_name"));

                // Thêm Batch vào Product
                product.getBatches().add(batch); // Giả sử có phương thức getBatches() trong Product

                // Thêm sản phẩm vào danh sách
                listProduct.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }

        return listProduct; // Trả về danh sách sản phẩm
    }
    public static List<Product> getListProductTop() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.image, SUM(b.quantity - b.currentQuantity) AS products_sold " +
                "FROM Batch b JOIN Products p ON b.product_id = p.id " +
                "GROUP BY p.id, p.name, p.image " +
                "ORDER BY products_sold DESC LIMIT 10;";

        try {
            connection = JDBCUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setImage(rs.getString("image"));
                // Initialize the list of batches
                product.setBatches(new ArrayList<>());

                // Fetch batches for this product
                String batchSql = "SELECT b.id, b.name, b.quantity, b.currentQuantity FROM Batch b WHERE b.product_id = ?";
                try (PreparedStatement batchPs = connection.prepareStatement(batchSql)) {
                    batchPs.setInt(1, product.getId());
                    ResultSet batchRs = batchPs.executeQuery();

                    while (batchRs.next()) {
                        Batch batch = new Batch();
                        batch.setId(batchRs.getInt("id"));
                        batch.setName(batchRs.getString("name"));
                        batch.setQuantity(batchRs.getInt("quantity"));
                        batch.setCurrentQuantity(batchRs.getInt("currentQuantity"));
                        product.getBatches().add(batch); // Add batch to product
                    }
                    batchRs.close();
                }

                // Add the product to the list
                list.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public static void main(String[] args) throws SQLException {
    }

}