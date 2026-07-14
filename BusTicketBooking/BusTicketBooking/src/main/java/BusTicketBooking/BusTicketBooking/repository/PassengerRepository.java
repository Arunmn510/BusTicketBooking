package BusTicketBooking.BusTicketBooking.repository;

import BusTicketBooking.BusTicketBooking.entitys.PassengerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassengerRepository extends JpaRepository<PassengerEntity,Integer> {

    //List<PassengerEntity> findByDestinationStartsWith(String Destination);

    @Query("select s from PassengerEntity s where s.p_Name = ?1")
    List<PassengerEntity> findByP_Name(String p_Name);


}
