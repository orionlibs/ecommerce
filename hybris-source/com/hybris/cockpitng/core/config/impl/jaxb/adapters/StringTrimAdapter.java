/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringTrimAdapter extends XmlAdapter<String, String>
{
    @Override
    public String unmarshal(final String value)
    {
        if(value == null)
        {
            return null;
        }
        return value.trim();
    }


    @Override
    public String marshal(final String value)
    {
        if(value == null)
        {
            return null;
        }
        return value.trim();
    }
}
