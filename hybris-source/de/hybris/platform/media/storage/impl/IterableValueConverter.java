package de.hybris.platform.media.storage.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import de.hybris.platform.media.storage.ConfigValueConverter;

public class IterableValueConverter implements ConfigValueConverter<Iterable<String>>
{
    public static final Splitter COMMA_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();


    public Iterable<String> convert(String input)
    {
        Preconditions.checkArgument((input != null), "Conversion input cannot be null");
        return COMMA_SPLITTER.split(input);
    }
}
