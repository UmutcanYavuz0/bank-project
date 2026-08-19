package bank.project.Repositories;

import bank.project.Entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UsersRepository  extends JpaRepository<User,Long>{


    @Transactional
    @Modifying
    @Query(value = "insert into users(username,password)values (:username,:password)",nativeQuery = true)
    void reigster(String username,String password);

    @Query(value = "select * from users where username=:username and password=:password)",nativeQuery = true)
    Optional<User> getUser(String username,String password);

    @Query(value = "select * from users where username=:username )",nativeQuery = true)
    Optional<User> getUserbyName(String username);




    @Query(value = "select * from users where username=:username",nativeQuery = true)
    boolean existsByUsername(String username);

    @Query(value = "select * from users where username=:username",nativeQuery = true)
    Optional<UserDetails> findByUsername(String username);


}
