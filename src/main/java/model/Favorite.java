package model;

import java.time.LocalDateTime;

public class Favorite {
    private int id;
    private Account user_id;
    private Product product_id;

    public Favorite(int id, Account user_id, Product product_id) {
        this.id = id;
        this.user_id = user_id;
        this.product_id = product_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Account getUser_id() {
        return user_id;
    }

    public void setUser_id(Account user_id) {
        this.user_id = user_id;
    }

    public Product getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Product product_id) {
        this.product_id = product_id;
    }


    @Override
    public String toString() {
        return "Favorite{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", product_id=" + product_id +
                '}';
    }
}
