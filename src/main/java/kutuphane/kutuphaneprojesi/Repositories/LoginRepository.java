package kutuphane.kutuphaneprojesi.Repositories;

import kutuphane.kutuphaneprojesi.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface LoginRepository extends JpaRepository<User,Long> {
    @Query(value = "select * from users where username=:username and password=:password", nativeQuery = true)
    Collection<User> getuser(String username,String password);
}
