package bank.project.Controller;

import bank.project.Entities.Transaction;
import bank.project.Entities.UserAccount;
import bank.project.Services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@CrossOrigin("*")
@RestController
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/admin/get/users")
    public void getUsers(){

    }
    @GetMapping("/admin/get/useaccounts")
    public void getUseraccounts(){

    }
    @GetMapping("/admin/get/transactions")
    public Collection<Transaction> gettransactions(@RequestParam String page){
        return adminService.getTransactions(page);
    }


}
