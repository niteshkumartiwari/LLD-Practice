package model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
public class Branch {
    private String name;
    private Map<String, List<Vehicle>> typeToVehicle;
    private List<Booking> bookingList;

    public Branch(String name, List<String> vehicleType) {
        this.name = name;
        this.typeToVehicle= new HashMap<>();
        vehicleType.stream().forEach(e-> this.typeToVehicle.put(e, new ArrayList<>()));
        this.bookingList= new ArrayList<>();
    }
}
