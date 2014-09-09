package com.alizarion.reference.filemanagement.entities;

import com.alizarion.reference.filemanagement.tools.FileHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Simple file reader, can be used to read all kind of
 * managed file on the file system,
 * @author selim@openlinux.fr.
 */
public class ManagedFileReaderVisitor  implements ManagedFileVisitor {

    private  String rootFolder;

    private File file;

    private FileInputStream inputStream;


    public ManagedFileReaderVisitor() {
    }

    public ManagedFileReaderVisitor(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    @Override
    public void visit(ImageManagedFile imageManagedFile) {
        getFile(imageManagedFile);
    }

    @Override
    public void visit(SimpleManagedFile simpleManagedFile) {
        getFile(simpleManagedFile);
    }

    public File getManagedFileAsFile(){
        return this.file;
    }

    public FileInputStream getInputStream(){
        return this.inputStream;
    }

    /**
     * Method to read ManagedFile on the file system and setIt as File, InputStream object.
     * @param managedFile  to read
     * @return  true if file readed successfully.
     */
    private boolean getFile( ManagedFile managedFile){
        try {
            this.file = new File(FileHelper.
                    getFileFullPath(managedFile,
                            this.rootFolder));
            this.inputStream =
                    new FileInputStream(this.file);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}