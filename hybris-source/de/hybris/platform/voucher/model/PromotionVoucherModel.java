package de.hybris.platform.voucher.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class PromotionVoucherModel extends VoucherModel
{
    public static final String _TYPECODE = "PromotionVoucher";
    public static final String VOUCHERCODE = "voucherCode";
    public static final String REDEMPTIONQUANTITYLIMIT = "redemptionQuantityLimit";
    public static final String REDEMPTIONQUANTITYLIMITPERUSER = "redemptionQuantityLimitPerUser";


    public PromotionVoucherModel()
    {
    }


    public PromotionVoucherModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionVoucherModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionVoucherModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "redemptionQuantityLimit", type = Accessor.Type.GETTER)
    public Integer getRedemptionQuantityLimit()
    {
        return (Integer)getPersistenceContext().getPropertyValue("redemptionQuantityLimit");
    }


    @Accessor(qualifier = "redemptionQuantityLimitPerUser", type = Accessor.Type.GETTER)
    public Integer getRedemptionQuantityLimitPerUser()
    {
        return (Integer)getPersistenceContext().getPropertyValue("redemptionQuantityLimitPerUser");
    }


    @Accessor(qualifier = "voucherCode", type = Accessor.Type.GETTER)
    public String getVoucherCode()
    {
        return (String)getPersistenceContext().getPropertyValue("voucherCode");
    }


    @Accessor(qualifier = "redemptionQuantityLimit", type = Accessor.Type.SETTER)
    public void setRedemptionQuantityLimit(Integer value)
    {
        getPersistenceContext().setPropertyValue("redemptionQuantityLimit", value);
    }


    @Accessor(qualifier = "redemptionQuantityLimitPerUser", type = Accessor.Type.SETTER)
    public void setRedemptionQuantityLimitPerUser(Integer value)
    {
        getPersistenceContext().setPropertyValue("redemptionQuantityLimitPerUser", value);
    }


    @Accessor(qualifier = "voucherCode", type = Accessor.Type.SETTER)
    public void setVoucherCode(String value)
    {
        getPersistenceContext().setPropertyValue("voucherCode", value);
    }
}
