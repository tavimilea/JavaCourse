package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            testCreateSave();
        }catch (IOException e) {
            System.out.println("error in create save");
        }

        try {
            testLoadView();
        } catch (CatalogUtils.InvalidCatalogException e) {
            System.out.println("error in catalog open");
        } catch (IOException e){
          System.out.println("error in catalog open");
        }
    }
    
    public static void testCreateSave() throws IOException {
        Catalog catalog =
                new Catalog("Java Resources", "/Users/tavi/Documents/example.txt");
        Document doc = new Document("java1", "Java Course 1",
                "https://profs.info.uaic.ro/~acf/java/slides/en/intro_slide_en.pdf");
        doc.addTag("type", "Slides");
        catalog.add(doc);

        CatalogUtils.save(catalog);
    }

    public static void testLoadView() throws IOException, CatalogUtils.InvalidCatalogException {
        Catalog catalog = CatalogUtils.load("d:/java/catalog.ser");
        Document doc = catalog.findById("java1");
        CatalogUtils.view(doc);
    }
}
