/**Design IRCTC Ticket Booking System

Features:
Customer - User 1
Search Train for customer journey (Date, Source, Destination)
Book Ticket
See Booked Ticket History
Train- Tier? - Types of seat.

Admin
Change Train Schedules (Permanent recurring Change)
Add more seats to the Train
**/

abstract class Seat{
	private int seatNo;
	private bool isBooked;

	public int getCost();
	public bool isBooked();
}

class FirstClassSeat extends Seat{
}

class SecondClassSeat extends Seat{
}

class ThirdClassSeat extends Seat{
}

class Coach {
	private List<Seat> seats;
	private boogieNo;
}

class StationSchedule{
	String stationName;
	Date scheduleAt;
}

abstract class TrainMetaData{
	private int trainNo;
	private List<StationSchedule> StationSchedule;
	private bool isAvailable;
	//TODO:: Schedule???
}

class Train extends TrainMetaData{
	private String scheduledAt;
	private List<Coach> coaches;
	private List<WeekDays> scheduleType;
}

abstract class User{
	private String name;
	private String id;
}

class Admin extends User{
	public bool changeTrainSchedule(Train tain, String newSchedule)
	public bool addTrainSeats(Train train, List<Seat> seats)
}

class Customer extends User{
	private List<Ticket> bookedTickets;

	public List<Ticket> getBookedTickets()
}

class Ticket{
	private Customer bookedBy;
	private List<Customer> passengers;
	private int price;
	private Train trainNo;
	private String pnr;
}

class IRCTCManager{
	private Map<Weekday, Train> trainMapper;
	private Map<String, User> userMapper;
	private Map<Pair<String,String>, List<Train>> routeTrainMapper;


	public List<Train> searchTrain(String src, String dest, Date day);
	public Ticket bookTicket(Train train, String seatNo)
}