package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.servicelayer.services.CMSNavigationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Deprecated(since = "1811", forRemoval = true)
public class NavigationNodePrepareInterceptor implements PrepareInterceptor
{
    private CMSNavigationService cmsNavigationService;
    private ModelService modelService;


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        Map<String, Set<Locale>> dirtyAttributes = ctx.getDirtyAttributes(model);
        CMSNavigationNodeModel node = (CMSNavigationNodeModel)model;
        CatalogVersionModel catalogVersion = node.getCatalogVersion();
        CMSNavigationNodeModel parent = node.getParent();
        if(catalogVersion == null || parent == null || parent.getCatalogVersion() == null)
        {
            return;
        }
        CatalogVersionModel parentCatalogVersion = parent.getCatalogVersion();
        if(!catalogVersion.equals(parentCatalogVersion))
        {
            if(dirtyAttributes.containsKey("catalogVersion"))
            {
                CMSNavigationNodeModel rootNaviNodeModel = this.cmsNavigationService.getSuperRootNavigationNode(catalogVersion);
                if(rootNaviNodeModel == null)
                {
                    rootNaviNodeModel = this.cmsNavigationService.createSuperRootNavigationNode(catalogVersion);
                    this.modelService.save(rootNaviNodeModel);
                }
                node.setParent(rootNaviNodeModel);
            }
            else if(dirtyAttributes.containsKey("parent"))
            {
                node.setCatalogVersion(parentCatalogVersion);
            }
            List<CMSNavigationNodeModel> allChildren = findAllChildren(node, new ArrayList<>());
            for(CMSNavigationNodeModel child : allChildren)
            {
                child.setCatalogVersion(node.getCatalogVersion());
                this.modelService.save(child);
                for(CMSNavigationEntryModel entry : child.getEntries())
                {
                    entry.setCatalogVersion(node.getCatalogVersion());
                    this.modelService.save(entry);
                }
            }
        }
    }


    protected List<CMSNavigationNodeModel> findAllChildren(CMSNavigationNodeModel node, List<CMSNavigationNodeModel> res)
    {
        for(CMSNavigationNodeModel child : node.getChildren())
        {
            if(!res.contains(child))
            {
                res.add(child);
                findAllChildren(child, res);
            }
        }
        return res;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setCmsNavigationService(CMSNavigationService cmsNavigationService)
    {
        this.cmsNavigationService = cmsNavigationService;
    }
}
