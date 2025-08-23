package de.hybris.bootstrap.xml;

public class BooleanValueTagListener extends SimpleValueTagListener
{
    public BooleanValueTagListener(TagListener parent, String tagName)
    {
        super(parent, tagName);
    }


    public BooleanValueTagListener(TagListener parent, String tagName, String valueAttribute, String mappingAttr)
    {
        super(parent, tagName, valueAttribute, mappingAttr);
    }


    public Object getValue()
    {
        String chars = getCharacters();
        if(chars == null || chars.length() == 0)
        {
            return null;
        }
        return "true".equalsIgnoreCase(chars) ? Boolean.TRUE : Boolean.FALSE;
    }
}
