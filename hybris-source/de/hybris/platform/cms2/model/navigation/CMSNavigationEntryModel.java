package de.hybris.platform.cms2.model.navigation;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CMSNavigationEntryModel extends CMSItemModel
{
    public static final String _TYPECODE = "CMSNavigationEntry";
    public static final String _CMSNAVNODESTOCMSNAVENTRIES = "CMSNavNodesToCMSNavEntries";
    public static final String ITEM = "item";
    public static final String NAVIGATIONNODEPOS = "navigationNodePOS";
    public static final String NAVIGATIONNODE = "navigationNode";


    public CMSNavigationEntryModel()
    {
    }


    public CMSNavigationEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSNavigationEntryModel(CatalogVersionModel _catalogVersion, ItemModel _item, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setItem(_item);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSNavigationEntryModel(CatalogVersionModel _catalogVersion, ItemModel _item, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setItem(_item);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "item", type = Accessor.Type.GETTER)
    public ItemModel getItem()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("item");
    }


    @Accessor(qualifier = "navigationNode", type = Accessor.Type.GETTER)
    public CMSNavigationNodeModel getNavigationNode()
    {
        return (CMSNavigationNodeModel)getPersistenceContext().getPropertyValue("navigationNode");
    }


    @Accessor(qualifier = "item", type = Accessor.Type.SETTER)
    public void setItem(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("item", value);
    }


    @Accessor(qualifier = "navigationNode", type = Accessor.Type.SETTER)
    public void setNavigationNode(CMSNavigationNodeModel value)
    {
        getPersistenceContext().setPropertyValue("navigationNode", value);
    }
}
