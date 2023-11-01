package app;

import app.config.ThymeleafConfig;
import app.controllers.AdminController;
import app.controllers.BasketController;
import app.controllers.CupCakeController;
import app.controllers.UserController;
import app.entities.Order;
import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.PasswordValidator;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Map;

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

            // render start:

            //app.get("/admin", ctx -> ctx.render("/admin.html"));
            //app.get("*", ctx -> CupCakeController.loadIndexSite(ctx,connectionPool));


            // login related:




            //app.get("/admin", ctx -> ctx.render("admin.html"));





        }
        app.get("/login", ctx -> ctx.render("login.html"));
        app.post("/login", ctx -> UserController.login(ctx,connectionPool));
        app.get("/create_user", ctx -> ctx.render("create_user.html"));
        app.post("/register_user", ctx -> UserController.registerUser(ctx,connectionPool));

        // Basket related:
        app.post("/add_to_basket", ctx -> BasketController.addToBasket(ctx, connectionPool));
        app.post("/kurv", ctx -> BasketController.loadBasket(ctx,connectionPool));
        app.post("/add_order", ctx -> BasketController.addOrder(ctx , connectionPool));


        //admin related:
        app.get("/adminInfoForUsers",ctx->AdminController.allUsers(ctx,connectionPool));
        app.post("/adminInfoForUsers",ctx->AdminController.allUsers(ctx,connectionPool));
        app.post("/updateUseBalance",ctx->AdminController.updateUserBalanceUsingEmail(ctx,connectionPool));
        app.post("/select_order",ctx-> {
            AdminController.select_order(ctx,connectionPool);
            ctx.redirect("/adminInfoForUsers");
        });
        app.post("/deleteAnUserByUsingUserIdAndOrderId",ctx->AdminController.deleteAnUserByUsingUserIdAndOrderId(ctx,connectionPool));
        // Routing
        app.get("/test", ctx -> ctx.render("test.html"));
        app.get("/", ctx -> ctx.render("index.html"));
        // System.out.println(PasswordValidator.isValidPassword("Hest!2rt")); // password validator test

    }
}