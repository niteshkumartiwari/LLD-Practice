package service;

import model.Branch;
import model.RentalAgency;
import model.Vehicle;

import java.util.List;


public class RentalService {
    private RentalAgency rentalAgency;

    public RentalService() {
        rentalAgency = new RentalAgency();
    }

    public boolean onBoardBranch(String branchName, List<String> vehicleTypes) {
        Branch branch = new Branch(branchName, vehicleTypes);
        rentalAgency.getBranchMap().put(branchName, branch);
        return true;
    }

    public boolean addVehicle(String branchName, String vehicleType, String vechicleId, int price) {
        if (!isBranchPresent(branchName)) {
            return false;
        }

        Vehicle vehicle = new Vehicle(vechicleId, vehicleType, price);
        return BranchService.addVehicle(rentalAgency.getBranchMap().get(branchName), vehicle);
    }

    public int bookVehicle(String branchName, String vehicleType, int startTime, int endTime) {
        if (!isBranchPresent(branchName) || startTime >= endTime) {
            return -1;
        }

        return BranchService.bookVehicle(rentalAgency.getBranchMap().get(branchName), vehicleType, startTime, endTime);
    }

    public void displayVehicles(String branchName, int startTime, int endTime) {
        if (!isBranchPresent(branchName)) {
            return;
        }

        BranchService.displayAvailableVehicles(rentalAgency.getBranchMap().get(branchName), startTime, endTime);
    }

    private boolean isBranchPresent(String branchName) {
        return rentalAgency.getBranchMap().containsKey(branchName);
    }
}
