package di.jsonparser;

public class Person {

    private FullName fullName;
    private String gender;
    private int age;

    public Person(FullName fullName) {
        this.fullName = fullName;
    }

    public Person(FullName fullName, String gender) {
        this.fullName = fullName;
        this.gender = gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public FullName getFullName() {
        return fullName;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }
}
