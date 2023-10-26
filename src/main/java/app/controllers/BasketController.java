package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.http.Context;

public class BasketController {

    public static void addToBasket(Context ctx, ConnectionPool connectionPool) {
        ctx.formParam("top_name");
        ctx.formParam("top_id");
        ctx.formParam("bottom_name");
        ctx.formParam("bottom_name");

    }
    public static void loadBasket(Context ctx, ConnectionPool connectionPool) {

    }

    public static void addOrder(Context ctx, ConnectionPool connectionPool) {

    }
}
