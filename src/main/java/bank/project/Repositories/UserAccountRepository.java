package bank.project.Repositories;

import bank.project.Entities.UserAccount;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount,Long> {

    @Transactional
    @Modifying
    @Query(value = "insert into usseraccount(userid,accountno,iban,balance)values(:userid,:accountno,:iban,:balance)",nativeQuery = true)
    void add(int userid,String accountno,String iban,int balance);


    @Query(value = "select * from useraccount where userid=:userid",nativeQuery = true)
    Collection<UserAccount> getAccount(int userid);


    @Query(value = "select * from useraccount where userid=:userid",nativeQuery = true)
    Collection<UserAccount> showbalance(int userid);

    @Query(value = "select * from useraccount where userid=:userid and accountno=:accountno",nativeQuery = true)
    boolean existsByAccountno(int userid,String accountno);

    @Query(value = "select * from useraccount where userid=:userid and accountno=:accountno",nativeQuery = true)
    Optional<UserAccount> getexistsByAccountno(int userid, String accountno);

    @Transactional
    @Modifying
    @Query(value = "delete from useraccount where userid=:userid and accountno=:accountno",nativeQuery = true)
    void closeAccount(int userid, String accountno);

    @Transactional
    @Modifying
    @Query(value = "update useraccount set balance=:newbalance where userid=:userid and accountno=:accountno",nativeQuery = true)
    void changeMoney(int userid,String accountno, int newbalance);




}
