package de.hybris.platform.configurablebundlebackoffice.synchronization.itemvisitors.impl;

import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.visitor.ItemVisitor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BundleTemplateItemVisitor implements ItemVisitor<BundleTemplateModel>
{
    public List<ItemModel> visit(BundleTemplateModel bundleTemplate, List<ItemModel> path, Map<String, Object> map)
    {
        List<ItemModel> items = new ArrayList<>();
        items.addAll(bundleTemplate.getRequiredBundleTemplates());
        items.addAll(bundleTemplate.getDependentBundleTemplates());
        items.addAll(bundleTemplate.getChildTemplates());
        items.add(bundleTemplate.getStatus());
        return items;
    }
}
