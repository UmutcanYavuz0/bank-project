package kutuphane.kutuphaneprojesi.Services;

import kutuphane.kutuphaneprojesi.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    private UserRepository userRepository;

    public String register(String username,String password){
        //kullanıcı adı daha önceden yoksa kaydet
        //
        boolean uservarmi=userRepository.existsByUsername(username);
        if(uservarmi){
            return "böyle bir kullanıcı mevcut lütfen login ol";
        }else{
            //demekki kulanıcı yok db ye kaydet
            userRepository.register(username,password,"USER");
            return "kayıt tamamlandı";

        }
    }
}
