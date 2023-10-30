package app.controllers;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.AdminMapper;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class AdminController {
    // TODO make sure the functions render the right pages and has the correct attribute / formParam names.
    public static void deleteUser(Context ctx , ConnectionPool connectionPool){
        try{
            User user = ctx.sessionAttribute("choosen_user");
            if(AdminMapper.deleteUser(user.getEmail(), connectionPool)){
                ctx.attribute("message","User: "+user.getEmail()+" Has been deleted");
                ctx.render("adminPACEHOLDER.html");  // TODO
            }
        }catch (DatabaseException | NullPointerException e){
            ctx.attribute("message", e.getMessage());
            ctx.render("adminPlACEHOLDER.html"); // TODO
        }
    }

    public static void updateUserBalance(Context ctx, ConnectionPool connectionPool){
        try {
            User user = ctx.sessionAttribute("choosen_user");
            int balanceUpdateAmount = Integer.parseInt(ctx.formParam("balanceupdateamount"));
            int newbalance = user.getBalance()+balanceUpdateAmount;
            AdminMapper.updateBalance(user.getEmail(),newbalance,connectionPool);
            ctx.attribute("message", user.getEmail()+"'s balance has successfully been updated with: "+balanceUpdateAmount+" and is now: "+newbalance);
            ctx.render("adminPLACEHOLDER.html");  // TODO
        }catch (DatabaseException | NullPointerException e){
            ctx.attribute("message", e.getMessage());
            ctx.render("adminPlACEHOLDER.html"); // TODO
        }catch (NumberFormatException e){
            ctx.attribute("message","Error, make sure you enter a number");
            ctx.render("adminPLACEHOLDER.html"); // TODO
        }
    }
    public static void deleteAnOrder(Context ctx, ConnectionPool connectionPool){
        try{User user = ctx.sessionAttribute("choosen_user");
           int orderID =  Integer.parseInt(ctx.formParam("order_id"));
           AdminMapper.deleteOrder(user.getUserID(),orderID,connectionPool);
           ctx.attribute("message","User: "+user.getUserID()+ " has been successfully deleted from the system");
           ctx.render("adminPLACEHOLDER.html"); // TODO
        }catch (NumberFormatException e){
            ctx.attribute("message","Error, make sure you enter a number");
            ctx.render("adminPLACEHOLDER.html"); // TODO
        }catch (DatabaseException | NullPointerException e){
            ctx.attribute("message", e.getMessage());
            ctx.render("adminPlACEHOLDER.html"); // TODO
        }
    }
    public static void allUsers(Context ctx, ConnectionPool connectionPool){
        List<User> userList = new ArrayList<>();
        try {
             userList = AdminMapper.getAllUsers(connectionPool);
             ctx.sessionAttribute("userlist",userList);
             ctx.render("adminPLACEHOLDER.html");        // TODO
        }catch (DatabaseException e){
            ctx.attribute("message",e.getMessage());
            ctx.render("adminPLACEHOLDER.html");         // TODO
        }
    } public static void allOrders(Context ctx, ConnectionPool connectionPool){
        List<Order> orderList = new ArrayList<>();
        try {
             orderList = AdminMapper.getAllOrders(connectionPool);
             ctx.sessionAttribute("userlist", orderList);
             ctx.render("adminPLACEHOLDER.html");        // TODO
        }catch (DatabaseException e){
            ctx.attribute("message",e.getMessage());
            ctx.render("adminPLACEHOLDER.html");         // TODO
        }
    }
}
