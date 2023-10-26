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
            }catch (SQLException e){
                throw new DatabaseException("hello there");
            }
        }catch (SQLException e){
            throw new DatabaseException("you failed to connect dingus");
        }
        return bottomInfoList;
    }

}
