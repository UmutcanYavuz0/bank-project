package kutuphane.kutuphaneprojesi.Repositories;

import jakarta.transaction.Transactional;
import kutuphane.kutuphaneprojesi.Dto.DtoBook;
import kutuphane.kutuphaneprojesi.Entities.Book;
import kutuphane.kutuphaneprojesi.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AdminRepository extends JpaRepository<User,Long> {

    @Query(value = "select * from users", nativeQuery = true)
    Collection<User> getusers();



}
