package com.para.analyse;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class IpaAnalyze {

    public static void main(String[] args) {
        getIpaInfoMap(new File("E:\\batdir\\ipafile\\qhapmTest.ipa"));
    }

    public static Map<String, String> getIpaInfoMap(File ipaFile){
        Map<String, String> map = new HashMap<>();
        try {
            ZipInputStream zipIns = new ZipInputStream(new FileInputStream(ipaFile));

            ZipEntry zipEntry;
            InputStream infoIs = null;
            NSDictionary rootDict = null;
            String icon = null;

            while ((zipEntry = zipIns.getNextEntry()) != null) {
                if( !zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    if(null != name && name.toLowerCase().contains(".app/info.plist")) {
                        ByteArrayOutputStream _copy = new ByteArrayOutputStream();
                        int chunk = 0;
                        byte[] data = new byte[1024];
                        while (-1 != (chunk = zipIns.read(data))) {
                            _copy.write(data, 0, chunk);
                        }
                        infoIs = new ByteArrayInputStream(_copy.toByteArray());
                        rootDict = (NSDictionary) PropertyListParser.parse(infoIs);

                        NSDictionary iconDict = (NSDictionary) rootDict.get("CFBundleIcons");
                    }
                }
            }

            for(String keyName : rootDict.allKeys()) {
                System.out.println(keyName + ":" + rootDict.get(keyName).toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (PropertyListFormatException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }


        return map;
    }

    private static File getIpaInfo(File oldFile) {
        try {
            int byteRead = 0;
            String fileName = oldFile.getAbsolutePath().replace(".ipa", ".zip");
            File newFile = new File(fileName);
            if (oldFile.exists()) {
                //创建一个zip文件
                InputStream in = new FileInputStream(oldFile);
                FileOutputStream fs = new FileOutputStream(newFile);
                byte[] buffer = new byte[14444];
                while ((byteRead = in.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteRead);
                }
            }

        } catch (Exception e) {

        } finally {

        }
        return null;
    }
}
