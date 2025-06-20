package security.sign;

import model.Account;
import model.Order;

public class HashOrder extends AbsSign {
    private String hashValue;

    public HashOrder() {

    }

    public HashOrder(int id, Account account, Order order, String createdTime, String hashValue) {
        super(id, account, order, createdTime);
        this.hashValue = hashValue;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    @Override
    public String toString() {
        return "HashOrder{" +
                "hashValue='" + hashValue + '\'' +
                '}';
    }
}
