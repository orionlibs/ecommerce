package de.hybris.platform.servicelayer.internal.model.impl;

import java.io.Serializable;
import java.util.Locale;

public interface AttributeProvider extends Serializable
{
    Object getAttribute(String paramString);


    Object getLocalizedAttribute(String paramString, Locale paramLocale);
}
