package kutuphane.kutuphaneprojesi.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="readinghistory")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReadingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;

    private String userid;

    private String bookid;
}
