package cv;

public class Education {
    private final String school;
    private final String department;
    private final String period;

    public Education(String school, String department, String period) {
        this.school = school;
        this.department = department;
        this.period = period;
    }

    public String getSchool() { return school; }
    public String getDepartment() { return department; }
    public String getPeriod() { return period; }
}
