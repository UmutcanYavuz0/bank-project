package kutuphane.kutuphaneprojesi.Services;

import kutuphane.kutuphaneprojesi.Dto.DönenToken;
import kutuphane.kutuphaneprojesi.Entities.User;
import kutuphane.kutuphaneprojesi.Jwt.JwtService;
import kutuphane.kutuphaneprojesi.Repositories.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class LoginService {
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private JwtService jwtService;

    public DönenToken login(String username,String password){
        Collection<User>userdb= loginRepository.getuser(username,password);

        if(userdb.isEmpty()){
            System.out.println("empty");
            throw new RuntimeException("kullanıcı adı veya şifre hatalı");
        }
        //burda toen oluşturup token dönmeliyiz
        return new DönenToken(jwtService.generateToken(username));
    }





}
