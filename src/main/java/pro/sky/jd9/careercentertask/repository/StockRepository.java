package pro.sky.jd9.careercentertask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.sky.jd9.careercentertask.model.Socks;
import pro.sky.jd9.careercentertask.model.StockRecord;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockRecord, Integer> {

    Optional<StockRecord> findStockRecordBySocks(Socks socks);

    @Modifying
    @Query(value = "delete from stock s where s.id=:id", nativeQuery = true)
    void deleteStockRecord(@Param("id") int id);
}
