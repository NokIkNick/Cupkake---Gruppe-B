package app.controllers;

import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import app.persistence.PasswordValidator;
import io.javalin.http.Context;
import app.exceptions.DatabaseException;


public class UserController {
    // TODO make sure the functions render the right pages and has the correct attribute / formParam names.
    public static void login(Context ctx, ConnectionPool connectionPool){
        String name = ctx.formParam("username");
        String password = ctx.formParam("password");
        try {
            User user =  UserMapper.login(name,password,connectionPool);
            ctx.sessionAttribute("currentuser",user); // a way to store info for the session, last until are idle to long or closes your explore or gets overridden
            CupCakeController.loadIndexSite(ctx,connectionPool);       // TODO
        } catch (DatabaseException e) {
            ctx.attribute("message",e.getMessage()); // gets the message from the error
            ctx.render("index.html");          // TODO
        }
    }

    public static void registerUser(Context ctx, ConnectionPool connectionPool){
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        String repeatPassword = ctx.formParam("password_repeat");
        if(password != null && repeatPassword != null) {
            if (!password.equals(repeatPassword)) {
                ctx.attribute("message", "Passwords did not match");
                ctx.render("create_user.html");  // TODO
                return;
            }
        }else {
            ctx.attribute("message", "Password cannot be empty");
            ctx.render("create_user.html");
            return;
        }
        if(PasswordValidator.isValidPassword(password)) {
            try {
                UserMapper.registerUser(username,password,2000,false,connectionPool); // balance is hardcoded right now
                //TODO fix balance so its not hardcoded, maybe?
                ctx.attribute("message","You can now log in with your new user");
                ctx.render("login.html");       // TODO
            } catch (DatabaseException e) {
                ctx.attribute("message",e.getMessage());
                ctx.render("createuser.html");      // TODO
            }
        }else {
            ctx.attribute("message", "Password was not complicated enough,length needs to be atleast 8 and you need atleast one uppercase letter, one number and one special character");
            ctx.render("createuser.html");
        }
    }
    public void updateBalance(Context ctx, ConnectionPool connectionPool){
        try {
            User user = ctx.sessionAttribute("currentuser");
            String email = user.getEmail();
            int balanceToAdd = Integer.parseInt(ctx.formParam("update_balance"));
            UserMapper.updateBalance(email,balanceToAdd, connectionPool);
        }catch (NumberFormatException e){
            ctx.attribute("PLACEHOLDER.message", "That's not a valid number!");
            ctx.render("PLACEHOLDER.HTML");    // TODO
        } catch (DatabaseException | NullPointerException e) {
            ctx.attribute("PLACEHOLDER.message", e.getMessage());
            ctx.render("PLACEHOLDER.HTML");     // TODO
        }
    }

}
