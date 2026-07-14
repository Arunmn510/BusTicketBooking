package BusTicketBooking.BusTicketBooking.repository;

import BusTicketBooking.BusTicketBooking.entitys.BussesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BussesRepository extends JpaRepository<BussesEntity,Integer> {


  //  List<BussesEntity> findByDestinationStartsWith(String destination);
}
