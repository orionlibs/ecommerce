package de.hybris.bootstrap.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTagListener extends SimpleValueTagListener
{
    private final String[] patterns;


    public DateTagListener(TagListener parent, String tagName, String[] patterns)
    {
        super(parent, tagName);
        this.patterns = patterns;
    }


    public DateTagListener(TagListener parent, String tagName, String valueAttribute, String mappingAttribute, String[] patterns)
    {
        super(parent, tagName, valueAttribute, mappingAttribute);
        this.patterns = patterns;
    }


    public Object getValue() throws ParseAbortException
    {
        String chars = getCharacters();
        if(chars == null || chars.length() == 0)
        {
            return null;
        }
        StringBuilder errors = new StringBuilder();
        int emptyStringBuilder = 0;
        Date ret = null;
        for(int i = 0; i < this.patterns.length; i++)
        {
            SimpleDateFormat df = new SimpleDateFormat(this.patterns[i]);
            try
            {
                ret = df.parse(chars);
            }
            catch(ParseException e)
            {
                if(errors.length() != 0)
                {
                    errors.append("\n");
                }
                errors.append("date pattern ").append(this.patterns[i]).append("caused error ").append(e.getMessage());
                errors.append(" for text '").append(chars).append("'");
            }
        }
        if(ret == null)
        {
            throw new InvalidFormatException("Could not parse date string '" + chars + "' due to " + errors.toString());
        }
        return ret;
    }
}
