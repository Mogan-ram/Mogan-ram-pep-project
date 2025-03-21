package DAO;

import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;

public class MsgDAO {

    public Message sendMessage(Message msg){
        Connection conn = ConnectionUtil.getConnection();

        try{
            String sql = "insert into message(posted_by, message_text, time_posted_epoch) values(?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        
            preparedStatement.setInt(1, msg.getPosted_by());
            preparedStatement.setString(2, msg.getMessage_text());
            preparedStatement.setLong(3, msg.getTime_posted_epoch());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected>0){
                ResultSet id = preparedStatement.getGeneratedKeys();
                if(id.next()){
                    int msg_id = id.getInt(1);
                    return new Message(msg_id, msg.getPosted_by(), msg.getMessage_text(), msg.getTime_posted_epoch());
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> allMessage(){
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try{
            String sql = "Select * from message";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                int msg_id = rs.getInt("message_id");
                int posted_by = rs.getInt("posted_by");
                String msg_text = rs.getString("message_text");
                long msg_tim = rs.getLong("time_posted_epoch");
                Message message = new Message(msg_id,posted_by,msg_text,msg_tim);
                messages.add(message);
            }

            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }


    public Message messageById(int msg_id){
        Connection conn = ConnectionUtil.getConnection();

        try{
            String sql = "Select * from message where message_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, msg_id);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                int message_id = rs.getInt("message_id");
                int posted_id = rs.getInt("posted_by");
                String msg_text = rs.getString("message_text");
                long msg_tim = rs.getLong("time_posted_epoch");

                return new Message(message_id,posted_id,msg_text,msg_tim);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessage(int msg_id){
        Connection conn = ConnectionUtil.getConnection();

        try{
            Message dlt = messageById(msg_id);
            if(dlt == null){
                return null;
            }
            String sql = "delete from message where message_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, msg_id);
            preparedStatement.executeUpdate();

            return dlt;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Message editMessage(int msg_id, String msg_text){
        Connection conn = ConnectionUtil.getConnection();

        try{
            Message existing_msg = messageById(msg_id);
            if(existing_msg == null){
                return null;
            }
            String sql = "update message set message_text = ? where message_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, msg_text);
            preparedStatement.setInt(2, msg_id);
            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                return new Message(msg_id, existing_msg.getPosted_by(), msg_text, existing_msg.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> msgHistory(int acc_id){
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try{
            String sql = "select * from message where posted_by = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, acc_id);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                int message_id = rs.getInt("message_id");
                int posted_id = rs.getInt("posted_by");
                String msg_text = rs.getString("message_text");
                long msg_tim = rs.getLong("time_posted_epoch");

                Message message =  new Message(message_id,posted_id,msg_text,msg_tim);
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}