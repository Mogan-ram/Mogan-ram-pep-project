package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    public AccountDAO accdao;

    public AccountService(){
        this.accdao = new AccountDAO();
    }
    public AccountService(AccountDAO accdao){
        this.accdao = accdao;
    }

    public  Account registration(Account acc){
        if(acc.getUsername() == null || acc.getUsername().trim().isEmpty()){
            return null;
        }
        if(acc.password ==null || acc.password.length()<4){
            return null;
        }


        Account registered = accdao.isUserThere(acc.getUsername());
        if(registered == null){
            return accdao.createUser(acc);
        }
        return null;
    }

    public Account login(Account acc){
        Account isthere = accdao.login(acc.username, acc.password);
        if(isthere != null){
            return isthere;
        }
        return null;
    }
    
}
