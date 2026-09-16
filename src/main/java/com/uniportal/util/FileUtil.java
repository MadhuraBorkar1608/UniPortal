package com.uniportal.util;

import com.uniportal.exception.FileOperationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;

public class FileUtil {

    private static final String BASE_DIR = "files";
    
    static {
        // Create base directories if they don't exist
        String[] dirs = {"notices", "study_material", "assignments", "submissions", "helpdesk"};
        for (String dir : dirs) {
            File f = new File(BASE_DIR + File.separator + dir);
            if (!f.exists()) {
                f.mkdirs();
            }
        }
    }

    public static File selectFile(Component parent, String title, String[] extensions, String description) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        if (extensions != null && extensions.length > 0) {
            FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extensions);
            fileChooser.setFileFilter(filter);
        }
        
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.length() > 10 * 1024 * 1024) { // 10MB limit
                throw new FileOperationException("File size exceeds the 10MB limit.");
            }
            return selectedFile;
        }
        return null;
    }

    public static String saveFile(File sourceFile, String moduleDir, String prefix) {
        if (sourceFile == null || !sourceFile.exists()) {
            throw new FileOperationException("Source file does not exist.");
        }
        
        try {
            String extension = "";
            String fileName = sourceFile.getName();
            int i = fileName.lastIndexOf('.');
            if (i > 0) {
                extension = fileName.substring(i);
            }
            
            String newFileName = prefix + "_" + System.currentTimeMillis() + extension;
            Path targetDir = Paths.get(BASE_DIR, moduleDir);
            Path targetPath = targetDir.resolve(newFileName);
            
            Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return moduleDir + "/" + newFileName; // Relative path
        } catch (IOException e) {
            throw new FileOperationException("Failed to save file: " + e.getMessage(), e);
        }
    }
}
