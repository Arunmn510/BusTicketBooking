package BusTicketBooking.BusTicketBooking.repository;

import BusTicketBooking.BusTicketBooking.entitys.BusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<BusEntity, Integer> {

    @Query("SELECT b FROM BusEntity b WHERE b.destination LIKE CONCAT(:destination,'%')")
    List<BusEntity> findBusByDestination(@Param("destination") String destination);


   @Query("SELECT b FROM BusEntity b WHERE b.source = ?1 AND b.destination = ?2")
   List<BusEntity> findBySourceAndDestination(String source, String destination);



//    @Query("SELECT b.busNumber AS busNumber, b.source AS source, b.destination AS destination FROM BusEntity b WHERE b.busNumber = :busNumber AND b.source = :source AND b.destination = :destination")
//    List<BusRouteView> findBySourceAndDestinations(@Param("busNumber") String busNumber,@Param("source") String source, @Param("destination") String destination);

    @Query("SELECT b.time AS time, b.busNumber AS busNumber, b.source AS source, b.destination AS destination FROM BusEntity b WHERE b.source = :source AND b.destination = :destination")
    List<BusRouteView> findBySourceAndDestinations(@Param("source") String source, @Param("destination") String destination);


    @Query("SELECT s FROM BusEntity s WHERE s.busNumber = ?1")
    List<BusEntity> findByBusNumber(@Param("busNumber") String busNumber);


//    List<BusEntity> findBySourceAndDestination(String source, String destination);
}



