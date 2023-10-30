package app.persistence;

import app.entities.Bottom;
import app.entities.Top;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BottomMapper {

    /*** Info from Rolin: made some changes to save on calls to the DB ***/
    /*** To Explain it, is checks if i'ts been checked within 15 minutes, if not, update it and the return it, otherwise just return the server cached list ***/
    private static Timestamp lastTimeChecked;
    private static final List<Bottom> bottomInfoList = new ArrayList<>();

    private static void checkForUpdate(ConnectionPool connectionPool) throws DatabaseException {
        if(lastTimeChecked==null || lastTimeChecked.before(new Timestamp(System.currentTimeMillis()-15*60*1000))) {
            lastTimeChecked = new Timestamp(System.currentTimeMillis());
            bottomInfoList.clear();
            String sql = "select * from bottom";
            try (Connection connection = connectionPool.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        int id = rs.getInt("bottom_id");
                        String name = rs.getString("name");
                        int price = rs.getInt("price");
                        bottomInfoList.add(new Bottom(id, name, price));
                    }
                }
            } catch (SQLException e) {
                throw new DatabaseException("you failed to connect DB");
            }
        }
    }

    public static List<Bottom> getAllBottomInfo(ConnectionPool connectionPool) throws DatabaseException {
        checkForUpdate(connectionPool);
        return bottomInfoList;
    }

    public static Bottom getBottomById(int topId, ConnectionPool connectionPool) throws DatabaseException{
        Bottom bottom = null;
        String sql = "select * from bottom where bottom_id =?";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setInt(1,topId);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    int id = rs.getInt(1);
                    String name = rs.getString(2);
                    int price = rs.getInt(3);
                    bottom = new Bottom(id,name,price);
                }
            }
        }catch (SQLException e){
            throw new DatabaseException("you failed to connect DB"+ e.getMessage());
        }
        return bottom;
    }
}
