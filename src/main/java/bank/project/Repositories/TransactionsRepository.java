package bank.project.Repositories;

import bank.project.Entities.Transaction;
import bank.project.Entities.UserAccount;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction,Long> {


    @Query(value = "select * from transactions where senderid=:userid or receiverid=:userid",nativeQuery = true)
    Collection<Transaction> getTransactions(String userid);

    @Query(value = "select * from transactions order by createdat desc limit 5  offset :page", nativeQuery = true)
    Collection<Transaction>gettransactions(int page);
}
