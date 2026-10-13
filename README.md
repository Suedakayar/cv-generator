# CV Oluşturucu (Java + OpenPDF)

Nesne yönelimli programlama dersi ödevi olarak hazırlanmış, kişisel bilgilerden **PDF formatında özgeçmiş** üreten bir Java konsol uygulamasıdır. Uygulamanın arayüzü yoktur. Çalıştırıldığında proje klasöründe `cv.pdf` dosyası oluşur.

Özgeçmişte fotoğraf, hakkında bölümü, eğitim, **3 hayali iş deneyimi** ve projeler yer alır. Kişisel bilgilerin tamamı ödev amacıyla hazırlanmıştır ve birebir gerçek olmak zorunda değildir.

## Örnek çıktı

Uygulamanın ürettiği dosya: [cv.pdf](cv.pdf)

## Kullanılan teknolojiler

| Teknoloji | Sürüm | Neden kullanıldı |
|---|---|---|
| Java | 21 (hedef sürüm) | Ders dili, nesne yönelimli yapı için uygun |
| Maven | 3.x | Bağımlılık yönetimi ve derleme |
| OpenPDF | 2.4.0 | Açık kaynak, Java ile PDF üretmeye yarayan kütüphane |

## Proje yapısı

```
cv-generator/
├── pom.xml
├── README.md
├── cv.pdf                      (örnek çıktı)
├── resources/
│   └── photo.jpg               (özgeçmişteki fotoğraf)
└── src/main/java/cv/
    ├── Main.java
    ├── Person.java
    ├── Experience.java
    ├── Education.java
    ├── Project.java
    └── CvPdfGenerator.java
```

## Sınıflar ve neden kullanıldı

Tasarımda **tek sorumluluk ilkesi** izlendi: her sınıfın tek bir işi vardır. Veriyi tutan sınıflar ile PDF üreten sınıf birbirinden ayrıdır.

| Sınıf | Sorumluluğu | Neden ayrı bir sınıf? |
|---|---|---|
| `Person` | Kişinin adı, e-postası, telefonu, özeti, fotoğraf yolu ve deneyim, eğitim, proje listelerini tutar | Özgeçmişin ana nesnesi. Diğer nesneleri bir arada toplar |
| `Experience` | Tek bir iş deneyimi (şirket, unvan, dönem, açıklama) | Her deneyim kendi bilgilerini taşıyan bağımsız bir nesne olsun diye |
| `Education` | Tek bir eğitim kaydı (okul, bölüm, dönem) | Aynı gerekçe, ileride birden fazla eğitim eklenebilir |
| `Project` | Tek bir proje (ad, açıklama) | Aynı gerekçe |
| `CvPdfGenerator` | `Person` nesnesinden PDF dosyası üretir | Ödev şartı: PDF işlemi `main` içinde değil, **ayrı bir sınıfta** yapılmalı |
| `Main` | Nesneleri oluşturur ve `CvPdfGenerator`'ı çağırır | Sadece uygulamanın giriş noktası, iş mantığı içermez |

### Sınıflar arası ilişki

```
Person ──(içerir)──> Experience, Education, Project   (1'e çok)
CvPdfGenerator ──(kullanır)──> Person
Main ──(oluşturur ve çağırır)──> Person, CvPdfGenerator
```

`Person` bir özgeçmişin bütününü temsil eder ve üç tür listeye sahiptir. `CvPdfGenerator`, `Person` nesnesini kurucusunda alır. Böylece PDF sınıfı veri sınıflarını değiştirmeden sadece okur.

## Kullanılan kütüphane nesneleri (OpenPDF)

| Nesne | Ne için kullanıldı |
|---|---|
| `Document` | PDF belgesinin kendisi. A4 boyutu ve kenar boşlukları burada verilir |
| `PdfWriter` | `Document` nesnesini dosyaya yazan nesne |
| `PdfPTable` ve `PdfPCell` | Üst kısımdaki iki sütunlu yerleşim: solda isim ve iletişim, sağda fotoğraf |
| `Image` | `photo.jpg` dosyasını okuyup PDF'e yerleştirir |
| `Paragraph` ve `Chunk` | Başlıklar, açıklamalar ve aynı satırdaki farklı biçimli parçalar (örneğin kalın okul adı + gri tarih) |
| `LineSeparator` | Bölüm başlıklarının altındaki bordo çizgi |
| `BaseFont` ve `Font` | Türkçe karakterleri (ş, ğ, ı, İ, ü, ö, ç) doğru göstermek için gömülü TrueType font (Arial) |
| `java.awt.Color` | Bordo, gri ve koyu gri renk tanımları |

## Erişim belirleyiciler

