package service

import model.Booking
import model.Branch
import model.Vehicle
import spock.lang.Specification

class BranchServiceTest extends Specification {
    Branch branch
    Vehicle v1,v2,v3

    void setup() {
        branch = new Branch("b1", Arrays.asList("car", "bike", "van"))
        v1 = new Vehicle("v1", "car", 10);
        v2 = new Vehicle("v2", "car", 20);
        v3 = new Vehicle("v3", "bike", 30);

        branch.getTypeToVehicle().put("car", Arrays.asList(v1, v2))
        branch.getTypeToVehicle().put("bike", Arrays.asList(v3))
        branch.getBookingList().add(new Booking(v1, 20, 1, 2))
    }

    def "test for get all available vehicles"() {
        when:
        List<Vehicle> res = BranchService.getAvailableVehicles(branch, 1, 3, Optional.empty())

        then:
        [v2,v3] == res.asList()
    }

    def "test for booking vehicles"(){
        when:
        int res1= BranchService.bookVehicle(branch,"van",1,2)
        int res2= BranchService.bookVehicle(branch, "car", 1,2)
        int res3= BranchService.bookVehicle(branch, "car", 1,2)
        int res4= BranchService.bookVehicle(branch,"dummy",1,2)

        then:
        res1 == -1
        res2 == 20
        res3 == -1
        res4 == -1
    }
}
