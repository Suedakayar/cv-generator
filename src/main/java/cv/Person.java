package cv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Person {
    private final String fullName;
    private final String email;
    private final String phone;
    private final String summary;
    private final String photoPath;
    private final List<Experience> experiences = new ArrayList<>();
    private final List<Education> educations = new ArrayList<>();
    private final List<Project> projects = new ArrayList<>();

    public Person(String fullName, String email, String phone,
                  String summary, String photoPath) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.summary = summary;
        this.photoPath = photoPath;
    }

    public void addExperience(Experience experience) { experiences.add(experience); }
    public void addEducation(Education education) { educations.add(education); }
    public void addProject(Project project) { projects.add(project); }

    public List<Experience> getExperiences() { return Collections.unmodifiableList(experiences); }
    public List<Education> getEducations() { return Collections.unmodifiableList(educations); }
    public List<Project> getProjects() { return Collections.unmodifiableList(projects); }

    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getSummary() { return summary; }
    public String getPhotoPath() { return photoPath; }
}
