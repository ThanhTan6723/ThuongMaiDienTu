package model;

public class Payment {
    private int id;
    private String methodName;

    public Payment() {
    }

    public Payment(int id, String methodName) {
        this.id = id;
        this.methodName = methodName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", methodName='" + methodName + '\'' +
                '}';
    }
}
