package model;

import java.util.Date;

public class Review {
    private int id;
    private String nameCommenter;
    private String phoneNumberCommenter;
    private Product productEvaluated;
    private int rating;
    private String comment;
    private String dateCreated;
    private String dateReply;
    private String image;
    private String reply;
    private boolean isAccept;

    public Review() {
    }

    public Review(int id, String nameCommenter, String phoneNumberCommenter, Product productEvaluated, int rating, String comment, String dateCreated, String image, String reply) {
        this.id = id;
        this.nameCommenter = nameCommenter;
        this.phoneNumberCommenter = phoneNumberCommenter;
        this.productEvaluated = productEvaluated;
        this.rating = rating;
        this.comment = comment;
        this.dateCreated = dateCreated;
        this.image = image;
        this.reply = reply;
    }

    public Review(int id, String nameCommenter, String phoneNumberCommenter, Product productEvaluated, int rating, String comment, String dateCreated, String image) {
        this.id = id;
        this.nameCommenter = nameCommenter;
        this.phoneNumberCommenter = phoneNumberCommenter;
        this.productEvaluated = productEvaluated;
        this.rating = rating;
        this.comment = comment;
        this.dateCreated = dateCreated;
        this.image = image;
    }

    public Review(int id, String nameCommenter, String phoneNumberCommenter, Product productEvaluated, int rating, String comment, String dateCreated, String dateReply, String image, String reply) {
        this.id = id;
        this.nameCommenter = nameCommenter;
        this.phoneNumberCommenter = phoneNumberCommenter;
        this.productEvaluated = productEvaluated;
        this.rating = rating;
        this.comment = comment;
        this.dateCreated = dateCreated;
        this.dateReply = dateReply;
        this.image = image;
        this.reply = reply;
    }

    public Review(int id, String nameCommenter, String phoneNumberCommenter, Product productEvaluated, int rating, String comment, String dateCreated, String dateReply, String image, String reply, boolean isAccept) {
        this.id = id;
        this.nameCommenter = nameCommenter;
        this.phoneNumberCommenter = phoneNumberCommenter;
        this.productEvaluated = productEvaluated;
        this.rating = rating;
        this.comment = comment;
        this.dateCreated = dateCreated;
        this.dateReply = dateReply;
        this.image = image;
        this.reply = reply;
        this.isAccept = isAccept;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameCommenter() {
        return nameCommenter;
    }

    public void setNameCommenter(String nameCommenter) {
        this.nameCommenter = nameCommenter;
    }

    public String getPhoneNumberCommenter() {
        return phoneNumberCommenter;
    }

    public void setPhoneNumberCommenter(String phoneNumberCommenter) {
        this.phoneNumberCommenter = phoneNumberCommenter;
    }

    public Product getProductEvaluated() {
        return productEvaluated;
    }

    public void setProductEvaluated(Product productEvaluated) {
        this.productEvaluated = productEvaluated;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateReply() {
        return dateReply;
    }

    public void setDateReply(String dateReply) {
        this.dateReply = dateReply;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public boolean isAccept() {
        return isAccept;
    }

    public void setAccept(boolean accept) {
        isAccept = accept;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", nameCommenter='" + nameCommenter + '\'' +
                ", phoneNumberCommenter='" + phoneNumberCommenter + '\'' +
                ", productEvaluated=" + productEvaluated +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateReply='" + dateReply + '\'' +
                ", image='" + image + '\'' +
                ", reply='" + reply + '\'' +
                ", isAccept=" + isAccept +
                '}';
    }
}