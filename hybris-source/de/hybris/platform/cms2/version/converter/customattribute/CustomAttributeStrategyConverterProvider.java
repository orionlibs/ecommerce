package de.hybris.platform.cms2.version.converter.customattribute;

import de.hybris.platform.core.model.ItemModel;
import java.util.List;

public interface CustomAttributeStrategyConverterProvider
{
    List<CustomAttributeContentConverter> getConverters(ItemModel paramItemModel);
}
