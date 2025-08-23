package de.hybris.platform.jalo.order;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.WebSessionFunctions;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpSession;

public class Cart extends GeneratedCart
{
    public static final String START_DATE = "Cart.startDate";
    public static final String END_DATE = "Cart.endDate";


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(!Cart.class.isAssignableFrom(type.getJaloClass()))
        {
            throw new JaloInvalidParameterException("Cart type " + type + " is incompatible to default Cart class", 0);
        }
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("user", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("currency", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("date", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("net", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing parameters " + missing + " to create a cart ", 0);
        }
        User user = (User)allAttributes.get("user");
        String code = (String)allAttributes.get("code");
        if(code != null)
        {
            Collection check = searchCarts(code, user);
            if(!check.isEmpty())
            {
                throw new ConsistencyCheckException(null, "duplicate cart code '" + code + "'; there is already such a cart for user " + user, 0);
            }
        }
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("user", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("currency", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("date", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("net", Item.AttributeMode.INITIAL);
        Cart result = (Cart)super.createItem(ctx, type, allAttributes);
        HttpSession httpSession = WebSessionFunctions.getCurrentHttpSession();
        if(httpSession != null)
        {
            result.setSessionId(httpSession.getId());
        }
        return (Item)result;
    }


    private Collection searchCarts(String code, User user)
    {
        String query = "GET {" + GeneratedCoreConstants.TC.ABSTRACTORDER + "} WHERE {user}=?user AND {code}=?code";
        Map<String, Object> params = new HashMap<>();
        params.put("user", user);
        params.put("code", code);
        return FlexibleSearch.getInstance().search(query, params, AbstractOrder.class).getResult();
    }


    @ForceJALO(reason = "abstract method implementation")
    protected String getAbstractOrderEntryTypeCode()
    {
        return OrderManager.getInstance().getCartEntryTypeCode();
    }


    public CartEntry createNewEntry(SessionContext ctx, ComposedType type, Product prod, long qtd, Unit unit, int pos)
    {
        if(type != null && !CartEntry.class.isAssignableFrom(type.getJaloClass()))
        {
            throw new JaloInvalidParameterException("type is not assignable from CartEntry", 0);
        }
        try
        {
            Item.ItemAttributeMap params = new Item.ItemAttributeMap();
            params.put("order", this);
            params.put("entryNumber", Integer.valueOf(pos));
            params.put("quantity", Long.valueOf(qtd));
            params.put("unit", unit);
            params.put("product", prod);
            if(type == null)
            {
                type = TypeManager.getInstance().getComposedType(CartEntry.class);
            }
            return (CartEntry)type.newInstance(getSession().getSessionContext(), (Map)params);
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }
}
