package de.hybris.platform.core.model.order.payment;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class PaymentInfoModel extends ItemModel
{
    public static final String _TYPECODE = "PaymentInfo";
    public static final String _USER2PAYMENTINFOS = "User2PaymentInfos";
    public static final String ORIGINAL = "original";
    public static final String CODE = "code";
    public static final String DUPLICATE = "duplicate";
    public static final String USER = "user";
    public static final String CARTTOORDERCRONJOB = "cartToOrderCronJob";
    public static final String BILLINGADDRESS = "billingAddress";
    public static final String SAVED = "saved";


    public PaymentInfoModel()
    {
    }


    public PaymentInfoModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PaymentInfoModel(String _code, UserModel _user)
    {
        setCode(_code);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PaymentInfoModel(String _code, ItemModel _original, ItemModel _owner, UserModel _user)
    {
        setCode(_code);
        setOriginal(_original);
        setOwner(_owner);
        setUser(_user);
    }


    @Accessor(qualifier = "billingAddress", type = Accessor.Type.GETTER)
    public AddressModel getBillingAddress()
    {
        return (AddressModel)getPersistenceContext().getPropertyValue("billingAddress");
    }


    @Accessor(qualifier = "cartToOrderCronJob", type = Accessor.Type.GETTER)
    public Collection<CartToOrderCronJobModel> getCartToOrderCronJob()
    {
        return (Collection<CartToOrderCronJobModel>)getPersistenceContext().getPropertyValue("cartToOrderCronJob");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "duplicate", type = Accessor.Type.GETTER)
    public Boolean getDuplicate()
    {
        Boolean value = (Boolean)getPersistenceContext().getPropertyValue("duplicate");
        return (value != null) ? value : Boolean.valueOf(false);
    }


    @Accessor(qualifier = "original", type = Accessor.Type.GETTER)
    public ItemModel getOriginal()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("original");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "saved", type = Accessor.Type.GETTER)
    public boolean isSaved()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("saved"));
    }


    @Accessor(qualifier = "billingAddress", type = Accessor.Type.SETTER)
    public void setBillingAddress(AddressModel value)
    {
        getPersistenceContext().setPropertyValue("billingAddress", value);
    }


    @Accessor(qualifier = "cartToOrderCronJob", type = Accessor.Type.SETTER)
    public void setCartToOrderCronJob(Collection<CartToOrderCronJobModel> value)
    {
        getPersistenceContext().setPropertyValue("cartToOrderCronJob", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "duplicate", type = Accessor.Type.SETTER)
    public void setDuplicate(Boolean value)
    {
        getPersistenceContext().setPropertyValue("duplicate", value);
    }


    @Accessor(qualifier = "original", type = Accessor.Type.SETTER)
    public void setOriginal(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("original", value);
    }


    @Accessor(qualifier = "saved", type = Accessor.Type.SETTER)
    public void setSaved(boolean value)
    {
        getPersistenceContext().setPropertyValue("saved", toObject(value));
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }
}
