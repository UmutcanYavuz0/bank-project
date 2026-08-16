package kutuphane.kutuphaneprojesi.Repositories;

import jakarta.transaction.Transactional;
import kutuphane.kutuphaneprojesi.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    @Transactional
    @Modifying
    @Query(value = "insert into users (username,password,role)values(:username, :password, :role)",nativeQuery = true)
    void register(String username,String password,String role);




}
