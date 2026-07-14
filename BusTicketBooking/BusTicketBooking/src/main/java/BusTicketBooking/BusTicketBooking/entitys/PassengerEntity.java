package BusTicketBooking.BusTicketBooking.entitys;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;


@Data
@Setter
@Entity
@Table(name = "PassengersDetails")
public class PassengerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String ticketNumber;
    private String p_Name;
    private Integer age;
    private String busNumber;
    private String source;
    private String destination;
    private Double price;
    private Integer seats;


}
