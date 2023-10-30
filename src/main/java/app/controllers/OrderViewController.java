package app.controllers;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

import java.util.List;

public class OrderViewController {
    public static void viewMyOrders(Context ctx, ConnectionPool connectionPool){
        // if not logged on - redirect
        User user = ctx.sessionAttribute("active_user");
        if(user==null){
            ctx.redirect("/login");
        // continue if logged in
        } else {
            try {
                List<Order> orders = OrderMapper.getOrderViews(user.getUserID(), connectionPool);
                ctx.attribute("orders", orders);
                ctx.render("ordre.html");
            } catch (DatabaseException e){
                ctx.attribute("message", e.getMessage());
                ctx.render("index.html");
            }
        }

    }
}
