package de.hybris.platform.cms2.version.converter.attribute.impl;

import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import java.util.Objects;

public class ItemToDataConverter implements Converter<ItemModel, PK>
{
    public PK convert(ItemModel source)
    {
        if(Objects.isNull(source))
        {
            return null;
        }
        return source.getPk();
    }
}
