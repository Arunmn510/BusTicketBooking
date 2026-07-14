package BusTicketBooking.BusTicketBooking.entitys;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "AllBus")
public class BussesEntity {

    @Id
    @GeneratedValue
    private Integer pID;
    private String Source;
    private String Destination;


//    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL)
//    private List<BusEntity> buses;

}
