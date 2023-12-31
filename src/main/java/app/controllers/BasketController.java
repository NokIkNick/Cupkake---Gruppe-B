package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.*;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class BasketController {
    public static void addToBasket(Context ctx, ConnectionPool connectionPool) {
        try {
            List<Orderline> basketOrderLines = ctx.sessionAttribute("basket_orderlines");
            if(basketOrderLines==null){
                basketOrderLines = new ArrayList<>();
            }
            int topId = Integer.parseInt(ctx.formParam("selectedTop"));
            int bottomId = Integer.parseInt(ctx.formParam("selectedBottom"));
            Top top = TopMapper.getTopById(topId, connectionPool);
            Bottom bottom = BottomMapper.getBottomById(bottomId, connectionPool);
            int quantity = 1;
            try {
                quantity = Integer.parseInt(ctx.formParam("quantity"));
            } catch (NumberFormatException ignored){}
            if(quantity < 0){
                throw new NumberFormatException("Quantity needs to be a positive number");
            }
            int totalPrice = (bottom.getPrice() + top.getPrice()) * quantity;
            Orderline orderline = new Orderline(top, bottom, totalPrice, quantity);
            basketOrderLines.add(orderline);
            ctx.sessionAttribute("basket_orderlines", basketOrderLines);
            CupCakeController.loadIndexSite(ctx,connectionPool);
        } catch (DatabaseException e) {
            e.printStackTrace();
        } catch (AssertionError e){
            e.printStackTrace();
        }catch (NumberFormatException e){
            ctx.attribute("message",e.getMessage());
            CupCakeController.loadIndexSite(ctx,connectionPool);
        }
    }
    public static void loadBasket(Context ctx) {
        List<Orderline> orderlines = ctx.sessionAttribute("basket_orderlines");
        if(orderlines == null) {
            orderlines = new ArrayList<>();
            ctx.sessionAttribute("total_price", 0);
            ctx.sessionAttribute("basket_orderlines", orderlines);
        }
        ctx.sessionAttribute("total_price", updateSum(orderlines));
        ctx.render("basket.html");
    }
    public static void deleteOrderLine(Context ctx){
        try {
            List<Orderline> basketOrderlines = ctx.sessionAttribute("basket_orderlines");
            int indexNumber = Integer.parseInt(ctx.formParam("deleteOrderLine"));
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
        if(note.isEmpty() || note == null){
            note = "No comment";
        }
        User user;
        if(ctx.sessionAttribute("basket_orderlines") == null || orderlines.isEmpty()){
            ctx.attribute("message", "you dont have any order lines yet.");
            loadBasket(ctx);
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
                    OrderMapper.addOrder(orderlines, connectionPool, note, user,totalprice);
                    UserController.updateBalance(ctx, user.getEmail(),newBalanceUser,connectionPool);
                    List<Orderline> emptyOrderlines = new ArrayList<>();
                    ctx.sessionAttribute("last_orderlines",orderlines);
                    ctx.sessionAttribute("basket_orderlines",emptyOrderlines);
                    ctx.attribute("message", "Your order has been successfully placed and you have been charged: "+totalprice+" you new balance is: "+newBalanceUser);
                    ctx.render("order_successfully_placed.html"); // TODO ctx.redirect("/");
                }
            }catch (DatabaseException | AssertionError e){
                ctx.attribute("message", e.getMessage());
                loadBasket(ctx);
            }
        }
    }

    public static double updateSum(List<Orderline> orderlines){
        int sum = 0;
        for (Orderline o : orderlines) {
            sum += o.getTotalPrice();
        }
        return sum;
    }
}
