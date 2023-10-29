package app.entities;

import io.javalin.json.JavalinGson;

import java.io.Serializable;

public class Bottom extends JavalinGson implements Serializable {

    private int bottomId;
    private String name;
    private int price;

    public Bottom(int bottomId, String name, int price) {
        this.bottomId = bottomId;
        this.name = name;
        this.price = price;
    }

    public int getBottomId() {
        return bottomId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

}
