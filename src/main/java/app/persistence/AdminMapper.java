package app.persistence;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import io.javalin.http.ExceptionHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminMapper {

    public static boolean deleteUser(String email, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "delete from users where email = ?";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, email);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected < 1) {
                    throw new DatabaseException("Error while deleting user, user may not exist");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while connecting to database " + e);
        }
        return true;
    }

    public static boolean deleteOrder(int userId, int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "delete from orders where user_id = ? and order_id = ?";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setInt(2, orderId);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected < 1) {
                    throw new DatabaseException("Error while deleting order, order may not exist");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while connecting to database " + e);
        }
        return true;
    }

    public static void updateBalance(String email, int balance, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "update users set balanace = ? where email = ?";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, balance);
                ps.setString(2, email);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected < 1) {
                    throw new DatabaseException("Error while updating balance");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while connecting to database " + e);
        }
    }

    public static List<User> getAllUsers(ConnectionPool connectionPool) throws DatabaseException {
        List<User> userList = new ArrayList<>();
        String sql = "select * from users";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    int balance = rs.getInt("balanace"); // Yes that's how christian wrote it
                    boolean isAdmin = rs.getBoolean("is_admin");
                    userList.add(new User(userId, email, password, balance, isAdmin));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching user list");
        }
        return userList;
    }


    public static List<Order> getAllOrdersFromCostumer(int user_Id, ConnectionPool connectionPool) throws DatabaseException {
        List<Order> orderList = new ArrayList<>();
        String sql = "select * from orders where user_id = ?";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, user_Id);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    int userId = rs.getInt("user_id");
                    int workerId = rs.getInt("worker_id");
                    String status = rs.getString("status");
                    Date date = rs.getDate("date");
                    String note = rs.getString("note");
                    int total_price = rs.getInt("total_price");
                    orderList.add(new Order(orderId, userId, workerId, status, date, note, total_price));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching order list");
        }
        return orderList;
    }


    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {
        List<Order> orderList = new ArrayList<>();
        String sql = "select * from orders";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    int userId = rs.getInt("user_id");
                    int workerId = rs.getInt("worker_id");
                    String status = rs.getString("status");
                    Date date = rs.getDate("date");
                    String note = rs.getString("note");
                    int total_price = rs.getInt("total_price");
                    orderList.add(new Order(orderId, userId, workerId, status, date, note, total_price));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching order list");
        }
        return orderList;
    }
}
