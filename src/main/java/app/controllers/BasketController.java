package app.controllers;

import app.entities.Bottom;
import app.entities.Orderline;
import app.entities.Top;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import com.google.gson.Gson;
import io.javalin.http.Context;
import kotlin.text.UStringsKt;

import java.util.ArrayList;
import java.util.List;

public class BasketController {
    public static void addToBasket(Context ctx, ConnectionPool connectionPool) {
        try {
            List<Orderline> basketOrderLines = ctx.sessionAttribute("basket_orderliness");
            String selectedTopName = ctx.formParam("selectedTop"); // Get the selected top's name
            int selectedTopId = Integer.parseInt(ctx.formParam("selectedTopId")); // Get the selected top's id
            int price = Integer.parseInt(ctx.formParam("selectedTopPrice")); // Get the selected top's price
            String selectedBottomName = ctx.formParam("selectedBottom");
            int selectedBottomId = Integer.parseInt(ctx.formParam("selectedBottomId")); // Get the custom data attribute
            int selectedBottemPrice = Integer.parseInt(ctx.formParam("selectedBottomPrice")); // Get the custom data attribute
            Top top = new Top(selectedTopId,selectedTopName,price);
            Bottom bottom = new Bottom(selectedBottomId,selectedBottomName,selectedBottemPrice);
            int quantity = Integer.parseInt(ctx.formParam("quantity"));
            assert top != null;
            assert bottom != null;
            int totalPrice = (top.getPrice() + bottom.getPrice())*quantity;
            Orderline orderline = new Orderline(top.getTopId(),top.getName(),bottom.getBottomId(),bottom.getName() ,totalPrice,quantity);
            assert basketOrderLines != null;
            basketOrderLines.add(orderline);
            ctx.sessionAttribute("basket_orderliness", basketOrderLines);
            basketOrderLines.stream().forEach(System.out::println);
            CupCakeController.loadIndexSite(ctx,connectionPool);
        }catch (NumberFormatException | AssertionError e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    public static void loadBasket(Context ctx) {
        List<Orderline> orderlines = ctx.sessionAttribute("basket_orderliness");  // test
        orderlines.stream().forEach(System.out::println);  // test
        ctx.render("basket.html"); // TODO
    }
    public static void deleteOrderLine(Context ctx){
        try {
            List<Orderline> basketOrderlines = ctx.sessionAttribute("basket_orderliness");
            int indexNumber = Integer.parseInt(ctx.formParam("index_number"));
            basketOrderlines.remove(indexNumber);
            ctx.sessionAttribute("basket_orderlines", basketOrderlines);
            loadBasket(ctx);
        }catch (NullPointerException | NumberFormatException e){
            ctx.attribute("message",e.getMessage());
            loadBasket(ctx);
        }
    }

    public static void addOrder(Context ctx, ConnectionPool connectionPool) {
        List<Orderline> orderlines = ctx.sessionAttribute("basket_orderlines");
        String note = ctx.formParam("note");
        User user;
        if(ctx.sessionAttribute("basket_orderlines") == null){
            ctx.attribute("message", "you dont have any order lines yet.");
            loadBasket(ctx); // TODO
            return;
        }
        if(ctx.sessionAttribute("active_user") == null){
            ctx.attribute("message", "You need to be logged in to order");
            ctx.render("login.html");
            return;
        }else {
            user = ctx.sessionAttribute("active_user");
            try {
                int totalprice = 0;
                for (Orderline orderline: orderlines) {
                    totalprice += orderline.getTotalPrice();
                }
                assert user != null;
                int newBalanceUser = UserMapper.getBalance(user.getEmail(), connectionPool) - totalprice;
                if(newBalanceUser < 0){
                    ctx.attribute("message", "you dont have enough money, add more to buy your order");
                    loadBasket(ctx); // TODO maybe redirect to buying more currency
                    return;
                }else {
                    OrderMapper.addOrder(orderlines, connectionPool, note, user);
                    UserMapper.updateBalance(user.getEmail(),newBalanceUser,connectionPool);
                    ctx.attribute("message", "Your order has been successfully placed and you have been charged: "+totalprice+" you new balance is: "+newBalanceUser);
                    ctx.render("order_successfully_placed.html"); // TODO
                }
            }catch (DatabaseException | AssertionError e){
                ctx.attribute("message", e.getMessage());
                loadBasket(ctx);       // TODO
            }
        }
    }
    public static void test(Context ctx,ConnectionPool connectionPool){
        String selectedTopName = ctx.formParam("selectedTop"); // Get the selected top's name
        String selectedTopId = ctx.formParam("selectedTopId"); // Get the selected top's id
        String price = ctx.formParam("selectedTopPrice"); // Get the selected top's price
        String top = ctx.formParam("selectedTop");
        System.out.println(top);
        /*String selectedBottomName = ctx.formParam("selectedBottom");
        int selectedBottomId = Integer.parseInt(ctx.formParam("bottom-id")); // Get the custom data attribute
        int selectedBottemPrice = Integer.parseInt(ctx.formParam("bottom-price"));*/
        System.out.println(selectedTopName);
        System.out.println(selectedTopId);
        System.out.println(price);
    }
}
