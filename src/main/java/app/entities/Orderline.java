package app.entities;

public class Orderline {

    private int topId;
    private int bottomId;
    private int totalPrice;
    private int quantity;

    public Orderline(int topId, int bottomId, int totalPrice, int quantity) {
        this.topId = topId;
        this.bottomId = bottomId;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTopId() {
        return topId;
    }

    public int getBottomId() {
        return bottomId;
    }
}
