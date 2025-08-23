package de.hybris.platform.core.systemsetup.datacreator.impl;

import de.hybris.platform.core.Constants;
import de.hybris.platform.core.systemsetup.datacreator.internal.CoreDataCreator;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;

public class EncodingsDataCreator implements CoreDataCreator
{
    private static final Logger LOG = Logger.getLogger(EncodingsDataCreator.class);


    public void populateDatabase()
    {
        EnumerationType enumType = getEnumerationType();
        List<EnumerationValue> systemEncodnings = getSystemEncodnings(enumType);
        try
        {
            enumType.setAttribute("values", systemEncodnings);
        }
        catch(Exception e)
        {
            LOG.error("Could not set encoding enums to " + systemEncodnings + " due to " + e.getMessage());
        }
    }


    private EnumerationType getEnumerationType()
    {
        return getEnumerationManager().getEnumerationType(Constants.ENUMS.ENCODINGENUM);
    }


    protected EnumerationManager getEnumerationManager()
    {
        return EnumerationManager.getInstance();
    }


    private List<EnumerationValue> getSystemEncodnings(EnumerationType enumType)
    {
        List<EnumerationValue> encodings = new ArrayList<>();
        for(String code : Charset.availableCharsets().keySet())
        {
            try
            {
                encodings.add(getEnumerationManager().getEnumerationValue(enumType, code));
            }
            catch(JaloItemNotFoundException e)
            {
                try
                {
                    EnumerationValue value = getEnumerationManager().createEnumerationValue(enumType, code);
                    value.setExtensionName("core");
                    encodings.add(value);
                }
                catch(Exception e1)
                {
                    LOG.error("Could not create encoding enum '" + code + " due to: " + e1.getMessage(), e1);
                }
            }
        }
        Collections.sort(encodings, (Comparator<? super EnumerationValue>)new Object(this));
        return encodings;
    }
}
