/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.locales.jaxb;

import java.util.Locale;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.apache.commons.lang3.Validate;

public class LocaleAdapter extends XmlAdapter<String, Locale>
{
    @Override
    public String marshal(final Locale value)
    {
        Validate.notNull(value);
        return value.toLanguageTag();
    }


    @Override
    public Locale unmarshal(final String value)
    {
        Validate.notEmpty(value);
        return Locale.forLanguageTag(value);
    }
}
