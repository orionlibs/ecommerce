package de.hybris.platform.adaptivesearch.context;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

public class AsKeyword implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final String value;
    private final Collection<AsKeywordModifier> modifiers;


    public AsKeyword(String value, AsKeywordModifier... modifiers)
    {
        this.value = value;
        this.modifiers = Arrays.asList(modifiers);
    }


    public String getValue()
    {
        return this.value;
    }


    public Collection<AsKeywordModifier> getModifiers()
    {
        return this.modifiers;
    }
}
