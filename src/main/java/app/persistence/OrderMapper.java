package app.persistence;

import app.entities.Orderline;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.time.LocalDate;
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

}
