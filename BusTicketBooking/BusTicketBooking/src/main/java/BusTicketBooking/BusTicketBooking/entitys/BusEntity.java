package BusTicketBooking.BusTicketBooking.entitys;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "BusDetails")
public class BusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer b_ID;
    private String busNumber;
    private String source;
    private String destination;
    private String busName;
    private String time;
    private Integer seats_available;
    private Double price;





    public void setbusNumber(String s) {
            this.busNumber = s;

    }


//    @ManyToOne
//    @JoinColumn(name="route_id")
//    private BussesEntity bus;

}
