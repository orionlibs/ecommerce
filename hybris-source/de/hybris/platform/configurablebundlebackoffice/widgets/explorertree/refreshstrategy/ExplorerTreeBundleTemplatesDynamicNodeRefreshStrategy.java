package de.hybris.platform.configurablebundlebackoffice.widgets.explorertree.refreshstrategy;

import com.google.common.collect.Sets;
import com.hybris.cockpitng.widgets.common.explorertree.refreshstrategy.ExplorerTreeRefreshStrategy;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class ExplorerTreeBundleTemplatesDynamicNodeRefreshStrategy implements ExplorerTreeRefreshStrategy
{
    public Collection<? extends Object> findRelatedObjectsToRefresh(Object object)
    {
        if(object instanceof BundleTemplateModel)
        {
            BundleTemplateModel bundleTemplate = (BundleTemplateModel)object;
            if(!bundleTemplate.getItemModelContext().isRemoved())
            {
                return (Collection)collectParentTemplates(bundleTemplate);
            }
        }
        return Collections.emptyList();
    }


    protected Collection<BundleTemplateModel> collectParentTemplates(BundleTemplateModel bundleTemplate)
    {
        return collectAllParentTemplates(bundleTemplate, Sets.newHashSet());
    }


    protected Collection<BundleTemplateModel> collectAllParentTemplates(BundleTemplateModel bundleTemplate, Set<BundleTemplateModel> parentBundles)
    {
        if(Objects.isNull(bundleTemplate.getParentTemplate()))
        {
            return parentBundles;
        }
        parentBundles.add(bundleTemplate.getParentTemplate());
        return collectAllParentTemplates(bundleTemplate.getParentTemplate(), parentBundles);
    }
}
