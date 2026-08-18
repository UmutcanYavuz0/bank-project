package kutuphane.kutuphaneprojesi.Controller;

import kutuphane.kutuphaneprojesi.Dto.DtoUser;
import kutuphane.kutuphaneprojesi.Dto.DönenToken;
import kutuphane.kutuphaneprojesi.Services.LoginService;
import kutuphane.kutuphaneprojesi.Services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class Login_Register {
    @Autowired
    private LoginService loginService;
    @Autowired
    private RegisterService registerService;

    @PostMapping("/login")
    public DönenToken login(@RequestBody DtoUser user){
        return loginService.login(user.getUsername(), user.getPassword());
    }

    @PostMapping("/register")
    public String register(@RequestBody DtoUser user){
        return registerService.register(user.getUsername(), user.getPassword());

    }



}
