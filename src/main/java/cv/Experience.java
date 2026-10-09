package cv;

public class Experience {
    private final String company;
    private final String title;
    private final String period;
    private final String description;

    public Experience(String company, String title, String period, String description) {
        this.company = company;
        this.title = title;
        this.period = period;
        this.description = description;
    }

    public String getCompany() { return company; }
    public String getTitle() { return title; }
    public String getPeriod() { return period; }
    public String getDescription() { return description; }
}
