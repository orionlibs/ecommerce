package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class RemoveItemsCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "RemoveItemsCronJob";
    public static final String ITEMPKS = "itemPKs";
    public static final String ITEMSFOUND = "itemsFound";
    public static final String ITEMSDELETED = "itemsDeleted";
    public static final String ITEMSREFUSED = "itemsRefused";
    public static final String CREATESAVEDVALUES = "createSavedValues";


    public RemoveItemsCronJobModel()
    {
    }


    public RemoveItemsCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RemoveItemsCronJobModel(MediaModel _itemPKs, JobModel _job)
    {
        setItemPKs(_itemPKs);
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RemoveItemsCronJobModel(MediaModel _itemPKs, JobModel _job, ItemModel _owner)
    {
        setItemPKs(_itemPKs);
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "createSavedValues", type = Accessor.Type.GETTER)
    public Boolean getCreateSavedValues()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("createSavedValues");
    }


    @Accessor(qualifier = "itemPKs", type = Accessor.Type.GETTER)
    public MediaModel getItemPKs()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("itemPKs");
    }


    @Accessor(qualifier = "itemsDeleted", type = Accessor.Type.GETTER)
    public Integer getItemsDeleted()
    {
        return (Integer)getPersistenceContext().getPropertyValue("itemsDeleted");
    }


    @Accessor(qualifier = "itemsFound", type = Accessor.Type.GETTER)
    public Integer getItemsFound()
    {
        return (Integer)getPersistenceContext().getPropertyValue("itemsFound");
    }


    @Accessor(qualifier = "itemsRefused", type = Accessor.Type.GETTER)
    public Integer getItemsRefused()
    {
        return (Integer)getPersistenceContext().getPropertyValue("itemsRefused");
    }


    @Accessor(qualifier = "createSavedValues", type = Accessor.Type.SETTER)
    public void setCreateSavedValues(Boolean value)
    {
        getPersistenceContext().setPropertyValue("createSavedValues", value);
    }


    @Accessor(qualifier = "itemPKs", type = Accessor.Type.SETTER)
    public void setItemPKs(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("itemPKs", value);
    }


    @Accessor(qualifier = "itemsDeleted", type = Accessor.Type.SETTER)
    public void setItemsDeleted(Integer value)
    {
        getPersistenceContext().setPropertyValue("itemsDeleted", value);
    }


    @Accessor(qualifier = "itemsFound", type = Accessor.Type.SETTER)
    public void setItemsFound(Integer value)
    {
        getPersistenceContext().setPropertyValue("itemsFound", value);
    }


    @Accessor(qualifier = "itemsRefused", type = Accessor.Type.SETTER)
    public void setItemsRefused(Integer value)
    {
        getPersistenceContext().setPropertyValue("itemsRefused", value);
    }
}
