package de.hybris.platform.core.model.order;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commerceservices.enums.QuoteNotificationType;
import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.Set;

public class QuoteModel extends AbstractOrderModel
{
    public static final String _TYPECODE = "Quote";
    public static final String _ASSIGNEE2QUOTES = "Assignee2Quotes";
    public static final String VERSION = "version";
    public static final String STATE = "state";
    public static final String CARTREFERENCE = "cartReference";
    public static final String PREVIOUSESTIMATEDTOTAL = "previousEstimatedTotal";
    public static final String ASSIGNEE = "assignee";
    public static final String GENERATEDNOTIFICATIONS = "generatedNotifications";


    public QuoteModel()
    {
    }


    public QuoteModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public QuoteModel(CurrencyModel _currency, Date _date, QuoteState _state, UserModel _user, Integer _version)
    {
        setCurrency(_currency);
        setDate(_date);
        setState(_state);
        setUser(_user);
        setVersion(_version);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public QuoteModel(CurrencyModel _currency, Date _date, ItemModel _owner, QuoteState _state, UserModel _user, Integer _version)
    {
        setCurrency(_currency);
        setDate(_date);
        setOwner(_owner);
        setState(_state);
        setUser(_user);
        setVersion(_version);
    }


    @Accessor(qualifier = "assignee", type = Accessor.Type.GETTER)
    public UserModel getAssignee()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("assignee");
    }


    @Accessor(qualifier = "cartReference", type = Accessor.Type.GETTER)
    public CartModel getCartReference()
    {
        return (CartModel)getPersistenceContext().getPropertyValue("cartReference");
    }


    @Accessor(qualifier = "generatedNotifications", type = Accessor.Type.GETTER)
    public Set<QuoteNotificationType> getGeneratedNotifications()
    {
        return (Set<QuoteNotificationType>)getPersistenceContext().getPropertyValue("generatedNotifications");
    }


    @Accessor(qualifier = "previousEstimatedTotal", type = Accessor.Type.GETTER)
    public Double getPreviousEstimatedTotal()
    {
        return (Double)getPersistenceContext().getPropertyValue("previousEstimatedTotal");
    }


    @Accessor(qualifier = "state", type = Accessor.Type.GETTER)
    public QuoteState getState()
    {
        return (QuoteState)getPersistenceContext().getPropertyValue("state");
    }


    @Accessor(qualifier = "version", type = Accessor.Type.GETTER)
    public Integer getVersion()
    {
        return (Integer)getPersistenceContext().getPropertyValue("version");
    }


    @Accessor(qualifier = "assignee", type = Accessor.Type.SETTER)
    public void setAssignee(UserModel value)
    {
        getPersistenceContext().setPropertyValue("assignee", value);
    }


    @Accessor(qualifier = "cartReference", type = Accessor.Type.SETTER)
    public void setCartReference(CartModel value)
    {
        getPersistenceContext().setPropertyValue("cartReference", value);
    }


    @Accessor(qualifier = "generatedNotifications", type = Accessor.Type.SETTER)
    public void setGeneratedNotifications(Set<QuoteNotificationType> value)
    {
        getPersistenceContext().setPropertyValue("generatedNotifications", value);
    }


    @Accessor(qualifier = "previousEstimatedTotal", type = Accessor.Type.SETTER)
    public void setPreviousEstimatedTotal(Double value)
    {
        getPersistenceContext().setPropertyValue("previousEstimatedTotal", value);
    }


    @Accessor(qualifier = "state", type = Accessor.Type.SETTER)
    public void setState(QuoteState value)
    {
        getPersistenceContext().setPropertyValue("state", value);
    }


    @Accessor(qualifier = "version", type = Accessor.Type.SETTER)
    public void setVersion(Integer value)
    {
        getPersistenceContext().setPropertyValue("version", value);
    }
}
