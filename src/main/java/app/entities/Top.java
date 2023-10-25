package app.entities;

public class Top {

    private int topId;
    private String name;
    private int price;

    public Top(int topId, String name, int price) {
        this.topId = topId;
        this.name = name;
        this.price = price;
    }

    public int getTopId() {
        return topId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
