package bank.project.Controller;

import bank.project.Dto.DtoUser;
import bank.project.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Register_Login {


    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody DtoUser user){
         return userService.login(user);
    }

    @PostMapping("/register")
    public String register(@RequestBody DtoUser user){

        return userService.register(user);

    }

}
