package de.hybris.platform.core.model.order.payment;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class DebitPaymentInfoModel extends PaymentInfoModel
{
    public static final String _TYPECODE = "DebitPaymentInfo";
    public static final String BANKIDNUMBER = "bankIDNumber";
    public static final String BANK = "bank";
    public static final String ACCOUNTNUMBER = "accountNumber";
    public static final String BAOWNER = "baOwner";


    public DebitPaymentInfoModel()
    {
    }


    public DebitPaymentInfoModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DebitPaymentInfoModel(String _accountNumber, String _baOwner, String _bank, String _bankIDNumber, String _code, UserModel _user)
    {
        setAccountNumber(_accountNumber);
        setBaOwner(_baOwner);
        setBank(_bank);
        setBankIDNumber(_bankIDNumber);
        setCode(_code);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DebitPaymentInfoModel(String _accountNumber, String _baOwner, String _bank, String _bankIDNumber, String _code, ItemModel _original, ItemModel _owner, UserModel _user)
    {
        setAccountNumber(_accountNumber);
        setBaOwner(_baOwner);
        setBank(_bank);
        setBankIDNumber(_bankIDNumber);
        setCode(_code);
        setOriginal(_original);
        setOwner(_owner);
        setUser(_user);
    }


    @Accessor(qualifier = "accountNumber", type = Accessor.Type.GETTER)
    public String getAccountNumber()
    {
        return (String)getPersistenceContext().getPropertyValue("accountNumber");
    }


    @Accessor(qualifier = "bank", type = Accessor.Type.GETTER)
    public String getBank()
    {
        return (String)getPersistenceContext().getPropertyValue("bank");
    }


    @Accessor(qualifier = "bankIDNumber", type = Accessor.Type.GETTER)
    public String getBankIDNumber()
    {
        return (String)getPersistenceContext().getPropertyValue("bankIDNumber");
    }


    @Accessor(qualifier = "baOwner", type = Accessor.Type.GETTER)
    public String getBaOwner()
    {
        return (String)getPersistenceContext().getPropertyValue("baOwner");
    }


    @Accessor(qualifier = "accountNumber", type = Accessor.Type.SETTER)
    public void setAccountNumber(String value)
    {
        getPersistenceContext().setPropertyValue("accountNumber", value);
    }


    @Accessor(qualifier = "bank", type = Accessor.Type.SETTER)
    public void setBank(String value)
    {
        getPersistenceContext().setPropertyValue("bank", value);
    }


    @Accessor(qualifier = "bankIDNumber", type = Accessor.Type.SETTER)
    public void setBankIDNumber(String value)
    {
        getPersistenceContext().setPropertyValue("bankIDNumber", value);
    }


    @Accessor(qualifier = "baOwner", type = Accessor.Type.SETTER)
    public void setBaOwner(String value)
    {
        getPersistenceContext().setPropertyValue("baOwner", value);
    }
}
