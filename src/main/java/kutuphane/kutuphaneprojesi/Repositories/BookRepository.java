package kutuphane.kutuphaneprojesi.Repositories;

import jakarta.transaction.Transactional;
import kutuphane.kutuphaneprojesi.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long> {
    @Transactional
    @Modifying
    @Query(value = "insert into books(name,stocknumber)values(:bookName,1)",nativeQuery = true)
    void addBook(String bookName);

    @Transactional
    @Modifying
    @Query(value = "delete from books where id=:id",nativeQuery = true)
    void deleteBookWithId(String id);


    @Query(value = "select * from books",nativeQuery = true)
    Collection<Book> getBooks();

    @Query(value = "select * from books where id=:id",nativeQuery = true)
    Optional<Book> getBookWithId(String id);

    @Query(value = "select * from books where id=:id",nativeQuery = true)
    Collection<Book> getstockofBook(String id);

    @Transactional
    @Modifying
    @Query(value = " update books set stocknumber=:stocknumber where id=:id",nativeQuery = true)
    void setStockOfBook(String id,String stocknumber);


}
