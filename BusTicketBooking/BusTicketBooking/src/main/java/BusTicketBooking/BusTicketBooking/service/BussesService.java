package BusTicketBooking.BusTicketBooking.service;

import BusTicketBooking.BusTicketBooking.entitys.BusEntity;
import BusTicketBooking.BusTicketBooking.entitys.BussesEntity;
import BusTicketBooking.BusTicketBooking.repository.BussesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class BussesService {

    private final BussesRepository repository;
    @Autowired
    public BussesService(BussesRepository repository) {
        this.repository = repository;
    }

    public List<BussesEntity> GetAllBusses() {
        return repository.findAll(); //get all
    }

//    public List<BussesEntity> findByDestinationStartsWith(String Destination) {
//        return repository.findByDestinationStartsWith(Destination); //get by id
//    }

    public BussesEntity addBus( BussesEntity bus) {
        return repository.save(bus);
    }



}
