package di.jsonparser;

import javax.inject.Inject;

public class FullName {

    private String firstName;
    private String secondName;

    @Inject
    public FullName() {
        firstName = "Ivan";
        secondName = "Shatalov";
    }

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
