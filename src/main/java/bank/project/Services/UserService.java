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
import java.util.Random;

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
            throw new RuntimeException("böyle bir kullanıcı mevcut login olun");
        }

        //adding new user to users table
        //usersRepository.reigster(username,password);
        usersRepository.save(new User(username,password));


        long id=usersRepository.findByUsernameAndPassword(username,password).get().getId();
        int userid = (int) id;


        //openin new account and iban and set money to 0
        Random r=new Random();
        String iban=String.valueOf(r.nextInt(1000));


        userAccountRepository.save(new UserAccount(userid,"account 1",iban,0));

        return "registered succesfully";
    }

    public String login(DtoUser user){
        String username=user.getUsername();
        String password=user.getPassword();
        if(usersRepository.existsByUsernameAndPassword(username,password)){
            //generata token
            return jwtService.generateToken(username);
        }else{
            throw new RuntimeException(" username or password incorrect") ;
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

        User user=getuser(username).get();
        String userid=String.valueOf(user.getId());

        // bu kişinin tüm hesaplarını getir
        Collection<UserAccount>accountsdb=userAccountRepository.findByUserid(userid);
        Random r=new Random();
        if(accountsdb.isEmpty()){
            //account1 adında hrsap ekle çık
            String iban=String.valueOf(r.nextInt(1000));
            UserAccount save=new UserAccount(Integer.parseInt(userid),"account 1",iban,0);
            userAccountRepository.save(save);
            return "yeni hesap açıldı";

        }

        ArrayList<String>names=new ArrayList<>();

        for (UserAccount caccount:accountsdb) {
            names.add(caccount.getAccountno());
        }



        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            if(!names.contains("account "+i)){
                String iban=String.valueOf(r.nextInt(1000));
                UserAccount save=new UserAccount(Integer.parseInt(userid),"account "+i,iban,0);
                userAccountRepository.save(save);
                break;
            }
        }

            //yen ihesabı ekle






        //open new account



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
        //decrase money from this account
        //  increase money from receiver account
        //transaction a kaydet

        //public class DtoMoneyTransefer {
        //    private String senderaccountno;
        //    private int receiverid;
        //    private String receiveraccountno;
        //    private int amount;
        //    private String receiverusername;
        User sender=getuser(username).get();
        String senderid=String.valueOf(sender.getId());
        String senderaccountno=dtoMoneyTransefer.getSenderaccountno();
        String receiverid=String.valueOf(dtoMoneyTransefer.getReceiverid());
        String receiveraccountno=dtoMoneyTransefer.getReceiveraccountno();
        int amount= dtoMoneyTransefer.getAmount();
        String receiverusername=dtoMoneyTransefer.getReceiverusername();



        //looking for the sender hava account
        if(!userAccountRepository.existsByUseridAndAccountno(senderid,senderaccountno)){
            throw new RuntimeException("there is no sender account");
        }
        int senderamount=userAccountRepository.findByUseridAndAccountno(senderid,senderaccountno).get().getBalance();
        //looking for the sender have enough money to send
        if(senderamount<amount){
            throw new RuntimeException("no enough money");
        }
        //looking for the receiver exitsts or not
        if(!userAccountRepository.existsByUserid(receiverid)){
            throw new RuntimeException("there is no user");
        }
        //looking for the receiver has acoount
        if(!userAccountRepository.existsByUseridAndAccountno(receiverid,receiveraccountno)){
            throw new RuntimeException("there is no receiver account");
        }
        // if program is here then everything is ok ey just sende money

        int sendernewbalance=senderamount-amount;
        //decrase money from sender
        userAccountRepository.updateBalance(String.valueOf(sendernewbalance),senderid,senderaccountno);

        //increase money from receiver
        UserAccount receiveraccount=userAccountRepository.findByUseridAndAccountno(receiverid,receiveraccountno).get();
        int receivernewbalance=receiveraccount.getBalance()+amount;
        userAccountRepository.updateBalance(String.valueOf(receivernewbalance),receiverid,receiveraccountno);

        //save transaction to transactions table
        //public Transaction(int senderid, String senderaccountno, int receiverid, String receiveraccountno, int amount, LocalDateTime createdAt) {
        //        this.senderid = senderid;
        //        this.senderaccountno = senderaccountno;
        //        this.receiverid = receiverid;
        //        this.receiveraccountno = receiveraccountno;
        //        this.amount = amount;
        //        this.createdAt = createdAt;
        //    }
        Transaction save=new Transaction(Integer.parseInt(senderid),
                senderaccountno,
                Integer.parseInt(receiverid),
                receiveraccountno,
                amount,
                LocalDateTime.now());
        transactionsRepository.save(save);



        return amount+" tl sended from "+senderid+" "+senderaccountno+" to "+receiverid+" "+receiveraccountno;


    }

    public String moneytransferByIban(String username,String receiveriban,String senderaccountno,int amount){
        //sender  heasabı var mı-->existsbyiban
        //para yeterlimi-->username in accountgetir bak
        //receiver ibanı doğru mu-->exists by iban
        //parayı yolla
        //sender dan parayı düş
        //receiver e ekle
        //transaction a kaydet
        User sender=getuser(username).get();

        //looking for does sender have account
        if(!userAccountRepository.existsByUseridAndAccountno(String.valueOf(sender.getId()),senderaccountno)){
            throw new RuntimeException("sender doesnt have th,s account");
        }
        UserAccount senderaccount=userAccountRepository.findByUseridAndAccountno(String.valueOf(sender.getId()),senderaccountno).get();

        //looking for has enough money
        int senderbalance= senderaccount.getBalance();
        if(senderbalance<amount){
            throw new RuntimeException("sender doesnt have enough money");
        }

        //looking for receiver iban exists
        if(!userAccountRepository.existsByIban(receiveriban)){
            throw new RuntimeException("receiver account not exists");
        }
        //everything is okey ,send money

        //decrase money from sender
        String sendernewbalance=String.valueOf(senderaccount.getBalance()-amount);
        //commit
        userAccountRepository.updateBalance(sendernewbalance,String.valueOf(sender.getId()),senderaccount.getAccountno());

        //increase money from receiver
        UserAccount receiveraccount=userAccountRepository.findByIban(receiveriban).get();
        String receiveruserid=String.valueOf(receiveraccount.getUserid());
        String receivernewbalance=String.valueOf(amount+receiveraccount.getBalance());
        userAccountRepository.updateBalance(receivernewbalance,receiveruserid,receiveraccount.getAccountno());

        //save to transactions table
        //public Transaction(int senderid, String senderaccountno, int receiverid, String receiveraccountno, int amount, LocalDateTime createdAt) {
        //        this.senderid = senderid;
        //        this.senderaccountno = senderaccountno;
        //        this.receiverid = receiverid;
        //        this.receiveraccountno = receiveraccountno;
        //        this.amount = amount;
        //        this.createdAt = createdAt;
        //    }
        Transaction save=new Transaction(
                (int)sender.getId(),
                senderaccount.getAccountno(),
                receiveraccount.getUserid(),
                receiveraccount.getAccountno(),
                amount,
                LocalDateTime.now()
        );
        transactionsRepository.save(save);


        return amount+" tl transfered from "+senderaccount.getAccountno()+" to "+receiveraccount.getAccountno();

    }
    private Optional<User> getuser(String username){
        return  usersRepository.findByUsername(username);
    }

    public String pay(String username,String accountno,String ödemetutarı){


        String userid=String.valueOf(getuser(username).get().getId());

        int ödenecektutar=Integer.parseInt(ödemetutarı);
        //hesap varsa
        if(!userAccountRepository.existsByUseridAndAccountno(userid,accountno)){
            throw new RuntimeException("there is no account");
        }

        UserAccount userAccount=userAccountRepository.findByUseridAndAccountno(userid,accountno).get();
        int bakiye=userAccount.getBalance();

        //para varsa
        if(bakiye<ödenecektutar){
            throw new RuntimeException("bakiye yetersiz");
        }

        int yenibakiye=bakiye-ödenecektutar;

        //hesaptan para düşüldü
        userAccountRepository.updateBalance(String.valueOf(yenibakiye),
                userid,accountno);

        //transactionlara kaydedildi
        Transaction save=new Transaction(Integer.parseInt(userid),
                accountno,
                Integer.parseInt(userid),
                "payment",
                Integer.parseInt(ödemetutarı),
                LocalDateTime.now());
        transactionsRepository.save(save);
        return "ödeme başarılı";



    }

}
