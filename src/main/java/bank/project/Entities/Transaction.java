package bank.project.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int senderid;
    private String senderaccountno;
    private  int receiverid;
    private String receiveraccountno;
    private int amount;

    @CreatedDate
    @Column(name = "createdat")
    private LocalDateTime createdAt;

}
