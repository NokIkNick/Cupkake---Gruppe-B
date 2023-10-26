package app.persistence;

import app.exceptions.DatabaseException;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopMapper {

    public static List<String> getAllTopNames(ConnectionPool connectionPool) throws DatabaseException{
        List<String> nameList = new ArrayList<>();
        String sql = "select name from top";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    String name = rs.getString("name");
                    nameList.add(name);
                }
            }catch (SQLException e){
                throw new DatabaseException("hello there");
            }
        }catch (SQLException e){
            throw new DatabaseException("you failed to connect dingus");
        }
        return nameList;
    }
    public static int getTopPrice(String name,ConnectionPool connectionPool) throws DatabaseException{
        int price = 0;
        String sql = "select price from top where name = ?";

        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setString(1,name);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        price = rs.getInt("price");
                    }
                }
            }catch (SQLException e){
                throw new DatabaseException("hello there");
            }
        }catch (SQLException e){
            throw new DatabaseException("you failed to connect dingus");
        }
        return price;
    }

}
