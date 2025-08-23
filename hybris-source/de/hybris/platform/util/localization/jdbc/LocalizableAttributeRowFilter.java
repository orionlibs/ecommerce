package de.hybris.platform.util.localization.jdbc;

import de.hybris.platform.util.localization.jdbc.rows.LocalizableAttributeRow;

public interface LocalizableAttributeRowFilter
{
    default boolean filter(LocalizableAttributeRow row)
    {
        return true;
    }
}
