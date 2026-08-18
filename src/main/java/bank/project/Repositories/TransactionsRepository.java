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

    @Transactional
    @Modifying
    @Query(value = "insert into transactions(senderid,senderaccountno,receiverid,receiveraccountno,amount,createdat" +
            ")values(:senderid,:senderaccountno,:receiverid,:receiveraccountno,:amount,NOW()" +
            ")",nativeQuery = true)
    void add(int senderid,String senderaccountno,int receiverid,String receiveraccountno,int amount);


    @Query(value = "select * from transactions where userid=:userid",nativeQuery = true)
    Collection<Transaction> getTransactions(int userid);


}
