package de.hybris.platform.cms2.version.service;

import de.hybris.platform.core.model.ItemModel;
import java.util.Map;
import java.util.function.Predicate;

public interface CMSVersionApplyAttributeValuesToModel
{
    void apply(ItemModel paramItemModel, Map<String, Object> paramMap);


    Predicate<ItemModel> getConstrainedBy();
}
