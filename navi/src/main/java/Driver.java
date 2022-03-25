import service.RentalService;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) throws Exception {
        RentalService rentalService = new RentalService();

        File file = new File(args[0]);
        Scanner sc = new Scanner(file);
        boolean res = false;
        int ans=0;

        while (sc.hasNextLine()) {
            String[] commands = sc.nextLine().split(" ");
            ans=0;
            switch (commands[0]) {
                case "ADD_BRANCH":
                    List<String> vehicleTypes = Arrays.asList(commands[2].split(","));
                    res = rentalService.onBoardBranch(commands[1], vehicleTypes);
                    System.out.println(res);
                    break;
                case "ADD_VEHICLE":
                    res = rentalService.addVehicle(commands[1], commands[2], commands[3], Integer.parseInt(commands[4]));
                    System.out.println(res);
                    break;
                case "BOOK":
                    ans = rentalService.bookVehicle(commands[1], commands[2], Integer.parseInt(commands[3]), Integer.parseInt(commands[4]));
                    System.out.println(ans);
                    break;
                case "DISPLAY_VEHICLES":
                    rentalService.displayVehicles(commands[1], Integer.parseInt(commands[2]), Integer.parseInt(commands[3]));
                    break;
            }
        }
    }
}
