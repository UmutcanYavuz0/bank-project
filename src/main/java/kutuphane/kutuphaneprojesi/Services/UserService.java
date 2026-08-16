package kutuphane.kutuphaneprojesi.Services;

import kutuphane.kutuphaneprojesi.Dto.DtoBook;
import kutuphane.kutuphaneprojesi.Entities.Book;
import kutuphane.kutuphaneprojesi.Entities.BorrowedBooks;
import kutuphane.kutuphaneprojesi.Entities.User;
import kutuphane.kutuphaneprojesi.Jwt.JwtService;
import kutuphane.kutuphaneprojesi.Repositories.BarrowBookRepository;
import kutuphane.kutuphaneprojesi.Repositories.BookRepository;
import kutuphane.kutuphaneprojesi.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BarrowBookRepository barrowBookRepository;

    public ArrayList<DtoBook> getBooks(){

       Collection<Book>dbbooks= bookRepository.getBooks();
        ArrayList<DtoBook>list=new ArrayList<>();
       for(Book book:dbbooks){
           DtoBook dtoBook=new DtoBook();
           dtoBook.setBookName(book.getName());
           list.add(dtoBook);
       }
       return list;
    }


    public String barrowBook(String username,String bookToBarroedId) {

        //user varsa,alıcağı kitap varsa ve stockta varsa al

        boolean uservarmi=userRepository.existsByUsername(username);
        //kullanıcı var mı diye kontrol ediyor
        if(!uservarmi){
            return "böyle bir user yok";
        }

        //kitap var mı diye kontrol ediyor
        if(!bookRepository.existsById(Long.valueOf(bookToBarroedId))){
            return "böyle bir kitap yok";
        }

        //stoktamı diye bakıyor
        Collection<Book>dbBook=bookRepository.getstockofBook(bookToBarroedId);
        int stocknumber=0;
        for(Book book:dbBook){
            stocknumber=Integer.valueOf(book.getStocknumber());
            break;
        }
        if(stocknumber<=0){
            return "malesef stokta yok";
        }

        //bir azaltıp barrowed bookslara ekle
        bookRepository.setStockOfBook(bookToBarroedId,String.valueOf(stocknumber-1));
        Optional<User> userdb=userRepository.findByUsername(username);
        User user=userdb.get();
        barrowBookRepository.add(String.valueOf(user.getId()),bookToBarroedId);
        return "kitap başarılı bir şekilde ödünç alındı";



    }

    public ArrayList<String> getMyBooks(String username){
        String id=findIdByUsername(username);
        Collection<BorrowedBooks>borrowedBooksdb=barrowBookRepository.getBorrowdBookswithId(id);
        //borrowed_book satırları geldi
        //burdan aldığı kitapları id lerini topla
        ArrayList<String>takenbooklist=new ArrayList<>();
        for(BorrowedBooks borrowedBooks:borrowedBooksdb){
            takenbooklist.add(borrowedBooks.getBookid());
        }
        //her bir kitabın adını al listeye ekle listeyi dön

        ArrayList<String>dönecek=new ArrayList<>();
        for(String bookid:takenbooklist){
            Optional<Book>book=bookRepository.getBookWithId(bookid);
            dönecek.add(book.get().getName());
        }

        return dönecek;
    }


    private String findIdByUsername(String username){
        Optional<User>userdb=userRepository.findByUsername(username);
        return String.valueOf(userdb.get().getId());
    }




}
