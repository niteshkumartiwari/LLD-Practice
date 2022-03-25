package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Booking {
    private Vehicle vehicle;
    private int price;
    private int startTime;
    private int endTime;
}
