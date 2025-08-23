package de.hybris.platform.cms2.version.service.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.core.model.ItemModel;
import java.util.List;
import java.util.Map;

public class DefaultCMSVersionApplyAttributeValuesToSlotItemModel extends DefaultCMSVersionApplyAttributeValuesToItemModel
{
    public void apply(ItemModel itemModel, Map<String, Object> values)
    {
        values.forEach((qualifier, value) -> {
            applyValueToQualifier(itemModel, qualifier, value);
            if(qualifier.equals("cmsComponents"))
            {
                List<AbstractCMSComponentModel> components = ((ContentSlotModel)itemModel).getCmsComponents();
                components.forEach(());
            }
        });
    }
}
