package bank.project.Services;

import bank.project.Entities.Transaction;
import bank.project.Entities.UserAccount;
import bank.project.Repositories.TransactionsRepository;
import bank.project.Repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AdminService {
    @Autowired
    private TransactionsRepository transactionsRepository;

    public Collection<Transaction> getTransactions(String page){

        return transactionsRepository.gettransactions((Integer.parseInt(page)-1)*5);
    }
}
