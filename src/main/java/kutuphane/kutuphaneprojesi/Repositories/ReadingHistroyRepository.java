package kutuphane.kutuphaneprojesi.Repositories;

import jakarta.transaction.Transactional;
import kutuphane.kutuphaneprojesi.Entities.ReadingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ReadingHistroyRepository extends JpaRepository<ReadingHistory,Long> {

    @Transactional
    @Modifying
    @Query(value = "insert into readinghistory(userid,bookid) values(:userid,:bookid)",nativeQuery = true)
    void add(String userid,String bookid);


    @Query(value = "select * from readinghistory",nativeQuery = true)
    Collection<ReadingHistory> getAll();

    @Query(value = "select * from readinghistory where userid=:id",nativeQuery = true)
    Collection<ReadingHistory> getbyId(String id);



}
