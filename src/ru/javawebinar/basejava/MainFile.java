package ru.javawebinar.basejava;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * gkislin
 * 21.07.2016
 */
public class MainFile {
    public static void main(String[] args) {

        String filePath = "basejava/.gitignore";

        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File("basejava/src");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        printDirectoryDeeply(dir);
    }

    // TODO: make pretty output
    public static void printDirectoryDeeply(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                int level = dir.toPath().getNameCount();
                for (int i = 0; i < level; i++) {
                    System.out.print(" . ");
                }
                if (file.isDirectory()) {
                    System.out.println("\u25BC [" + file.getName() + "]");
                    printDirectoryDeeply(file);
                } else {
                    System.out.println(file.getName());
                }
            }
        }
    }
}
