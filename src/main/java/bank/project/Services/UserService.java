package bank.project.Services;

import bank.project.Dto.AccountNoAndBalance;
import bank.project.Dto.AccountNoAndIban;
import bank.project.Dto.DtoMoneyTransefer;
import bank.project.Dto.DtoUser;
import bank.project.Entities.Transaction;
import bank.project.Entities.User;
import bank.project.Entities.UserAccount;
import bank.project.Jwt.JwtService;
import bank.project.Repositories.TransactionsRepository;
import bank.project.Repositories.UserAccountRepository;
import bank.project.Repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Autowired
    private JwtService jwtService;

    public String register(DtoUser user){
        //if user not exists
        //add new user to users
        //open new account and iban and set money to 0 on userAccount


        String username=user.getUsername();
        String password=user.getPassword();

        if(usersRepository.existsByUsername(username)){
            return "böyle bir kullanıcı mevcut login olun";
        }

        //adding new user to users table
        //usersRepository.reigster(username,password);
        usersRepository.save(new User(username,password));


        long id=usersRepository.findByUsernameAndPassword(username,password).get().getId();
        int userid = (int) id;


        //openin new account and iban and set money to 0

        userAccountRepository.save(new UserAccount(userid,"account 1","iban 1",0));

        return "registered succesfully";
    }

    public String login(DtoUser user){
        String username=user.getUsername();
        String password=user.getPassword();
        if(usersRepository.existsByUsernameAndPassword(username,password)){
            //generata token
            return jwtService.generateToken(username);
        }else{
            return " username or password incorrect";
        }
    }

    public ArrayList<AccountNoAndBalance> showbalance(String username){
        //her hesabı ve hesaplardaki parayı göster
        User user=getuser(username).get();
        String userid=String.valueOf(user.getId());

        Collection<UserAccount>dbAccounts=userAccountRepository.getBalance(userid);
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
        String userid=String.valueOf(user.getId());
        return transactionsRepository.getTransactions(userid);

    }

    public ArrayList<AccountNoAndIban> showAccountnoAndIban(String username){
        //hesaplar ve ibanları gösterir
        User user=getuser(username).get();
        String userid=String.valueOf(user.getId());
        ArrayList<AccountNoAndIban>list=new ArrayList<>();
        for(UserAccount accountdb:userAccountRepository.getAccounts(userid)){

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
        String userid=String.valueOf(user.getId());

        int numberofaccount=1+userAccountRepository.getAccounts(userid).toArray().length;
        String accountno="accountno "+numberofaccount;
        String iban="iban "+numberofaccount;
        //userAccountRepository.add(userid,accountno,iban,0);
        UserAccount save=new UserAccount(Integer.parseInt(userid),accountno,iban,0);
        userAccountRepository.save(save);
        return "yeni hesap açıldı";


    }

    public String addMoney(String username,String depositMoney,String accountno){

        String userid=String.valueOf(getuser(username).get().getId());
        if(userAccountRepository.existsByUseridAndAccountno(userid,accountno)){
            Optional<UserAccount> userAccount=userAccountRepository.findByUseridAndAccountno(userid,accountno);
            int newbalance=userAccount.get().getBalance()+Integer.parseInt(depositMoney);


            userAccountRepository.updateBalance(String.valueOf(newbalance),
                    userid,accountno);
            return "para "+accountno+" hesabınıza "+depositMoney+" tl yatırıldı";
        }else {
            return "there is no account";
        }
    }

    public String closeAccount(String username,String accountno){
        //close account if it exists if there is money send another account if exists else money is gone
        User user=getuser(username).get();
        String userid=String.valueOf(user.getId());



        if(!userAccountRepository.existsByUseridAndAccountno(userid,accountno)){
            return "there is already no such an account";
        }
        //kullanıcının sahip olduğu hesabı getirir
        Optional<UserAccount>accountdb=userAccountRepository.findByUseridAndAccountno(userid,accountno);

        if(accountdb.get().getBalance()==0){
            //there is no money just close account
            userAccountRepository.closeAccount(userid,accountno);
            return "account is closed";
        }else{

            //if user has another account send the money a-other account
            //if user doesnt have another account money just gone

            //eski hesabın parasını yakaladık
            Optional<UserAccount>olduseraccount=userAccountRepository.findByUseridAndAccountno(userid,accountno);
            int oldmoney=olduseraccount.get().getBalance();

            //eski hesabı sil
            userAccountRepository.closeAccount(userid,accountno);



            if(userAccountRepository.existsByUserid(userid)){//başka hesabı varsa
                //there is another acount ,transfer money  and close account
                Optional<UserAccount>userfirstaccount=userAccountRepository.findFirstByUserid(userid);
                String aktarılacakuseracccount=userfirstaccount.get().getAccountno();

                //yeni parayı kaydet
                int newmoney=oldmoney+userfirstaccount.get().getBalance();
                userAccountRepository.updateBalance(String.valueOf(newmoney),userid,aktarılacakuseracccount);
            }
            return "account  closed";
        }

        //if account doesnt have money just close





    }

    public String moneytransefer(String username, DtoMoneyTransefer dtoMoneyTransefer){
        //transfer money from one account to another
        //if sender have account
        // and money
        // and receiver exists
        // and have account
        // then send the money
        //decrase money from this account and increase money from receiver account

        User moneysender=getuser(username).get();
        String userid=String.valueOf(moneysender.getId());

        //para gönderenin hesabı var mı diye bakıyor
        if(!userAccountRepository.existsByUseridAndAccountno(String.valueOf(moneysender.getId()),dtoMoneyTransefer.getSenderaccountno())){
            return "there is no sender account";
        }

        if(!userAccountRepository.existsByUserid(String.valueOf(dtoMoneyTransefer.getReceiverid()))){
            return "böyle bir alıcı yok";
        }
        if(!userAccountRepository.existsByUseridAndAccountno(String.valueOf(dtoMoneyTransefer.getReceiverid()),
                dtoMoneyTransefer.getReceiveraccountno())){
            return "alıcı hesabı yok";
        }

        UserAccount moneysendersaccount=userAccountRepository.findByUseridAndAccountno(
                String.valueOf(moneysender.getId()),dtoMoneyTransefer.getSenderaccountno()
        ).get();
        if(moneysendersaccount.getBalance()<dtoMoneyTransefer.getAmount()){
            return "yetersiz bakiye";
        }

        if(!usersRepository.existsByUsername(dtoMoneyTransefer.getReceiverusername())){
            return "böyle br alıcı yok";
        }
        User alıcıuser=getuser(dtoMoneyTransefer.getReceiverusername()).get();
        UserAccount alıcıaccount=userAccountRepository.findByUseridAndAccountno(String.valueOf(dtoMoneyTransefer.getReceiverid()),dtoMoneyTransefer.getReceiveraccountno()).get();


        //gönderenin hesabından düş
        userAccountRepository.updateBalance(
                String.valueOf(moneysendersaccount.getBalance()- dtoMoneyTransefer.getAmount()),
                String.valueOf(moneysender.getId()),
                moneysendersaccount.getAccountno()
        );

        //alıcıya ekle

        userAccountRepository.updateBalance(
                String.valueOf(alıcıaccount.getBalance()+dtoMoneyTransefer.getAmount()),
                String.valueOf(alıcıuser.getId()),
                dtoMoneyTransefer.getReceiveraccountno()
        );
        Transaction save=new Transaction(Integer.parseInt(userid),
                dtoMoneyTransefer.getSenderaccountno(),
                dtoMoneyTransefer.getReceiverid(),
                dtoMoneyTransefer.getReceiveraccountno(),
                dtoMoneyTransefer.getAmount(),
                LocalDateTime.now());
        transactionsRepository.save(save);


        return "para aktarıldı "+moneysender.getUsername()+" "+dtoMoneyTransefer.getSenderaccountno()+
                " hesabından "+dtoMoneyTransefer.getReceiverusername()+" "+dtoMoneyTransefer.getReceiveraccountno()+
                "hesabına "+dtoMoneyTransefer.getAmount()+" tl para aktarıldı";


    }

    private Optional<User> getuser(String username){
        return  usersRepository.findByUsername(username);
    }

}
