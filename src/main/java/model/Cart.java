package model;

import com.google.gson.Gson;
import dao.client.ProductDAO;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private List<OrderDetail> items;

    public Cart() {
        items = new ArrayList<>();
    }

    public Cart(List<OrderDetail> items) {
        super();
        this.items = items;
    }

    public List<OrderDetail> getItems() {
        return items;
    }

    public void setItems(List<OrderDetail> items) {
        this.items = items;
    }

    private OrderDetail getItemById(int id) {
        for (OrderDetail i : items) {
            if (i.getProduct().getId() == id) {
                return i;
            }
        }
        return null;
    }

    public int getQuantityById(int id) {
        return getItemById(id).getQuantity();
    }

    // them vao gio
    public void addItem(OrderDetail t) {
        // nếu đã có rồi
        if (getItemById(t.getProduct().getId()) != null) {
            OrderDetail m = getItemById(t.getProduct().getId());
            m.setQuantity(m.getQuantity() + t.getQuantity());
        } else {
            items.add(t);
        }
    }

    public void removeItem(int id) {
        if (getItemById(id) != null) {
            items.remove(getItemById(id));
        }
    }

    public double getTotalMoney() {
        double t = 0;
        for (OrderDetail i : items) {
            t += (i.getQuantity() * i.getProduct().getPrice());
        }
        return t;

    }

    public static Map<Integer, OrderDetail> readCartFromCookies(HttpServletRequest request, int accId) {
        Map<Integer, OrderDetail> cart = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().startsWith("cart_" + accId)) {
                    String value = cookie.getValue();
                    String[] parts = value.split("_");
                    int productId = Integer.parseInt(parts[0]);
                    int quantity = Integer.parseInt(parts[1]);
                    double price = Double.parseDouble(parts[2]);

                    Product product = ProductDAO.getProductById(productId);
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setProduct(product);
                    orderDetail.setProductPrice(product.getPrice());
                    orderDetail.setQuantity(quantity);
                    orderDetail.setPrice(price);

                    cart.put(productId, orderDetail);
                }
            }
        }
        return cart;
    }

    public static void writeCartToCookies(HttpServletRequest request, HttpServletResponse response, Map<Integer, OrderDetail> cart, int accId) {
        // Xóa tất cả cookies hiện tại liên quan đến giỏ hàng
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().startsWith("cart_")) {
//                    cookie.setMaxAge(0); // Xóa cookie bằng cách đặt thời gian sống thành 0
//                    response.addCookie(cookie); // Gửi lại cookie để trình duyệt xóa nó
//                }
//            }
//        }
        // Ghi lại tất cả các mục hiện tại trong giỏ hàng vào cookies
        for (Map.Entry<Integer, OrderDetail> entry : cart.entrySet()) {
            int productId = entry.getKey();
            OrderDetail orderDetail = entry.getValue();
            String value = productId + "_" + orderDetail.getQuantity() + "_" + orderDetail.getPrice();
            Cookie cookie = new Cookie("cart_" + accId + productId, value);
            cookie.setMaxAge(60 * 60 * 24 * 7); // 1 tuần
            response.addCookie(cookie);
        }
    }

    public static void deleteCartItemToCookies(HttpServletRequest request, HttpServletResponse response, int accId, int productId) {
        // Xóa tất cả cookies hiện tại liên quan đến giỏ hàng
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().startsWith("cart_" + accId + productId)) {
                    String value = cookie.getValue();
                    String[] parts = value.split("_");
                    int product_id = Integer.parseInt(parts[0]);
                    if (product_id == productId) {
                        cookie.setMaxAge(0); // Xóa cookie bằng cách đặt thời gian sống thành 0
                        response.addCookie(cookie); // Gửi lại cookie để trình duyệt xóa nó
                    }
                }
            }
        }
    }

    public static void deleteCartToCookies(HttpServletRequest request, HttpServletResponse response, int accId) {
        // Xóa tất cả cookies hiện tại liên quan đến giỏ hàng
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().startsWith("cart_" + accId)) {
                    cookie.setMaxAge(0); // Xóa cookie bằng cách đặt thời gian sống thành 0
                    response.addCookie(cookie); // Gửi lại cookie để trình duyệt xóa nó
                }
            }
        }
    }

//    public static void writeSizeCartToCookies(HttpServletResponse response, int sizeCart) {
//        Cookie cookie = new Cookie("size_cart_", String.valueOf(sizeCart));
//        cookie.setMaxAge(60 * 60 * 24 * 7); // 1 tuần
//        response.addCookie(cookie);
//    }
//
//    public static void readSizeCartToCookies(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().startsWith("size_cart_")) {
//                    String value = cookie.getValue();
//                    String[] parts = value.split("_");
//                    int size = Integer.parseInt(parts[2]);
//                }
//            }
//        }
//    }

}
