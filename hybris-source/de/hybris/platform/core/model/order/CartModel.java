package de.hybris.platform.core.model.order;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorservices.enums.ImportStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;

public class CartModel extends AbstractOrderModel
{
    public static final String _TYPECODE = "Cart";
    public static final String SESSIONID = "sessionId";
    public static final String CARTTOORDERCRONJOB = "cartToOrderCronJob";
    public static final String SAVETIME = "saveTime";
    public static final String SAVEDBY = "savedBy";
    public static final String QUOTEREFERENCE = "quoteReference";
    public static final String IMPORTSTATUS = "importStatus";


    public CartModel()
    {
    }


    public CartModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CartModel(CurrencyModel _currency, Date _date, UserModel _user)
    {
        setCurrency(_currency);
        setDate(_date);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CartModel(CurrencyModel _currency, Date _date, ItemModel _owner, UserModel _user)
    {
        setCurrency(_currency);
        setDate(_date);
        setOwner(_owner);
        setUser(_user);
    }


    @Accessor(qualifier = "cartToOrderCronJob", type = Accessor.Type.GETTER)
    public Collection<CartToOrderCronJobModel> getCartToOrderCronJob()
    {
        return (Collection<CartToOrderCronJobModel>)getPersistenceContext().getPropertyValue("cartToOrderCronJob");
    }


    @Accessor(qualifier = "importStatus", type = Accessor.Type.GETTER)
    public ImportStatus getImportStatus()
    {
        return (ImportStatus)getPersistenceContext().getPropertyValue("importStatus");
    }


    @Accessor(qualifier = "quoteReference", type = Accessor.Type.GETTER)
    public QuoteModel getQuoteReference()
    {
        return (QuoteModel)getPersistenceContext().getPropertyValue("quoteReference");
    }


    @Accessor(qualifier = "savedBy", type = Accessor.Type.GETTER)
    public UserModel getSavedBy()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("savedBy");
    }


    @Accessor(qualifier = "saveTime", type = Accessor.Type.GETTER)
    public Date getSaveTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("saveTime");
    }


    @Accessor(qualifier = "sessionId", type = Accessor.Type.GETTER)
    public String getSessionId()
    {
        return (String)getPersistenceContext().getPropertyValue("sessionId");
    }


    @Accessor(qualifier = "cartToOrderCronJob", type = Accessor.Type.SETTER)
    public void setCartToOrderCronJob(Collection<CartToOrderCronJobModel> value)
    {
        getPersistenceContext().setPropertyValue("cartToOrderCronJob", value);
    }


    @Accessor(qualifier = "importStatus", type = Accessor.Type.SETTER)
    public void setImportStatus(ImportStatus value)
    {
        getPersistenceContext().setPropertyValue("importStatus", value);
    }


    @Accessor(qualifier = "quoteReference", type = Accessor.Type.SETTER)
    public void setQuoteReference(QuoteModel value)
    {
        getPersistenceContext().setPropertyValue("quoteReference", value);
    }


    @Accessor(qualifier = "savedBy", type = Accessor.Type.SETTER)
    public void setSavedBy(UserModel value)
    {
        getPersistenceContext().setPropertyValue("savedBy", value);
    }


    @Accessor(qualifier = "saveTime", type = Accessor.Type.SETTER)
    public void setSaveTime(Date value)
    {
        getPersistenceContext().setPropertyValue("saveTime", value);
    }


    @Accessor(qualifier = "sessionId", type = Accessor.Type.SETTER)
    public void setSessionId(String value)
    {
        getPersistenceContext().setPropertyValue("sessionId", value);
    }
}
