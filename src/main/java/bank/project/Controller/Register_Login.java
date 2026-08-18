package bank.project.Controller;

import bank.project.Dto.DtoUser;
import bank.project.Services.LoginService;
import bank.project.Services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Register_Login {
    @Autowired
    private LoginService loginService;
    @Autowired
    private RegisterService registerService;

    @PostMapping("/login")
    public void login(@RequestBody DtoUser user){
         loginService.login(user.getUsername(), user.getPassword());
    }

    @PostMapping("/register")
    public String register(@RequestBody DtoUser user){

        return registerService.register(user.getUsername(), user.getPassword());

    }

}
