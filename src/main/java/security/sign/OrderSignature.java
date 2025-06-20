package security.sign;

import model.Account;
import model.Order;

public class OrderSignature extends AbsSign {
    private String digitalSignature;

    public OrderSignature(){

    }

    public OrderSignature(int id, Account account, Order order, String createdTime, String digitalSignature) {
        super(id, account, order, createdTime);
        this.digitalSignature = digitalSignature;
    }

    public String getDigitalSignature() {
        return digitalSignature;
    }

    @Override
    public String toString() {
        return "OrderSignature{" +
                "digitalSignature='" + digitalSignature + '\'' +
                '}';
    }

    public void setDigitalSignature(String digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

}
