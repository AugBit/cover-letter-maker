package org.example;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.nio.file.Paths;

public class PdfMaker {

    public String makePdf(String text, String companyName) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String desktopPath;
            if (os.contains("linux") && System.getenv("WSL_DISTRO_NAME") != null) {
                // Running in WSL
                desktopPath = "/mnt/c/Users/augus/Desktop";
            } else {
                // Running on Windows or other OS
                String userHome = System.getProperty("user.home");
                desktopPath = Paths.get(userHome, "Desktop").toString();
            }
            String fileName = "August Rydnell Personligt Brev Till " + companyName + ".pdf";
            String fullPath = Paths.get(desktopPath, fileName).toString();
            PdfWriter writer = new PdfWriter(fullPath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            document.add(new Paragraph(text));
            document.close();
            return fullPath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
