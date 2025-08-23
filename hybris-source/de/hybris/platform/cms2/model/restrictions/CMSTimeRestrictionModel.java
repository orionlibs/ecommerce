package de.hybris.platform.cms2.model.restrictions;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class CMSTimeRestrictionModel extends AbstractRestrictionModel
{
    public static final String _TYPECODE = "CMSTimeRestriction";
    public static final String ACTIVEFROM = "activeFrom";
    public static final String ACTIVEUNTIL = "activeUntil";
    public static final String USESTORETIMEZONE = "useStoreTimeZone";


    public CMSTimeRestrictionModel()
    {
    }


    public CMSTimeRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSTimeRestrictionModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSTimeRestrictionModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
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


    @Accessor(qualifier = "useStoreTimeZone", type = Accessor.Type.GETTER)
    public Boolean getUseStoreTimeZone()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("useStoreTimeZone");
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


    @Accessor(qualifier = "useStoreTimeZone", type = Accessor.Type.SETTER)
    public void setUseStoreTimeZone(Boolean value)
    {
        getPersistenceContext().setPropertyValue("useStoreTimeZone", value);
    }
}
