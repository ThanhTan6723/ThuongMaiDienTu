package security.key;

import model.Account;

public class PublicKeyForUser {
    private int id;
    private Account account;
    private String createTime;
    private String endTime;
    private String publicKey;

    public PublicKeyForUser() {

    }

    public PublicKeyForUser(int id, Account account, String createTime, String endTime, String publicKey) {
        this.id = id;
        this.account = account;
        this.createTime = createTime;
        this.endTime = endTime;
        this.publicKey = publicKey;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "PublicKeyForUser{" +
                "id=" + id +
                ", account=" + account +
                ", createTime='" + createTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }
}
