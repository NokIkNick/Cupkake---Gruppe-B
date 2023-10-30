package app.persistence;

import app.config.ThymeleafConfig;
import app.entities.Orderline;
import app.entities.User;
import app.exceptions.DatabaseException;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class OrderMapperTest {


    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcakeTest";
    private static ConnectionPool connectionPool = null;

    private List<Orderline> orderlines;
    private User currentUser;
    private String note;

    @BeforeEach
    void setUp() {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/static");
            JavalinThymeleaf.init(ThymeleafConfig.templateEngine());
        }).start(7070);

        try{
            connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
        } catch (Exception e){
            app.get("*", ctx -> ctx.render("/"));
            app.post("*", ctx -> ctx.render("/"));
        }
        orderlines = new ArrayList<>();
        orderlines.add(new Orderline(1,1,100,1));
        orderlines.add(new Orderline(1,1,200,2));
        currentUser = new User(1,"admin@test.dk","1234",2000,true);
        note = "Hej jeg vil gerne have kage, tak";
    }

    @Test
    void addOrder() throws DatabaseException {
        int orderId = 0;
        String sql = "insert into orders (user_id,worker_id,status,date,note) values (?,?,?,?,?)";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setInt(1,currentUser.getUserID());
                ps.setInt(2,1);
                ps.setString(3,"PENDING");
                java.util.Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                ps.setDate(4,sqlDate);
                ps.setString(5,note);

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
            throw new DatabaseException("Error while connecting to database" + e.getMessage());
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