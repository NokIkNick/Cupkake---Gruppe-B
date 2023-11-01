package app.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private int orderId;
    private int userId;
    private int workerId;
    private String status;
    private Date date;
    private String note;
    private int totalPrice;  // TODO
    private List<Orderline> orderlines = new ArrayList<>();

    public Order(int orderId, int userId, int workerId, String status, Date date, String note, int totalPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.workerId = workerId;
        this.status = status;
        this.date = date;
        this.note = note;
        this.totalPrice = totalPrice;
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

    public int getTotalPrice() {
        return totalPrice;
    }
    public void addOrderLine(Orderline orderline){
        orderlines.add(orderline);
    }

    public List<Orderline> getOrderlines(){
        return orderlines;
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
