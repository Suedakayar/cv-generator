package cv;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CvPdfGenerator {

    private static final Color BORDO = new Color(128, 0, 32);
    private static final Color GRI = new Color(100, 100, 100);
    private static final Color KOYU = new Color(40, 40, 40);

    private static final String[][] FONT_ADAYLARI = {
        {"/System/Library/Fonts/Supplemental/Arial.ttf",
         "/System/Library/Fonts/Supplemental/Arial Bold.ttf"},
        {"C:/Windows/Fonts/arial.ttf", "C:/Windows/Fonts/arialbd.ttf"},
        {"/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf",
         "/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf"}
    };

    private final Person person;
    private BaseFont normalFont;
    private BaseFont kalinFont;

    public CvPdfGenerator(Person person) {
        this.person = person;
    }

    public void generate(String outputPath) throws IOException, DocumentException {
        loadFonts();
        Document doc = new Document(PageSize.A4, 40, 40, 40, 40);
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            PdfWriter.getInstance(doc, fos);
            doc.open();
            addHeader(doc);
            addAbout(doc);
            addEducations(doc);
            addExperiences(doc);
            addProjects(doc);
            doc.close();
        }
    }

    private void loadFonts() throws IOException, DocumentException {
        for (String[] aday : FONT_ADAYLARI) {
            if (Files.exists(Path.of(aday[0])) && Files.exists(Path.of(aday[1]))) {
                normalFont = BaseFont.createFont(aday[0], BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                kalinFont = BaseFont.createFont(aday[1], BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                return;
            }
        }
        throw new IOException("Turkce karakterleri destekleyen bir font bulunamadi.");
    }

    private Font normal(float size, Color color) {
        return new Font(normalFont, size, Font.NORMAL, color);
    }

    private Font kalin(float size, Color color) {
        return new Font(kalinFont, size, Font.NORMAL, color);
    }

    private void addHeader(Document doc) throws IOException, DocumentException {
        PdfPTable table = new PdfPTable(new float[]{3.7f, 1.3f});
        table.setWidthPercentage(100);

        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);
        infoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        infoCell.setPaddingLeft(0);
        infoCell.addElement(new Paragraph(person.getFullName(), kalin(24, BORDO)));
        Paragraph iletisim = new Paragraph(person.getEmail() + "\n" + person.getPhone(),
                normal(11, GRI));
        iletisim.setSpacingBefore(6);
        infoCell.addElement(iletisim);
        table.addCell(infoCell);

        Image photo = Image.getInstance(person.getPhotoPath());
        photo.scaleToFit(100, 125);
        PdfPCell photoCell = new PdfPCell(photo, false);
        photoCell.setBorder(Rectangle.NO_BORDER);
        photoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        photoCell.setPadding(4);
        table.addCell(photoCell);

        doc.add(table);
    }

    private void addAbout(Document doc) throws DocumentException {
        addSectionTitle(doc, "Hakkımda");
        doc.add(new Paragraph(person.getSummary(), normal(11, KOYU)));
    }

    private void addEducations(Document doc) throws DocumentException {
        addSectionTitle(doc, "Eğitim");
        for (Education e : person.getEducations()) {
            Paragraph p = new Paragraph();
            p.add(new Chunk(e.getSchool(), kalin(11.5f, KOYU)));
            p.add(new Chunk("   " + e.getPeriod(), normal(10, GRI)));
            doc.add(p);
            doc.add(new Paragraph(e.getDepartment(), normal(11, KOYU)));
        }
    }

    private void addExperiences(Document doc) throws DocumentException {
        addSectionTitle(doc, "İş Deneyimi");
        for (Experience e : person.getExperiences()) {
            Paragraph p = new Paragraph();
            p.add(new Chunk(e.getTitle() + " - " + e.getCompany(), kalin(11.5f, KOYU)));
            p.add(new Chunk("   " + e.getPeriod(), normal(10, GRI)));
            doc.add(p);
            Paragraph aciklama = new Paragraph(e.getDescription(), normal(10.5f, KOYU));
            aciklama.setSpacingAfter(8);
            doc.add(aciklama);
        }
    }

    private void addProjects(Document doc) throws DocumentException {
        addSectionTitle(doc, "Projeler");
        for (Project pr : person.getProjects()) {
            doc.add(new Paragraph(pr.getName(), kalin(11.5f, KOYU)));
            Paragraph aciklama = new Paragraph(pr.getDescription(), normal(10.5f, KOYU));
            aciklama.setSpacingAfter(8);
            doc.add(aciklama);
        }
    }

    private void addSectionTitle(Document doc, String text) throws DocumentException {
        Paragraph title = new Paragraph(text, kalin(14, BORDO));
        title.setSpacingBefore(16);
        doc.add(title);
        LineSeparator line = new LineSeparator(1.5f, 100, BORDO, Element.ALIGN_CENTER, -2);
        Paragraph separator = new Paragraph();
        separator.add(new Chunk(line));
        separator.setLeading(6f);
        separator.setSpacingAfter(4);
        doc.add(separator);
    }
}
