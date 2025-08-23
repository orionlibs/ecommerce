package de.hybris.platform.media.storage.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.media.storage.ConfigValueConverter;

public class IntegerValueConverter implements ConfigValueConverter<Integer>
{
    public Integer convert(String input)
    {
        Preconditions.checkArgument((input != null), "Conversion input cannot be null");
        try
        {
            return Integer.valueOf(input);
        }
        catch(NumberFormatException e)
        {
            throw new IllegalArgumentException("Cannot convert property value: " + input + " into Integer (reason: " + e
                            .getMessage() + ")", e);
        }
    }
}
