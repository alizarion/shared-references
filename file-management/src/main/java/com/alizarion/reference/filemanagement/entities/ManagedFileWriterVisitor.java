package com.alizarion.reference.filemanagement.entities;

import com.alizarion.reference.filemanagement.exception.WritingManagedFileException;
import com.alizarion.reference.filemanagement.tools.FileHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Simple class to write all kind of managedFile on the file system.
 * @author selim@openlinux.fr.
 */
public class ManagedFileWriterVisitor implements ManagedFileVisitor {

    /**
     * root folder of all managed files
     */
    private  String rootFolder;

    /**
     * inputStream that have to be writed on the file system
     */
    private InputStream inputStream;

    public ManagedFileWriterVisitor(InputStream inputStream,
                                    String rootFolder) {
        this.inputStream = inputStream;
        this.rootFolder = rootFolder;
    }

    /**
     * Method to write ImageManagedFile on this file system.
     * @param imageManagedFile to write.
     */
    @Override
    public Boolean visit(ImageManagedFile imageManagedFile) throws WritingManagedFileException {
        return simpleManagedFilePathBasedWriter(imageManagedFile);
    }

    /**
     * Method to write a SimpleManagedFile on the file system.
     * @param simpleManagedFile to write
     */
    @Override
    public Boolean visit(SimpleManagedFile simpleManagedFile) throws WritingManagedFileException {
        return simpleManagedFilePathBasedWriter(simpleManagedFile);
    }

    /**
     * Method to write all kind of managed file on file system.
     * @param managedFile managedfile to write
     * @return  true if writed successfully.
     */
    private Boolean simpleManagedFilePathBasedWriter(ManagedFile managedFile) throws WritingManagedFileException {
        File fileToWrite =
                new File(FileHelper.
                        getFileFullPath(managedFile,this.rootFolder));
        if (!fileToWrite.getParentFile().exists()){
            if (!fileToWrite.getParentFile().mkdirs()){
                throw new WritingManagedFileException("Cannot make " +
                        fileToWrite.getParentFile().getAbsolutePath()
                        + "Folder") ;
            }
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(fileToWrite);


            FileHelper.writeFile(this.inputStream,out);
            return null;
        } catch (IOException e) {
            throw new WritingManagedFileException(managedFile.toString(),e) ;
        }

    }

}
