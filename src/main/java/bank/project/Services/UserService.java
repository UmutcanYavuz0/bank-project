package bank.project.Services;

import bank.project.Dto.AccountNoAndBalance;
import bank.project.Dto.AccountNoAndIban;
import bank.project.Dto.DtoMoneyTransefer;
import bank.project.Entities.Transaction;
import bank.project.Entities.User;
import bank.project.Entities.UserAccount;
import bank.project.Repositories.TransactionsRepository;
import bank.project.Repositories.UserAccountRepository;
import bank.project.Repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
@Service
public class UserService {
    @Autowired
    private TransactionsRepository transactionsRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private UsersRepository usersRepository;

    public String register(){
        //if user not exists
        //add new user to users
        //open new account and iban and set money to 0 on userAccount

        String username="";
        String password="";




        if(usersRepository.existsByUsername(username)){
            return "böyle bir kullanıcı mevcut login olun";
        }

        //adding new user to users table
        usersRepository.reigster(username,password);

        long id=usersRepository.getUser(username,password).get().getId();
        int userid = (int) id;


        //openin new account and iban and set money to 0
        userAccountRepository.add(userid,"account 1","iban 1",0);

        return "";
    }

    public ArrayList<AccountNoAndBalance> showbalance(String username){
        //her hesabı ve hesaplardaki parayı göster
        User user=getuser(username).get();
        int userid=(int)user.getId();

        Collection<UserAccount>dbAccounts=userAccountRepository.showbalance(userid);
        ArrayList<AccountNoAndBalance>list=new ArrayList<>();

        //
        for(UserAccount accountdb:dbAccounts){
            AccountNoAndBalance newaccount=new AccountNoAndBalance();

            newaccount.setAccountno(accountdb.getAccountno());
            newaccount.setBalance(accountdb.getBalance());

            list.add(newaccount);
        }
        return list;


    }

    public Collection<Transaction> showTransactions(String usernmae){
        //show transactions
        User user=getuser(usernmae).get();
        int userid=(int)user.getId();
        return transactionsRepository.getTransactions(userid);

    }

    public ArrayList<AccountNoAndIban> showAccountnoAndIban(String username){
        //hesaplar ve ibanları gösterir
        User user=getuser(username).get();
        int userid=(int)user.getId();
        ArrayList<AccountNoAndIban>list=new ArrayList<>();
        for(UserAccount accountdb:userAccountRepository.getAccount(userid)){

            AccountNoAndIban newaccountandiban=new AccountNoAndIban();

            newaccountandiban.setAccountno(accountdb.getAccountno());
            newaccountandiban.setIban(accountdb.getIban());

            list.add(newaccountandiban);
        }
        return list;
    }

    public String openNewAccount(String username){
        //open new account
        User user=getuser(username).get();
        int userid=(int)user.getId();

        int numberofaccount=1+userAccountRepository.getAccount(userid).toArray().length;
        String accountno="accountno "+numberofaccount;
        String iban="iban "+numberofaccount;
        userAccountRepository.add(userid,accountno,iban,0);
        return "yeni hesap açıldı";


    }

    public String closeAccount(String username,String accountno){
        //close account if it exists if there is money send another account if exists else money is gone
        User user=getuser(username).get();
        int userid=(int)user.getId();



        if(!userAccountRepository.existsByAccountno(userid,accountno)){
            return "there is already no such an account";
        }
        Optional<UserAccount>accountdb=userAccountRepository.getexistsByAccountno(userid,accountno);

        if(accountdb.get().getBalance()==0){
            //there is no money just close account
            userAccountRepository.closeAccount(userid,accountno);
            return "account is closed";
        }else{
            //if user has another account send the money a-other account
            //if user doesnt have another account money just gone
        }

        //if account doesnt have money just close




        return "";
    }

    public String moneytransefer(String username, DtoMoneyTransefer dtoMoneyTransefer){
        //transfer money from one account to another
        //if sender have account and money and receiver exists and have account then send the money
        //decrase money from this account and increase money from receiver account

        User user=getuser(username).get();
        int userid=(int)user.getId();


        String senderaccountno=dtoMoneyTransefer.getSenderaccountno();
        int receiverid= dtoMoneyTransefer.getReceiverid();
        String receiveraccountno=dtoMoneyTransefer.getReceiveraccountno();
        int amount= dtoMoneyTransefer.getAmount();
        String receiverusername=dtoMoneyTransefer.getReceiverusername();

        //looking sender has a account?
        if(!userAccountRepository.existsByAccountno(userid,senderaccountno)){
            return "there is no such an account";
        }
        //looking receiver is exists?
        if(!usersRepository.existsByUsername(receiverusername)){
            return "there is no such an user";
        }
        //looking receiver has account ?
        if(!userAccountRepository.existsByAccountno(receiverid,receiveraccountno)){
            return "there is no such an account";
        }



        Optional<UserAccount>myaccountdb=userAccountRepository.getexistsByAccountno(userid,senderaccountno);
        int mybalance=myaccountdb.get().getBalance();
        if(mybalance>=amount){
            //everything is great send money

            //deduct balance  bu hesaptan parayı düşelim
            userAccountRepository.changeMoney(userid,senderaccountno,mybalance-amount);

            //add the money to receiver
            Optional<UserAccount>receviverdb=userAccountRepository.getexistsByAccountno(receiverid,receiveraccountno);
            int receiverbalance=receviverdb.get().getBalance();
            userAccountRepository.changeMoney(receiverid,receiveraccountno,receiverbalance+amount);


            return "the money sended ";
        }else{
            return "insufficient balance";
        }


    }

    private Optional<User> getuser(String username){
        return  usersRepository.getUserbyName(username);
    }

}
