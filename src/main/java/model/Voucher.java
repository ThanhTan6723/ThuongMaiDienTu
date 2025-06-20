package model;

import java.math.BigDecimal;
import java.util.Date;

public class Voucher {
    private int id;
    private String code;
    private DiscountType discountType;
    private BigDecimal discountPercentage;
    private Product product;
    private Category category;
    private int quantity;
    private Date startDate;
    private Date endDate;
    private double minimumOrderValue;
    private double maximumDiscount;
    private boolean isActive;

    public Voucher() {
        this.isActive = true;
    }

    public Voucher(int id, String code, DiscountType discountType, BigDecimal discountPercentage, Product product, Category category, int quantity, Date startDate, Date endDate, boolean isActive) {
        this.id = id;
        this.code = code;
        this.discountType = discountType;
        this.discountPercentage = discountPercentage;
        this.product = product;
        this.category = category;
        this.quantity = quantity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }

    public Voucher(int id, String code, DiscountType discountType, BigDecimal discountPercentage, Product product, Category category, int quantity, Date startDate, Date endDate, double minimumOrderValue, double maximumDiscount, boolean isActive) {
        this.id = id;
        this.code = code;
        this.discountType = discountType;
        this.discountPercentage = discountPercentage;
        this.product = product;
        this.category = category;
        this.quantity = quantity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.minimumOrderValue = minimumOrderValue;
        this.maximumDiscount = maximumDiscount;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getMinimumOrderValue() {
        return minimumOrderValue;
    }

    public void setMinimumOrderValue(double minimumOrderValue) {
        this.minimumOrderValue = minimumOrderValue;
    }

    public double getMaximumDiscount() {
        return maximumDiscount;
    }

    public void setMaximumDiscount(double maximumDiscount) {
        this.maximumDiscount = maximumDiscount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", discountType=" + discountType +
                ", discountPercentage=" + discountPercentage +
                ", product=" + product +
                ", category=" + category +
                ", quantity=" + quantity +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", minimumOrderValue=" + minimumOrderValue +
                ", maximumDiscount=" + maximumDiscount +
                ", isActive=" + isActive +
                '}';
    }
}
