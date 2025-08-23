package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAgreement extends GenericItem
{
    public static final String ID = "id";
    public static final String STARTDATE = "startdate";
    public static final String ENDDATE = "enddate";
    public static final String CATALOG = "Catalog";
    public static final String BUYER = "buyer";
    public static final String SUPPLIER = "supplier";
    public static final String BUYERCONTACT = "buyerContact";
    public static final String SUPPLIERCONTACT = "supplierContact";
    public static final String CURRENCY = "currency";
    public static final String CATALOGVERSION = "catalogVersion";
    protected static final BidirectionalOneToManyHandler<GeneratedAgreement> CATALOGVERSIONHANDLER = new BidirectionalOneToManyHandler(GeneratedCatalogConstants.TC.AGREEMENT, false, "catalogVersion", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("id", Item.AttributeMode.INITIAL);
        tmp.put("startdate", Item.AttributeMode.INITIAL);
        tmp.put("enddate", Item.AttributeMode.INITIAL);
        tmp.put("Catalog", Item.AttributeMode.INITIAL);
        tmp.put("buyer", Item.AttributeMode.INITIAL);
        tmp.put("supplier", Item.AttributeMode.INITIAL);
        tmp.put("buyerContact", Item.AttributeMode.INITIAL);
        tmp.put("supplierContact", Item.AttributeMode.INITIAL);
        tmp.put("currency", Item.AttributeMode.INITIAL);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Company getBuyer(SessionContext ctx)
    {
        return (Company)getProperty(ctx, "buyer");
    }


    public Company getBuyer()
    {
        return getBuyer(getSession().getSessionContext());
    }


    public void setBuyer(SessionContext ctx, Company value)
    {
        setProperty(ctx, "buyer", value);
    }


    public void setBuyer(Company value)
    {
        setBuyer(getSession().getSessionContext(), value);
    }


    public User getBuyerContact(SessionContext ctx)
    {
        return (User)getProperty(ctx, "buyerContact");
    }


    public User getBuyerContact()
    {
        return getBuyerContact(getSession().getSessionContext());
    }


    public void setBuyerContact(SessionContext ctx, User value)
    {
        setProperty(ctx, "buyerContact", value);
    }


    public void setBuyerContact(User value)
    {
        setBuyerContact(getSession().getSessionContext(), value);
    }


    Catalog getCatalog(SessionContext ctx)
    {
        return (Catalog)getProperty(ctx, "Catalog");
    }


    Catalog getCatalog()
    {
        return getCatalog(getSession().getSessionContext());
    }


    void setCatalog(SessionContext ctx, Catalog value)
    {
        setProperty(ctx, "Catalog", value);
    }


    void setCatalog(Catalog value)
    {
        setCatalog(getSession().getSessionContext(), value);
    }


    public CatalogVersion getCatalogVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "catalogVersion");
    }


    public CatalogVersion getCatalogVersion()
    {
        return getCatalogVersion(getSession().getSessionContext());
    }


    public void setCatalogVersion(SessionContext ctx, CatalogVersion value)
    {
        CATALOGVERSIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setCatalogVersion(CatalogVersion value)
    {
        setCatalogVersion(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        CATALOGVERSIONHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Currency getCurrency(SessionContext ctx)
    {
        return (Currency)getProperty(ctx, "currency");
    }


    public Currency getCurrency()
    {
        return getCurrency(getSession().getSessionContext());
    }


    public void setCurrency(SessionContext ctx, Currency value)
    {
        setProperty(ctx, "currency", value);
    }


    public void setCurrency(Currency value)
    {
        setCurrency(getSession().getSessionContext(), value);
    }


    public Date getEnddate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "enddate");
    }


    public Date getEnddate()
    {
        return getEnddate(getSession().getSessionContext());
    }


    public void setEnddate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "enddate", value);
    }


    public void setEnddate(Date value)
    {
        setEnddate(getSession().getSessionContext(), value);
    }


    public String getId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "id");
    }


    public String getId()
    {
        return getId(getSession().getSessionContext());
    }


    public void setId(SessionContext ctx, String value)
    {
        setProperty(ctx, "id", value);
    }


    public void setId(String value)
    {
        setId(getSession().getSessionContext(), value);
    }


    public Date getStartdate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "startdate");
    }


    public Date getStartdate()
    {
        return getStartdate(getSession().getSessionContext());
    }


    public void setStartdate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "startdate", value);
    }


    public void setStartdate(Date value)
    {
        setStartdate(getSession().getSessionContext(), value);
    }


    public Company getSupplier(SessionContext ctx)
    {
        return (Company)getProperty(ctx, "supplier");
    }


    public Company getSupplier()
    {
        return getSupplier(getSession().getSessionContext());
    }


    public void setSupplier(SessionContext ctx, Company value)
    {
        setProperty(ctx, "supplier", value);
    }


    public void setSupplier(Company value)
    {
        setSupplier(getSession().getSessionContext(), value);
    }


    public User getSupplierContact(SessionContext ctx)
    {
        return (User)getProperty(ctx, "supplierContact");
    }


    public User getSupplierContact()
    {
        return getSupplierContact(getSession().getSessionContext());
    }


    public void setSupplierContact(SessionContext ctx, User value)
    {
        setProperty(ctx, "supplierContact", value);
    }


    public void setSupplierContact(User value)
    {
        setSupplierContact(getSession().getSessionContext(), value);
    }
}
