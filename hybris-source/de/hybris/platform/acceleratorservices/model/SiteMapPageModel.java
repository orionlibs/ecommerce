package de.hybris.platform.acceleratorservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorservices.enums.SiteMapChangeFrequencyEnum;
import de.hybris.platform.acceleratorservices.enums.SiteMapPageEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SiteMapPageModel extends ItemModel
{
    public static final String _TYPECODE = "SiteMapPage";
    public static final String CODE = "code";
    public static final String FREQUENCY = "frequency";
    public static final String PRIORITY = "priority";
    public static final String ACTIVE = "active";


    public SiteMapPageModel()
    {
    }


    public SiteMapPageModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SiteMapPageModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public SiteMapPageEnum getCode()
    {
        return (SiteMapPageEnum)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "frequency", type = Accessor.Type.GETTER)
    public SiteMapChangeFrequencyEnum getFrequency()
    {
        return (SiteMapChangeFrequencyEnum)getPersistenceContext().getPropertyValue("frequency");
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
    public Double getPriority()
    {
        return (Double)getPersistenceContext().getPropertyValue("priority");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(SiteMapPageEnum value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "frequency", type = Accessor.Type.SETTER)
    public void setFrequency(SiteMapChangeFrequencyEnum value)
    {
        getPersistenceContext().setPropertyValue("frequency", value);
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
    public void setPriority(Double value)
    {
        getPersistenceContext().setPropertyValue("priority", value);
    }
}
