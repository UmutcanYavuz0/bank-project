package kutuphane.kutuphaneprojesi.Repositories;

import jakarta.transaction.Transactional;

import kutuphane.kutuphaneprojesi.Entities.BorrowedBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BarrowBookRepository extends JpaRepository<BorrowedBooks,Long> {

    @Query(value = "select * from borrowed_books",nativeQuery = true)
    Collection<BorrowedBooks>getBorrowdBooks();

    //id ye sahip kullanıcının sahip olduğu kitap borrowed_books satırlarını   döner
    @Query(value = "select * from borrowed_books where userid=:id",nativeQuery = true)
    Collection<BorrowedBooks>getBorrowdBookswithId(String id);

    @Transactional
    @Modifying
    @Query(value = "insert into borrowed_books(userId,bookId) values(:userId,:bookId)",nativeQuery = true)
    void add(String userId,String bookId);


}
