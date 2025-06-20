<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ page isELIgnored="false" %>

<c:if test="${empty listProductOrder}">
    <div class="col-lg-12">
        <div class="empty" style="text-align: center;padding: 80px">
            <h4 style="color: #8f8f8f">Bạn chưa có đơn hàng nào</h4>
        </div>
    </div>
</c:if>
<c:if test="${not empty listProductOrder}">
    <div class="col-lg-12 col-md-6">
        <c:forEach items="${listProductOrder}" var="orderDetail" varStatus="loop">
            <c:if test="${orderDetail.order.id != currentOrderId}">
                <c:set var="currentOrderId" value="${orderDetail.order.id}"/>
                <div class="row" style="border-bottom: 1px dashed #e7e7e7;background-color: #fffcf5;height: 180px;">
                    <div class="col-lg-6">
                        <div class="checkout__input">
                            <div class="order-info">Mã đơn hàng: <span class="data" id="order-id">${orderDetail.order.id}</span></div>
                            <div class="order-info">Ngày đặt hàng: <span class="data">${orderDetail.order.bookingDate}</span></div>
                            <c:if test="${orderDetail.order.diliveryDate != null}">
                                <div class="order-info">Ngày giao hàng: <span class="data">${orderDetail.order.diliveryDate}</span></div>
                            </c:if>
                            <div class="order-info">Địa chỉ giao hàng: <span class="data">${orderDetail.order.address}</span></div>
                            <div class="order-info">Tổng tiền đơn hàng: <span class="data" style="color: red">${orderDetail.order.totalPrice}</span></div>
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <div class="checkout__input" style="text-align: right">
                            <button onclick="sendCancelRequest(${orderDetail.order.id})" class="btn btn-danger" style="margin-right: 20px;">Hủy đơn hàng</button>
                        </div>
                    </div>
                </div>
            </c:if>
            <div class="row" style="border-bottom: 1px dashed #e7e7e7;padding-bottom: 10px;">
                <div class="col-lg-2">
                    <div class="shoping__cart__item">
                        <img src="${orderDetail.product.image}" alt="">
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="shoping__cart__item">
                        <h6>${orderDetail.product.name}</h6>
                        <h6>Loại: ${orderDetail.product.type}</h6>
                        <h6>Số lượng: ${orderDetail.quantity}</h6>
                    </div>
                </div>
                <div class="col-lg-3">
                    <div class="shoping__cart__item">
                        <h6>Giá: ${orderDetail.product.price}</h6>
                    </div>
                </div>
                <div class="col-lg-3">
                    <div class="shoping__cart__item">
                        <h6>Thành tiền: <fmt:formatNumber value="${orderDetail.product.price * orderDetail.quantity}" type="currency" currencySymbol="₫"/></h6>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</c:if>
