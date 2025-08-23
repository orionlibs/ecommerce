package de.hybris.platform.voucher.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class RestrictionModel extends ItemModel
{
    public static final String _TYPECODE = "Restriction";
    public static final String _VOUCHERRESTRICTIONSRELATION = "VoucherRestrictionsRelation";
    public static final String POSITIVE = "positive";
    public static final String DESCRIPTION = "description";
    public static final String VIOLATIONMESSAGE = "violationMessage";
    public static final String RESTRICTIONTYPE = "restrictionType";
    public static final String VOUCHERPOS = "voucherPOS";
    public static final String VOUCHER = "voucher";


    public RestrictionModel()
    {
    }


    public RestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RestrictionModel(VoucherModel _voucher)
    {
        setVoucher(_voucher);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RestrictionModel(ItemModel _owner, VoucherModel _voucher)
    {
        setOwner(_owner);
        setVoucher(_voucher);
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


    @Accessor(qualifier = "positive", type = Accessor.Type.GETTER)
    public Boolean getPositive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("positive");
    }


    @Accessor(qualifier = "restrictionType", type = Accessor.Type.GETTER)
    public String getRestrictionType()
    {
        return getRestrictionType(null);
    }


    @Accessor(qualifier = "restrictionType", type = Accessor.Type.GETTER)
    public String getRestrictionType(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("restrictionType", loc);
    }


    @Accessor(qualifier = "violationMessage", type = Accessor.Type.GETTER)
    public String getViolationMessage()
    {
        return getViolationMessage(null);
    }


    @Accessor(qualifier = "violationMessage", type = Accessor.Type.GETTER)
    public String getViolationMessage(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("violationMessage", loc);
    }


    @Accessor(qualifier = "voucher", type = Accessor.Type.GETTER)
    public VoucherModel getVoucher()
    {
        return (VoucherModel)getPersistenceContext().getPropertyValue("voucher");
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


    @Accessor(qualifier = "positive", type = Accessor.Type.SETTER)
    public void setPositive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("positive", value);
    }


    @Accessor(qualifier = "violationMessage", type = Accessor.Type.SETTER)
    public void setViolationMessage(String value)
    {
        setViolationMessage(value, null);
    }


    @Accessor(qualifier = "violationMessage", type = Accessor.Type.SETTER)
    public void setViolationMessage(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("violationMessage", loc, value);
    }


    @Accessor(qualifier = "voucher", type = Accessor.Type.SETTER)
    public void setVoucher(VoucherModel value)
    {
        getPersistenceContext().setPropertyValue("voucher", value);
    }
}
