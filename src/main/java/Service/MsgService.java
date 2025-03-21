package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MsgDAO;
import Model.Account;
import Model.Message;

public class MsgService{
    public AccountDAO accdao;
    public MsgDAO msgdao;

    public MsgService(){
        this.accdao = new AccountDAO();
        this.msgdao = new MsgDAO();
    }
    public MsgService(MsgDAO msgDAO){
        this.msgdao = msgDAO;
    }

    public Message msgCreation(Message msg){
        if(msg.getMessage_text() == null || msg.getMessage_text().trim().isEmpty()){
            return null;
        }
        if(msg.message_text.length()>255){
            return null;
        }
        Account active_user = accdao.isUserThere_ID(msg.getPosted_by());
        if(active_user != null){
            return msgdao.sendMessage(msg);
        }
        return null;

    }

    public List<Message> getAllMessage(){
        return msgdao.allMessage();
    }
    public List<Message> msgHistory(int acc_id){
        return msgdao.msgHistory(acc_id);
    }
    public Message msgById(int msg_id){
        return msgdao.messageById(msg_id);
    }
    public Message deleteMsg(int msg_id){
        return msgdao.deleteMessage(msg_id);
    }

    public Message updateMsg(int msg_id, String msg_text){
        if(msg_text == null || msg_text.trim().isEmpty()){
            return null;
        }
        if(msg_text.length()>255){
            return null;
        }
        return msgdao.editMessage(msg_id, msg_text);

    }
}