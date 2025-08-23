/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.exception;

public class NotExtensibleWidgetException extends WidgetTestException
{
    public static final String FINAL_WIDGET_CLASS = "Final widget class";
    public static final String INEXTENSIBLE_METHOD = "Inextensible method";
    public static final String PRIVATE_SERVICE_OR_FACADE = "Private service or facade";
    public static final String NO_ACCESSIBLE_CONSTRUCTOR = "No accessible constructor";
    public static final String PACKAGE_PRIVATE_CONSTRUCTOR = "Package private constructor";
    public static final String INACCESSIBLE_ZK_FIELD = "Inaccessible ZK field";


    public NotExtensibleWidgetException(final String cause)
    {
        super(cause);
    }


    public NotExtensibleWidgetException(final String cause, final String message)
    {
        super(String.format("Cause: %s\nMessage: %s", cause, message));
    }
}
