package kutuphane.kutuphaneprojesi.Controller;

import kutuphane.kutuphaneprojesi.Dto.DtoBook;

import kutuphane.kutuphaneprojesi.Entities.Book;
import kutuphane.kutuphaneprojesi.Entities.BorrowedBooks;
import kutuphane.kutuphaneprojesi.Entities.ReadingHistory;
import kutuphane.kutuphaneprojesi.Entities.User;
import kutuphane.kutuphaneprojesi.Jwt.JwtService;
import kutuphane.kutuphaneprojesi.Services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;
@CrossOrigin("*")
@RestController
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/admin/get/users")
    public Collection<User> getusers(){
        return adminService.getUsers();
    }


    @PostMapping("/add/book")
    public String addBook(@RequestBody DtoBook book){
        return adminService.addBook(book.getBookName());
    }

    @DeleteMapping("/delete/book")
    public String deleteBook(@RequestParam String id){
        return adminService.deleteBook(id);
    }
    @GetMapping("/admin/get/books")
    public Collection<Book> getBooks(){
        return adminService.getBooks();
    }

    @GetMapping("/get/barrowedbooks")
    public Collection<BorrowedBooks> barrowedBooks(){
        return adminService.getBorrowedBooks();
    }

    @GetMapping("/get/allreadinghistory")
    public Collection<ReadingHistory> getallreadinghistory(){
        return adminService.getallhistory();
    }

    @GetMapping("/admin/get/readinghistory")
    public Collection<ReadingHistory> getreadinghitory(@RequestParam String userid){
        return adminService.getreadinghistory(userid);
    }
    //admin istediği kişinin historysini görsün


    private String getusernameFromToken(String header){
        String token = header;
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7).trim(); // İlk 7 karakteri ("Bearer ") atar ve boşlukları siler
        }
        String username = jwtService.extractUsername(token);
        return username;
    }


}
