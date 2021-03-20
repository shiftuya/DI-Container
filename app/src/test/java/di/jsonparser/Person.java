package di.jsonparser;

import javax.inject.Provider;

public class Person {

    private FullName fullName;
    private ICar car;
    private String gender;
    private String age;
    private Provider<IPassport> passport;

    public Person() {}

    public Person(FullName fullName) {
        this.fullName = fullName;
    }

    public Person(FullName fullName, String gender, Provider<IPassport> passport) {
        this.fullName = fullName;
        this.gender = gender;
        this.passport = passport;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public FullName getFullName() {
        return fullName;
    }

    public ICar getCar() {
        return car;
    }

    public void setCar(ICar car) {
        this.car = car;
    }

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }
}
