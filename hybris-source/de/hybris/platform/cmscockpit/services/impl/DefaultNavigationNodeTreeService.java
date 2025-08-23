package de.hybris.platform.cmscockpit.services.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.servicelayer.services.CMSNavigationService;
import de.hybris.platform.cmscockpit.services.NavigationNodeTreeService;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultNavigationNodeTreeService implements NavigationNodeTreeService
{
    private TypeService typeService;
    private CMSNavigationService cmsNavigationService;


    public TypedObject getChild(TypedObject node, int index)
    {
        CMSNavigationNodeModel nodeModel = (CMSNavigationNodeModel)node.getObject();
        return getTypeService().wrapItem(nodeModel.getChildren().get(index));
    }


    public int getChildCount(TypedObject node)
    {
        CMSNavigationNodeModel nodeModel = (CMSNavigationNodeModel)node.getObject();
        return nodeModel.getChildren().size();
    }


    public boolean isLeaf(TypedObject node)
    {
        CMSNavigationNodeModel nodeModel = (CMSNavigationNodeModel)node.getObject();
        return CollectionUtils.isEmpty(nodeModel.getChildren());
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public List<TypedObject> getRootNavigationNodes(CatalogVersionModel catVer)
    {
        return getTypeService().wrapItems(getCmsNavigationService().getRootNavigationNodes(catVer));
    }


    protected CMSNavigationService getCmsNavigationService()
    {
        return this.cmsNavigationService;
    }


    @Required
    public void setCmsNavigationService(CMSNavigationService navigationNodeService)
    {
        this.cmsNavigationService = navigationNodeService;
    }
}
