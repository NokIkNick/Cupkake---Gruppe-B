package app.entities;

public class Orderline {

    private int topId;
    private String topName;
    private int bottomId;
    private String bottomName;
    private int totalPrice;
    private int quantity;

    public Orderline(int topId, int bottomId, int totalPrice, int quantity) {
        this.topId = topId;
        this.bottomId = bottomId;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
    }
    public Orderline(int topId,String topName, int bottomId,String bottomName, int totalPrice, int quantity) {
        this.topId = topId;
        this.topName = topName;
        this.bottomId = bottomId;
        this.bottomName = bottomName;
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

    public String getTopName() {
        return topName;
    }

    public String getBottomName() {
        return bottomName;
    }

    @Override
    public String toString() {
        return "Orderline{" +
                "topId=" + topId +
                ", topName='" + topName + '\'' +
                ", bottomId=" + bottomId +
                ", bottomName='" + bottomName + '\'' +
                ", totalPrice=" + totalPrice +
                ", quantity=" + quantity +
                '}';
    }
}
