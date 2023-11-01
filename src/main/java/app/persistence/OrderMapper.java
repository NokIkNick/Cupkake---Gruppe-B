package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderMapper {

    public static void addOrder(List<Orderline> orderlines, ConnectionPool connectionPool, String note, User currentUser, int totalPrice) throws DatabaseException {
        int orderId = 0;
        String sql = "insert into orders (user_id,worker_id,status,date,note,total_price) values (?,?,?,?,?,?)";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setInt(1,currentUser.getUserID());
                ps.setInt(2,1);
                ps.setString(3,"PENDING");
                java.util.Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                ps.setDate(4,sqlDate);
                ps.setString(5,note);
                ps.setInt(6,totalPrice);

                int rowsAffected = ps.executeUpdate();
                if(rowsAffected < 1){
                    throw new DatabaseException("Error while creating your order, try again later.");
                }

                try(ResultSet rs = ps.getGeneratedKeys()){
                    while(rs.next()){
                        orderId = rs.getInt("order_id");
                    }
                }
                createOrderlines(orderlines,orderId,connectionPool);

            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new DatabaseException("Error while connecting to database");
        }
    }

    private static void createOrderlines(List<Orderline> orderlines, int orderId, ConnectionPool connectionPool) throws DatabaseException{
        for(Orderline orderline : orderlines){
            String sql = "insert into orderline (top_id, bottom_id, total_price, quantity, order_id) Values (?,?,?,?,?)";
            try(Connection connection = connectionPool.getConnection()){
                try(PreparedStatement ps = connection.prepareStatement(sql)){
                    ps.setInt(1,orderline.getTopId());
                    ps.setInt(2,orderline.getBottomId());
                    ps.setInt(3,orderline.getTotalPrice());
                    ps.setInt(4,orderline.getQuantity());
                    ps.setInt(5,orderId);

                    int rowsAffected = ps.executeUpdate();
                    if(rowsAffected < 1){
                        throw new DatabaseException("Error while creating orderline");
                    }
                }
            }catch(SQLException e){
                throw new DatabaseException("Error while connecting to database" + e.getMessage());
            }
        }
    }

    public static List<Order> getOrderViews(int userID, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "select order_id, user_id, worker_id, status, date, note, total_price from orders where user_id = ?";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setInt(1, userID);
                try(ResultSet rs = ps.executeQuery()){
                    List<Order> orderViews = extractRSOrder(rs);
                    // very important without this we wouldn't have any idea what is actually ordered.
                    addOrdersToOrderViewList(orderViews, connectionPool);
                    return orderViews;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Something went wrong with the order, try again later.");
        }

    }

    private static List<Order> extractRSOrder(ResultSet rs) throws SQLException {
        List<Order> result = new ArrayList<>();
        while(rs.next()) {
            int order_id = rs.getInt("order_id");
            int user_id = rs.getInt("user_id");
            int worker_id = rs.getInt("worker_id");
            String status = rs.getString("status");
            java.sql.Date date = rs.getDate("date");
            String note = rs.getString("note");
            int totalPrice = rs.getInt("total_price");
            Order orderView = new Order(order_id, user_id, worker_id, status, date, note, totalPrice);
            result.add(orderView);
        }
        return result;
    }

    private static void addOrdersToOrderViewList(List<Order> orders, ConnectionPool connectionPool) throws DatabaseException {
        for (Order orderView: orders) {
            String sql = "select top_id, bottom_id, quantity, total_price from orderline where order_id = ?";
            try(Connection connection = connectionPool.getConnection()){
                try(PreparedStatement ps = connection.prepareStatement(sql)){
                    ps.setInt(1, orderView.getOrderId());
                    try(ResultSet rs = ps.executeQuery()){
                        while(rs.next()){
                            int top_id = rs.getInt("top_id");
                            Top top = TopMapper.getTopById(top_id, connectionPool);
                            int bottom_id = rs.getInt("bottom_id");;
                            Bottom bottom = BottomMapper.getBottomById(bottom_id, connectionPool);
                            int total_price = rs.getInt("total_price");;
                            int quantity = rs.getInt("quantity");
                            Orderline orderline = new Orderline(top, bottom, total_price, quantity);
                            orderView.addOrderLine(orderline);
                        }
                    }
                }
            } catch (SQLException e) {
                throw new DatabaseException("Something went wrong getting your order products, try again later.");
            }
        }
    }
}
