/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.handler.login;

import java.io.Serializable;

public class LoginInformationConfigData implements Serializable
{
    private final String key;
    private final String type;
    private final String name;
    private String value;
    private int index;


    public LoginInformationConfigData(final String key, final String name, final String type)
    {
        this.key = key;
        this.name = name;
        this.type = type;
    }


    public LoginInformationConfigData(final String key, final String name, final String type, final String value)
    {
        this.key = key;
        this.name = name;
        this.type = type;
        this.value = value;
    }


    public LoginInformationConfigData(final String key, final String name, final String type, final int index)
    {
        this.key = key;
        this.name = name;
        this.type = type;
        this.index = index;
    }


    public LoginInformationConfigData(final String key, final String name, final String type, final String value, final int index)
    {
        this.key = key;
        this.name = name;
        this.type = type;
        this.value = value;
        this.index = index;
    }


    public String getKey()
    {
        return key;
    }


    public String getType()
    {
        return type;
    }


    public String getName()
    {
        return name;
    }


    public String getValue()
    {
        return value;
    }


    public int getIndex()
    {
        return index;
    }
}
