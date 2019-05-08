package com.para.diskio;

import java.io.*;

public class FileIOOp {

    public static void main(String[] args) {
        FileOutputStream fileOutputStream = null;
        File file = null;
        File outputFile = null;
        FileInputStream fileInputStream = null;
        try {
            outputFile = new File("E:\\batdir\\output.txt");
            fileOutputStream = new FileOutputStream(outputFile);
            file = new File("E:\\batdir\\a.txt");
            fileInputStream = new FileInputStream(file);
            FileDescriptor descriptor = fileInputStream.getFD();
            FileDescriptor descriptor1 = fileOutputStream.getFD();
            byte[] data = new byte[1024];
            int count = 0;
            while (-1 != (count = fileInputStream.read(data))) {
                System.out.println(new String(data, 0, count));
                fileOutputStream.write(data, 0, count);
            }
            System.out.println(System.getProperty("file.encoding"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
