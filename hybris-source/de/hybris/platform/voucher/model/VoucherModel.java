package de.hybris.platform.voucher.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

public class VoucherModel extends DiscountModel
{
    public static final String _TYPECODE = "Voucher";
    public static final String _ORDERDISCOUNTRELATION = "OrderDiscountRelation";
    public static final String DESCRIPTION = "description";
    public static final String FREESHIPPING = "freeShipping";
    public static final String VALUESTRING = "valueString";
    public static final String RESTRICTIONS = "restrictions";
    public static final String INVALIDATIONS = "invalidations";


    public VoucherModel()
    {
    }


    public VoucherModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VoucherModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VoucherModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return getDescription(null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("description", loc);
    }


    @Accessor(qualifier = "freeShipping", type = Accessor.Type.GETTER)
    public Boolean getFreeShipping()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("freeShipping");
    }


    @Accessor(qualifier = "invalidations", type = Accessor.Type.GETTER)
    public Collection<VoucherInvalidationModel> getInvalidations()
    {
        return (Collection<VoucherInvalidationModel>)getPersistenceContext().getPropertyValue("invalidations");
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.GETTER)
    public Set<RestrictionModel> getRestrictions()
    {
        return (Set<RestrictionModel>)getPersistenceContext().getPropertyValue("restrictions");
    }


    @Accessor(qualifier = "valueString", type = Accessor.Type.GETTER)
    public String getValueString()
    {
        return (String)getPersistenceContext().getPropertyValue("valueString");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        setDescription(value, null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("description", loc, value);
    }


    @Accessor(qualifier = "freeShipping", type = Accessor.Type.SETTER)
    public void setFreeShipping(Boolean value)
    {
        getPersistenceContext().setPropertyValue("freeShipping", value);
    }


    @Accessor(qualifier = "invalidations", type = Accessor.Type.SETTER)
    public void setInvalidations(Collection<VoucherInvalidationModel> value)
    {
        getPersistenceContext().setPropertyValue("invalidations", value);
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.SETTER)
    public void setRestrictions(Set<RestrictionModel> value)
    {
        getPersistenceContext().setPropertyValue("restrictions", value);
    }
}
