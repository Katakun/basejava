package ru.javawebinar.basejava;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * gkislin
 * 21.07.2016
 */
public class MainFile {
    public static void main(String[] args) throws IOException {

        printAllFilesInDirectory("./basejava");

    }

    private static void printAllFilesInDirectory(String filePath) {
        File x = new File(filePath);
        File[] files = x.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    printAllFilesInDirectory(file.getPath());
                } else {
                    System.out.println(file.getName());
                }
            }
        }
    }

    private static void printCanonicalFilePath(String filePath) {
        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }
    }

    private static void printFilesList(String filePath) {
        File dir = new File(filePath);
        System.out.println("isDirectory = " + dir.isDirectory());
        System.out.println("isFile = " + dir.isFile());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }
    }

    private static void printFirstBitOfFile(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
