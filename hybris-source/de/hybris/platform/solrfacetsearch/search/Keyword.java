package de.hybris.platform.solrfacetsearch.search;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

public class Keyword implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final String value;
    private final Collection<KeywordModifier> modifiers;


    public Keyword(String value, KeywordModifier... modifiers)
    {
        this.value = value;
        this.modifiers = Arrays.asList(modifiers);
    }


    public String getValue()
    {
        return this.value;
    }


    public Collection<KeywordModifier> getModifiers()
    {
        return this.modifiers;
    }
}
