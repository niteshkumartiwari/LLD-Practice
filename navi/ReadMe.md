Vehicle Rental
The Challenge

Rent different kinds of vehicles such as cars and bikes.

Features:
Rental service has multiple branches throughout the city.
Each branch has a limited number of different kinds of vehicles.
Each vehicle can be booked with a predefined fixed price.
Each vehicle can be booked in multiples of 1-hour slots each. (For simplicity, assume slots of a single day)
Requirements:
Onboard a new branch with available vehicles.
Onboard new vehicle(s) of an existing type to a particular branch.
Rent a vehicle for a time slot and a vehicle type(the lowest price as the default choice extendable to any other strategy).
Display available vehicles for a given branch sorted on price.
The vehicle will have to be dropped at the same branch where it was picked up.
Bonus question:
Dynamic pricing – demand vs supply. If 80% of cars in a particular branch are booked, increase the price by 10%.
Input and Output scenarios:
COMMAND	OUTPUT	DESCRIPTION
ADD_BRANCH	TRUE if the operation succeeds else FALSE	onboard branch (Branch Name, Vehicle Type)
ADD_VEHICLE	TRUE if the operation succeeds else FALSE	onboard vehicle - (Branch Name, Vehicle Type, vehicle id, price)
BOOK	Booking Price, if booking succeeds else -1	book a vehicle - (Branch id, vehicle type, start time, end time)
DISPLAY_VEHICLES	Vehicle Ids, comma-separated	display all available vehicles for a branch, sorted on price ( Branch id, start time, end time )
SAMPLE INPUT-OUTPUT 1
INPUT	OUTPUT
ADD_BRANCH B1 CAR,BIKE,VAN	TRUE
ADD_VEHICLE B1 CAR V1 500	TRUE
ADD_VEHICLE B1 CAR V2 1000	TRUE
ADD_VEHICLE B1 BIKE V3 250	TRUE
ADD_VEHICLE B1 BIKE V4 300	TRUE
ADD_VEHICLE B1 BUS V5 2500	FALSE
BOOK B1 VAN 1 5	-1
BOOK B1 CAR 1 3	1000
BOOK B1 BIKE 2 3	250
BOOK B1 BIKE 2 5	900
DISPLAY_VEHICLES B1 1 5	V2
Other Details:
Use the in-memory store.
Do not create any UI for the application.
Write a driver class for demo purposes. Which will execute all the commands in one place in the code and test cases.
Please prioritize code compilation, execution, and completion.
Work on the expected output first and then add good-to-have features of your own.
Expectations:
Make sure that you can execute your code and show that it is working.
Make sure that the code is functionally correct.
Work on the expected output first and then add good-to-have features of your own.
Code should be modular and readable.
Separation of concern should be addressed.
Code should easily accommodate new requirements with minimal changes.
Code should be easily testable.
Input can be taken in your desired format[not necessary to follow the same grammar], but the API’s should remain as is(should contain all the input params)
Things to keep in mind while submitting the code:

Coding solution should be in Java.
Working code is required.
Code is evaluated for proper domain modeling and design, need not be optimized.
Separation of concerns must be taken care.
Solution should be extensible.
Writing all the necessary Unit tests is expected.
Necessary design patterns must be implemented.
Naming variable conventions.
Input needs to be read from a text file, and output should be printed to console. Your program should execute and take the location to the test file as a parameter.
Input needs to be read from a text file, and output should be printed to console. Your program should execute and take the location to the test file as parameter.
