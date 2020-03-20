package com.company;

import java.awt.*;
import java.io.*;
import java.net.URI;

public class CatalogUtils {
    public static void save(Catalog catalog)
            throws IOException {
        try (var oos = new ObjectOutputStream(
                new FileOutputStream(catalog.getPath()))) {
            oos.writeObject(catalog);
        }
    }

    public static Catalog load(String path)
                 throws InvalidCatalogException {
        try (var oos = new ObjectInputStream(
                new FileInputStream(path))) {
            Object cat = oos.readObject();
            if(cat instanceof Catalog) {
                return (Catalog) cat;
            } else {
                throw  new InvalidCatalogException();
            }
        } catch (FileNotFoundException e) {
           throw new InvalidCatalogException();
        } catch (IOException e) {
            throw new InvalidCatalogException();
        } catch (ClassNotFoundException e) {
            throw new InvalidCatalogException();
        }
    }

    public static void view(Document doc) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        //… browse or open, depending of the location type
        if(doc.getLocation().contains(".")) {
            desktop.open(new File(doc.getLocation()));
        } else {
            desktop.browse(URI.create(doc.getLocation()));
        }
    }

    public static class InvalidCatalogException extends Exception {
    }
}
