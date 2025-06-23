package org.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            String companyName = enterCompanyName(scanner);
            System.out.println(companyName);
            String templateToEdit = fileEditor(scanner, companyName);
            savePdfToDesktop(templateToEdit, companyName);
        }
    }

    private static String fileEditor(Scanner scanner, String companyName) {
        String pathToTemplate = selectTemplate(scanner);
        String templateToEdit = getTemplate(pathToTemplate);
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        templateToEdit = templateToEdit.replace("[Datum]", date);
        String jobTitle = enterJobTitle(scanner);
        templateToEdit = templateToEdit.replace("[Tjänstens namn]", jobTitle);
        String articleNumber = enterArticleNumber(scanner);

        if (Objects.equals(articleNumber, "")){
            templateToEdit = templateToEdit.replace("– Annonsnummer [XXXXXX]", articleNumber);
        }

        templateToEdit = templateToEdit.replace("[XXXXXX]", articleNumber);
        templateToEdit = templateToEdit.replace("[CompanyName]", companyName);
        return templateToEdit;
    }

    private static String enterCompanyName(Scanner scanner) {
        System.out.println("enter company name: ");
        String input = scanner.nextLine();
        return replaceSpecialCharacters(input);
    }

    private static String enterArticleNumber(Scanner scanner) {
        System.out.println("enter article number: ");
        String input = scanner.nextLine();
        return replaceSpecialCharacters(input);
    }

    private static String enterJobTitle(Scanner scanner) {
        System.out.println("enter job title: ");
        String input = scanner.nextLine();
        return replaceSpecialCharacters(input);
    }

    private static String replaceSpecialCharacters(String input) {
        return input.replace("å", "\u00E5")
                .replace("ä", "\u00E4")
                .replace("ö", "\u00F6");
    }

    private static String selectTemplate(Scanner scanner) {
        while (true) {
            System.out.println("select which CV to print:");
            System.out.println("1. software developer");
            System.out.println("2. it tech");
            System.out.println("3. support");
            System.out.println("8. template");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    return "softwareDeveloper";
                case 2:
                    return "itTech";
                case 3:
                    return "support";
                case 8:
                    return "template";
                case 9:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static String getTemplate(String fileToSelect) {
        try {
            ClassLoader classLoader = Main.class.getClassLoader();
            String resourcePath = fileToSelect;
            InputStream inputStream = classLoader.getResourceAsStream(resourcePath);

            if (inputStream == null) {
                System.err.println("Resource not found: " + resourcePath);
                return null;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append(System.lineSeparator());
                }
                System.out.println("File read successfully");
                return content.toString();
            }
        } catch (Exception e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static void savePdfToDesktop(String modifiedText, String companyName) {
        try {
            PdfMaker pdfMaker = new PdfMaker();
            pdfMaker.makePdf(modifiedText, companyName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}