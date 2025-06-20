package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Batch {
    private int id;
    private String name;
    private Date manufacturingDate;// ngày sản xuất
    private Date expiryDate;
    private Date dateOfImporting; // Ngày nhập hàng
    private int quantity;//sản phẩm nhập vào
    private int currentQuantity;//sản phẩm hiện tại
    private double priceImport;
    private Provider provider;
    private Account adminCreate;

    public Batch() {
    }

    public Batch(String name, Date manufacturingDate, Date expiryDate, Date dateOfImporting, int quantity, int currentQuantity, double priceImport, Provider provider,Account account) {
        this.name = name;
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.dateOfImporting = dateOfImporting;
        this.quantity = quantity;
        this.currentQuantity = currentQuantity;
        this.priceImport = priceImport;
        this.provider = provider;
        this.adminCreate = account;
    }

    public Batch(int id, String name, Date manufacturingDate, Date expiryDate, Date dateOfImporting, int quantity, double priceImport, Provider provider) {
        this.id = id;
        this.name = name;
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.dateOfImporting = dateOfImporting;
        this.quantity = quantity;
        this.priceImport = priceImport;
        this.provider = provider;
    }

    public Batch(int id,  String name, Date manufacturingDate, Date expiryDate, Date dateOfImporting, int quantity,int currentQuantity, double priceImport, Provider provider, Account adminCreate) {
        this.id = id;
        this.name = name;
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.dateOfImporting = dateOfImporting;
        this.quantity = quantity;
        this.currentQuantity = currentQuantity;
        this.priceImport = priceImport;
        this.provider = provider;
        this.adminCreate = adminCreate;

    }

    public Batch(int id, String name, Date manufacturingDate, Date expiryDate, Date dateOfImporting, int quantity, double priceImport, Provider provider, Account adminCreate) {
        this.id = id;
        this.name = name;
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.dateOfImporting = dateOfImporting;
        this.quantity = quantity;
        this.priceImport = priceImport;
        this.provider = provider;
        this.adminCreate = adminCreate;
    }

    public Batch(int id,  String name, Date manufacturingDate, Date expiryDate, Date dateOfImporting, int quantity, int currentQuantity) {
        this.id = id;
        this.name = name;
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.dateOfImporting = dateOfImporting;
        this.quantity = quantity;
        this.currentQuantity = currentQuantity;
    }

    public Batch(Date manufacturingDate, Date expiryDate, Date dateOfImporting, int quantity, int currentQuantity) {
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.dateOfImporting = dateOfImporting;
        this.quantity = quantity;
        this.currentQuantity = currentQuantity;
    }

    public Batch(int id,  String name, Date manufacturingDate, Date expiryDate, Date dateOfImporting, int quantity, double priceImport) {
        this.id = id;
        this.name = name;
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.dateOfImporting = dateOfImporting;
        this.quantity = quantity;
        this.priceImport = priceImport;
    }

    public Batch(int id, String name, Date dateOfImporting, int currentQuantity) {
        this.id = id;
        this.name = name;
        this.dateOfImporting = dateOfImporting;
        this.currentQuantity = currentQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(Date manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getDateOfImporting() {
        return dateOfImporting;
    }

    public void setDateOfImporting(Date dateOfImporting) {
        this.dateOfImporting = dateOfImporting;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public double getPriceImport() {
        return priceImport;
    }

    public void setPriceImport(double priceImport) {
        this.priceImport = priceImport;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Account getAdminCreate() {

        return adminCreate;
    }

    public void setAdminCreate(Account adminCreate) {
        this.adminCreate = adminCreate;
    }


    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return "Batch{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manufacturingDate=" + (manufacturingDate != null ? sdf.format(manufacturingDate) : "null") +
                ", expiryDate=" + (expiryDate != null ? sdf.format(expiryDate) : "null") +
                ", dateOfImporting=" + (dateOfImporting != null ? sdf.format(dateOfImporting) : "null") +
                ", quantity=" + quantity +
                ", currentQuantity=" + currentQuantity +
                ", priceImport=" + priceImport +
                ", provider=" + provider +
                ", adminCreate=" + adminCreate +
                '}';
    }

    public static void main(String[] args) {
        System.out.println(new Batch(1,"hihi",null,null,null,100,200,12,null,null));
    }
}