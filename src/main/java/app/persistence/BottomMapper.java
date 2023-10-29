package app.persistence;

import app.entities.Bottom;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BottomMapper {

    public static List<Bottom> getAllBottomInfo(ConnectionPool connectionPool) throws DatabaseException {
        List<Bottom> bottomInfoList = new ArrayList<>();
        String sql = "select * from bottom";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    int id = rs.getInt("bottom_id");
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    bottomInfoList.add(new Bottom(id,name,price));
                }
            }
        }catch (SQLException e){
            throw new DatabaseException("you failed to connect DB");
        }
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
