package de.hybris.bootstrap.xml;

public class IntegerValueTagListener extends SimpleValueTagListener
{
    public IntegerValueTagListener(TagListener parent, String tagName)
    {
        super(parent, tagName);
    }


    public IntegerValueTagListener(TagListener parent, String tagName, String valueAttribute, String mappingAttribute)
    {
        super(parent, tagName, valueAttribute, mappingAttribute);
    }


    public synchronized Object getValue() throws ParseAbortException
    {
        String chars = getCharacters();
        if(chars == null || chars.length() == 0)
        {
            return null;
        }
        Integer intValue = null;
        synchronized(getClass())
        {
            try
            {
                intValue = Integer.valueOf(chars);
            }
            catch(NumberFormatException e)
            {
                throw new InvalidFormatException("Cannot parse integer '" + chars + "'", e);
            }
        }
        return intValue;
    }
}
