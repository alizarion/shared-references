package com.alizarion.reference.social.entities.notification;

import com.alizarion.reference.location.entities.Address;

/**
 * @author selim@openlinux.fr
 * Interface to implement
 */
public interface NotificationMessageFactory {

    public static final String TITLE_FILE_TEMPLATE_PREFIX ="Title";

    public static final String TEXT_FILE_TEMPLATE_PREFIX ="Text";

    public static final String RICH_FILE_TEMPLATE_PREFIX ="Rich";

    public String getTitle(Address destination) throws Exception;

    public String getText(Address destination) throws Exception;

    public Object getRichContent(Address destination) throws Exception;


}
