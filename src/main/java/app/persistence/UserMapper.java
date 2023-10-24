package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {


    public static User login(String username, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from users where name=? and password=?";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    int id = rs.getInt("id");
                    int balance = rs.getInt("balanace");
                    boolean isAdmin = rs.getBoolean("is_admin");
                    return new User(id,username,password,balance,isAdmin);
                }else {
                    throw new DatabaseException("Error while logging in, try again.");
                }
            }
        }catch(SQLException e){
            throw new DatabaseException("Error while connecting to database");
        }
    }

    public static void registerUser(String username, String password, int balance, boolean isAdmin, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "insert into users (email, password, balanace, is_admin) values (?,?,?,?)";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setString(1,username);
                ps.setString(2,password);
                ps.setInt(3,balance);
                ps.setBoolean(4,isAdmin);
                int rowsAffected = ps.executeUpdate();
                if(rowsAffected < 0){
                    throw new DatabaseException("Error handling registration of user");
                }
            }
        } catch(SQLException e){
            throw new DatabaseException("Error while connection to database");
        }
    }

}
