package app.controllers;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.AdminMapper;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class AdminController {
    // TODO make sure the functions render the right pages and has the correct attribute / formParam names.
    public static void deleteUser(Context ctx , ConnectionPool connectionPool){
        try{
            User user = ctx.sessionAttribute("chosen_user");
            if(AdminMapper.deleteUser(user.getEmail(), connectionPool)){
                ctx.attribute("message","User: " + user.getEmail() + " Has been deleted");
                ctx.render("admin.html");
            }
        }catch (DatabaseException | NullPointerException e){
            ctx.attribute("message", e.getMessage());
            ctx.render("admin.html");
        }
    }

    public static void deleteAnUserByUsingUserIdAndOrderId(Context ctx, ConnectionPool connectionPool) throws DatabaseException{
        checkUsers(ctx, connectionPool);
        String userEmail = ctx.sessionAttribute("userEmail");
        try {
            if(ctx.formParam("selected_order") == null){
                throw new AssertionError();
            }
            int orderid = Integer.parseInt(ctx.formParam("selected_order"));
            UserMapper.deleteUserSpecificOrder(orderid,connectionPool);
            List<Order> orderlist = ctx.sessionAttribute("orderlist");
            assert orderlist != null;
            for(int i = 0; i < orderlist.size(); i++){
                if(orderlist.get(i).getOrderId()==orderid){
                    orderlist.remove(i);
                    break;
                }
            }
            ctx.render("admin.html"); // TODO
        } catch (DatabaseException|AssertionError e){
            ctx.redirect("/adminInfoForUsers");
            ctx.attribute("message",e.getMessage());
        }

    }

    public static void updateUserBalanceUsingEmail(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        checkUsers(ctx, connectionPool);
        try {
            String userEmail = ctx.formParam("userEmail");
            int balanceUpdateAmount = Integer.parseInt(ctx.formParam("updateBalance"));
            int newbalance = UserMapper.getBalanceViaEmail(userEmail, connectionPool) + balanceUpdateAmount;
            AdminMapper.updateBalance(userEmail,newbalance,connectionPool);
            ctx.attribute("message", userEmail + "'s balance has successfully been updated with: " + balanceUpdateAmount + " and is now: " + newbalance);
            ctx.render("admin.html");
        }catch (DatabaseException| NullPointerException e){
            ctx.attribute("message", e.getMessage());
               System.out.println(e);
               ctx.render("admin.html");
        }catch (NumberFormatException e){
            ctx.attribute("message","Error, make sure you enter a number");
            ctx.render("admin.html"); // TODO
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
        try {
            checkUsers(ctx, connectionPool);
            ctx.render("admin.html");        // TODO
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");         // TODO
        }
    }

    public static void checkUsers(Context ctx, ConnectionPool connectionPool) throws DatabaseException{
        if(ctx.sessionAttribute("selected_user")==null){
            getUsers(ctx, connectionPool);
        }
    }

    public static void getUsers(Context ctx, ConnectionPool connectionPool) throws DatabaseException{
        List<User> userList;
        userList = AdminMapper.getAllUsers(connectionPool);
        ctx.sessionAttribute("userlist",userList);
        if(ctx.formParam("selected_user")!=null){
            getAllOrdersFromCostumer(ctx, connectionPool);
        }
    }



    public static void getAllOrdersFromCostumer(Context ctx, ConnectionPool connectionPool){
        List<Order> orderList;
        try {
            String userEmail = ctx.formParam("selected_user");
            ctx.sessionAttribute("userEmail",userEmail);
            User user = UserMapper.getUserByEmail(userEmail,connectionPool);
            ctx.sessionAttribute("admin_chosen_user",user);
            if(user!=null){
                orderList = AdminMapper.getAllOrdersFromCostumer(user.getUserID(), connectionPool);
            } else {
                orderList = null;
            }
            ctx.sessionAttribute("orderlist",orderList);
            ctx.render("admin.html");
        }catch(DatabaseException e){
            ctx.redirect("/");
            ctx.attribute("message",e.getMessage());
        }
    }
    public static void select_order(Context ctx,ConnectionPool connectionPool){
        try {
            checkUsers(ctx, connectionPool);
            if(ctx.formParam("selected_order")!=null) {
                int selectedOrderId = Integer.parseInt(ctx.formParam("selected_order"));
                System.out.println(selectedOrderId);
                ctx.sessionAttribute("selected_order", selectedOrderId);
            }
        } catch(DatabaseException e){
            ctx.redirect("/");
            ctx.attribute("message",e.getMessage());
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
