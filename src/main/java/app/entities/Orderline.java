package app.entities;

public class Orderline {

    private int totalPrice;
    private int quantity;

    public Orderline(int totalPrice, int quantity) {
        this.totalPrice = totalPrice;
        this.quantity = quantity;
    }


    public int getTotalPrice() {
        return totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }


}
