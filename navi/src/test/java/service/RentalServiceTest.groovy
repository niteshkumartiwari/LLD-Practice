package service

import spock.lang.Specification

class RentalServiceTest extends Specification {
    def "test for edge cases for startTime and endTime"() {
        given:
        RentalService rentalService = new RentalService()

        when:
        boolean res1 = rentalService.onBoardBranch("b1", Arrays.asList("car", "bike"))
        boolean res2 = rentalService.addVehicle("b1", "car", "v1", 10)
        boolean res3 = rentalService.addVehicle("b1", "car", "v2", 100)
        int res4 = rentalService.bookVehicle("b1", "car", 1, 5)
        int res5 = rentalService.bookVehicle("b1", "car", 5, 6)

        then:
        res1 == true
        res2 == true
        res3 == true
        res4 == 40
        res5 == 100
    }

    def "test for validation cases"() {
        given:
        RentalService rentalService = new RentalService()

        when:
        boolean res1 = rentalService.onBoardBranch("b1", Arrays.asList("car", "bike"))
        boolean res2 = rentalService.addVehicle("b2", "car", "v1", 10)
        boolean res3 = rentalService.addVehicle("b1", "car", "v1", 10)
        int res4 = rentalService.bookVehicle("b1", "car", 1, 10)

        then:
        res1 == true
        res2 == false
        res3 == true
        res4 == 90
    }
}
