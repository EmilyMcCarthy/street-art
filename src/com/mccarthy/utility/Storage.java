package com.mccarthy.utility;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
public class Storage {

    public static final String OUTPUT_DIR = "streetart";

    public byte[] readFile(String file) throws IOException {
        return readFile(new File(file));
    }

    public byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }

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
