import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

enum SpotType {
    SMALL, 
    MEDIUM, 
    LARGE,
    MEDIUM_WITH_ELECTIC_CHARGER
}

enum EmittionType {
    PETROL,
    DIESEL,
    ELECTRIC
}

enum VehicleType {
    TWO_WHEELER,
    FOUR_WHEELER
}

class ParkingSpot {
    public String spotIdentifier;
    public SpotType spotType;

    public ParkingSpot(String sportIdentifier, SpotType spotType) {
        this.spotIdentifier = sportIdentifier;
        this.spotType = spotType;
    }
}

class Vehicle {
    public String vehicleNumber;
    public VehicleType type;
    public EmittionType emittionType;

    public Vehicle(String vehicleNumber, VehicleType type, EmittionType emittionType) {
        this.vehicleNumber = vehicleNumber;
        this.type = type;
        this.emittionType = emittionType;
    }
}

class Ticket {
    public Vehicle vehicle;
    public Long entryTime;
    public ParkingSpot spot;

    public Ticket(Vehicle vehicle, ParkingSpot spot, Long entryTime) {
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = entryTime;
    }
}

class TicketHistory {
    public Ticket ticket;
    public Long endTime;
    public Long amount;
    
    public TicketHistory(Ticket ticket, Long endTime, Long amount) {
        this.ticket = ticket;
        this.endTime = endTime;
        this.amount = amount;
    }
}

class ParkingSpotNotAvailable extends RuntimeException {
    public ParkingSpotNotAvailable() {
        super("No parking space available at the moment.");
    }
}

interface CostCalculator {
    Long calculateCost(Ticket ticket);
}

class SimpleCostCalculator implements CostCalculator{
    public Long calculateCost(Ticket ticket) {
        Long currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        Long duration = currentTime - ticket.entryTime;
        int hours = (int)(duration / (60*24));
        Long cost = 20l;

        if(hours > 1) {
            cost += 10 * (hours-1);
        }
        
        return cost;
    }
}

class ParkingLot {
    public List<TicketHistory> parkingHistory;
    public Map<SpotType, List<ParkingSpot>> spotToParkingSpotMap;
    public CostCalculator costCalculator;

    private ParkingLot() {// dependency injection
        parkingHistory = new ArrayList<>();
        costCalculator = new SimpleCostCalculator();
        spotToParkingSpotMap = new HashMap<>();
    }

    // Creating singleton with holder pattern
    private static class Holder {
        private static final ParkingLot INSTANCE = new ParkingLot();
    }

    public static ParkingLot getInstance() {
        return Holder.INSTANCE;
    }

    public Ticket assingParkingSpot(Vehicle vehicle) {
        final SpotType spotType = getSpotType(vehicle);
        List<ParkingSpot> availableSpots = spotToParkingSpotMap.get(spotType);
        Long currentTimeInEpochSeconds = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        if(Objects.isNull(availableSpots) || availableSpots.isEmpty()) {
            throw new ParkingSpotNotAvailable();
        }

        // Read-Write Lock to handle concurrency.
        ParkingSpot parkingSpot = availableSpots.get(0);
        availableSpots.remove(0);

        spotToParkingSpotMap.put(spotType, availableSpots);
        

        System.out.println("Assigned vehicle : "+ vehicle.vehicleNumber + " parking spot-no: "+ parkingSpot.spotIdentifier + " at time: "+ currentTimeInEpochSeconds);

        return new Ticket(vehicle, parkingSpot, currentTimeInEpochSeconds); // Factory pattern
    }

    public Long releaseParkingSpot(Ticket ticket) {
        Long cost = costCalculator.calculateCost(ticket);
        Long currentTimeInEpochSeconds = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        // Mark the parking spot as available
        ParkingSpot spot = ticket.spot;
        List<ParkingSpot> availableSpot = spotToParkingSpotMap.get(spot.spotType);
        availableSpot.add(spot);
        spotToParkingSpotMap.put(spot.spotType, availableSpot);

        parkingHistory.add(new TicketHistory(ticket, currentTimeInEpochSeconds, cost));

        System.out.println(" Releasing parking spot-no: "+ spot.spotIdentifier + " at time: "+ currentTimeInEpochSeconds);

        return cost;
    }

    public boolean createParkingSpot(String spotId, SpotType spotType) {
        final ParkingSpot parkingSpot = new ParkingSpot(spotId, spotType);
        if(!spotToParkingSpotMap.containsKey(spotType)){
            spotToParkingSpotMap.put(spotType, new ArrayList<>()); 
        }

        // Remove from the available spots
        List<ParkingSpot> parkingSpots = spotToParkingSpotMap.get(spotType);
        parkingSpots.add(parkingSpot);
        spotToParkingSpotMap.put(spotType, parkingSpots);
        return true;
    }

    private SpotType getSpotType(Vehicle vehicle) {
        switch (vehicle.type) {
            case TWO_WHEELER:
                if(vehicle.emittionType.equals(EmittionType.ELECTRIC)) {
                    return SpotType.MEDIUM_WITH_ELECTIC_CHARGER;
                }
                return SpotType.SMALL ;
        
            case FOUR_WHEELER:
                if(vehicle.emittionType.equals(EmittionType.ELECTRIC)) {
                    return SpotType.MEDIUM_WITH_ELECTIC_CHARGER;
                }
                return SpotType.MEDIUM;
        }

        return SpotType.SMALL;
    }
}

class Driver{
    public static void main(String[] args) {
        ParkingLot parkingLot = ParkingLot.getInstance();

        parkingLot.createParkingSpot("1", SpotType.SMALL);
        parkingLot.createParkingSpot("2", SpotType.MEDIUM);
        parkingLot.createParkingSpot("3", SpotType.MEDIUM);
        parkingLot.createParkingSpot("4", SpotType.MEDIUM_WITH_ELECTIC_CHARGER);

        Vehicle thar = new Vehicle("Mahindra Thar", VehicleType.FOUR_WHEELER, EmittionType.DIESEL);
        Vehicle scorpioN = new Vehicle("Mahindra ScorpioN", VehicleType.FOUR_WHEELER, EmittionType.PETROL);
        Vehicle xuv_700 = new Vehicle("Mahindra XUV700", VehicleType.FOUR_WHEELER, EmittionType.PETROL);
        Vehicle comet = new Vehicle("MG Comet", VehicleType.FOUR_WHEELER, EmittionType.ELECTRIC);

        Ticket tharTicket = parkingLot.assingParkingSpot(thar);
        Ticket scorpioNTicket = parkingLot.assingParkingSpot(scorpioN);
        parkingLot.releaseParkingSpot(tharTicket);
        Ticket xuv700Ticket = parkingLot.assingParkingSpot(xuv_700);
        Ticket cometTicket = parkingLot.assingParkingSpot(comet);
        parkingLot.releaseParkingSpot(scorpioNTicket);
        parkingLot.releaseParkingSpot(xuv700Ticket);
        parkingLot.releaseParkingSpot(cometTicket);
    }
}