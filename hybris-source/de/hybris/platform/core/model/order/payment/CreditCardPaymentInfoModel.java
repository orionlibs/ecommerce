package de.hybris.platform.core.model.order.payment;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CreditCardPaymentInfoModel extends PaymentInfoModel
{
    public static final String _TYPECODE = "CreditCardPaymentInfo";
    public static final String CCOWNER = "ccOwner";
    public static final String NUMBER = "number";
    public static final String TYPE = "type";
    public static final String VALIDTOMONTH = "validToMonth";
    public static final String VALIDTOYEAR = "validToYear";
    public static final String VALIDFROMMONTH = "validFromMonth";
    public static final String VALIDFROMYEAR = "validFromYear";
    public static final String SUBSCRIPTIONID = "subscriptionId";
    public static final String ISSUENUMBER = "issueNumber";
    public static final String SUBSCRIPTIONVALIDATED = "subscriptionValidated";


    public CreditCardPaymentInfoModel()
    {
    }


    public CreditCardPaymentInfoModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CreditCardPaymentInfoModel(String _ccOwner, String _code, String _number, CreditCardType _type, UserModel _user, String _validToMonth, String _validToYear)
    {
        setCcOwner(_ccOwner);
        setCode(_code);
        setNumber(_number);
        setType(_type);
        setUser(_user);
        setValidToMonth(_validToMonth);
        setValidToYear(_validToYear);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CreditCardPaymentInfoModel(String _ccOwner, String _code, String _number, ItemModel _original, ItemModel _owner, CreditCardType _type, UserModel _user, String _validToMonth, String _validToYear)
    {
        setCcOwner(_ccOwner);
        setCode(_code);
        setNumber(_number);
        setOriginal(_original);
        setOwner(_owner);
        setType(_type);
        setUser(_user);
        setValidToMonth(_validToMonth);
        setValidToYear(_validToYear);
    }


    @Accessor(qualifier = "ccOwner", type = Accessor.Type.GETTER)
    public String getCcOwner()
    {
        return (String)getPersistenceContext().getPropertyValue("ccOwner");
    }


    @Accessor(qualifier = "issueNumber", type = Accessor.Type.GETTER)
    public Integer getIssueNumber()
    {
        return (Integer)getPersistenceContext().getPropertyValue("issueNumber");
    }


    @Accessor(qualifier = "number", type = Accessor.Type.GETTER)
    public String getNumber()
    {
        return (String)getPersistenceContext().getPropertyValue("number");
    }


    @Accessor(qualifier = "subscriptionId", type = Accessor.Type.GETTER)
    public String getSubscriptionId()
    {
        return (String)getPersistenceContext().getPropertyValue("subscriptionId");
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public CreditCardType getType()
    {
        return (CreditCardType)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "validFromMonth", type = Accessor.Type.GETTER)
    public String getValidFromMonth()
    {
        return (String)getPersistenceContext().getPropertyValue("validFromMonth");
    }


    @Accessor(qualifier = "validFromYear", type = Accessor.Type.GETTER)
    public String getValidFromYear()
    {
        return (String)getPersistenceContext().getPropertyValue("validFromYear");
    }


    @Accessor(qualifier = "validToMonth", type = Accessor.Type.GETTER)
    public String getValidToMonth()
    {
        return (String)getPersistenceContext().getPropertyValue("validToMonth");
    }


    @Accessor(qualifier = "validToYear", type = Accessor.Type.GETTER)
    public String getValidToYear()
    {
        return (String)getPersistenceContext().getPropertyValue("validToYear");
    }


    @Accessor(qualifier = "subscriptionValidated", type = Accessor.Type.GETTER)
    public boolean isSubscriptionValidated()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("subscriptionValidated"));
    }


    @Accessor(qualifier = "ccOwner", type = Accessor.Type.SETTER)
    public void setCcOwner(String value)
    {
        getPersistenceContext().setPropertyValue("ccOwner", value);
    }


    @Accessor(qualifier = "issueNumber", type = Accessor.Type.SETTER)
    public void setIssueNumber(Integer value)
    {
        getPersistenceContext().setPropertyValue("issueNumber", value);
    }


    @Accessor(qualifier = "number", type = Accessor.Type.SETTER)
    public void setNumber(String value)
    {
        getPersistenceContext().setPropertyValue("number", value);
    }


    @Accessor(qualifier = "subscriptionId", type = Accessor.Type.SETTER)
    public void setSubscriptionId(String value)
    {
        getPersistenceContext().setPropertyValue("subscriptionId", value);
    }


    @Accessor(qualifier = "subscriptionValidated", type = Accessor.Type.SETTER)
    public void setSubscriptionValidated(boolean value)
    {
        getPersistenceContext().setPropertyValue("subscriptionValidated", toObject(value));
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(CreditCardType value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }


    @Accessor(qualifier = "validFromMonth", type = Accessor.Type.SETTER)
    public void setValidFromMonth(String value)
    {
        getPersistenceContext().setPropertyValue("validFromMonth", value);
    }


    @Accessor(qualifier = "validFromYear", type = Accessor.Type.SETTER)
    public void setValidFromYear(String value)
    {
        getPersistenceContext().setPropertyValue("validFromYear", value);
    }


    @Accessor(qualifier = "validToMonth", type = Accessor.Type.SETTER)
    public void setValidToMonth(String value)
    {
        getPersistenceContext().setPropertyValue("validToMonth", value);
    }


    @Accessor(qualifier = "validToYear", type = Accessor.Type.SETTER)
    public void setValidToYear(String value)
    {
        getPersistenceContext().setPropertyValue("validToYear", value);
    }
}
