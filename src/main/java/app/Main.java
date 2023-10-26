package app;

import app.config.ThymeleafConfig;
import app.controllers.CupCakeController;
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
            app.get("*", ctx -> ctx.render("/"));
            app.post("*", ctx -> ctx.render("/"));
        }
        // Routing

        app.get("/", ctx ->  ctx.render("index.html"));
        // System.out.println(PasswordValidator.isValidPassword("Hest!2rt")); // password validator test
        
    }
}