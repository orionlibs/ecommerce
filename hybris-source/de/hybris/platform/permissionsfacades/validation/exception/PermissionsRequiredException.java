package de.hybris.platform.permissionsfacades.validation.exception;

import org.apache.commons.lang.StringUtils;

public class PermissionsRequiredException extends RuntimeException
{
    private static final String ERROR_MESSAGE_TEMPLATE = "Attribute 'permissions' is a required field for type %s.";
    private static final int DEFAULT_ITEM_NUMBER = 0;


    public PermissionsRequiredException(String type)
    {
        super(StringUtils.isBlank(type) ? buildMessage(0) : buildMessage(type));
    }


    public PermissionsRequiredException(int index)
    {
        super((index < 0) ? buildMessage(0) : buildMessage(index));
    }


    private static String buildMessage(String argument)
    {
        return String.format("Attribute 'permissions' is a required field for type %s.", new Object[] {argument});
    }


    private static String buildMessage(int itemIndex)
    {
        return buildMessage("permissions entry " + itemIndex);
    }
}
