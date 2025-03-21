package DAO;

import Util.ConnectionUtil;
import Model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDAO {
    

    public Account createUser(Account account){
        Connection conn = ConnectionUtil.getConnection();

        try{
            String sql = "insert into account(username, password) values(?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            int rowsAffected = preparedStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet id = preparedStatement.getGeneratedKeys();
                if (id.next()) {
                    int account_id = id.getInt(1);
                    return new Account(account_id, account.getUsername(), account.getPassword());
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account login(String username, String password){
        Connection conn = ConnectionUtil.getConnection();

        try{
            String sql = "select * from account where username =? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                int account_id = rs.getInt("account_id");
                String userName = rs.getString("username");
                String pwd = rs.getString("password");

                return new Account(account_id,userName,pwd);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    
    public Account isUserThere(String username){
        Connection conn = ConnectionUtil.getConnection();


        try{
            String sql ="Select * from account where username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                int account_id = rs.getInt("account_id");
                String userName = rs.getString("username");
                String pwd = rs.getString("password");

                return new Account(account_id,userName,pwd);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account isUserThere_ID(int user_id){
        Connection conn = ConnectionUtil.getConnection();


        try{
            String sql ="Select * from account where account_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, user_id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                int account_id = rs.getInt("account_id");
                String userName = rs.getString("username");
                String pwd = rs.getString("password");

                return new Account(account_id,userName,pwd);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
