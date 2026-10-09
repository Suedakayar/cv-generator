package cv;

public class Project {
    private final String name;
    private final String description;

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
}
