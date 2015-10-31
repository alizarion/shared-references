package com.alizarion.reference.filemanagement.entities;

import com.alizarion.reference.filemanagement.exception.ReadingManagedFileException;
import com.alizarion.reference.filemanagement.tools.FileHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Simple file reader, can be used to read all kind of
 * managed file on the file system,
 * @author selim@openlinux.fr.
 */
public class ManagedFileReaderVisitor  implements ManagedFileVisitor {

    private  String rootFolder;

    private File file;



    public ManagedFileReaderVisitor() {
    }

    public ManagedFileReaderVisitor(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    @Override
    public File visit(ImageManagedFile imageManagedFile) throws ReadingManagedFileException {
        return getFile(imageManagedFile);
    }

    @Override
    public File visit(SimpleManagedFile simpleManagedFile) throws ReadingManagedFileException {
        return getFile(simpleManagedFile);
    }

    public File getManagedFileAsFile(){
        return this.file;
    }

    /**
     * Method to read ManagedFile on the file system and setIt as File, InputStream object.
     * @param managedFile  to read
     * @return  true if file readed successfully.
     */
    private File getFile( ManagedFile managedFile) throws ReadingManagedFileException {
        FileInputStream inputStream =null;
        try {
            this.file = new File(FileHelper.
                    getFileFullPath(managedFile,
                            this.rootFolder));

            inputStream = new FileInputStream(this.file);
            inputStream.close();

            return this.file;
        } catch (IOException e) {
             if (inputStream!= null){
                 try {
                     inputStream.close();
                 } catch (IOException ignored) {
                 }
             }
            throw new ReadingManagedFileException("error during reading file  "+
                    FileHelper.getFileFullPath(managedFile,
                            this.rootFolder),e);

        }
    }
}
