package com.alizarion.reference.filemanagement.entities;

import com.alizarion.reference.resource.mbean.PersistentMBean;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author selim@openlinux.fr.
 */
public class FileManagementMBean extends PersistentMBean {

    public final static String CATEGORY =
            "com.alizarion.reference.filemanagement.properties";

    public final static String MANAGED_FILE_ROOT_FOLDER =
            "managed-file-root-folder";

    public final static String MANAGED_FILE_TEMP_FOLDER =
                "managed-file-temp-folder";

    @Override
    public String getCategory() {
        return CATEGORY;
    }

    public URI getManagedFileRootFolder() throws URISyntaxException {
       return new  URI(getValue(MANAGED_FILE_ROOT_FOLDER));
    }


}
