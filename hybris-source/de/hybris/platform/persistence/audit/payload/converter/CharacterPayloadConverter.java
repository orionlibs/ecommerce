package de.hybris.platform.persistence.audit.payload.converter;

import org.apache.commons.lang.CharUtils;

public class CharacterPayloadConverter implements PayloadConverter<Character>
{
    public String convertToString(Character obj)
    {
        return obj.toString();
    }


    public Character convertFromString(String str)
    {
        return Character.valueOf(CharUtils.toChar(str, false));
    }


    public Class<Character> forClass()
    {
        return Character.class;
    }
}
