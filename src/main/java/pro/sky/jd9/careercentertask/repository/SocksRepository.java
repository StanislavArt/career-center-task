package pro.sky.jd9.careercentertask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.sky.jd9.careercentertask.model.Socks;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocksRepository extends JpaRepository<Socks, Integer> {

    @Query(value = "select sum(stock.quantity) from socks inner join stock " +
            "on socks.id = stock.socks_id " +
            "where socks.color = :color and socks.cotton_part > :cottonPart", nativeQuery = true)
    Integer getRemainderByFilterIsMoreThan(@Param("color") String color, @Param("cottonPart") int cottonPart);

    @Query(value = "select sum(stock.quantity) from socks inner join stock " +
            "on socks.id = stock.socks_id " +
            "where socks.color = :color and socks.cotton_part < :cottonPart", nativeQuery = true)
    Integer getRemainderByFilterIsLessThan(@Param("color") String color, @Param("cottonPart") int cottonPart);

    @Query(value = "select sum(stock.quantity) from socks inner join stock " +
            "on socks.id = stock.socks_id " +
            "where socks.color = :color and socks.cotton_part = :cottonPart", nativeQuery = true)
    Integer getRemainderByFilterIsEqual(@Param("color") String color, @Param("cottonPart") int cottonPart);

    Optional<Socks> findSocksByColorAndCottonPart(String color, Integer cottonPart);

    @Modifying
    @Query(value = "delete from socks s where s.id=:id", nativeQuery = true)
    void deleteSocks(@Param("id") int id);
}
