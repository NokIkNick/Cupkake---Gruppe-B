package app.controllers;

import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import app.persistence.PasswordValidator;
import io.javalin.http.Context;
import app.exceptions.DatabaseException;



public class UserController {
    public static void login(Context ctx, ConnectionPool connectionPool){
        String name = ctx.formParam("email");
        String password = ctx.formParam("password");
        try {
            User user =  UserMapper.login(name,password,connectionPool);
            ctx.attribute("message","You have been logged in successfully");
            ctx.sessionAttribute("active_user",user); // a way to store info for the session, last until are idle to long or closes your explore or gets overridden
            CupCakeController.loadIndexSite(ctx,connectionPool);
        } catch (DatabaseException e) {
            ctx.attribute("message",e.getMessage()); // gets the message from the error
            ctx.render("login.html");
        }
    }

    public static void registerUser(Context ctx, ConnectionPool connectionPool){
        String username = ctx.formParam("email");
        String password = ctx.formParam("password");
        String repeatPassword = ctx.formParam("rpt_password");
        if(password != null && repeatPassword != null) {
            if (!password.equals(repeatPassword)) {
                ctx.attribute("message", "Passwords did not match");
                ctx.render("registration.html");
                return;
            }
        }else {
            ctx.attribute("message", "Password cannot be empty");
            ctx.render("registration.html");
            return;
        }
        if(PasswordValidator.isValidPassword(password)) {
            try {
                UserMapper.registerUser(username,password,2000,false,connectionPool); // balance is hardcoded right now
                //TODO fix balance so its not hardcoded, maybe?
                ctx.attribute("message","You can now log in with your new user");
                ctx.render("login.html");
            } catch (DatabaseException e) {
                ctx.attribute("message",e.getMessage());
                ctx.render("registration.html");
            }
        }else {
            ctx.attribute("message", "Password was not complicated enough,length needs to be atleast 8 and you need atleast one uppercase letter, one number and one special character");
            ctx.render("registration.html");
        }
    }
    public static void updateBalance(Context ctx, String email, int newBal, ConnectionPool connectionPool){
        try {
            UserMapper.updateBalance(email,newBal, connectionPool);
        }catch (NumberFormatException e){
            ctx.attribute("PLACEHOLDER.message", "That's not a valid number!");
            ctx.render("basket.html");
        } catch (DatabaseException | NullPointerException e) {
            ctx.attribute("PLACEHOLDER.message", e.getMessage());
            ctx.render("basket.html");
        }
    }

}
