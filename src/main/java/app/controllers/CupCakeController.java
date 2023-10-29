package app.controllers;

import app.entities.Orderline;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.BottomMapper;
import app.persistence.ConnectionPool;
import app.persistence.TopMapper;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;


public class CupCakeController {

        // TODO  make sure the names of the variables matches the thymeleaf context variables.
        // TODO add all the extra variables that is needed to render the index plage properly.
    public static void loadIndexSite(Context ctx, ConnectionPool connectionPool){
        try {
            ctx.attribute("top_info", TopMapper.getAllTopInfo(connectionPool));          // TODO
            ctx.attribute("bottom_info", BottomMapper.getAllBottomInfo(connectionPool)); // TODO
            User user = ctx.sessionAttribute("active_user");
            if(user != null){
                ctx.attribute("email", user.getEmail());
            }
            ctx.render("index.html");    // TODO
        }catch (DatabaseException e){
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");  // TODO
        }
    }


    public static void loadInitialIndexSite(Context ctx, ConnectionPool connectionPool){
        try{
            ctx.sessionAttribute("active_user", new User());
            ctx.sessionAttribute("basket_orderliness", new ArrayList<Orderline>());
            loadIndexSite(ctx,connectionPool);
        }catch(Exception e){

        }
    }

}
