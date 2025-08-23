package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.b2b.enums.MerchantCheckStatus;
import de.hybris.platform.b2b.enums.MerchantCheckStatusEmail;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class B2BMerchantCheckResultModel extends ItemModel
{
    public static final String _TYPECODE = "B2BMerchantCheckResult";
    public static final String MERCHANTCHECK = "merchantcheck";
    public static final String MERCHANTCHECKTYPECODE = "merchantCheckTypeCode";
    public static final String STATUS = "status";
    public static final String STATUSEMAIL = "statusEmail";
    public static final String NOTE = "note";


    public B2BMerchantCheckResultModel()
    {
    }


    public B2BMerchantCheckResultModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BMerchantCheckResultModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "merchantcheck", type = Accessor.Type.GETTER)
    public B2BMerchantCheckModel getMerchantcheck()
    {
        return (B2BMerchantCheckModel)getPersistenceContext().getPropertyValue("merchantcheck");
    }


    @Accessor(qualifier = "merchantCheckTypeCode", type = Accessor.Type.GETTER)
    public String getMerchantCheckTypeCode()
    {
        return (String)getPersistenceContext().getPropertyValue("merchantCheckTypeCode");
    }


    @Accessor(qualifier = "note", type = Accessor.Type.GETTER)
    public String getNote()
    {
        return getNote(null);
    }


    @Accessor(qualifier = "note", type = Accessor.Type.GETTER)
    public String getNote(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("note", loc);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public MerchantCheckStatus getStatus()
    {
        return (MerchantCheckStatus)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "statusEmail", type = Accessor.Type.GETTER)
    public MerchantCheckStatusEmail getStatusEmail()
    {
        return (MerchantCheckStatusEmail)getPersistenceContext().getPropertyValue("statusEmail");
    }


    @Accessor(qualifier = "merchantcheck", type = Accessor.Type.SETTER)
    public void setMerchantcheck(B2BMerchantCheckModel value)
    {
        getPersistenceContext().setPropertyValue("merchantcheck", value);
    }


    @Accessor(qualifier = "merchantCheckTypeCode", type = Accessor.Type.SETTER)
    public void setMerchantCheckTypeCode(String value)
    {
        getPersistenceContext().setPropertyValue("merchantCheckTypeCode", value);
    }


    @Accessor(qualifier = "note", type = Accessor.Type.SETTER)
    public void setNote(String value)
    {
        setNote(value, null);
    }


    @Accessor(qualifier = "note", type = Accessor.Type.SETTER)
    public void setNote(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("note", loc, value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(MerchantCheckStatus value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }


    @Accessor(qualifier = "statusEmail", type = Accessor.Type.SETTER)
    public void setStatusEmail(MerchantCheckStatusEmail value)
    {
        getPersistenceContext().setPropertyValue("statusEmail", value);
    }
}
