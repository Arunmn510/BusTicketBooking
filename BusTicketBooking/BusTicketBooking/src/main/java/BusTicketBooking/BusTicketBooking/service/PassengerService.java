package BusTicketBooking.BusTicketBooking.service;

import BusTicketBooking.BusTicketBooking.entitys.BussesEntity;
import BusTicketBooking.BusTicketBooking.entitys.PassengerEntity;
import BusTicketBooking.BusTicketBooking.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import BusTicketBooking.BusTicketBooking.entitys.BusEntity;
import BusTicketBooking.BusTicketBooking.repository.BusRepository;

import java.util.List;
import java.util.Random;

@Service
public class PassengerService {

    private final PassengerRepository repository;
    private final Random random = new Random();
    private final BusRepository busRepository;

    @Autowired
    public PassengerService(
            PassengerRepository repository,
            BusRepository busRepository) {

        this.repository = repository;
        this.busRepository = busRepository;
    }


    public List<PassengerEntity> GetAllPassengers() {
        return repository.findAll(); //get all
    }

    public List<PassengerEntity> findByP_Name(String p_Name) {
        return repository.findByP_Name(p_Name);
    }

    public PassengerEntity BookBusTicket(PassengerEntity passenger) {
        passenger.setTicketNumber("TKT" + (1000 + new Random().nextInt(9000)));
        return repository.save(passenger);
    }


//    public PassengerEntity BookBusTickets(PassengerEntity passenger) {
//
//        passenger.setTicketNumber("TKT" + (1000 + random.nextInt(9000)));
//
//        // Get bus
//        List<BusEntity> buses = busRepository.findByBusNumber(passenger.getBusNumber());
//
//        if (buses.isEmpty()) {
//            throw new RuntimeException("Bus not found");
//        }
//        BusEntity bus = buses.get(0);
//
//        // Check seats
//        if (passenger.getSeats() > bus.getSeats_available()) {
//            throw new RuntimeException("Seats not available");
//        }
//
//        // Minus seats
//        bus.setSeats_available(bus.getSeats_available() - passenger.getSeats());
//
//        // UPDATE DB
//        busRepository.save(bus);
//        return repository.save(passenger);
//    }

    public PassengerEntity BookBusTickets(PassengerEntity passenger) {

        passenger.setTicketNumber("TKT" + (1000 + random.nextInt(9000)));

        List<BusEntity> buses = busRepository.findByBusNumber(passenger.getBusNumber());// Get bus

        if (buses.isEmpty()) {
            throw new RuntimeException("Bus not found");
        }

        BusEntity bus = buses.get(0);

        passenger.setPrice(bus.getPrice() * passenger.getSeats()); // Calculate price

        System.out.println("Total Price = " + passenger.getPrice());  // Print only in console

        // Check seats
        if (passenger.getSeats() > bus.getSeats_available()) {
            throw new RuntimeException("Seats not available");
        }

        // Minus seats
        bus.setSeats_available(bus.getSeats_available() - passenger.getSeats());

        busRepository.save(bus);

        return repository.save(passenger);
    }

//    public PassengerEntity BookBusTickets(PassengerEntity passenger) {
//
//        passenger.setTicketNumber("TKT" + random.nextInt(10000));
//
//        BusEntity bus = busRepository.findBySourceAndDestination(passenger.getSource(), passenger.getDestination()).get(0);
//
//        passenger.setBusNumber(bus.getBusNumber());
//
//        passenger.setPrice(bus.getPrice() * passenger.getSeats());
//
//        bus.setSeats_available(bus.getSeats_available() - passenger.getSeats());
//
//        busRepository.save(bus);
//
//        return repository.save(passenger);
//    }
//

}
