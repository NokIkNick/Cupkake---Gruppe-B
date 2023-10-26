package app.entities;

public class Bottom {
    private int id;
    private String name;
    private int price;

    public Bottom(int id, String name, int price){
        this.price = price;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString(){
        return "Top{ "+"id: "
                +getId()+
                " name: "
                +name+
                " price: " +
                getPrice()+
                "}";
    }
}
