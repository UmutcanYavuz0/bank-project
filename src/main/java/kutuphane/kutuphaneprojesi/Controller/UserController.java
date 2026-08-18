package kutuphane.kutuphaneprojesi.Controller;

import kutuphane.kutuphaneprojesi.Dto.DtoBook;
import kutuphane.kutuphaneprojesi.Entities.ReadingHistory;
import kutuphane.kutuphaneprojesi.Jwt.JwtService;
import kutuphane.kutuphaneprojesi.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
@CrossOrigin(origins = "*")
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("user/get/books")
    public ArrayList<DtoBook> getBooks(){
        return userService.getBooks();
    }

    @PostMapping("/user/barrow/book")
    public String barrowBook(@RequestHeader("Authorization") String header,
                            @RequestParam String id){
        String token = header;
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7).trim(); // İlk 7 karakteri ("Bearer ") atar ve boşlukları siler
        }
        String username = jwtService.extractUsername(token);

        return userService.barrowBook(username,id);

    }

    @GetMapping("/get/mybooks")
    public ArrayList<String> getmybooks(@RequestHeader("Authorization") String header){

        String token=header.substring(7).trim();
        String username= jwtService.extractUsername(token);
        return userService.getMyBooks(username);
    }

    @GetMapping("/get/readinghistory")
    public Collection<ReadingHistory> getreadinghistory(@RequestHeader("Authorization") String header){
        String token = header;
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7).trim(); // İlk 7 karakteri ("Bearer ") atar ve boşlukları siler
        }
        String username = jwtService.extractUsername(token);
        return userService.getreadinghistory(username);


    }

    @PostMapping("/user/return/book")
    public String returnbook(@RequestParam String id,
                           @RequestHeader("Authorization") String header){
        String token = header;
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7).trim(); // İlk 7 karakteri ("Bearer ") atar ve boşlukları siler
        }
        String username = jwtService.extractUsername(token);
        return userService.returnBook(username,id);

    }


}
