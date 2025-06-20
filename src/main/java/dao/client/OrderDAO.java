package dao.client;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import model.*;

public class OrderDAO {
    //    private static Connection connection = JDBCUtil.getConnection();
    public static List<Product> getListProductsById(int pid) {
        List<Product> listProducts = new ArrayList<Product>();
        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "SELECT * FROM Products where id=? ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, pid);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                listProducts.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getString(5),
                        ProductDAO.getCategoryById1(rs.getInt(6))));

            }
            JDBCUtil.closeConnection(connection);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listProducts;

    }

    public static Category getCategory(int cid) {
        try {
            String sql = "SELECT * FROM Category where id=? ";
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, cid);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                return new Category(rs.getInt(1), rs.getString(2));
            }

            JDBCUtil.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public static List<Order> getListOrdersById(int accountId) {
        List<Order> listOrders = new ArrayList<>();

        try {
            String sql = "SELECT * FROM Orders where account_id=? ";
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId); // Assuming you want only shipped orders

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                listOrders.add(new Order(rs.getInt(1), rs.getString(2), rs.getString(3), AccountDAO.getAccountById(rs.getInt(4)), rs.getString(5), rs.getString(6),
                        rs.getDouble(7), rs.getDouble(8), rs.getString(9), rs.getString(10), rs.getString(11)));
            }

            JDBCUtil.closeConnection(connection);

        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception properly
        }

        return listOrders;
    }

    public static List<OrderDetail> getOrderDetailByBid(int orderId) {
        List<OrderDetail> list = new ArrayList<>();
        String query = "SELECT od.id, od.quantity, od.product_price, od.priceWithQuantity " +
                "FROM OrderDetails od " +
                "JOIN Orders o ON od.order_id = o.id " +
                "WHERE od.order_id = ?";

        try {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            // Lấy danh sách sản phẩm một lần trước khi vào vòng lặp
            List<Product> products = OrderDAO.getListProductByOrderId(orderId);
            int i = 0;

            while (rs.next()) {
                if (i >= products.size()) {
                    throw new IndexOutOfBoundsException("Không đủ sản phẩm trong danh sách");
                }

                list.add(new OrderDetail(
                        rs.getInt(1), // id
                        OrderDAO.getOrderforOrderDetail(orderId), // order
                        products.get(i), // product
                        rs.getDouble(4), // priceWithQuantity
                        rs.getInt(2), // quantity
                        rs.getDouble(3) // product_price
                ));
                i++;
            }

            JDBCUtil.closeConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public static List<OrderDetail> getOrderDetailsByStatus(String status) {
        List<OrderDetail> listOrderdetails = new ArrayList<>();

        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "\r\n"
                    + "SELECT O.id , P.id ,O.orderStatus,O.address,O.account_id,O.consignee_name,O.consignee_phone,O.ship,O.discountValue, P.name, P.price,OD.quantity,OD.priceWithQuantity , P.image, P.description, P.category_Id,O.totalMoney,O.booking_date,O.delivery_date\r\n"
                    + "FROM Orders O\r\n" + "INNER JOIN OrderDetails OD ON O.id = OD.order_id\r\n"
                    + "INNER JOIN Products P ON OD.product_id = P.id\r\n"
                    + "INNER JOIN Category C ON P.category_id = C.id\r\n" + "WHERE O.orderStatus = ?\r\n"
                    + "ORDER BY O.id DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setBookingDate(rs.getString("booking_date"));
                order.setConsigneeName(rs.getString("consignee_name"));
                order.setConsigneePhone(rs.getString("consignee_phone"));
                order.setDiliveryDate(rs.getString("delivery_date"));
                order.setShip(rs.getDouble("ship"));
                order.setDiscountValue(rs.getDouble("discountValue"));

                order.setTotalMoney(rs.getFloat("totalMoney"));
                order.setOrderStatus(rs.getString("orderStatus"));
                order.setAddress(rs.getString("address"));
                order.setAccount(AccountDAO.getAccountById(rs.getInt("account_id")));

                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setImage(rs.getString("image"));
                product.setDescription(rs.getString("description"));

                Category category = new Category();
                category.setId(rs.getInt("id"));

                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(product);
                orderDetail.setQuantity(rs.getInt("quantity"));
                orderDetail.setProductPrice(rs.getDouble("price"));
                orderDetail.setPrice(rs.getDouble("priceWithQuantity"));

                listOrderdetails.add(orderDetail);
            }

            JDBCUtil.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOrderdetails;
    }


    public static Order getOrderforOrderDetail(int orderId) {
        String query = "SELECT o.id, o.booking_date, o.delivery_date, o.account_id, o.consignee_name, " +
                "o.consignee_phone, o.ship, o.discountValue, o.totalMoney, o.address, " +
                "o.orderNotes, o.orderStatus, o.payment_id " +
                "FROM Orders o " +
                "JOIN OrderDetails od ON od.order_id = o.id " +
                "WHERE od.order_id = ?";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Order(
                        rs.getInt("id"),
                        rs.getString("booking_date"),
                        rs.getString("delivery_date"),
                        AccountDAO.getAccountById(rs.getInt("account_id")),
                        rs.getString("consignee_name"),
                        rs.getString("consignee_phone"),
                        rs.getDouble("ship"),
                        rs.getDouble("discountValue"),
                        rs.getDouble("totalMoney"),
                        rs.getString("address"),
                        rs.getString("orderNotes"),
                        rs.getString("orderStatus"),
                        OrderDAO.getPayment(rs.getInt("payment_id"))
                );
            }
            JDBCUtil.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static List<Product> getListProductByOrderId(int id) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * from Products p join OrderDetails od on p.id = od.product_id where od.order_id = ? ";
        try {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getString(5), rs.getString(6),
                        new Category(rs.getInt(7)), rs.getInt(8))
                );

            }
            JDBCUtil.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void insertOrderdetail(OrderDetail oderDetail) {
        String query = "INSERT INTO OrderDetails(order_id,product_id, product_price,quantity,priceWithQuantity) VALUES (?,?,?,?,?) ";
        try {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, oderDetail.getOrder().getId());
            ps.setInt(2, oderDetail.getProduct().getId());
            ps.setDouble(3, oderDetail.getProductPrice());
            ps.setInt(4, oderDetail.getQuantity());
            ps.setDouble(5, oderDetail.getPrice());

            ps.executeUpdate();
            System.out.println("insert orderdetail success");
            JDBCUtil.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertOrder(Order order) {
//		List<Product> list = new ArrayList<>();
        String sql = "INSERT INTO Orders(booking_date,account_id,consignee_name, consignee_phone,ship, discountValue, totalMoney, address,orderNotes,OrderStatus,payment_id) VALUES (?,?,?,?, ?,?,?,?,?,?,?) ";
        try {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, order.getBookingDate());
            ps.setInt(2, order.getAccount().getId());
            ps.setString(3, order.getConsigneeName());
            ps.setString(4, order.getConsigneePhone());
            ps.setDouble(5, order.getShip());
            ps.setDouble(6, order.getDiscountValue());
            ps.setDouble(7, order.getTotalMoney());
            ps.setString(8, order.getAddress());
            ps.setString(9, order.getOrderNotes());
            ps.setString(10, order.getOrderStatus());
            ps.setInt(11, order.getPayment().getId());
            ps.executeUpdate();
            System.out.println("insert order success");
            JDBCUtil.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCurrentIdBill(Order order) {
        String query = "SELECT id as LastID FROM Orders ORDER BY id DESC LIMIT 1";
        try {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                order.setId(rs.getInt("LastID"));
            }
            JDBCUtil.closeConnection(connection);

        } catch (Exception e) {
            // Xử lý ngoại lệ
            e.printStackTrace();
        }
    }


    public static void updateOrders(Order order) {
        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "UPDATE Orders " + "SET" + " totalMoney=? " + "where id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setDouble(1, order.getTotalMoney());
            ps.setInt(2, order.getId());
            ps.executeUpdate();
            JDBCUtil.closeConnection(connection);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public static List<Order> getAllOrders(String status) {
        List<Order> lists = new ArrayList<Order>();

        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "SELECT * FROM Orders where OrderStatus=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lists.add(new Order(rs.getInt(1), rs.getString(2), rs.getString(3), AccountDAO.getAccountById(rs.getInt(4)), rs.getString(5), rs.getString(6),
                        rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs.getString(10), rs.getString(11), rs.getString(12), OrderDAO.getPayment(rs.getInt(13))));
            }
            JDBCUtil.closeConnection(connection);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return lists;
    }

    public static List<Order> getAllOrder() {
        List<Order> lists = new ArrayList<Order>();

        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "SELECT * FROM Orders";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lists.add(new Order(rs.getInt(1), rs.getString(2), rs.getString(3), AccountDAO.getAccountById(rs.getInt(4)), rs.getString(5), rs.getString(6),
                        rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs.getString(10), rs.getString(11), rs.getString(12), OrderDAO.getPayment(rs.getInt(13))));
            }
            JDBCUtil.closeConnection(connection);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return lists;
    }

    public static Payment getPayment(int id) {
        Payment payment = null;
        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "SELECT * FROM Payment  where id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                payment = new Payment(rs.getInt(1), rs.getString(2));
            }
            JDBCUtil.closeConnection(connection);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return payment;
    }

    public static List<Order> getAllOrders() {
        List<Order> lists = new ArrayList<Order>();

        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "SELECT * FROM Orders ";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lists.add(new Order(rs.getInt(1), rs.getString(2), rs.getString(3), AccountDAO.getAccountById(rs.getInt(4)), rs.getString(5), rs.getString(6),
                        rs.getDouble(7), rs.getDouble(8), rs.getString(9), rs.getString(10), rs.getString(11)));
            }
            JDBCUtil.closeConnection(connection);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return lists;
    }

