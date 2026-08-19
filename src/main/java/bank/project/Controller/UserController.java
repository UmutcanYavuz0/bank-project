package bank.project.Controller;

import bank.project.Dto.AccountNoAndBalance;
import bank.project.Dto.AccountNoAndIban;
import bank.project.Dto.DtoMoneyTransefer;
import bank.project.Entities.Transaction;
import bank.project.Jwt.JwtService;
import bank.project.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class UserController {
    //1-)bakiye görme
    //2-)hesap hareketleri görme
    //3-)hesapları ve ibanları görme
    //4-)hesap açma/kapama
    //5-)para transfer etme
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    //@RequestHeader("Authorization") String header


    @GetMapping("/user/get/balance")
    public ArrayList<AccountNoAndBalance> showbalance(@RequestHeader("Authorization") String header){
        String username=getusernameFromToken(header);
        return userService.showbalance(username);
    }

    @GetMapping("/user/get/transactions")
    public Collection<Transaction> showTransactions(@RequestHeader("Authorization") String header){
        String username=getusernameFromToken(header);
        return userService.showTransactions(username);
    }

    @GetMapping("/user/get/accountandiban")
    public ArrayList<AccountNoAndIban> showAccountnoAndIban(@RequestHeader("Authorization") String header){
        String username=getusernameFromToken(header);

        return userService.showAccountnoAndIban(username);

    }

    @PostMapping("/user/open/newaccount")
    public String openNewAccount(@RequestHeader("Authorization") String header){
        String username=getusernameFromToken(header);
        return userService.openNewAccount(username);
    }

    @PostMapping("/user/close/account")
    public String closeAccount(@RequestHeader("Authorization") String header,
                             @RequestParam String accountno) {
        String username=getusernameFromToken(header);
        return userService.closeAccount(username,accountno);

    }
    @PostMapping("/transfer/money")
    public String moneytransefer(@RequestHeader("Authorization") String header,
                               DtoMoneyTransefer dtoMoneyTransefer){
        String username=getusernameFromToken(header);

        return userService.moneytransefer(username,dtoMoneyTransefer);
    }

    private String getusernameFromToken(String header){
        String token = header;
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7).trim(); // İlk 7 karakteri ("Bearer ") atar ve boşlukları siler
        }
        String username = jwtService.extractUsername(token);
        return username;
    }
}
