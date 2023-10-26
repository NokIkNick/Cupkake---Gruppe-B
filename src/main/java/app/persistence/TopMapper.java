package app.persistence;

import app.entities.Top;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopMapper {

    public static List<Top> getAllTopInfo(ConnectionPool connectionPool) throws DatabaseException {
        List<Top> topInfoList = new ArrayList<>();
        String sql = "select * from top";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    int id = rs.getInt("top_id");
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    topInfoList.add(new Top(id,name,price));
                }
            }
        }catch (SQLException e){
            throw new DatabaseException("you failed to connect DB");
        }
        return topInfoList;
    }


}
