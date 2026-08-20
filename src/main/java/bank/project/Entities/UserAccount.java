package bank.project.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "useraccount")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAccount {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;

    private int userid;
    private String accountno;
    private String iban;
    private int balance;

    public UserAccount(int userid, String accountno, String iban, int balance) {
        this.userid = userid;
        this.accountno = accountno;
        this.iban = iban;
        this.balance = balance;
    }
}
