package BusTicketBooking.BusTicketBooking.controller;

import BusTicketBooking.BusTicketBooking.entitys.BussesEntity;
import BusTicketBooking.BusTicketBooking.entitys.PassengerEntity;
import BusTicketBooking.BusTicketBooking.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookTicket")
public class PassengerController {

    private final PassengerService service;

    @Autowired
    public PassengerController(PassengerService service) {
        this.service = service;
    }


    @GetMapping("/getAllPassenger")
    public List<PassengerEntity> GetAllPassengers() {
        return service.GetAllPassengers();
    }

    @GetMapping("/passengerName/{p_Name}")
    public List<PassengerEntity> findByP_Name(@PathVariable String p_Name) {
        return service.findByP_Name(p_Name);
    }

    @PostMapping("/bookBusTicket") //normal booking type without seats minus
    public PassengerEntity BookBusTicket(@RequestBody PassengerEntity bus) {
        return service.BookBusTicket(bus);
    }

    @PostMapping("/bookBusTickets")//main booking url
    public PassengerEntity BookBusTickets(@RequestBody PassengerEntity bus) {
        return service.BookBusTickets(bus);
    }




}
