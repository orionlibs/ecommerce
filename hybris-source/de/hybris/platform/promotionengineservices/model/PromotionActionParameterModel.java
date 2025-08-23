package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class PromotionActionParameterModel extends ItemModel
{
    public static final String _TYPECODE = "PromotionActionParameter";
    public static final String UUID = "uuid";
    public static final String VALUE = "value";


    public PromotionActionParameterModel()
    {
    }


    public PromotionActionParameterModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionActionParameterModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "uuid", type = Accessor.Type.GETTER)
    public String getUuid()
    {
        return (String)getPersistenceContext().getPropertyValue("uuid");
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public Object getValue()
    {
        return getPersistenceContext().getPropertyValue("value");
    }


    @Accessor(qualifier = "uuid", type = Accessor.Type.SETTER)
    public void setUuid(String value)
    {
        getPersistenceContext().setPropertyValue("uuid", value);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(Object value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }
}
