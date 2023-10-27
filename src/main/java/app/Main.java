package app;

import app.config.ThymeleafConfig;
import app.controllers.BasketController;
import app.controllers.CupCakeController;
import app.controllers.UserController;
import app.persistence.ConnectionPool;
import app.persistence.PasswordValidator;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.thymeleaf.context.Context;

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
            app.get("*", ctx -> CupCakeController.loadIndexSite(ctx,connectionPool));
            app.post("*", ctx -> ctx.render("/"));
        }
        // render start:
        app.get("*", ctx -> CupCakeController.loadIndexSite(ctx,connectionPool));
        app.post("*", ctx -> ctx.render("/"));

        // login related:
        app.get("/login", ctx -> ctx.render("login.html"));
        app.post("/login", ctx -> UserController.login(ctx,connectionPool));
        app.get("/create_user", ctx -> ctx.render("create_user.html"));
        app.post("/register_user", ctx -> UserController.registerUser(ctx,connectionPool));

        // Basket related:
        app.post("/add_to_basket", ctx -> BasketController.addToBasket(ctx, connectionPool));
        app.post("/basket", ctx -> BasketController.loadBasket(ctx));
        app.post("/add_order", ctx -> BasketController.addOrder(ctx , connectionPool));

        // Admin related:
        // TODO

        //app.get("/", ctx ->  ctx.render("index.html"));
        // System.out.println(PasswordValidator.isValidPassword("Hest!2rt")); // password validator test
    }
}