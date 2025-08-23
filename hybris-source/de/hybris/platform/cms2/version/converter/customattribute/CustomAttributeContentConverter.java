package de.hybris.platform.cms2.version.converter.customattribute;

import de.hybris.platform.core.model.ItemModel;
import java.util.function.Predicate;

public interface CustomAttributeContentConverter
{
    String getQualifier();


    Predicate<ItemModel> getConstrainedBy();


    Object convertModelToData(ItemModel paramItemModel);


    void populateItemModel(ItemModel paramItemModel, String paramString);
}
