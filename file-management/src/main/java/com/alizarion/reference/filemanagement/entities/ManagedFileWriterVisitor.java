package com.alizarion.reference.filemanagement.entities;

import com.alizarion.reference.filemanagement.tools.FileHelper;

import java.io.*;

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
    public void visit(ImageManagedFile imageManagedFile)  {
        try {
            if (!simpleManagedFilePathBasedWriter(imageManagedFile)){
                //TODO ajouter un vrai traitement des exceptions
                throw new Exception("Error wile file ImageManagedFile write");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to write a SimpleManagedFile on the file system.
     * @param simpleManagedFile to write
     */
    @Override
    public void visit(SimpleManagedFile simpleManagedFile) {
        try {
            if (!simpleManagedFilePathBasedWriter(simpleManagedFile)){
                //TODO ajouter un vrai traitement des exceptions
                throw new Exception("Error wile file SimpleManagedFile write");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to write all kind of managed file on file system.
     * @param managedFile managedfile to write
     * @return  true if writed successfully.
     */
    private boolean simpleManagedFilePathBasedWriter(ManagedFile managedFile){
        try {
            File fileToWrite =
                    new File(FileHelper.
                            getFileFullPath(managedFile,this.rootFolder));
            if (!fileToWrite.getParentFile().exists()){
                if (!fileToWrite.getParentFile().mkdirs()){
                    return false;
                }
            }
            FileOutputStream out =
                    new FileOutputStream(fileToWrite);
            try {
                FileHelper.writeFile(this.inputStream,out);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}
