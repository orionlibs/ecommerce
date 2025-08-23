package de.hybris.bootstrap.xml;

public class StringValueTagListener extends SimpleValueTagListener
{
    public StringValueTagListener(TagListener parent, String tagName)
    {
        super(parent, tagName);
    }


    public StringValueTagListener(TagListener parent, String tagName, String valueAttribute, String mappingAttribute)
    {
        super(parent, tagName, valueAttribute, mappingAttribute);
    }


    public Object getValue()
    {
        return getCharacters();
    }
}
