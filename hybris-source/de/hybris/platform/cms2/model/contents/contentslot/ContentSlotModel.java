package de.hybris.platform.cms2.model.contents.contentslot;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.List;

public class ContentSlotModel extends CMSItemModel
{
    public static final String _TYPECODE = "ContentSlot";
    public static final String ACTIVE = "active";
    public static final String ACTIVEFROM = "activeFrom";
    public static final String ACTIVEUNTIL = "activeUntil";
    public static final String CURRENTPOSITION = "currentPosition";
    public static final String ORIGINALSLOT = "originalSlot";
    public static final String CMSCOMPONENTS = "cmsComponents";


    public ContentSlotModel()
    {
    }


    public ContentSlotModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ContentSlotModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ContentSlotModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
    }


    @Accessor(qualifier = "activeFrom", type = Accessor.Type.GETTER)
    public Date getActiveFrom()
    {
        return (Date)getPersistenceContext().getPropertyValue("activeFrom");
    }


    @Accessor(qualifier = "activeUntil", type = Accessor.Type.GETTER)
    public Date getActiveUntil()
    {
        return (Date)getPersistenceContext().getPropertyValue("activeUntil");
    }


    @Accessor(qualifier = "cmsComponents", type = Accessor.Type.GETTER)
    public List<AbstractCMSComponentModel> getCmsComponents()
    {
        return (List<AbstractCMSComponentModel>)getPersistenceContext().getPropertyValue("cmsComponents");
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "currentPosition", type = Accessor.Type.GETTER)
    public String getCurrentPosition()
    {
        return (String)getPersistenceContext().getPropertyValue("currentPosition");
    }


    @Accessor(qualifier = "originalSlot", type = Accessor.Type.GETTER)
    public ContentSlotModel getOriginalSlot()
    {
        return (ContentSlotModel)getPersistenceContext().getPropertyValue("originalSlot");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
    }


    @Accessor(qualifier = "activeFrom", type = Accessor.Type.SETTER)
    public void setActiveFrom(Date value)
    {
        getPersistenceContext().setPropertyValue("activeFrom", value);
    }


    @Accessor(qualifier = "activeUntil", type = Accessor.Type.SETTER)
    public void setActiveUntil(Date value)
    {
        getPersistenceContext().setPropertyValue("activeUntil", value);
    }


    @Accessor(qualifier = "cmsComponents", type = Accessor.Type.SETTER)
    public void setCmsComponents(List<AbstractCMSComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("cmsComponents", value);
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "currentPosition", type = Accessor.Type.SETTER)
    public void setCurrentPosition(String value)
    {
        getPersistenceContext().setPropertyValue("currentPosition", value);
    }


    @Accessor(qualifier = "originalSlot", type = Accessor.Type.SETTER)
    public void setOriginalSlot(ContentSlotModel value)
    {
        getPersistenceContext().setPropertyValue("originalSlot", value);
    }
}
