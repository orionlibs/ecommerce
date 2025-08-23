package de.hybris.platform.europe1.jalo;

import de.hybris.platform.europe1.constants.GeneratedEurope1Constants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.AbstractPriceFactory;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedEurope1PriceFactory extends AbstractPriceFactory
{
    protected static final OneToManyHandler<DiscountRow> PRODUCT2OWNEUROPE1DISCOUNTSOWNEUROPE1DISCOUNTSHANDLER = new OneToManyHandler(GeneratedEurope1Constants.TC.DISCOUNTROW, true, "product", null, false, true, 0);
    protected static final OneToManyHandler<PriceRow> PRODUCT2OWNEUROPE1PRICESOWNEUROPE1PRICESHANDLER = new OneToManyHandler(GeneratedEurope1Constants.TC.PRICEROW, true, "product", null, false, true, 0);
    protected static final OneToManyHandler<TaxRow> PRODUCT2OWNEUROPE1TAXESOWNEUROPE1TAXESHANDLER = new OneToManyHandler(GeneratedEurope1Constants.TC.TAXROW, true, "product", null, false, true, 0);
    protected static final OneToManyHandler<GlobalDiscountRow> USER2OWNEUROPE1DISCOUNTSOWNEUROPE1DISCOUNTSHANDLER = new OneToManyHandler(GeneratedEurope1Constants.TC.GLOBALDISCOUNTROW, true, "user", null, false, true, 0);
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("Europe1PriceFactory_PPG", Item.AttributeMode.INITIAL);
        tmp.put("Europe1PriceFactory_PTG", Item.AttributeMode.INITIAL);
        tmp.put("Europe1PriceFactory_PDG", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.product.Product", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("Europe1PriceFactory_UDG", Item.AttributeMode.INITIAL);
        tmp.put("Europe1PriceFactory_UPG", Item.AttributeMode.INITIAL);
        tmp.put("Europe1PriceFactory_UTG", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.user.User", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("userDiscountGroup", Item.AttributeMode.INITIAL);
        tmp.put("userPriceGroup", Item.AttributeMode.INITIAL);
        tmp.put("userTaxGroup", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.user.UserGroup", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("Europe1PriceFactory_UDG", Item.AttributeMode.INITIAL);
        tmp.put("Europe1PriceFactory_UPG", Item.AttributeMode.INITIAL);
        tmp.put("Europe1PriceFactory_UTG", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.order.AbstractOrder", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("Europe1PriceFactory_PPG", Item.AttributeMode.INITIAL);
        tmp.put("Europe1PriceFactory_PTG", Item.AttributeMode.INITIAL);
        tmp.put("Europe1PriceFactory_PDG", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.order.AbstractOrderEntry", Collections.unmodifiableMap(tmp));
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public DiscountRow createDiscountRow(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedEurope1Constants.TC.DISCOUNTROW);
            return (DiscountRow)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating DiscountRow : " + e.getMessage(), 0);
        }
    }


    public DiscountRow createDiscountRow(Map attributeValues)
    {
        return createDiscountRow(getSession().getSessionContext(), attributeValues);
    }


    public GlobalDiscountRow createGlobalDiscountRow(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedEurope1Constants.TC.GLOBALDISCOUNTROW);
            return (GlobalDiscountRow)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating GlobalDiscountRow : " + e.getMessage(), 0);
        }
    }


    public GlobalDiscountRow createGlobalDiscountRow(Map attributeValues)
    {
        return createGlobalDiscountRow(getSession().getSessionContext(), attributeValues);
    }


    public PriceRow createPriceRow(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedEurope1Constants.TC.PRICEROW);
            return (PriceRow)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating PriceRow : " + e.getMessage(), 0);
        }
    }


    public PriceRow createPriceRow(Map attributeValues)
    {
        return createPriceRow(getSession().getSessionContext(), attributeValues);
    }


    public TaxRow createTaxRow(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedEurope1Constants.TC.TAXROW);
            return (TaxRow)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating TaxRow : " + e.getMessage(), 0);
        }
    }


    public TaxRow createTaxRow(Map attributeValues)
    {
        return createTaxRow(getSession().getSessionContext(), attributeValues);
    }


    public EnumerationValue getEurope1PriceFactory_PDG(SessionContext ctx, Product item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedEurope1Constants.Attributes.Product.EUROPE1PRICEFACTORY_PDG);
    }


    public EnumerationValue getEurope1PriceFactory_PDG(Product item)
    {
        return getEurope1PriceFactory_PDG(getSession().getSessionContext(), item);
    }


    public void setEurope1PriceFactory_PDG(SessionContext ctx, Product item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedEurope1Constants.Attributes.Product.EUROPE1PRICEFACTORY_PDG, value);
    }


    public void setEurope1PriceFactory_PDG(Product item, EnumerationValue value)
    {
        setEurope1PriceFactory_PDG(getSession().getSessionContext(), item, value);
    }


    public EnumerationValue getEurope1PriceFactory_PDG(SessionContext ctx, AbstractOrderEntry item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedEurope1Constants.Attributes.AbstractOrderEntry.EUROPE1PRICEFACTORY_PDG);
    }


    public EnumerationValue getEurope1PriceFactory_PDG(AbstractOrderEntry item)
    {
        return getEurope1PriceFactory_PDG(getSession().getSessionContext(), item);
    }


    public void setEurope1PriceFactory_PDG(SessionContext ctx, AbstractOrderEntry item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedEurope1Constants.Attributes.AbstractOrderEntry.EUROPE1PRICEFACTORY_PDG, value);
    }


    public void setEurope1PriceFactory_PDG(AbstractOrderEntry item, EnumerationValue value)
    {
        setEurope1PriceFactory_PDG(getSession().getSessionContext(), item, value);
    }


    public EnumerationValue getEurope1PriceFactory_PPG(SessionContext ctx, Product item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedEurope1Constants.Attributes.Product.EUROPE1PRICEFACTORY_PPG);
    }


    public EnumerationValue getEurope1PriceFactory_PPG(Product item)
    {
        return getEurope1PriceFactory_PPG(getSession().getSessionContext(), item);
    }


    public void setEurope1PriceFactory_PPG(SessionContext ctx, Product item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedEurope1Constants.Attributes.Product.EUROPE1PRICEFACTORY_PPG, value);
    }


    public void setEurope1PriceFactory_PPG(Product item, EnumerationValue value)
    {
        setEurope1PriceFactory_PPG(getSession().getSessionContext(), item, value);
    }


    public EnumerationValue getEurope1PriceFactory_PPG(SessionContext ctx, AbstractOrderEntry item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedEurope1Constants.Attributes.AbstractOrderEntry.EUROPE1PRICEFACTORY_PPG);
    }


    public EnumerationValue getEurope1PriceFactory_PPG(AbstractOrderEntry item)
    {
        return getEurope1PriceFactory_PPG(getSession().getSessionContext(), item);
    }


    public void setEurope1PriceFactory_PPG(SessionContext ctx, AbstractOrderEntry item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedEurope1Constants.Attributes.AbstractOrderEntry.EUROPE1PRICEFACTORY_PPG, value);
    }


    public void setEurope1PriceFactory_PPG(AbstractOrderEntry item, EnumerationValue value)
    {
        setEurope1PriceFactory_PPG(getSession().getSessionContext(), item, value);
    }


    public EnumerationValue getEurope1PriceFactory_PTG(SessionContext ctx, Product item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedEurope1Constants.Attributes.Product.EUROPE1PRICEFACTORY_PTG);
    }


    public EnumerationValue getEurope1PriceFactory_PTG(Product item)
    {
        return getEurope1PriceFactory_PTG(getSession().getSessionContext(), item);
    }


    public void setEurope1PriceFactory_PTG(SessionContext ctx, Product item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedEurope1Constants.Attributes.Product.EUROPE1PRICEFACTORY_PTG, value);
    }


    public void setEurope1PriceFactory_PTG(Product item, EnumerationValue value)
    {
        setEurope1PriceFactory_PTG(getSession().getSessionContext(), item, value);
    }


    public EnumerationValue getEurope1PriceFactory_PTG(SessionContext ctx, AbstractOrderEntry item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedEurope1Constants.Attributes.AbstractOrderEntry.EUROPE1PRICEFACTORY_PTG);
    }


    public EnumerationValue getEurope1PriceFactory_PTG(AbstractOrderEntry item)
    {
        return getEurope1PriceFactory_PTG(getSession().getSessionContext(), item);
    }


    public void setEurope1PriceFactory_PTG(SessionContext ctx, AbstractOrderEntry item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedEurope1Constants.Attributes.AbstractOrderEntry.EUROPE1PRICEFACTORY_PTG, value);
    }


    public void setEurope1PriceFactory_PTG(AbstractOrderEntry item, EnumerationValue value)
    {
        setEurope1PriceFactory_PTG(getSession().getSessionContext(), item, value);
    }


    public EnumerationValue getEurope1PriceFactory_UDG(SessionContext ctx, User item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedEurope1Constants.Attributes.User.EUROPE1PRICEFACTORY_UDG);
    }


    public EnumerationValue getEurope1PriceFactory_UDG(User item)
    {
        return getEurope1PriceFactory_UDG(getSession().getSessionContext(), item);
    }


    public void setEurope1PriceFactory_UDG(SessionContext ctx, User item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedEurope1Constants.Attributes.User.EUROPE1PRICEFACTORY_UDG, value);
    }


    public void setEurope1PriceFactory_UDG(User item, EnumerationValue value)
    {
        setEurope1PriceFactory_UDG(getSession().getSessionContext(), item, value);
    }


    public EnumerationValue getEurope1PriceFactory_UDG(SessionContext ctx, AbstractOrder item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedEurope1Constants.Attributes.AbstractOrder.EUROPE1PRICEFACTORY_UDG);
    }


    public EnumerationValue getEurope1PriceFactory_UDG(AbstractOrder item)
    {
        return getEurope1PriceFactory_UDG(getSession().getSessionContext(), item);
    }


    public void setEurope1PriceFactory_UDG(SessionContext ctx, AbstractOrder item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedEurope1Constants.Attributes.AbstractOrder.EUROPE1PRICEFACTORY_UDG, value);
    }


    public void setEurope1PriceFactory_UDG(AbstractOrder item, EnumerationValue value)
    {
        setEurope1PriceFactory_UDG(getSession().getSessionContext(), item, value);
    }


    public EnumerationValue getEurope1PriceFactory_UPG(SessionContext ctx, User item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedEurope1Constants.Attributes.User.EUROPE1PRICEFACTORY_UPG);
    }


    public EnumerationValue getEurope1PriceFactory_UPG(User item)
    {
        return getEurope1PriceFactory_UPG(getSession().getSessionContext(), item);
    }


    public void setEurope1PriceFactory_UPG(SessionContext ctx, User item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedEurope1Constants.Attributes.User.EUROPE1PRICEFACTORY_UPG, value);
    }


    public void setEurope1PriceFactory_UPG(User item, EnumerationValue value)
    {
        setEurope1PriceFactory_UPG(getSession().getSessionContext(), item, value);
    }


    public EnumerationValue getEurope1PriceFactory_UPG(SessionContext ctx, AbstractOrder item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedEurope1Constants.Attributes.AbstractOrder.EUROPE1PRICEFACTORY_UPG);
    }


    public EnumerationValue getEurope1PriceFactory_UPG(AbstractOrder item)
    {
        return getEurope1PriceFactory_UPG(getSession().getSessionContext(), item);
    }


    public void setEurope1PriceFactory_UPG(SessionContext ctx, AbstractOrder item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedEurope1Constants.Attributes.AbstractOrder.EUROPE1PRICEFACTORY_UPG, value);
    }


    public void setEurope1PriceFactory_UPG(AbstractOrder item, EnumerationValue value)
    {
        setEurope1PriceFactory_UPG(getSession().getSessionContext(), item, value);
    }


    public EnumerationValue getEurope1PriceFactory_UTG(SessionContext ctx, User item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedEurope1Constants.Attributes.User.EUROPE1PRICEFACTORY_UTG);
    }


    public EnumerationValue getEurope1PriceFactory_UTG(User item)
    {
        return getEurope1PriceFactory_UTG(getSession().getSessionContext(), item);
    }


    public void setEurope1PriceFactory_UTG(SessionContext ctx, User item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedEurope1Constants.Attributes.User.EUROPE1PRICEFACTORY_UTG, value);
    }


    public void setEurope1PriceFactory_UTG(User item, EnumerationValue value)
    {
        setEurope1PriceFactory_UTG(getSession().getSessionContext(), item, value);
    }


    public EnumerationValue getEurope1PriceFactory_UTG(SessionContext ctx, AbstractOrder item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedEurope1Constants.Attributes.AbstractOrder.EUROPE1PRICEFACTORY_UTG);
    }


    public EnumerationValue getEurope1PriceFactory_UTG(AbstractOrder item)
    {
        return getEurope1PriceFactory_UTG(getSession().getSessionContext(), item);
    }


    public void setEurope1PriceFactory_UTG(SessionContext ctx, AbstractOrder item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedEurope1Constants.Attributes.AbstractOrder.EUROPE1PRICEFACTORY_UTG, value);
    }


    public void setEurope1PriceFactory_UTG(AbstractOrder item, EnumerationValue value)
    {
        setEurope1PriceFactory_UTG(getSession().getSessionContext(), item, value);
    }


    public String getName()
    {
        return "europe1";
    }


    public Collection<DiscountRow> getOwnEurope1Discounts(SessionContext ctx, Product item)
    {
        return PRODUCT2OWNEUROPE1DISCOUNTSOWNEUROPE1DISCOUNTSHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<DiscountRow> getOwnEurope1Discounts(Product item)
    {
        return getOwnEurope1Discounts(getSession().getSessionContext(), item);
    }


    public void setOwnEurope1Discounts(SessionContext ctx, Product item, Collection<DiscountRow> value)
    {
        PRODUCT2OWNEUROPE1DISCOUNTSOWNEUROPE1DISCOUNTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setOwnEurope1Discounts(Product item, Collection<DiscountRow> value)
    {
        setOwnEurope1Discounts(getSession().getSessionContext(), item, value);
    }


    public void addToOwnEurope1Discounts(SessionContext ctx, Product item, DiscountRow value)
    {
        PRODUCT2OWNEUROPE1DISCOUNTSOWNEUROPE1DISCOUNTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToOwnEurope1Discounts(Product item, DiscountRow value)
    {
        addToOwnEurope1Discounts(getSession().getSessionContext(), item, value);
    }


    public void removeFromOwnEurope1Discounts(SessionContext ctx, Product item, DiscountRow value)
    {
        PRODUCT2OWNEUROPE1DISCOUNTSOWNEUROPE1DISCOUNTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromOwnEurope1Discounts(Product item, DiscountRow value)
    {
        removeFromOwnEurope1Discounts(getSession().getSessionContext(), item, value);
    }


    public Collection<GlobalDiscountRow> getOwnEurope1Discounts(SessionContext ctx, User item)
    {
        return USER2OWNEUROPE1DISCOUNTSOWNEUROPE1DISCOUNTSHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<GlobalDiscountRow> getOwnEurope1Discounts(User item)
    {
        return getOwnEurope1Discounts(getSession().getSessionContext(), item);
    }


    public void setOwnEurope1Discounts(SessionContext ctx, User item, Collection<GlobalDiscountRow> value)
    {
        USER2OWNEUROPE1DISCOUNTSOWNEUROPE1DISCOUNTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setOwnEurope1Discounts(User item, Collection<GlobalDiscountRow> value)
    {
        setOwnEurope1Discounts(getSession().getSessionContext(), item, value);
    }


    public void addToOwnEurope1Discounts(SessionContext ctx, User item, GlobalDiscountRow value)
    {
        USER2OWNEUROPE1DISCOUNTSOWNEUROPE1DISCOUNTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToOwnEurope1Discounts(User item, GlobalDiscountRow value)
    {
        addToOwnEurope1Discounts(getSession().getSessionContext(), item, value);
    }


    public void removeFromOwnEurope1Discounts(SessionContext ctx, User item, GlobalDiscountRow value)
    {
        USER2OWNEUROPE1DISCOUNTSOWNEUROPE1DISCOUNTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromOwnEurope1Discounts(User item, GlobalDiscountRow value)
    {
        removeFromOwnEurope1Discounts(getSession().getSessionContext(), item, value);
    }


    public Collection<PriceRow> getOwnEurope1Prices(SessionContext ctx, Product item)
    {
        return PRODUCT2OWNEUROPE1PRICESOWNEUROPE1PRICESHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<PriceRow> getOwnEurope1Prices(Product item)
    {
        return getOwnEurope1Prices(getSession().getSessionContext(), item);
    }


    public void setOwnEurope1Prices(SessionContext ctx, Product item, Collection<PriceRow> value)
    {
        PRODUCT2OWNEUROPE1PRICESOWNEUROPE1PRICESHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setOwnEurope1Prices(Product item, Collection<PriceRow> value)
    {
        setOwnEurope1Prices(getSession().getSessionContext(), item, value);
    }


    public void addToOwnEurope1Prices(SessionContext ctx, Product item, PriceRow value)
    {
        PRODUCT2OWNEUROPE1PRICESOWNEUROPE1PRICESHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToOwnEurope1Prices(Product item, PriceRow value)
    {
        addToOwnEurope1Prices(getSession().getSessionContext(), item, value);
    }


    public void removeFromOwnEurope1Prices(SessionContext ctx, Product item, PriceRow value)
    {
        PRODUCT2OWNEUROPE1PRICESOWNEUROPE1PRICESHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromOwnEurope1Prices(Product item, PriceRow value)
    {
        removeFromOwnEurope1Prices(getSession().getSessionContext(), item, value);
    }


    public Collection<TaxRow> getOwnEurope1Taxes(SessionContext ctx, Product item)
    {
        return PRODUCT2OWNEUROPE1TAXESOWNEUROPE1TAXESHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<TaxRow> getOwnEurope1Taxes(Product item)
    {
        return getOwnEurope1Taxes(getSession().getSessionContext(), item);
    }


    public void setOwnEurope1Taxes(SessionContext ctx, Product item, Collection<TaxRow> value)
    {
        PRODUCT2OWNEUROPE1TAXESOWNEUROPE1TAXESHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setOwnEurope1Taxes(Product item, Collection<TaxRow> value)
    {
        setOwnEurope1Taxes(getSession().getSessionContext(), item, value);
    }


    public void addToOwnEurope1Taxes(SessionContext ctx, Product item, TaxRow value)
    {
        PRODUCT2OWNEUROPE1TAXESOWNEUROPE1TAXESHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToOwnEurope1Taxes(Product item, TaxRow value)
    {
        addToOwnEurope1Taxes(getSession().getSessionContext(), item, value);
    }


    public void removeFromOwnEurope1Taxes(SessionContext ctx, Product item, TaxRow value)
    {
        PRODUCT2OWNEUROPE1TAXESOWNEUROPE1TAXESHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromOwnEurope1Taxes(Product item, TaxRow value)
    {
        removeFromOwnEurope1Taxes(getSession().getSessionContext(), item, value);
    }


    public EnumerationValue getUserDiscountGroup(SessionContext ctx, UserGroup item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedEurope1Constants.Attributes.UserGroup.USERDISCOUNTGROUP);
    }


    public EnumerationValue getUserDiscountGroup(UserGroup item)
    {
        return getUserDiscountGroup(getSession().getSessionContext(), item);
    }


    public void setUserDiscountGroup(SessionContext ctx, UserGroup item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedEurope1Constants.Attributes.UserGroup.USERDISCOUNTGROUP, value);
    }


    public void setUserDiscountGroup(UserGroup item, EnumerationValue value)
    {
        setUserDiscountGroup(getSession().getSessionContext(), item, value);
    }


    public EnumerationValue getUserPriceGroup(SessionContext ctx, UserGroup item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedEurope1Constants.Attributes.UserGroup.USERPRICEGROUP);
    }


    public EnumerationValue getUserPriceGroup(UserGroup item)
    {
        return getUserPriceGroup(getSession().getSessionContext(), item);
    }


    public void setUserPriceGroup(SessionContext ctx, UserGroup item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedEurope1Constants.Attributes.UserGroup.USERPRICEGROUP, value);
    }


    public void setUserPriceGroup(UserGroup item, EnumerationValue value)
    {
        setUserPriceGroup(getSession().getSessionContext(), item, value);
    }


    public EnumerationValue getUserTaxGroup(SessionContext ctx, UserGroup item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedEurope1Constants.Attributes.UserGroup.USERTAXGROUP);
    }


    public EnumerationValue getUserTaxGroup(UserGroup item)
    {
        return getUserTaxGroup(getSession().getSessionContext(), item);
    }


    public void setUserTaxGroup(SessionContext ctx, UserGroup item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedEurope1Constants.Attributes.UserGroup.USERTAXGROUP, value);
    }


    public void setUserTaxGroup(UserGroup item, EnumerationValue value)
    {
        setUserTaxGroup(getSession().getSessionContext(), item, value);
    }
}
