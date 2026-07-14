package BusTicketBooking.BusTicketBooking.controller;

import BusTicketBooking.BusTicketBooking.entitys.BusEntity;
import BusTicketBooking.BusTicketBooking.entitys.BussesEntity;
import BusTicketBooking.BusTicketBooking.service.BussesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/busses")
public class BussesController {

    private final BussesService service;
    @Autowired
    public BussesController(BussesService service) {
        this.service = service;
    }


    @GetMapping("/getAllBus")
    public List<BussesEntity> GetAllBusses() {
        return service.GetAllBusses();
    }

//    @GetMapping("/Destination/{Destination}")
//    public List<BussesEntity> findByDestinationStartsWith(@PathVariable String Destination) {
//        return service.findByDestinationStartsWith(Destination); //get by id
//    }

    @PostMapping("/addBus")
    public BussesEntity addBus(@RequestBody BussesEntity bus) {
        return service.addBus(bus);
    }




}
