package service;

import model.Booking;
import model.Branch;
import model.Vehicle;

import java.util.*;
import java.util.stream.Collectors;

public class BranchService {
    public static void displayAvailableVehicles(Branch branch, int startTime, int endTime) {
        getAvailableVehicles(branch, startTime, endTime, Optional.empty()).stream()
                .forEach(e -> System.out.println(e.getId()));
    }

    public static int bookVehicle(Branch branch, String vehicleType, int startTime, int endTime) {
        if (!branch.getTypeToVehicle().containsKey(vehicleType)) {
            return -1;
        }

        Optional<Vehicle> vehicle = getAvailableVehicles(branch, startTime, endTime, Optional.of(vehicleType)).stream()
                .sorted(Comparator.comparing(e -> e.getPrice()))
                .findFirst();

        if (vehicle.isPresent()) {
            Booking newBooking = new Booking(vehicle.get(), vehicle.get().getPrice() * (endTime - startTime), startTime, endTime);
            branch.getBookingList().add(newBooking);
            return newBooking.getPrice();
        }

        return -1;
    }

    public static boolean addVehicle(Branch branch, Vehicle vehicle) {
        if (!branch.getTypeToVehicle().containsKey(vehicle.getType())) {
            return false;
        }

        branch.getTypeToVehicle().get(vehicle.getType()).add(vehicle);
        return true;
    }

    private static List<Vehicle> getAvailableVehicles(Branch branch, int startTime, int endTime, Optional<String> vehicleType) {
        // Get all required vehicle types
        List<Vehicle> branchVehicles = new ArrayList<>();
        if (vehicleType.isPresent()) {
            branchVehicles = branch.getTypeToVehicle().get(vehicleType.get());
        } else {
            for (Map.Entry<String, List<Vehicle>> entry : branch.getTypeToVehicle().entrySet()) {
                branchVehicles.addAll(entry.getValue());
            }
        }

        // Get all booked vehicles in given period
        Set<String> bookedVehiclesDuringGivenPeriod = new HashSet<>();
        branch.getBookingList().stream()
                .filter(e -> !isVehicleAvailable(e, startTime, endTime))
                .forEach(e -> bookedVehiclesDuringGivenPeriod.add(e.getVehicle().getId()));

        // return which are not booked in this period
        return branchVehicles.stream()
                .filter(e -> !bookedVehiclesDuringGivenPeriod.contains(e.getId()))
                .collect(Collectors.toList());
    }

    private static boolean isVehicleAvailable(Booking booking, int startTime, int endTime) {
        if (booking.getStartTime() > endTime || booking.getEndTime() < startTime) {
            return true;
        }
        return false;
    }
}
