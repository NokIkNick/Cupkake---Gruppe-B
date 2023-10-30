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
            User user = ctx.sessionAttribute("chosen_user");
            if(AdminMapper.deleteUser(user.getEmail(), connectionPool)){
                ctx.attribute("message","User: "+user.getEmail()+" Has been deleted");
                ctx.render("admin.html");
            }
        }catch (DatabaseException | NullPointerException e){
            ctx.attribute("message", e.getMessage());
            ctx.render("admin.html");
        }
    }

    public static void updateUserBalance(Context ctx, ConnectionPool connectionPool){
        try {
            User user = ctx.sessionAttribute("chosenuser");
            int balanceUpdateAmount = Integer.parseInt(ctx.formParam("balanceupdateamount"));
            int newbalance = user.getBalance()+balanceUpdateAmount;
            AdminMapper.updateBalance(user.getEmail(),newbalance,connectionPool);
            ctx.attribute("message", user.getEmail()+"'s balance has successfully been updated with: "+balanceUpdateAmount+" and is now: "+newbalance);
            ctx.render("admin.html");
        }catch (DatabaseException | NullPointerException e){
            ctx.attribute("message", e.getMessage());
            ctx.render("admin.html");
        }catch (NumberFormatException e){
            ctx.attribute("message","Error, make sure you enter a number");
            ctx.render("admin.html"); // TODO
        }
    }



    public static void deleteAnOrder(Context ctx, ConnectionPool connectionPool){
        try{User user = ctx.sessionAttribute("chosen_user");
           int orderID =  Integer.parseInt(ctx.formParam("order_id"));
           AdminMapper.deleteOrder(user.getUserID(),orderID,connectionPool);
           ctx.attribute("message","User: "+user.getUserID()+ " has been successfully deleted from the system");
           ctx.render("admin.html");
        }catch (NumberFormatException e){
            ctx.attribute("message","Error, make sure you enter a number");
            ctx.render("admin.html");
        }catch (DatabaseException | NullPointerException e){
            ctx.attribute("message", e.getMessage());
            ctx.render("admin.html");
        }
    }
    public static void allUsers(Context ctx, ConnectionPool connectionPool) {
        List<User> userList;
        try {
            userList = AdminMapper.getAllUsers(connectionPool);
            //System.out.println("Userlist: " + userList);
            ctx.attribute("userlist", userList);
            ctx.render("admin.html");        // TODO
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");         // TODO
        }
    }



    public static void getAllOrdersFromCostumers(Context ctx, ConnectionPool connectionPool){
        List<Order> orderList;
        try {
            int userID = Integer.parseInt(ctx.formParam("selected_user"));
            orderList = AdminMapper.getAllOrdersFromCostumer(userID,connectionPool);
            ctx.attribute("orderlist",orderList);
            ctx.render("admin.html");
        }catch(DatabaseException e){
            ctx.attribute("message",e.getMessage());
            ctx.render("index.html");
        }
    }

     public static void allOrders(Context ctx, ConnectionPool connectionPool){
        List<Order> orderList = new ArrayList<>();
        try {
             orderList = AdminMapper.getAllOrders(connectionPool);
             ctx.sessionAttribute("orderlist", orderList);
             ctx.render("admin.html");        // TODO
        }catch (DatabaseException e){
            ctx.attribute("message",e.getMessage());
            ctx.render("admin.html");         // TODO
        }
    }
}
