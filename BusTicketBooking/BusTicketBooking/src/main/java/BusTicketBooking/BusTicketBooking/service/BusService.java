package BusTicketBooking.BusTicketBooking.service;

import BusTicketBooking.BusTicketBooking.entitys.BusEntity;
import BusTicketBooking.BusTicketBooking.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class BusService {

    private final BusRepository repository;
    private final Random random = new Random();

    @Autowired
    public BusService(BusRepository repository) {
        this.repository = repository;
    }


    public List<BusEntity> GetAllBusses() {
        return repository.findAll(); //get all
    }

    public List<BusEntity> findBySourceAndDestination(String source, String destination) {
        return repository.findBySourceAndDestination(source, destination);
    }

    public BusEntity GetAllBusByID(Integer B_ID) {
        return repository.findById(B_ID).orElse(null); //get by id
    }


    public List<BusEntity> findByDestinationStartsWith(String destination) {
        return repository.findBusByDestination(destination);
    }

    public BusEntity addBussesDetails(BusEntity bus) {
        bus.setbusNumber("Ind-KA-" + (1000 + new Random().nextInt(9000)));
        return repository.save(bus);
    }


    public List<BusEntity> findByBusNumber(String busNumber) {
        return repository.findByBusNumber(busNumber);
    }



}
