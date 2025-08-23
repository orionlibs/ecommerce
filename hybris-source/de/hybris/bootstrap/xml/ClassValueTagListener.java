package de.hybris.bootstrap.xml;

public class ClassValueTagListener extends SimpleValueTagListener
{
    public ClassValueTagListener(TagListener parent, String tagName)
    {
        super(parent, tagName);
    }


    public ClassValueTagListener(TagListener parent, String tagName, String valueAttribute, String mappingAttribute)
    {
        super(parent, tagName, valueAttribute, mappingAttribute);
    }


    public Object getValue() throws ParseAbortException
    {
        String className = getCharacters();
        if(className == null || className.trim().isEmpty())
        {
            return null;
        }
        try
        {
            return Class.forName(className);
        }
        catch(ClassNotFoundException e)
        {
            throw new InvalidFormatException("Could not parse classname '" + className + "'", e);
        }
    }
}
