package app.entities;

public class Top {
    private int id;
    private String name;
    private int price;
    public Top(int id,String name,int price){
        this.name = name;
        this.price = price;
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
                " price: "
                +getPrice()
                +"}";
    }

}
