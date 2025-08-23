package de.hybris.platform.media.storage.impl;

import de.hybris.platform.media.storage.ConfigValueConverter;

public class BooleanValueConverter implements ConfigValueConverter<Boolean>
{
    public Boolean convert(String input)
    {
        return Boolean.valueOf(input);
    }
}
