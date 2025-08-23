package de.hybris.platform.personalizationcms.synchronization.itemsvisitors.impl;

import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cmsfacades.synchronization.itemvisitors.AbstractCMSComponentContainerModelVisitor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel;
import java.util.List;
import java.util.Map;

public class CxCmsComponentContainerModelVisitor extends AbstractCMSComponentContainerModelVisitor<CxCmsComponentContainerModel>
{
    public List<ItemModel> visit(CxCmsComponentContainerModel source, List<ItemModel> arg1, Map<String, Object> arg2)
    {
        List<ItemModel> collectedItems = super.visit((AbstractCMSComponentContainerModel)source, arg1, arg2);
        collectedItems.add(source.getDefaultCmsComponent());
        return collectedItems;
    }
}
