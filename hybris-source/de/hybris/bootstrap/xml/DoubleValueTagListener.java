package de.hybris.bootstrap.xml;

public class DoubleValueTagListener extends SimpleValueTagListener
{
    public DoubleValueTagListener(TagListener parent, String tagName)
    {
        super(parent, tagName);
    }


    public DoubleValueTagListener(TagListener parent, String tagName, String valueAttribute, String mappingAttribute)
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
        Double doubleValue = null;
        synchronized(getClass())
        {
            try
            {
                doubleValue = new Double(chars);
            }
            catch(NumberFormatException e)
            {
                throw new InvalidFormatException("Cannot parse double '" + chars + "'", e);
            }
        }
        return doubleValue;
    }
}
