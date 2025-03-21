package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MsgService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MsgService msgService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.msgService = new MsgService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterationHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::editMessageHandler);
        app.get("/accounts/{account_id}/messages", this::userMessagesHandler);
        
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    private void postRegisterationHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(context.body(), Account.class);
        Account addedUser = accountService.registration(acc);
        if(addedUser!=null){
            context.json(addedUser);
        }else{
            context.status(400);
        }
    }

    private void postLoginHandler(Context context) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(context.body(), Account.class);
        Account logging = accountService.login(acc);
        if(logging !=null){
            context.json(logging);
        }
        else{
            context.status(401);
        }
    }
    
    private void postMessageHandler(Context context) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(context.body(), Message.class);
        Message actual_msg = msgService.msgCreation(msg);
        if(actual_msg !=null){
            context.json(actual_msg);
        }
        else{
            context.status(400);
        }
    }
    
    private void getAllMessageHandler(Context context) {
        List<Message> messages = msgService.getAllMessage();
        context.json(messages);
    }
    
    private void getMessageByIdHandler(Context context) {
        int msg_id = Integer.parseInt(context.pathParam("message_id"));
        Message msg = msgService.msgById(msg_id);
        if(msg !=null){
            context.json(msg);
        }
        else{
            context.json("");
        }
    }
    
    private void deleteMessageHandler(Context context) {
        int msg_id = Integer.parseInt(context.pathParam("message_id"));
        Message msg = msgService.deleteMsg(msg_id);
        context.json(msg != null ? msg : "");
    }
    
    private void editMessageHandler(Context context) throws JsonMappingException, JsonProcessingException {
        int msg_id = Integer.parseInt(context.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message messageUpdate = mapper.readValue(context.body(), Message.class);
        
        Message updatedMessage = msgService.updateMsg(msg_id, messageUpdate.getMessage_text());
        
        if (updatedMessage != null) {
            context.json(updatedMessage);
        } else {
            context.status(400);
        }
    }
    
    private void userMessagesHandler(Context context) {
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = msgService.msgHistory(account_id);
        context.json(messages);
    }
}