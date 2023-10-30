package app;

import app.config.ThymeleafConfig;
import app.controllers.BasketController;
import app.controllers.CupCakeController;
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
            config.staticFiles.add("/static");
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
        app.post("/register", ctx -> ctx.render("registration.html"));
        app.post("/register_user", ctx -> UserController.registerUser(ctx,connectionPool));

        // Basket related:
        app.post("/add_to_basket", ctx -> BasketController.addToBasket(ctx, connectionPool));
        app.get("/basket", ctx -> BasketController.loadBasket(ctx));
        app.post("/basket", ctx -> BasketController.loadBasket(ctx));
        app.post("/add_order", ctx -> BasketController.addOrder(ctx , connectionPool));

        //app.get("/", ctx ->  ctx.render("index.html"));
        // System.out.println(PasswordValidator.isValidPassword("Hest!2rt")); // password validator test
        
    }
}