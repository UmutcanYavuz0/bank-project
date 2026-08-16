package kutuphane.kutuphaneprojesi.Services;

import kutuphane.kutuphaneprojesi.Entities.Book;
import kutuphane.kutuphaneprojesi.Repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class SearchService {

    //word gelicek book tanloasundan bu adı içerenleri dön

    @Autowired
    private BookRepository bookRepository;
    public ArrayList<String> search(String word){
        Collection<Book>dbbooks=bookRepository.getBooks();
        ArrayList<String>nameOfBooks=new ArrayList<>();

        for(Book book:dbbooks){
            nameOfBooks.add(book.getName());
        }
        ArrayList<String>dönecek=new ArrayList<>();
        for(String currentword:nameOfBooks){
            if(currentword.contains(word)){
                dönecek.add(currentword);
            }
        }

        return dönecek;
    }

}
