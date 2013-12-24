package com.mccarthy.utility;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
public class Storage {

    public static final String OUTPUT_DIR = "streetart";


    public File createFile(String fileName) throws IOException {
        // Create an image file name
        File directory = getOutputDirectory();
        File image = new File(directory.getAbsolutePath(), fileName);
        image.createNewFile();
        return image;
    }

    private File getOutputDirectory() {
        String dirPath = Environment.getExternalStorageDirectory() + File.separator + OUTPUT_DIR;
        File directory = new File(dirPath);
        if(!directory.exists())
            directory.mkdirs();
        return directory;
    }
}
