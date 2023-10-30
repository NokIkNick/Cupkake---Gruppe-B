package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

}
