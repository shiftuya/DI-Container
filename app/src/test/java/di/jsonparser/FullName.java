package di.jsonparser;

public class FullName {

    private String firstName;
    private String secondName;

    public FullName(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }
}