| Belirleyici | Nerede kullanıldı | Gerekçe |
|---|---|---|
| `private` | Veri sınıflarındaki tüm alanlar, `CvPdfGenerator`'daki yardımcı metotlar ve sabitler | Alanlara dışarıdan doğrudan erişilmesin (**kapsülleme**). PDF sınıfının iç adımları (`addHeader`, `addAbout` gibi) dışarıya açılmasın |
| `public` | Sınıflar, kurucular, getter metotları, `addExperience`/`addEducation`/`addProject`, `generate` | Diğer sınıfların kullanabileceği arayüz bunlardır |
| `final` | Veri sınıflarındaki alanlar, `Person` içindeki listelerin referansları | Alan kurucuda bir kez atanır, sonra başka bir nesneye bağlanamaz |
| `static final` | `CvPdfGenerator` içindeki renk ve font yolu sabitleri | Tüm nesneler için ortak, değişmeyen değerler |
| `protected` ve varsayılan (paket) erişim | Kullanılmadı | Projede kalıtım yok ve tüm sınıflar aynı paketteki `public` sınıflar, bu yüzden gerek duyulmadı |

### Listelerin korunması

`Person` sınıfındaki getter'lar listeyi doğrudan vermez:

```java
public List<Experience> getExperiences() {
    return Collections.unmodifiableList(experiences);
}
```

Dışarıdan `getExperiences().add(...)` yazılırsa hata alınır. Liste yalnızca `addExperience` metoduyla değişebilir. `experiences` alanı `final` olsa da listenin **içine eleman eklenebilir**, çünkü `final` yalnızca referansın başka bir listeye bağlanmasını engeller. Elemanları korumak için `unmodifiableList` kullanıldı.

## Nesne yaşam döngüsü

Uygulama çalışırken nesneler şu sırayla oluşur ve kullanılır:

1. **Oluşturma:** `Main` içinde `new Person(...)` ile kişi nesnesi bellekte (heap) oluşur. Kurucu (constructor) alanları doldurur.
2. **Durum değişimi:** `new Experience(...)`, `new Education(...)` ve `new Project(...)` ile oluşturulan nesneler `addExperience`, `addEducation`, `addProject` ile `Person` içindeki listelere eklenir. Bu andan sonra bu nesnelere `Person` üzerinden de erişilebilir.
3. **Kullanım:** `new CvPdfGenerator(person)` oluşturulur ve `generate("cv.pdf")` çağrılır. Bu metodun içinde sırasıyla:
   - Fontlar yüklenir (`BaseFont`).
   - `Document` nesnesi oluşturulur, `PdfWriter` ile bir dosya akışına bağlanır.
   - `doc.open()` ile belge açılır.
   - Başlık, hakkımda, eğitim, deneyim ve proje bölümleri `doc.add(...)` ile eklenir.
   - `doc.close()` ile belge kapatılır ve PDF dosyaya yazılmış olur.
4. **Kaynakların kapanması:** Dosya akışı (`FileOutputStream`) `try-with-resources` ile açıldığı için, `generate` bittiğinde ya da hata çıktığında **otomatik kapanır**.
5. **Sonlanma:** `main` metodu bitince `person`, `Experience`, `Education`, `Project` ve `CvPdfGenerator` nesnelerine artık ulaşılamaz. Bu nesneler **çöp toplayıcı (garbage collector)** tarafından temizlenmeye uygun hâle gelir. Java'da nesneleri elle silmek gerekmez.

## Çalıştırma adımları

Gereksinimler: JDK 21 ya da üstü, Maven.

```bash
git clone https://github.com/Suedakayar/cv-generator.git
cd cv-generator
mvn compile exec:java -Dexec.mainClass=cv.Main
```

Komut bittiğinde terminalde `PDF oluşturuldu: cv.pdf` yazar ve proje klasöründe `cv.pdf` oluşur.

Not: Türkçe karakterler için sistemdeki Arial (macOS ve Windows) ya da DejaVu Sans (Linux) yazı tipi kullanılır. Hiçbiri bulunamazsa uygulama açıklayıcı bir hata mesajı verir.

## Yapay zekâ kullanımı

Bu projede dil modeli olarak **Claude** (Anthropic) sohbet arayüzünden yardımcı olarak kullanıldı.

**Nasıl yararlanıldı:**
- Ödev metninden çıkarılacak gereksinimlerin özeti ve proje yapısı için öneri alındı.
- Maven kurulumu, Homebrew, Git ve GitHub adımlarında yönlendirme alındı.
- `Person`, `Experience`, `Education`, `Project` ve `CvPdfGenerator` sınıflarının ilk taslakları oluşturuldu.
- Derleme hataları ve terminal çıktıları paylaşılarak hataların nedenleri bulundu (örneğin kodun içine karışan kıvrık tırnak karakterleri).

**Kendi katkım:**
- Kişisel bilgiler, hayali iş deneyimleri, eğitim ve proje içeriklerini kendim yazdım.
- Özgeçmişin tasarımını (tek sütun, bordo renk, fotoğrafın sağda olması, eğitim ve proje bölümleri) kendim seçtim.
- Üretilen PDF'i kontrol ettim, ad ve yerleşim düzeltmelerini kendim yönlendirdim.
- Kodun her bölümünün ne işe yaradığını inceleyip bu belgeyi ve el yazısı A4 raporunu hazırladım.

**Öğrendiklerim:** Kapsülleme, `final` ve `private` kullanımı, `unmodifiableList` ile listelerin korunması, sorumlulukların ayrı sınıflara dağıtılması, `try-with-resources` ile kaynak yönetimi ve nesne yaşam döngüsünü öğrendim.


