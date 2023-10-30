package app.entities;

import java.sql.Date;

public class Order {

    private int orderId;
    private int userId;
    private int workerId;
    private String status;
    private Date date;
    private String note;

    public Order(int orderId, int userId, int workerId, String status, Date date, String note) {
        this.orderId = orderId;
        this.userId = userId;
        this.workerId = workerId;
        this.status = status;
        this.date = date;
        this.note = note;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public int getWorkerId() {
        return workerId;
    }

    public String getStatus() {
        return status;
    }

    public Date getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", workerId=" + workerId +
                ", status='" + status + '\'' +
                ", date=" + date +
                '}';
    }
}
