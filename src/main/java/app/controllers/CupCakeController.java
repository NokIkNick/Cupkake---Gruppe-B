package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.BottomMapper;
import app.persistence.ConnectionPool;
import app.persistence.TopMapper;
import io.javalin.http.Context;


public class CupCakeController {

        // TODO  make sure the names of the variables matches the thymeleaf context variables.
        // TODO add all the extra variables that is needed to render the index plage properly.
        // TODO add global list for top and bottom info.
    public static void loadIndexSite(Context ctx, ConnectionPool connectionPool){
        try {
            ctx.attribute("top_info", TopMapper.getAllTopInfo(connectionPool));          // TODO
            ctx.attribute("bottom_info", BottomMapper.getAllBottomInfo(connectionPool)); // TODO
            User user = ctx.sessionAttribute("currentuser");
            if(user != null){
                ctx.attribute("login", user.getEmail());
            }
            ctx.render("login.html");    // TODO
        }catch (DatabaseException e){
            ctx.attribute("message", e.getMessage());
            ctx.render("indexPLACEHOLDER.html");  // TODO
        }
    }
}
