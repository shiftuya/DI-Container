package di.beanparser.json.objects;

public class Profile {

    private String include;
    private String exclude;

    @Override
    public String toString() {
        return "Profile{" +
            "include='" + include + '\'' +
            ", exclude='" + exclude + '\'' +
            '}';
    }

    public String getInclude() {
        return include;
    }

    public String getExclude() {
        return exclude;
    }
}
