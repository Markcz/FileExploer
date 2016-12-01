package com.example.mark.fileexploer.util;

import java.io.File;

/**
 * Created by mark on 16-11-19.
 */

public class MFile  {

    public MFile(File file) {
        this.file = file;
    }

    private File file;

    public File getFile() {
        return file;
    }
}
