package cv;

public class Main {
    public static void main(String[] args) {
        Person person = new Person("Süeda Kayar", "suedakyr07@gmail.com", "0551 535 10 25",
                "Yazılıma ilgi duyan, meraklı ve öğrenmeye açık bir öğrenciyim.",
                "resources/photo.jpg");

        person.addEducation(new Education("Kırklareli Üniversitesi",
                "Yazılım Mühendisliği", "2025-2029"));

        person.addExperience(new Experience("Google", "Software Engineering Intern",
                "2022-2023", "Java kullanarak dahili ekiplerin kullandığı arka uç (backend) servislerinin ve REST API uç noktalarının geliştirilmesine katkı sağladım."));
        person.addExperience(new Experience("Amazon", "Junior Software Development Engineer",
                "2023-2024", "Dağıtık mimaride veri akışını yönetmek amacıyla asenkron kuyruk mekanizmaları ve veri modelleri üzerine çalıştım."));
        person.addExperience(new Experience("Teknopark | Ar-Ge Yazılım Laboratuvarı", "Backend Developer",
                "2024-2025", "Çok katmanlı mimari prensiplerine uygun olarak Core Java ile veri ayrıştırma ve raporlama otomasyon araçları geliştirdim."));

        person.addProject(new Project("E-Ticaret Sipariş ve Envanter Yönetim API'si",
                "Spring Boot ve JPA ile sipariş ve stok süreçlerini yöneten RESTful mikroservis mimarisi geliştirdim."));
        person.addProject(new Project("Çok İplikli Görev Zamanlayıcı",
                "java.util.concurrent ve thread havuzları kullanarak öncelikli görevleri asenkron işleyen thread-safe bir kuyruk motoru kodladım."));

        try {
            new CvPdfGenerator(person).generate("cv.pdf");
            System.out.println("PDF oluşturuldu: cv.pdf");
        } catch (Exception ex) {
            System.err.println("Hata: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
