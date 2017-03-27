/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sachin.qa.app;

import java.io.File;

/**
 *
 * @author sku202
 */
public class MyCustomFilter extends javax.swing.filechooser.FileFilter {
    
    private String type;
    
    public MyCustomFilter(String str) {
        this.type = str;
    }
    
    @Override
    public boolean accept(File file) {
        // Allow only directories, or files with ".txt" extension
        return file.isDirectory() || file.getAbsolutePath().endsWith(type);
    }
    
    @Override
    public String getDescription() {
        // This description will be displayed in the dialog,
        // hard-coded = ugly, should be done via I18N
        return "All " + type.substring(1) + " Files";
    }
    
}
