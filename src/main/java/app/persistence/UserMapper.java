package app.persistence;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    /*Login method, takes login credentials from the UserController and tries to connect to the database to return the existing user*/
    public static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from users where email=? and password=?";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, email);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("user_id");
                    int balance = rs.getInt("balanace");
                    boolean isAdmin = rs.getBoolean("is_admin");
                    return new User(id, email, password, balance, isAdmin);
                } else {
                    throw new DatabaseException("Your info did not match anything from our db");
                }
            }
        } catch (SQLException e) {
                throw new DatabaseException("Error while connecting to database " + e);
        }
    }

    /*RegisterUser method to register user based on the credentials sent from the UserMapper, updates the database with the corresponding credentials if the
    * given input is valid*/
    public static void registerUser(String email, String password, int balance, boolean isAdmin, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "insert into users (email, password, balanace, is_admin) values (?,?,?,?)";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setString(1,email);
                ps.setString(2,password);
                ps.setInt(3,balance);
                ps.setBoolean(4,isAdmin);
                int rowsAffected = ps.executeUpdate();
                if(rowsAffected < 1){
                    throw new DatabaseException("Error handling registration of user");
                }
            }
        } catch(SQLException e){
            throw new DatabaseException("Error while connecting to database "+e);
        }
    }

    public static void deleteUserSpecificOrder(int orderId,ConnectionPool connectionPool)throws DatabaseException {
        String sql = "delete from orderline where order_id = ?;" +
                "delete from orders where order_id = ?;";
        try(Connection connection = connectionPool.getConnection()) {
            try(PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, orderId);
                ps.setInt(2, orderId);
                int rowsAffected = ps.executeUpdate();

                if(rowsAffected<1){
                    throw new  DatabaseException("Hello there.");
                }

            }
        }catch (SQLException e){
            throw new DatabaseException(" " + e);
        }
    }

    public static int getBalanceViaEmail(String email, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "select balanace from users where email = ?";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int userBalanace = rs.getInt(1);
                    return userBalanace;
                }else {
                    throw new DatabaseException("we could not get your balance");
                }
            }
        }catch(SQLException e){
            throw new DatabaseException("Error while connecting to database "+e);
        }
    }
    public static void updateBalance(String email, int balance, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "update users set balanace = ? where email = ?";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setInt(1, balance);
                ps.setString(2, email);
                int rowsAffected = ps.executeUpdate();
                if(rowsAffected < 1){
                    throw new DatabaseException("Error while updating balance");
                }
            }
        }catch(SQLException e){
            throw new DatabaseException("Error while connecting to database " + e);
        }
    }
    public static int getBalance(String email, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "select balanace from users where email=?";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    return rs.getInt("balanace");
                }else {
                    throw new DatabaseException("Error while getting you balance, get an admin for help.");
                }
            }
        }catch(SQLException e){
            throw new DatabaseException("Error while connecting to database "+e);
        }
    }

    public static List<Order> getOrders(User activeUser, ConnectionPool connectionPool) throws DatabaseException{
        List<Order> orderList = new ArrayList<>();
        String sql = "select * from orders where user_id = ?";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setInt(1,activeUser.getUserID());

                ResultSet rs = ps.executeQuery();

                while(rs.next()){
                    int orderId = rs.getInt("order_id");
                    int workerId = rs.getInt("worker_id");
                    String status = rs.getString("status");
                    Date date = rs.getDate("date");
                    String note = rs.getString("note");
                    int total_price = rs.getInt("total_price");
                    orderList.add(new Order (orderId, activeUser.getUserID(), workerId, status, date, note, total_price));
                }
            }
        }catch (SQLException e){
            throw new DatabaseException("Error while fetching orders");
        }
        return orderList;
    }

    public static User getUserByEmail(String email, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from users where email = ?";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setString(1,email);
                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    int userId = rs.getInt("user_id");
                    //String userEmail = rs.getString("email");
                    String password = rs.getString("password");
                    int balance = rs.getInt("balanace");
                    boolean isAdmin = rs.getBoolean("is_admin");
                    return new User(userId, email, password, balance, isAdmin);
                }
            }
        }catch(SQLException e){
            throw new DatabaseException("Error while connecting to database " + e);
        }
        return null;
    }
}
