package de.hybris.platform.cms2.version.service.impl;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.core.model.ItemModel;
import java.util.List;
import java.util.Map;

public class DefaultCMSVersionApplyAttributeValuesToPageItemModel extends DefaultCMSVersionApplyAttributeValuesToItemModel
{
    public void apply(ItemModel itemModel, Map<String, Object> values)
    {
        values.forEach((qualifier, value) -> {
            applyValueToQualifier(itemModel, qualifier, value);
            if(qualifier.equals("restrictions"))
            {
                List<AbstractRestrictionModel> restrictions = ((AbstractPageModel)itemModel).getRestrictions();
                restrictions.forEach(());
            }
        });
    }
}
