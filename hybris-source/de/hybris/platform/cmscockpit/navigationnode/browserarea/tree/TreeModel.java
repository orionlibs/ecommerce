package de.hybris.platform.cmscockpit.navigationnode.browserarea.tree;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmscockpit.services.NavigationNodeTreeService;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.ItemModel;
import java.util.List;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zul.AbstractTreeModel;

public class TreeModel extends AbstractTreeModel
{
    private final NavigationNodeTreeService navigationNodeService;


    public TreeModel(TypedObject root)
    {
        super(root);
        this.navigationNodeService = (NavigationNodeTreeService)SpringUtil.getBean("navigationNodeTreeService", NavigationNodeTreeService.class);
    }


    public Object getChild(Object nodeObj, int index)
    {
        ItemModel nodeModel = (ItemModel)((TypedObject)nodeObj).getObject();
        if(nodeModel instanceof CatalogVersionModel)
        {
            List<TypedObject> roots = this.navigationNodeService.getRootNavigationNodes((CatalogVersionModel)nodeModel);
            return roots.get(index);
        }
        return this.navigationNodeService.getChild((TypedObject)nodeObj, index);
    }


    public int getChildCount(Object nodeObj)
    {
        ItemModel nodeModel = (ItemModel)((TypedObject)nodeObj).getObject();
        if(nodeModel instanceof CatalogVersionModel)
        {
            List<TypedObject> roots = this.navigationNodeService.getRootNavigationNodes((CatalogVersionModel)nodeModel);
            return roots.size();
        }
        return this.navigationNodeService.getChildCount((TypedObject)nodeObj);
    }


    public boolean isLeaf(Object nodeObj)
    {
        ItemModel nodeModel = (ItemModel)((TypedObject)nodeObj).getObject();
        if(nodeModel instanceof CatalogVersionModel)
        {
            List<TypedObject> roots = this.navigationNodeService.getRootNavigationNodes((CatalogVersionModel)nodeModel);
            return roots.isEmpty();
        }
        return this.navigationNodeService.isLeaf((TypedObject)nodeObj);
    }
}
