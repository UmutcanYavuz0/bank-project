package kutuphane.kutuphaneprojesi.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="borrowed_books")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BorrowedBooks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;
    private String userid;
    private String bookid;
}
