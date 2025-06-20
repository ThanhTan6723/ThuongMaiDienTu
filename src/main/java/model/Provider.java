package model;

public class Provider {
    private int id;
    private String name;
    private String address;

    public Provider(int id) {
        this.id = id;
    }

    public Provider(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Provider(String name, String address) {
        this.name = name;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

