package app;

import app.config.ThymeleafConfig;
import app.controllers.AdminController;
import app.controllers.BasketController;
import app.controllers.CupCakeController;
import app.controllers.OrderViewController;
import app.controllers.UserController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;


public class Main {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake";

    private static ConnectionPool connectionPool = null; // = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);


    public static void main(String[] args)
    {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            JavalinThymeleaf.init(ThymeleafConfig.templateEngine());
        }).start(7070);

        try{
            connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
        } catch (Exception e){

        }
        // Routing

        // render start:
        app.get("/", ctx -> CupCakeController.loadInitialIndexSite(ctx,connectionPool));
        //app.post("*", ctx -> ctx.render("index.html"));

        // login related:
        app.post("/login_from_index", ctx -> ctx.render("login.html"));
        app.post("/login", ctx -> UserController.login(ctx,connectionPool));
        app.get("/login", ctx -> ctx.render("login.html"));
        app.post("/register", ctx -> ctx.render("registration.html"));
        app.post("/register_user", ctx -> UserController.registerUser(ctx,connectionPool));
        app.post("/loginRender", ctx -> ctx.render("login.html"));
        app.get("/logout", ctx -> CupCakeController.logout(ctx,connectionPool));

        // Basket related:
        app.post("/add_to_basket", ctx -> BasketController.addToBasket(ctx, connectionPool));
        app.get("/basket", ctx -> BasketController.loadBasket(ctx));
        app.post("/basket", ctx -> BasketController.loadBasket(ctx));
        app.post("/delete_orderline", ctx -> BasketController.deleteOrderLine(ctx));
        app.post("/add_order", ctx -> BasketController.addOrder(ctx , connectionPool));

        // View Orders
        app.get("/orders", ctx -> OrderViewController.viewMyOrders(ctx, connectionPool));


        //admin related:
        app.get("/adminInfoForUsers",ctx->AdminController.allUsers(ctx,connectionPool));
        app.post("/adminInfoForUsers",ctx->AdminController.allUsers(ctx,connectionPool));
        app.post("/updateUseBalance",ctx->AdminController.updateUserBalanceUsingEmail(ctx,connectionPool));
        app.post("/select_order",ctx-> {
            AdminController.select_order(ctx,connectionPool);
            ctx.redirect("/adminInfoForUsers");
        });
        app.post("/deleteAnUserByUsingUserIdAndOrderId",ctx->AdminController.deleteAnUserByUsingUserIdAndOrderId(ctx,connectionPool));
    }
}