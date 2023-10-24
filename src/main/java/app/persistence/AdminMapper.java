package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AdminMapper {

    public static boolean deleteUser(String email, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "delete from users where email = ?";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setString(1,email);
                int rowsAffected = ps.executeUpdate();
                if(rowsAffected < 1){
                    throw new DatabaseException("Error while deleting user, user may not exist");
                }
            }
        }catch(SQLException e){
            throw new DatabaseException("Error while connecting to database "+e);
        }
        return true;
    }

    public static boolean deleteOrder(int userId, int orderId, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "delete from orders where user_id = ? and order_id = ?";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setInt(1,userId);
                ps.setInt(2,orderId);
                int rowsAffected = ps.executeUpdate();
                if(rowsAffected < 1){
                    throw new DatabaseException("Error while deleting order, order may not exist");
                }
            }
        }catch(SQLException e){
            throw new DatabaseException("Error while connecting to database "+e);
        }
        return true;
    }

    public static void updateBalance(String email, int balance, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "update users set balanace = ? where email = ?";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setInt(1,balance);
                ps.setString(2,email);
                int rowsAffected = ps.executeUpdate();
                if(rowsAffected < 1){
                    throw new DatabaseException("Error while updating balance");
                }
            }
        }catch(SQLException e){
            throw new DatabaseException("Error while connecting to database "+e);
        }
    }

    public static List<User> showAllUsers(ConnectionPool connectionPool){
        String sql = "select * from users";
        return null;
    }


}
