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
        printAllFilesInDirectory("./basejava/src");
    }

    private static void printAllFilesInDirectory(String filePath) throws IOException {
        File directory = new File(filePath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                int deep = file.getAbsolutePath().split("\\\\").length -
                        file.getPath().split("\\\\").length;
                int steps = file.getPath().split("\\\\").length - deep;
                for (int i = 0; i <= steps; i++) {
                    System.out.print(" . ");
                }
                if (file.isDirectory()) {
                    System.out.println("\u25BC [" + file.getName() + "]");
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
