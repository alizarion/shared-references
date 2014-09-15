package com.alizarion.reference.filemanagement.entities;

import com.alizarion.reference.resource.exception.PersistentResourceNotFoundException;
import com.alizarion.reference.resource.mbean.PersistentMBean;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Managed file params, root folder, temp folder .
 * @author selim@openlinux.fr.
 */
public class FileManagementMBean extends PersistentMBean {

    private static final long serialVersionUID = -8298079648347720667L;


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

    public URI getManagedFileRootFolder() throws
            URISyntaxException,
            PersistentResourceNotFoundException {
       return new  URI(getValue(MANAGED_FILE_ROOT_FOLDER));
    }


}
