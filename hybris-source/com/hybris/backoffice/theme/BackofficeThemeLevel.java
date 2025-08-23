package com.hybris.backoffice.theme;

public enum BackofficeThemeLevel
{
    SYSTEM, USER;


    public static BackofficeThemeLevel fromValue(String v)
    {
        for(BackofficeThemeLevel c : values())
        {
            if(c.name().equalsIgnoreCase(v))
            {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
