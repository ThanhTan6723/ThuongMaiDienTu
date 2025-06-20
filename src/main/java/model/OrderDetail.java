package model;

public class OrderDetail {
    private int id;
    private Order order;
    private Product product;
    private double productPrice;
    private int quantity;
    private double price;

    public OrderDetail() {
        super();
    }

    public OrderDetail(int id, Order order, Product product, double productPrice, int quantity, double price) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", order=" + order +
                ", product=" + product +
                ", productPrice=" + productPrice +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
