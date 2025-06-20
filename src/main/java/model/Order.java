package model;

import java.util.List;

public class Order {
    private int id;
    private String bookingDate;
    private String diliveryDate;
    private Account account;
    private String consigneeName;
    private String consigneePhone;
    private double ship;
    private double discountValue;
    private double totalMoney;
    private String address;
    private String orderNotes;
    private String orderStatus;
    private Payment payment;

    public Order() {
        super();
    }

    public Order(int id, String bookingDate, Account account, String consigneeName, String consigneePhone, double ship, double totalMoney, String address, String orderNotes, String orderStatus) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.account = account;
        this.consigneeName = consigneeName;
        this.consigneePhone = consigneePhone;
        this.ship = ship;
        this.totalMoney = totalMoney;
        this.address = address;
        this.orderNotes = orderNotes;
        this.orderStatus = orderStatus;
    }

    public Order(int id, String bookingDate, String diliveryDate, Account account, String consigneeName, String consigneePhone, double ship, double totalMoney, String address, String orderNotes, String orderStatus) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.diliveryDate = diliveryDate;
        this.account = account;
        this.consigneeName = consigneeName;
        this.consigneePhone = consigneePhone;
        this.ship = ship;
        this.totalMoney = totalMoney;
        this.address = address;
        this.orderNotes = orderNotes;
        this.orderStatus = orderStatus;
    }

    public Order(int id, String bookingDate, String diliveryDate, Account account, String consigneeName, String consigneePhone, double ship, double totalMoney, String address, String orderNotes, String orderStatus, Payment payment) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.diliveryDate = diliveryDate;
        this.account = account;
        this.consigneeName = consigneeName;
        this.consigneePhone = consigneePhone;
        this.ship = ship;
        this.totalMoney = totalMoney;
        this.address = address;
        this.orderNotes = orderNotes;
        this.orderStatus = orderStatus;
        this.payment = payment;
    }

    public Order(int id, String bookingDate, String diliveryDate, Account account, String consigneeName, String consigneePhone, double ship, double discountValue, double totalMoney, String address, String orderNotes, String orderStatus, Payment payment) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.diliveryDate = diliveryDate;
        this.account = account;
        this.consigneeName = consigneeName;
        this.consigneePhone = consigneePhone;
        this.ship = ship;
        this.discountValue = discountValue;
        this.totalMoney = totalMoney;
        this.address = address;
        this.orderNotes = orderNotes;
        this.orderStatus = orderStatus;
        this.payment = payment;
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

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderNotes() {
        return orderNotes;
    }

    public void setOrderNotes(String orderNotes) {
        this.orderNotes = orderNotes;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getDiliveryDate() {
        return diliveryDate;
    }

    public void setDiliveryDate(String diliveryDate) {
        this.diliveryDate = diliveryDate;
    }

    public double getShip() {
        return ship;
    }

    public void setShip(double ship) {
        this.ship = ship;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", bookingDate='" + bookingDate + '\'' +
                ", diliveryDate='" + diliveryDate + '\'' +
                ", account=" + account +
                ", consigneeName='" + consigneeName + '\'' +
                ", consigneePhone='" + consigneePhone + '\'' +
                ", ship=" + ship +
                ", discountValue=" + discountValue +
                ", totalMoney=" + totalMoney +
                ", address='" + address + '\'' +
                ", orderNotes='" + orderNotes + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", payment=" + payment +
                '}';
    }
}
