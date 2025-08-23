package de.hybris.platform.commerceservices.model.storelocator;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.util.Collection;
import java.util.Locale;

public class StoreLocatorFeatureModel extends ItemModel
{
    public static final String _TYPECODE = "StoreLocatorFeature";
    public static final String _STORELOCATION2STORELOCATORFEATURE = "StoreLocation2StoreLocatorFeature";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String ICON = "icon";
    public static final String POINTOFSERVICES = "pointOfServices";


    public StoreLocatorFeatureModel()
    {
    }


    public StoreLocatorFeatureModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StoreLocatorFeatureModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "icon", type = Accessor.Type.GETTER)
    public MediaModel getIcon()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("icon");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "pointOfServices", type = Accessor.Type.GETTER)
    public Collection<PointOfServiceModel> getPointOfServices()
    {
        return (Collection<PointOfServiceModel>)getPersistenceContext().getPropertyValue("pointOfServices");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "icon", type = Accessor.Type.SETTER)
    public void setIcon(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("icon", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "pointOfServices", type = Accessor.Type.SETTER)
    public void setPointOfServices(Collection<PointOfServiceModel> value)
    {
        getPersistenceContext().setPropertyValue("pointOfServices", value);
    }
}
