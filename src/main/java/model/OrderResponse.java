package model;

public class OrderResponse {
    private OrderDetail orderDetail;
    private String verifyStatus;

    public OrderResponse(OrderDetail orderDetail, String verifyStatus) {
        this.orderDetail = orderDetail;
        this.verifyStatus = verifyStatus;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }
}
