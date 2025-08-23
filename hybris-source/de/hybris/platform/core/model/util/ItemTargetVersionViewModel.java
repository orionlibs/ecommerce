package de.hybris.platform.core.model.util;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ItemTargetVersionViewModel extends ItemModel
{
    public static final String _TYPECODE = "ItemTargetVersionView";
    public static final String SOURCEITEM = "sourceItem";
    public static final String TARGETITEM = "targetItem";
    public static final String TARGETVERSION = "targetVersion";


    public ItemTargetVersionViewModel()
    {
    }


    public ItemTargetVersionViewModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ItemTargetVersionViewModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "sourceItem", type = Accessor.Type.GETTER)
    public ItemModel getSourceItem()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("sourceItem");
    }


    @Accessor(qualifier = "targetItem", type = Accessor.Type.GETTER)
    public ItemModel getTargetItem()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("targetItem");
    }


    @Accessor(qualifier = "targetVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getTargetVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("targetVersion");
    }
}
