package model;

import dao.client.IModel;

public class Account implements IModel {
    private int id;
    private String name;
    private String password;
    private String email;
    private String telephone;
    private Role role;
    private int failed;
    private boolean isLocked;

    public Account() {
        // TODO Auto-generated constructor stub
    }

    public Account(int id, String name, String password, String email, String telephone, Role role, int failed, boolean isLocked) {

        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.telephone = telephone;
        this.role = role;
        this.failed = failed;
        this.isLocked = isLocked;
    }

    public Account(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Account(int anInt) {
    }

    public Account(String name) {
        this.name = name;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", role=" + role +
                ", failed=" + failed +
                ", isLocked=" + isLocked +
                '}';
    }

    @Override
    public String getTable() {
        return "Accounts";
    }

    @Override
    public Object getBeforeData() {
        return new Account(id, name, password, email, telephone, role,failed,isLocked);
    }

    @Override
    public Object getAfterData() {
        return this;
    }

}
