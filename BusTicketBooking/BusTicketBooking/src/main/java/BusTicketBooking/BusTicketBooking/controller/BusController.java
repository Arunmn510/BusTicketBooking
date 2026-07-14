package BusTicketBooking.BusTicketBooking.controller;

import BusTicketBooking.BusTicketBooking.entitys.BusEntity;
import BusTicketBooking.BusTicketBooking.repository.BusRepository;
import BusTicketBooking.BusTicketBooking.repository.BusRouteView;
import BusTicketBooking.BusTicketBooking.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/allBusses")
public class BusController {

    private final BusService service;
    private final BusRepository busRepository;

    @Autowired
    public BusController(BusService service, BusRepository busRepository) {
        this.service = service;
        this.busRepository = busRepository;
    }


    @GetMapping("/getAllBusses")
    public List<BusEntity> GetAllBusses() {
        return service.GetAllBusses(); //get all
    }

    @GetMapping("/getBussesBy") //use only when to getall buses with all details
    public List<BusEntity> findBySourceAndDestination(@RequestParam String source, @RequestParam String destination) {
        return service.findBySourceAndDestination(source, destination);
    }


    @GetMapping("/{b_ID}")
    public BusEntity GetAllBusByID(@PathVariable Integer b_ID) {
        return service.GetAllBusByID(b_ID); //get by id
    }


    @GetMapping("/destination/{destination}")
    public List<BusEntity> findByDestinationStartsWith(@PathVariable String destination) {
        return service.findByDestinationStartsWith(destination);
    }

    @PostMapping("/addBusDetails")
    public BusEntity addBussesDetails(@RequestBody BusEntity bus) {
        return service.addBussesDetails(bus);
    }


    @GetMapping("/getBusBy")
    public ResponseEntity<List<BusRouteView>> getBussesBy(@RequestParam String source, @RequestParam String destination) {
        List<BusRouteView> routes = busRepository.findBySourceAndDestinations(source, destination);
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/getByBusNumber/{busNumber}")
    public List<BusEntity> findByBusNumber(@PathVariable String busNumber) {
        return service.findByBusNumber(busNumber); //get by id
    }


}
