package com.hybris.backoffice.proxy;

import java.util.Locale;

public interface LabelServiceProxy
{
    String getObjectLabel(Object paramObject, Locale paramLocale);


    String getObjectDescription(Object paramObject);
}
