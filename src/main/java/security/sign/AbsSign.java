package security.sign;

import model.Account;
import model.Order;

public class AbsSign {
    private int id;
    private Account account;
    private Order order;
    private String createdTime;

    public AbsSign() {
    }

    public AbsSign(int id, Account account, Order order, String createdTime) {
        this.id = id;
        this.account = account;
        this.order = order;
        this.createdTime = createdTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "AbsSign{" +
                "id=" + id +
                ", account=" + account +
                ", order=" + order +
                ", createdTime='" + createdTime + '\'' +
                '}';
    }
}
