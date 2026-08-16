package kutuphane.kutuphaneprojesi.Services;

import kutuphane.kutuphaneprojesi.Dto.DtoBook;

import kutuphane.kutuphaneprojesi.Entities.Book;
import kutuphane.kutuphaneprojesi.Entities.BorrowedBooks;
import kutuphane.kutuphaneprojesi.Entities.ReadingHistory;
import kutuphane.kutuphaneprojesi.Entities.User;
import kutuphane.kutuphaneprojesi.Repositories.AdminRepository;
import kutuphane.kutuphaneprojesi.Repositories.BarrowBookRepository;
import kutuphane.kutuphaneprojesi.Repositories.BookRepository;
import kutuphane.kutuphaneprojesi.Repositories.ReadingHistroyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BarrowBookRepository barrowBookRepository;
    @Autowired
    private ReadingHistroyRepository readingHistroyRepository;

    public Collection<User> getUsers(){
        return adminRepository.getusers();
    }

    public String addBook(String bookname){
        bookRepository.addBook(bookname);
        return "kitap eklendi";
    }

    public String deleteBook(String id){
        boolean varmi=bookRepository.existsById(Long.valueOf(id));
        if(varmi){
            bookRepository.deleteBookWithId(id);
            return "kitap silindi";

        }else{
            return "bu id de kitap yok";
        }

    }

    public Collection<Book> getBooks(){
        return bookRepository.getBooks();
    }

    public Collection<BorrowedBooks> getBorrowedBooks(){
        return barrowBookRepository.getBorrowdBooks();
    }
    public Collection<ReadingHistory> getallhistory(){
        return readingHistroyRepository.getAll();
    }




}
