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


    @Query(value = "select * from useraccount where userid=:userid",nativeQuery = true)
    Collection<UserAccount> getBalance(String userid);

    @Query(value = "select * from useraccount where userid=:userid",nativeQuery = true)
    Collection<UserAccount>getAccounts(String userid);

    boolean existsByUseridAndAccountno(String userid,String accountno);


    Optional<UserAccount>findByUseridAndAccountno(String userid,String accountno);

    Collection<UserAccount>findByUserid(String userid);

    @Transactional
    @Modifying
    @Query(value = "delete from useraccount where userid=:userid and accountno=:accountno",nativeQuery = true)
    void closeAccount(String userid,String accountno);

    @Transactional
    @Modifying
    @Query(value = "update useraccount set balance=:newbalance where userid=:userid and accountno=:accountno",nativeQuery = true)
    void updateBalance(String newbalance,String userid,String accountno);

    //bu var olan hesaplar arasından yanlızca birini döner
    Optional<UserAccount> findFirstByUserid(String username);

    boolean existsByUserid(String userid);

    boolean existsByIban(String iban);

    Optional<UserAccount> findByIban(String iban);










}
