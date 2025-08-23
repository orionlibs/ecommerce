package de.hybris.platform.cms2.version.service;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.ArrayList;
import java.util.List;

public interface CMSVersionHelper
{
    @Deprecated(since = "1811", forRemoval = true)
    boolean isCollectionAttribute(AttributeDescriptorModel paramAttributeDescriptorModel);


    List<AttributeDescriptorModel> getSerializableAttributes(ItemModel paramItemModel);


    default List<String> getAttributesNotVersion(ItemModel itemModel)
    {
        return new ArrayList<>();
    }
}
