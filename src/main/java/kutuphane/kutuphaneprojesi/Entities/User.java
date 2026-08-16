package kutuphane.kutuphaneprojesi.Entities;

import jakarta.persistence.*;
import kutuphane.kutuphaneprojesi.Enums.Role;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements UserDetails {
    ///Bu sınıf iki farklı görevi aynı anda üstleniyor: hem JPA entity'si (veritabanı tablosu)
    /// hem de Spring Security'nin anladığı UserDetails sözleşmesi. implements UserDetails yazdığın an,
    /// aşağıdaki 6 metodu (veya bunların default halini) sağlamak zorundasın.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }


}
