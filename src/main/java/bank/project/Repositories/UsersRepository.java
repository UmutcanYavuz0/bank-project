package bank.project.Repositories;

import bank.project.Entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UsersRepository  extends JpaRepository<User,Long>{

    //Veritabanından belirli alanlara göre kaydın kendisini (verisini)
    // çekmek için kullanılır. İlgili Entity'yi, Optional<Entity>
    //  nesnesini veya bir listeyi (List<Entity>) döner.
    //Tekil Sonuç: Optional<User> findByUsername(String username);
    //
    //    Arka planda çalıştırdığı sorgu: SELECT * FROM users WHERE username = ?
    //
    //Çoklu Şart (And / Or): Optional<User> findByEmailAndPassword(String email, String password);
    //
    //    Arka planda çalıştırdığı sorgu: SELECT * FROM users WHERE email = ? AND password = ?
    //
    //Liste Döndürme: List<User> findByRole(String role);
    //
    //    Arka planda çalıştırdığı sorgu: SELECT * FROM users WHERE role = ?
    //========================================================================================
    //existsBy Nedir ve Nasıl Çalışır?
    //
    //Veritabanında belirli bir şarta uyan kaydın var olup olmadığını kontrol etmek için kullanılır.
    // Verinin tamamını çekmek yerine yalnızca varlık durumunu kontrol eder ve geriye doğrudan boolean (true/false) döner.
    //
    //    Kullanıcı adı kontrolü: boolean existsByUsername(String username);
    //
    //        Arka planda optimize bir sorgu çalıştırır (veritabanına göre SELECT count(*) > 0 veya SELECT 1 ... LIMIT 1).
    //
    //    Çoklu Şart: boolean existsByEmailAndIsActive(String email, boolean isActive);

    //1. Doğrudan Entity Sınıfı (Tüm Kolonlar Çekiliyorsa)
    //Optional<User>   List<User>

    //2. Tek Bir Değer veya Kolon Çekiliyorsa (Primitive / Wrapper)
    //STRİNG lONG

    @Transactional
    @Modifying
    @Query(value = "insert into users(username,password)values (:username,:password)",nativeQuery = true)
    void reigster(String username,String password);


    //username e sahip kişi varsa true döner yoksa false
    boolean existsByUsername(String username);

    //kişiyi bulur username ve password ile
    Optional<User> findByUsernameAndPassword(String username,String password);

    //username ve passworda sahip biri varsa true döner
    boolean existsByUsernameAndPassword(String username,String password);




    Optional<User>findByUsername(String username);




}
