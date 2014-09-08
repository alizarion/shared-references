package com.alizarion.reference.filemanagement.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author selim@openlinux.fr.
 */
public class FileHelper {

    public static final String DATE_PATH_FORMATTER = File.separator+"yyyy"+
            File.separator+"MM";

    public static String dateToFilePath(Date  date){
        SimpleDateFormat simpleDateFormatter = new SimpleDateFormat(DATE_PATH_FORMATTER);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return simpleDateFormatter.format(date)+
                File.separator+cal.get(Calendar.WEEK_OF_YEAR);
    }

    public static Boolean writeFile(InputStream inputStream ,
                             FileOutputStream fileOutputStream)
            throws IOException {

        int BUFFER_SIZE = 8192;
        byte[] buffer = new byte[BUFFER_SIZE];
        int a;
        while (true) {
            a = inputStream.read(buffer);
            if (a < 0)
                break;
            fileOutputStream.write(buffer, 0, a);
            fileOutputStream.flush();
        }
        fileOutputStream.close();
        inputStream.close();
        return true;
    }
}
