package ru.geekstar;

import java.io.*;

public class IOFile {

    public static void write(String pathToFile, String text, boolean append) {

    try {
        File file = new File(pathToFile);
        FileWriter fileWriter = new FileWriter(file, append);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.append(text);
        writer.append("\n");
        writer.flush();
        writer.close();

    } catch (IOException ioEx) {

        System.out.println("Не удалось записать в файл " + ioEx.getMessage());

        }

    }
    public static String reader(String pathToFile) {
        StringBuffer stringBuffer = new StringBuffer();

    try {
        File file = new File(pathToFile);
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);

        String line;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line + "\n");
        }
        reader.close();
    } catch (FileNotFoundException fileNotFoundEx)  {
        System.out.println("Файл не найден " + fileNotFoundEx.getMessage());
    } catch (IOException ioEx) {
        System.out.println("Не удалось прочитать файл " + ioEx.getMessage());
    }

        return stringBuffer.toString();
    }
}