//	public static List<OrderDetail> getAllOrderDetails() {
//		List<OrderDetail> lists = new ArrayList<OrderDetail>();
//
//		try {
//			Connection connect = JDBCUtil.getConnection();
//
//			String sql = "SELECT * FROM OrderDetails";
//			PreparedStatement ps = connect.prepareStatement(sql);
//			ResultSet rs = ps.executeQuery();
//			while (rs.next()) {
//				lists.add(new OrderDetail(rs.getInt(1), OrderDAO.getOrderforOrderDetail(rs.getInt(2)),
//						OrderDAO.getProductBybid(rs.getInt(3)).get(0), rs.getInt(4), rs.getDouble(5)));
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		return lists;
//	}

    public static void updateOrderStatus(int id, String status) {
        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "Update Orders SET " + "orderStatus=? " + "where id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, id);

            ps.executeUpdate();
            JDBCUtil.closeConnection(connection);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    public static void updateDeliveryDate(int id) {
        String deliveryDate = LocalDateTime.now().toString();
        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "Update Orders SET " + "delivery_date = ? " + "where id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, deliveryDate);
            ps.setInt(2, id);

            ps.executeUpdate();
            JDBCUtil.closeConnection(connection);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }


    public static List<OrderDetail> getListOrder(int id) {
        List<OrderDetail> listOrderdetails = new ArrayList<>();

        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "\r\n"
                    + "SELECT O.id , P.id ,O.orderStatus,O.address,O.account_id,O.consignee_name,O.consignee_phone,O.ship,O.discountValue, P.name, P.price,OD.quantity,OD.priceWithQuantity , P.image, P.description, P.category_Id,O.totalMoney,O.booking_date,O.delivery_date\r\n"
                    + "FROM Orders O\r\n" + "INNER JOIN OrderDetails OD ON O.id = OD.order_id\r\n"
                    + "INNER JOIN Products P ON OD.product_id = P.id\r\n"
                    + "INNER JOIN Category C ON P.category_id = C.id\r\n" + "WHERE O.account_id = ?\r\n"
                    + "ORDER BY O.id DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setBookingDate(rs.getString("booking_date"));
                order.setConsigneeName(rs.getString("consignee_name"));
                order.setConsigneePhone(rs.getString("consignee_phone"));
                order.setDiliveryDate(rs.getString("delivery_date"));
                order.setShip(rs.getDouble("ship"));
                order.setDiscountValue(rs.getDouble("discountValue"));

                order.setTotalMoney(rs.getFloat("totalMoney"));
                order.setOrderStatus(rs.getString("orderStatus"));
                order.setAddress(rs.getString("address"));
                order.setAccount(AccountDAO.getAccountById(rs.getInt("account_id")));

                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setImage(rs.getString("image"));
                product.setDescription(rs.getString("description"));

                Category category = new Category();
                category.setId(rs.getInt("id"));

                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(product);
                orderDetail.setQuantity(rs.getInt("quantity"));
                orderDetail.setProductPrice(rs.getDouble("price"));
                orderDetail.setPrice(rs.getDouble("priceWithQuantity"));

                listOrderdetails.add(orderDetail);
            }
            JDBCUtil.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOrderdetails;
    }

    public static int getQuantityWithOderId(int orderId) {
        int sum = 0;
        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "SELECT SUM(quantity) AS totalQuantity\r\n" + "FROM OrderDetails\r\n" + "WHERE order_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sum = rs.getInt(1);
            }
            JDBCUtil.closeConnection(connection);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return sum;
    }

    public static int deleteOrderDetail(int id) {
        int re = 0;

        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "DELETE from OrderDetails where order_id=?";
            PreparedStatement pre = connection.prepareStatement(sql);
            pre.setInt(1, id);
            re = pre.executeUpdate();
            JDBCUtil.closeConnection(connection);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return re;

    }

    public static int deleteOrder(int id) {
        int re = 0;

        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "DELETE from Orders where id=?";
            PreparedStatement pre = connection.prepareStatement(sql);
            pre.setInt(1, id);
            re = pre.executeUpdate();
            JDBCUtil.closeConnection(connection);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return re;


    }

    public static void main(String[] args) {
//        System.out.println(getCategory(1));
        System.out.println(getOrderDetailByBid(1));
//        System.out.println(getQuantityWithOderId(3));
//        System.out.println(getPayment(3));
        updateDeliveryDate(17);
    }
}
