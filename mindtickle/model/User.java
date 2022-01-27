package mindtickle.model;

public class User {
    private String name;
    private UserType userType= UserType.DEFAULT;

    public User(String name, UserType userType) {
        this.name = name;
        this.userType = userType;
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
