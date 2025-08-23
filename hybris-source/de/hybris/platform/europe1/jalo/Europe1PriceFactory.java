package de.hybris.platform.europe1.jalo;

import com.google.common.base.Preconditions;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.ItemSyncTimestamp;
import de.hybris.platform.catalog.jalo.ProductFeature;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.cronjob.jalo.ChangeDescriptor;
import de.hybris.platform.cronjob.jalo.JobLog;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.europe1.channel.strategies.RetrieveChannelStrategy;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.constants.Europe1Tools;
import de.hybris.platform.europe1.constants.GeneratedEurope1Constants;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.jalo.order.price.ProductPriceInformations;
import de.hybris.platform.jalo.order.price.Tax;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DateRange;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.StandardDateRange;
import de.hybris.platform.util.TaxValue;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.collections.fast.YLongToObjectMap;
import de.hybris.platform.util.localization.Localization;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class Europe1PriceFactory extends GeneratedEurope1PriceFactory
{
    private static final Logger LOG = Logger.getLogger(Europe1PriceFactory.class);
    public static final long MATCH_ANY = PK.NULL_PK.getLongValue();
    public static final long MATCH_BY_PRODUCT_ID = MATCH_ANY - 1L;
    private RetrieveChannelStrategy retrieveChannelStrategy;
    private static final long[] ANY_COLLECTION = new long[] {MATCH_ANY};
    public static final String USE_FAST_ALGORITHMS = "use.fast.algorithms";


    public static Europe1PriceFactory getInstance()
    {
        return (Europe1PriceFactory)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("europe1");
    }


    private final YLongToObjectMap<Collection<CatalogVersionAwareCachedTax>> catalogAwareCachedTaxes = new YLongToObjectMap();
    private volatile Boolean cachesTaxes = null;
    private final InvalidationListener invalidationListener = (InvalidationListener)new Object(this);


    public void init()
    {
        registerInvalidationListener();
    }


    private void registerInvalidationListener()
    {
        InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.addInvalidationListener(this.invalidationListener);
    }


    protected synchronized void invalidateTaxCache()
    {
        this.cachesTaxes = null;
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
    }


    private static final Set EUROPE1_ENUMS = new HashSet(Arrays.asList((Object[])new String[] {"ProductDiscountGroup", "ProductTaxGroup", "ProductPriceGroup", "UserDiscountGroup", "UserTaxGroup", "UserPriceGroup"}));


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
        try
        {
            if(item instanceof Product)
            {
                removeRowsFor(ctx, (Product)item);
            }
            else if(item instanceof User)
            {
                removeRowsFor(ctx, (User)item);
            }
            else if(item instanceof Currency)
            {
                removeRowsFor(ctx, (Currency)item);
            }
            else if(item instanceof Unit)
            {
                removeRowsFor(ctx, (Unit)item);
            }
            else if(item instanceof Tax)
            {
                removeRowsFor(ctx, (Tax)item);
            }
            else if(item instanceof Discount)
            {
                removeRowsFor(ctx, (Discount)item);
            }
            else if(item instanceof EnumerationValue && EUROPE1_ENUMS.contains(item.getComposedType().getCode()))
            {
                removeRowsFor(ctx, (EnumerationValue)item);
            }
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e);
        }
    }


    protected void removeRowsFor(SessionContext ctx, Product product) throws ConsistencyCheckException
    {
        List<PDTRow> rows = FlexibleSearch.getInstance()
                        .search("SELECT tbl.PK FROM ({{ SELECT {" + Item.PK + "} as PK FROM {" + TypeManager.getInstance().getComposedType(PriceRow.class).getCode() + "*} WHERE {product}=?item }}  UNION ALL {{ SELECT {" + Item.PK + "} as PK FROM {" + TypeManager.getInstance()
                                                        .getComposedType(TaxRow.class).getCode() + "*} WHERE {product}=?item }}  UNION ALL {{ SELECT {" + Item.PK + "} as PK FROM {" + TypeManager.getInstance().getComposedType(DiscountRow.class).getCode() + "*} WHERE {product}=?item }} ) tbl",
                                        Collections.singletonMap("item", product), PDTRow.class).getResult();
        for(PDTRow toRemove : rows)
        {
            toRemove.remove(ctx);
        }
    }


    protected void removeRowsFor(SessionContext ctx, User user) throws ConsistencyCheckException
    {
        List<PDTRow> rows = FlexibleSearch.getInstance()
                        .search("SELECT tbl.PK FROM ({{SELECT {" + Item.PK + "} as PK FROM {" + TypeManager.getInstance().getComposedType(PriceRow.class).getCode() + "*} WHERE {user}=?item }} UNION ALL {{SELECT {" + Item.PK + "} as PK FROM {" + TypeManager.getInstance().getComposedType(TaxRow.class)
                                        .getCode() + "*} WHERE {user}=?item }} UNION ALL {{SELECT {" + Item.PK + "} as PK FROM {" + TypeManager.getInstance().getComposedType(DiscountRow.class).getCode() + "*} WHERE {user}=?item }} UNION ALL {{SELECT {" + Item.PK + "} as PK FROM {"
                                        + TypeManager.getInstance().getComposedType(GlobalDiscountRow.class).getCode() + "*} WHERE {user}=?item }} ) tbl", Collections.singletonMap("item", user), PDTRow.class).getResult();
        for(PDTRow toRemove : rows)
        {
            toRemove.remove(ctx);
        }
    }


    protected void removeRowsFor(SessionContext ctx, Currency currency) throws ConsistencyCheckException
    {
        List<PDTRow> rows = FlexibleSearch.getInstance()
                        .search("SELECT tbl.PK FROM ({{SELECT {" + Item.PK + "} as PK FROM {" + TypeManager.getInstance().getComposedType(PriceRow.class).getCode() + "*} WHERE {currency}=?item }} UNION ALL {{SELECT {" + Item.PK + "} as PK FROM {" + TypeManager.getInstance()
                                                        .getComposedType(DiscountRow.class).getCode() + "*} WHERE {currency}=?item }} UNION ALL {{SELECT {" + Item.PK + "} as PK FROM {" + TypeManager.getInstance().getComposedType(GlobalDiscountRow.class).getCode() + "*} WHERE {currency}=?item }} ) tbl",
                                        Collections.singletonMap("item", currency), PDTRow.class).getResult();
        for(PDTRow toRemove : rows)
        {
            toRemove.remove(ctx);
        }
    }


    protected void removeRowsFor(SessionContext ctx, Unit unit) throws ConsistencyCheckException
    {
        List<PDTRow> rows = FlexibleSearch.getInstance().search("SELECT {" + Item.PK + "} FROM {" + TypeManager.getInstance().getComposedType(PriceRow.class).getCode() + "*} WHERE {unit}=?item", Collections.singletonMap("item", unit), PDTRow.class).getResult();
        for(PDTRow toRemove : rows)
        {
            toRemove.remove(ctx);
        }
    }


    protected void removeRowsFor(SessionContext ctx, Tax tax) throws ConsistencyCheckException
    {
        List<PDTRow> rows = FlexibleSearch.getInstance().search("SELECT {" + Item.PK + "} FROM {" + TypeManager.getInstance().getComposedType(TaxRow.class).getCode() + "*} WHERE {tax}=?item", Collections.singletonMap("item", tax), PDTRow.class).getResult();
        for(PDTRow toRemove : rows)
        {
            toRemove.remove(ctx);
        }
    }


    protected void removeRowsFor(SessionContext ctx, Discount discount) throws ConsistencyCheckException
    {
        List<PDTRow> rows = FlexibleSearch.getInstance()
                        .search("SELECT tbl.PK FROM ({{ SELECT {" + Item.PK + "} as PK FROM {" + TypeManager.getInstance().getComposedType(DiscountRow.class).getCode() + "*} WHERE {discount}=?item }}  UNION ALL {{ SELECT {" + Item.PK + "} as PK FROM {" + TypeManager.getInstance()
                                        .getComposedType(GlobalDiscountRow.class).getCode() + "*} WHERE {discount}=?item }} ) tbl", Collections.singletonMap("item", discount), PDTRow.class).getResult();
        for(PDTRow toRemove : rows)
        {
            toRemove.remove(ctx);
        }
    }


    protected void removeRowsFor(SessionContext ctx, EnumerationValue enumerationValue) throws ConsistencyCheckException
    {
        String typeCode = enumerationValue.getComposedType().getCode();
        String query = null;
        if("ProductPriceGroup".equalsIgnoreCase(typeCode))
        {
            query = "SELECT {" + Item.PK + "} FROM {" + TypeManager.getInstance().getComposedType(PriceRow.class).getCode() + "*} WHERE {pg}=?item";
        }
        if("UserPriceGroup".equalsIgnoreCase(typeCode))
        {
            query = "SELECT {" + Item.PK + "} FROM {" + TypeManager.getInstance().getComposedType(PriceRow.class).getCode() + "*} WHERE {ug}=?item";
        }
        if("ProductTaxGroup".equalsIgnoreCase(typeCode))
        {
            query = "SELECT {" + Item.PK + "} FROM {" + TypeManager.getInstance().getComposedType(TaxRow.class).getCode() + "*} WHERE {pg}=?item";
        }
        if("UserTaxGroup".equalsIgnoreCase(typeCode))
        {
            query = "SELECT {" + Item.PK + "} FROM {" + TypeManager.getInstance().getComposedType(TaxRow.class).getCode() + "*} WHERE {ug}=?item";
        }
        if("ProductDiscountGroup".equalsIgnoreCase(typeCode))
        {
            query = "SELECT tbl.PK FROM ({{ SELECT {" + Item.PK + "} as PK FROM {" + TypeManager.getInstance().getComposedType(DiscountRow.class).getCode() + "*} WHERE {pg}=?item }}  UNION ALL {{ SELECT {" + Item.PK + "} as PK FROM {" + TypeManager.getInstance()
                            .getComposedType(GlobalDiscountRow.class).getCode() + "*} WHERE {pg}=?item }} ) tbl";
        }
        if("UserDiscountGroup".equalsIgnoreCase(typeCode))
        {
            query = "SELECT tbl.PK FROM ({{ SELECT {" + Item.PK + "} as PK FROM {" + TypeManager.getInstance().getComposedType(DiscountRow.class).getCode() + "*} WHERE {ug}=?item }}  UNION ALL {{ SELECT {" + Item.PK + "} as PK FROM {" + TypeManager.getInstance()
                            .getComposedType(GlobalDiscountRow.class).getCode() + "*} WHERE {ug}=?item }} ) tbl";
        }
        List<PDTRow> rows = FlexibleSearch.getInstance().search(query, Collections.singletonMap("item", enumerationValue), PDTRow.class).getResult();
        for(PDTRow toRemove : rows)
        {
            toRemove.remove(ctx);
        }
    }


    public PriceRow createPriceRow(Product product, EnumerationValue productPriceGroup, User user, EnumerationValue userPriceGroup, long minQuantity, Currency currency, Unit unit, int unitFactor, boolean net, DateRange dateRange, double price) throws JaloPriceFactoryException
    {
        return createPriceRow(getSession().getSessionContext(), product, productPriceGroup, user, userPriceGroup, minQuantity, currency, unit, unitFactor, net, dateRange, price);
    }


    public PriceRow createPriceRow(Product product, EnumerationValue productPriceGroup, User user, EnumerationValue userPriceGroup, long minQuantity, Currency currency, Unit unit, int unitFactor, boolean net, DateRange dateRange, double price, EnumerationValue channel)
                    throws JaloPriceFactoryException
    {
        return createPriceRow(getSession().getSessionContext(), product, productPriceGroup, user, userPriceGroup, minQuantity, currency, unit, unitFactor, net, dateRange, price, channel);
    }


    public PriceRow createPriceRow(SessionContext ctx, Product product, EnumerationValue productPriceGroup, User user, EnumerationValue userPriceGroup, long minQuantity, Currency currency, Unit unit, int unitFactor, boolean net, DateRange dateRange, double price, EnumerationValue channel)
                    throws JaloPriceFactoryException
    {
        PriceRow result = null;
        try
        {
            result = (PriceRow)ComposedType.newInstance(getSession().getSessionContext(), PriceRow.class, new Object[] {
                            "product", product, "pg", productPriceGroup, "user", user, "ug", userPriceGroup, "minqtd",
                            Long.valueOf(minQuantity),
                            "currency", currency, "unit", unit, "unitFactor",
                            Integer.valueOf(unitFactor), "net", Boolean.valueOf(net), "dateRange", dateRange,
                            "price",
                            Double.valueOf(price), "channel", channel});
            return result;
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
            if(jaloGenericCreationException1 instanceof JaloPriceFactoryException)
            {
                throw (JaloPriceFactoryException)jaloGenericCreationException1;
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


    public PriceRow createPriceRow(SessionContext ctx, Product product, EnumerationValue productPriceGroup, User user, EnumerationValue userPriceGroup, long minQuantity, Currency currency, Unit unit, int unitFactor, boolean net, DateRange dateRange, double price) throws JaloPriceFactoryException
    {
        try
        {
            return (PriceRow)ComposedType.newInstance(getSession().getSessionContext(), PriceRow.class, new Object[] {
                            "product", product, "pg", productPriceGroup, "user", user, "ug", userPriceGroup, "minqtd",
                            Long.valueOf(minQuantity),
                            "currency", currency, "unit", unit, "unitFactor",
                            Integer.valueOf(unitFactor), "net", Boolean.valueOf(net), "dateRange", dateRange,
                            "price",
                            Double.valueOf(price)});
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
            if(jaloGenericCreationException1 instanceof JaloPriceFactoryException)
            {
                throw (JaloPriceFactoryException)jaloGenericCreationException1;
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


    @Deprecated(since = "ages", forRemoval = false)
    public TaxRow createTaxRow(Product product, EnumerationValue productPriceGroup, User user, EnumerationValue userPriceGroup, Tax tax) throws JaloPriceFactoryException
    {
        return createTaxRow(getSession().getSessionContext(), product, productPriceGroup, user, userPriceGroup, tax, null, null);
    }


    public TaxRow createTaxRow(Product product, EnumerationValue productPriceGroup, User user, EnumerationValue userPriceGroup, Tax tax, DateRange dateRange, Double value) throws JaloPriceFactoryException
    {
        return createTaxRow(getSession().getSessionContext(), product, productPriceGroup, user, userPriceGroup, tax, dateRange, value);
    }


    public TaxRow createTaxRow(SessionContext ctx, Product product, EnumerationValue productPriceGroup, User user, EnumerationValue userPriceGroup, Tax tax, DateRange dateRange, Double value) throws JaloPriceFactoryException
    {
        try
        {
            return (TaxRow)ComposedType.newInstance(getSession().getSessionContext(), TaxRow.class, new Object[] {
                            "product", product, "pg", productPriceGroup, "user", user, "ug", userPriceGroup, "tax", tax,
                            "dateRange", dateRange, "value", value});
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
            if(jaloGenericCreationException1 instanceof JaloPriceFactoryException)
            {
                throw (JaloPriceFactoryException)jaloGenericCreationException1;
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


    public DiscountRow createDiscountRow(Product product, EnumerationValue productPriceGroup, User user, EnumerationValue userPriceGroup, Currency currency, Double value, DateRange dateRange, Discount discount) throws JaloPriceFactoryException
    {
        return createDiscountRow(getSession().getSessionContext(), product, productPriceGroup, user, userPriceGroup, currency, value, dateRange, discount);
    }


    public DiscountRow createDiscountRow(SessionContext ctx, Product product, EnumerationValue productPriceGroup, User user, EnumerationValue userPriceGroup, Currency currency, Double value, DateRange dateRange, Discount discount) throws JaloPriceFactoryException
    {
        try
        {
            return (DiscountRow)ComposedType.newInstance(getSession().getSessionContext(), DiscountRow.class, new Object[] {
                            "product", product, "pg", productPriceGroup, "user", user, "ug", userPriceGroup, "currency", currency,
                            "value", value, "dateRange", dateRange, "discount", discount});
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
            if(jaloGenericCreationException1 instanceof JaloPriceFactoryException)
            {
                throw (JaloPriceFactoryException)jaloGenericCreationException1;
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


    public GlobalDiscountRow createGlobalDiscountRow(User user, EnumerationValue userPriceGroup, Currency currency, Double value, DateRange dateRange, Discount discount) throws JaloPriceFactoryException
    {
        return createGlobalDiscountRow(getSession().getSessionContext(), user, userPriceGroup, currency, value, dateRange, discount);
    }


    public GlobalDiscountRow createGlobalDiscountRow(SessionContext ctx, User user, EnumerationValue userPriceGroup, Currency currency, Double value, DateRange dateRange, Discount discount) throws JaloPriceFactoryException
    {
        try
        {
            return (GlobalDiscountRow)ComposedType.newInstance(getSession().getSessionContext(), GlobalDiscountRow.class, new Object[] {
                            "user", user, "ug", userPriceGroup, "currency", currency, "value", value, "dateRange", dateRange,
                            "discount", discount});
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
            if(jaloGenericCreationException1 instanceof JaloPriceFactoryException)
            {
                throw (JaloPriceFactoryException)jaloGenericCreationException1;
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


    @Deprecated(since = "ages", forRemoval = false)
    public void checkAllPriceRows() throws JaloPriceFactoryException
    {
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void checkAllPriceRows(SessionContext ctx) throws JaloPriceFactoryException
    {
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void checkAllTaxRows() throws JaloPriceFactoryException
    {
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void checkAllTaxRows(SessionContext ctx) throws JaloPriceFactoryException
    {
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void checkAllDiscountRows() throws JaloPriceFactoryException
    {
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void checkAllDiscountRows(SessionContext ctx) throws JaloPriceFactoryException
    {
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void checkAllGlobalDiscountRows() throws JaloPriceFactoryException
    {
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void checkAllGlobalDiscountRows(SessionContext ctx) throws JaloPriceFactoryException
    {
    }


    @SLDSafe(portingClass = "ProductEurope1PricesAttributeHandler", portingMethod = "get(final ProductModel model)")
    public Collection<PriceRow> getEurope1Prices(SessionContext ctx, Product product)
    {
        CatalogManager.getInstance();
        if(CatalogManager.isSyncInProgressAsPrimitive(ctx))
        {
            return getRealPartOfPriceRows(ctx, product, getPPG(ctx, product));
        }
        return getProductPriceRows(ctx, product, getPPG(ctx, product));
    }


    @SLDSafe(portingClass = "ProductEurope1PricesAttributeHandler", portingMethod = "get(final ProductModel model)")
    public Collection<PriceRow> getEurope1Prices(Product item)
    {
        return getEurope1Prices(getSession().getSessionContext(), item);
    }


    public Collection<PriceRow> getRealPartOfPriceRows(SessionContext ctx, Product product, EnumerationValue productGroup)
    {
        if(product == null)
        {
            throw new JaloInvalidParameterException("cannot find price rows without product ", 0);
        }
        PDTRowsQueryBuilder builder = getPDTRowsQueryBuilderFor(GeneratedEurope1Constants.TC.PRICEROW);
        PDTRowsQueryBuilder.QueryWithParams queryAndParams = builder.withProduct(product.getPK()).build();
        Collection<PriceRow> ret = getSession().getFlexibleSearch().search(ctx, queryAndParams.getQuery(), queryAndParams.getParams(), Collections.singletonList(PriceRow.class), true, true, 0, -1).getResult();
        if(!useFastAlg(ctx) && ret.size() > 1)
        {
            List<PriceRow> list = new ArrayList<>(ret);
            Collections.sort(list, PR_COMP);
            ret = list;
        }
        return ret;
    }


    @SLDSafe(portingClass = "ProductEurope1PricesAttributeHandler", portingMethod = "set(final ProductModel model, final Collection<PriceRowModel> value)")
    public void setEurope1Prices(SessionContext ctx, Product item, Collection<?> prices)
    {
        Collection<PriceRow> toRemove = new HashSet<>(getEurope1Prices(ctx, item));
        if(prices != null)
        {
            toRemove.removeAll(prices);
        }
        try
        {
            for(Iterator<PriceRow> it = toRemove.iterator(); it.hasNext(); )
            {
                PriceRow priceRow = it.next();
                if(item.equals(priceRow.getProduct(ctx)))
                {
                    priceRow.remove(ctx);
                }
            }
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @SLDSafe(portingClass = "ProductEurope1PricesAttributeHandler", portingMethod = "set(final ProductModel model, final Collection<PriceRowModel> value)")
    public void setEurope1Prices(Product item, Collection<PriceRow> value)
    {
        setEurope1Prices(getSession().getSessionContext(), item, value);
    }


    @SLDSafe(portingClass = "ProductEurope1TaxesAttributeHandler", portingMethod = "get(final ProductModel model)")
    public Collection<TaxRow> getEurope1Taxes(SessionContext ctx, Product product)
    {
        if(CatalogManager.isSyncInProgressAsPrimitive(ctx))
        {
            return getRealPartOfTaxRows(ctx, product, getPTG(ctx, product));
        }
        return getProductTaxRows(ctx, product, getPTG(ctx, product));
    }


    @SLDSafe(portingClass = "ProductEurope1TaxesAttributeHandler", portingMethod = "get(final ProductModel model)")
    public Collection<TaxRow> getEurope1Taxes(Product item)
    {
        return getEurope1Taxes(getSession().getSessionContext(), item);
    }


    private Collection<TaxRow> getRealPartOfTaxRows(SessionContext ctx, Product product, EnumerationValue productGroup)
    {
        if(product == null)
        {
            throw new JaloInvalidParameterException("cannot find tax rows without product ", 0);
        }
        String taxTypeCode = TypeManager.getInstance().getComposedType(TaxRow.class).getCode();
        PDTRowsQueryBuilder builder = getPDTRowsQueryBuilderFor(taxTypeCode);
        PDTRowsQueryBuilder.QueryWithParams queryAndParams = builder.withProduct(product.getPK()).build();
        Collection<TaxRow> ret = getSession().getFlexibleSearch().search(ctx, queryAndParams.getQuery(), queryAndParams.getParams(), Collections.singletonList(TaxRow.class), true, true, 0, -1).getResult();
        if(!useFastAlg(ctx) && ret.size() > 1)
        {
            List<TaxRow> list = new ArrayList<>(ret);
            Collections.sort(list, TR_COMP);
            ret = list;
        }
        return ret;
    }


    @SLDSafe(portingClass = "ProductEurope1TaxesAttributeHandler", portingMethod = "set(final ProductModel model, final Collection<TaxRowModel> value)")
    public void setEurope1Taxes(SessionContext ctx, Product item, Collection<?> taxes)
    {
        Collection<TaxRow> toRemove = new HashSet<>(getEurope1Taxes(ctx, item));
        if(taxes != null)
        {
            toRemove.removeAll(taxes);
        }
        try
        {
            for(Iterator<TaxRow> it = toRemove.iterator(); it.hasNext(); )
            {
                TaxRow taxRow = it.next();
                if(taxRow.getProduct(ctx) != null)
                {
                    taxRow.remove(ctx);
                }
            }
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @SLDSafe(portingClass = "ProductEurope1TaxesAttributeHandler", portingMethod = "set(final ProductModel model, final Collection<TaxRowModel> value)")
    public void setEurope1Taxes(Product item, Collection<TaxRow> value)
    {
        setEurope1Taxes(getSession().getSessionContext(), item, value);
    }


    @SLDSafe(portingClass = "ProductEurope1DiscountsAttributeHandler", portingMethod = "get(final ProductModel model)")
    public Collection<DiscountRow> getEurope1Discounts(SessionContext ctx, Product product)
    {
        if(CatalogManager.isSyncInProgressAsPrimitive(ctx))
        {
            return getRealPartOfDiscountRows(ctx, product, getPDG(ctx, product));
        }
        return getProductDiscountRows(ctx, product, getPDG(ctx, product));
    }


    @SLDSafe(portingClass = "ProductEurope1DiscountsAttributeHandler", portingMethod = "get(final ProductModel model)")
    public Collection<DiscountRow> getEurope1Discounts(Product item)
    {
        return getEurope1Discounts(getSession().getSessionContext(), item);
    }


    private Collection<DiscountRow> getRealPartOfDiscountRows(SessionContext ctx, Product product, EnumerationValue productGroup)
    {
        if(product == null)
        {
            throw new JaloInvalidParameterException("cannot find discount rows without product ", 0);
        }
        String discountTypeCode = TypeManager.getInstance().getComposedType(DiscountRow.class).getCode();
        PDTRowsQueryBuilder builder = getPDTRowsQueryBuilderFor(discountTypeCode);
        PDTRowsQueryBuilder.QueryWithParams queryAndParams = builder.withProduct(product.getPK()).build();
        Collection<DiscountRow> ret = getSession().getFlexibleSearch().search(ctx, queryAndParams.getQuery(), queryAndParams.getParams(), Collections.singletonList(DiscountRow.class), true, true, 0, -1).getResult();
        if(!useFastAlg(ctx) && ret.size() > 1)
        {
            List<DiscountRow> list = new ArrayList<>(ret);
            Collections.sort(list, DR_COMP);
            ret = list;
        }
        return ret;
    }


    @SLDSafe(portingClass = "ProductEurope1DiscountsAttributeHandler", portingMethod = "set(final ProductModel model, final Collection<DiscountRowModel> value)")
    public void setEurope1Discounts(SessionContext ctx, Product item, Collection<?> discounts)
    {
        Collection<DiscountRow> toRemove = new HashSet<>(getEurope1Discounts(ctx, item));
        if(discounts != null)
        {
            toRemove.removeAll(discounts);
        }
        try
        {
            for(Iterator<DiscountRow> it = toRemove.iterator(); it.hasNext(); )
            {
                DiscountRow discountRow = it.next();
                if(discountRow.getProduct(ctx) != null)
                {
                    discountRow.remove(ctx);
                }
            }
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @SLDSafe(portingClass = "UserEurope1DiscountsAttributeHandler", portingMethod = "get(final UserModel model)")
    public Collection<GlobalDiscountRow> getEurope1Discounts(User user)
    {
        return getEurope1Discounts(getSession().getSessionContext(), user);
    }


    @SLDSafe(portingClass = "ProductEurope1DiscountsAttributeHandler", portingMethod = "set(final ProductModel model, final Collection<DiscountRowModel> value)")
    public void setEurope1Discounts(Product product, Collection discounts)
    {
        setEurope1Discounts(getSession().getSessionContext(), product, discounts);
    }


    @SLDSafe(portingClass = "UserEurope1DiscountsAttributeHandler", portingMethod = "get(final UserModel model)")
    public Collection<GlobalDiscountRow> getEurope1Discounts(SessionContext ctx, User user)
    {
        return getUserGlobalDiscountRows(user, (EnumerationValue)user.getProperty(ctx, "Europe1PriceFactory_UDG"));
    }


    @SLDSafe(portingClass = "UserEurope1DiscountsAttributeHandler", portingMethod = "set(final UserModel model, final Collection<GlobalDiscountRowModel> globalDiscountRowModels)")
    public void setEurope1Discounts(User user, Collection discounts)
    {
        setEurope1Discounts(getSession().getSessionContext(), user, discounts);
    }


    @SLDSafe(portingClass = "UserEurope1DiscountsAttributeHandler", portingMethod = "set(final UserModel model, final Collection<GlobalDiscountRowModel> globalDiscountRowModels)")
    public void setEurope1Discounts(SessionContext ctx, User user, Collection<?> discounts)
    {
        Collection<GlobalDiscountRow> toRemove = new HashSet<>(getEurope1Discounts(ctx, user));
        if(discounts != null)
        {
            toRemove.removeAll(discounts);
        }
        try
        {
            for(Iterator<GlobalDiscountRow> it = toRemove.iterator(); it.hasNext(); )
            {
                GlobalDiscountRow gdr = it.next();
                if(gdr.getUser(ctx) != null)
                {
                    gdr.remove(ctx);
                }
            }
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public Collection getTaxValues(AbstractOrderEntry entry) throws JaloPriceFactoryException
    {
        if(isCachingTaxes())
        {
            SessionContext sessionContext = getSession().getSessionContext();
            AbstractOrder abstractOrder = entry.getOrder(sessionContext);
            Collection<CachedTaxValue> taxes = getCachedTaxes(entry
                                            .getProduct(sessionContext),
                            getPTG(sessionContext, entry), abstractOrder
                                            .getUser(sessionContext),
                            getUTG(sessionContext, entry), abstractOrder
                                            .getDate(sessionContext));
            if(taxes.isEmpty())
            {
                return Collections.EMPTY_LIST;
            }
            List<TaxValue> list = new ArrayList<>(taxes.size());
            Currency currency = abstractOrder.getCurrency(sessionContext);
            String isoCode = currency.getIsoCode();
            for(TaxValue tv : taxes)
            {
                TaxValue toAdd = tv;
                boolean abs = toAdd.isAbsolute();
                if(abs)
                {
                    String rowCurr = toAdd.getCurrencyIsoCode();
                    if(rowCurr != null && !rowCurr.equals(isoCode))
                    {
                        toAdd = new TaxValue(toAdd.getCode(), C2LManager.getInstance().getCurrencyByIsoCode(rowCurr).convertAndRound(currency, toAdd.getValue()), true, isoCode);
                    }
                }
                list.add(toAdd);
            }
            return list;
        }
        SessionContext ctx = getSession().getSessionContext();
        AbstractOrder order = entry.getOrder(ctx);
        List<TaxRow> rows = matchTaxRows(entry
                                        .getProduct(ctx),
                        getPTG(ctx, entry), order
                                        .getUser(ctx),
                        getUTG(ctx, entry), order
                                        .getDate(ctx), -1);
        if(rows.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<TaxValue> ret = new ArrayList<>(rows.size());
        Currency reqCurr = order.getCurrency(ctx);
        String reqIsoCode = reqCurr.getIsoCode();
        for(TaxRow tr : rows)
        {
            double value;
            String isoCode;
            Tax tax = tr.getTax(ctx);
            boolean hasValue = tr.hasValue(ctx);
            boolean abs = hasValue ? tr.isAbsoluteAsPrimitive() : tax.isAbsolute().booleanValue();
            if(abs)
            {
                Currency rowCurr = abs ? (hasValue ? tr.getCurrency(ctx) : tax.getCurrency(ctx)) : null;
                if(rowCurr != null && !rowCurr.equals(reqCurr))
                {
                    double taxDoubleValue = hasValue ? tr.getValue(ctx).doubleValue() : tax.getValue().doubleValue();
                    value = rowCurr.convertAndRound(reqCurr, taxDoubleValue);
                }
                else
                {
                    value = hasValue ? tr.getValue(ctx).doubleValue() : tax.getValue().doubleValue();
                }
                isoCode = reqIsoCode;
            }
            else
            {
                value = hasValue ? tr.getValue(ctx).doubleValue() : tax.getValue().doubleValue();
                isoCode = null;
            }
            ret.add(new TaxValue(tax.getCode(), value, abs, isoCode));
        }
        return ret;
    }


    public PriceValue getBasePrice(AbstractOrderEntry entry) throws JaloPriceFactoryException
    {
        PriceRow row;
        SessionContext ctx = getSession().getSessionContext();
        AbstractOrder order = entry.getOrder(ctx);
        Currency currency = null;
        EnumerationValue productGroup = null;
        User user = null;
        EnumerationValue userGroup = null;
        Unit unit = null;
        long quantity = 0L;
        boolean net = false;
        Date date = null;
        Product product = entry.getProduct();
        boolean giveAwayMode = entry.isGiveAway(ctx).booleanValue();
        boolean entryIsRejected = entry.isRejected(ctx).booleanValue();
        if(giveAwayMode && entryIsRejected)
        {
            row = null;
        }
        else
        {
            row = matchPriceRowForPrice(ctx, product,
                            productGroup = getPPG(ctx, product),
                            user = order.getUser(),
                            userGroup = getUPG(ctx, user),
                            quantity = entry.getQuantity(ctx).longValue(),
                            unit = entry.getUnit(ctx),
                            currency = order.getCurrency(ctx),
                            date = order.getDate(ctx),
                            net = order.isNet().booleanValue(), giveAwayMode);
        }
        if(row != null)
        {
            double price;
            Currency rowCurr = row.getCurrency();
            if(currency.equals(rowCurr))
            {
                price = row.getPriceAsPrimitive() / row.getUnitFactorAsPrimitive();
            }
            else
            {
                price = rowCurr.convert(currency, row.getPriceAsPrimitive() / row.getUnitFactorAsPrimitive());
            }
            Unit priceUnit = row.getUnit();
            Unit entryUnit = entry.getUnit();
            double convertedPrice = priceUnit.convertExact(entryUnit, price);
            return new PriceValue(currency
                            .getIsoCode(), convertedPrice, row
                            .isNetAsPrimitive());
        }
        if(giveAwayMode)
        {
            return new PriceValue(order.getCurrency(ctx).getIsoCode(), 0.0D, order.isNet().booleanValue());
        }
        String msg = Localization.getLocalizedString("exception.europe1pricefactory.getbaseprice.jalopricefactoryexception1", new Object[] {product, productGroup, user, userGroup,
                        Long.toString(quantity), unit, currency, date,
                        Boolean.toString(net)});
        throw new JaloPriceFactoryException(msg, 0);
    }


    public List getDiscountValues(AbstractOrderEntry entry) throws JaloPriceFactoryException
    {
        SessionContext ctx = getSession().getSessionContext();
        AbstractOrder order = entry.getOrder(ctx);
        return Europe1Tools.createDiscountValueList(
                        matchDiscountRows(entry
                                                        .getProduct(ctx),
                                        getPDG(ctx, entry), order
                                                        .getUser(ctx),
                                        getUDG(ctx, entry), order
                                                        .getCurrency(ctx), order
                                                        .getDate(ctx), -1));
    }


    public List getDiscountValues(AbstractOrder order) throws JaloPriceFactoryException
    {
        SessionContext ctx = getSession().getSessionContext();
        return Europe1Tools.createDiscountValueList(
                        matchDiscountRows(null, null, order
                                                        .getUser(ctx),
                                        getUDG(ctx, order), order
                                                        .getCurrency(ctx), order
                                                        .getDate(ctx), -1));
    }


    public ProductPriceInformations getAllPriceInformations(SessionContext ctx, Product product, Date date, boolean net) throws JaloPriceFactoryException
    {
        User user = ctx.getUser();
        Currency curr = ctx.getCurrency();
        List taxes = getTaxInformations(product, getPTG(ctx, product), user, getUTG(ctx, user), date);
        Collection prices = getPriceInformations(ctx, product, getPPG(ctx, product), user, getUPG(ctx, user), curr, net, date,
                        Europe1Tools.getTaxValues(taxes));
        Collection discounts = getDiscountInformations(product, getPDG(ctx, product), user, getUDG(ctx, user), curr, date);
        return new ProductPriceInformations(prices, taxes, discounts);
    }


    public List getProductDiscountInformations(SessionContext ctx, Product product, Date date, boolean net) throws JaloPriceFactoryException
    {
        return getDiscountInformations(product, getPDG(ctx, product), ctx.getUser(), getUDG(ctx, ctx.getUser()), ctx
                        .getCurrency(), date);
    }


    public List getProductPriceInformations(SessionContext ctx, Product product, Date date, boolean net) throws JaloPriceFactoryException
    {
        return getPriceInformations(ctx, product, getPPG(ctx, product), ctx.getUser(), getUPG(ctx, ctx.getUser()), ctx
                        .getCurrency(), net, date, null);
    }


    public List getProductTaxInformations(SessionContext ctx, Product product, Date date) throws JaloPriceFactoryException
    {
        return getTaxInformations(product, getPTG(ctx, product), ctx.getUser(), getUTG(ctx, ctx.getUser()), date);
    }


    protected List getDiscountInformations(Product product, EnumerationValue productGroup, User user, EnumerationValue userGroup, Currency curr, Date date) throws JaloPriceFactoryException
    {
        List<Europe1DiscountInformation> discountInfos = new LinkedList();
        for(Iterator<DiscountRow> it = matchDiscountRows(product, productGroup, user, userGroup, curr, date, -1).iterator(); it.hasNext(); )
        {
            discountInfos.add(new Europe1DiscountInformation((AbstractDiscountRow)it.next()));
        }
        return discountInfos;
    }


    protected List getPriceInformations(SessionContext ctx, Product product, EnumerationValue productGroup, User user, EnumerationValue userGroup, Currency curr, boolean net, Date date, Collection taxValues) throws JaloPriceFactoryException
    {
        Collection<PriceRow> priceRows = filterPriceRows(matchPriceRowsForInfo(ctx, product, productGroup, user, userGroup, curr, date, net));
        List<PriceInformation> priceInfos = new ArrayList<>(priceRows.size());
        Collection theTaxValues = taxValues;
        List<PriceInformation> defaultPriceInfos = new ArrayList<>(priceRows.size());
        PriceRowChannel channel = this.retrieveChannelStrategy.getChannel(ctx);
        for(PriceRow row : priceRows)
        {
            PriceInformation pInfo = Europe1Tools.createPriceInformation(row, curr);
            if(pInfo.getPriceValue().isNet() != net)
            {
                if(theTaxValues == null)
                {
                    theTaxValues = Europe1Tools.getTaxValues(getTaxInformations(product, getPTG(ctx, product), user,
                                    getUTG(ctx, user), date));
                }
                pInfo = new PriceInformation(pInfo.getQualifiers(), pInfo.getPriceValue().getOtherPrice(theTaxValues));
            }
            if(row.getChannel() == null)
            {
                defaultPriceInfos.add(pInfo);
            }
            if(channel == null && row.getChannel() == null)
            {
                priceInfos.add(pInfo);
                continue;
            }
            if(channel != null && row.getChannel() != null && row.getChannel()
                            .getCode()
                            .equalsIgnoreCase(channel.getCode()))
            {
                priceInfos.add(pInfo);
            }
        }
        if(priceInfos.size() == 0)
        {
            return defaultPriceInfos;
        }
        return priceInfos;
    }


    protected List getTaxInformations(Product product, EnumerationValue productGroup, User user, EnumerationValue userGroup, Date date) throws JaloPriceFactoryException
    {
        List<Europe1TaxInformation> taxInfos = null;
        if(isCachingTaxes())
        {
            Collection<CachedTaxValue> cachedValues = getSuperCachedTaxes(product, productGroup, user, userGroup, date);
            if(!cachedValues.isEmpty())
            {
                taxInfos = new ArrayList(cachedValues.size());
                for(CachedTaxValue tv : cachedValues)
                {
                    taxInfos.add(new Europe1TaxInformation((TaxValue)tv, tv.getTaxRowPK()));
                }
            }
        }
        else
        {
            List<TaxRow> rows = matchTaxRows(product, productGroup, user, userGroup, date, -1);
            if(!rows.isEmpty())
            {
                taxInfos = new ArrayList<>(rows.size());
                for(TaxRow tr : rows)
                {
                    taxInfos.add(new Europe1TaxInformation(tr));
                }
            }
        }
        return (taxInfos != null) ? taxInfos : Collections.EMPTY_LIST;
    }


    protected List<PriceRow> filterPriceRows(List<PriceRow> priceRows)
    {
        if(priceRows.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        Unit lastUnit = null;
        long lastMin = -1L;
        ArrayList<PriceRow> ret = new ArrayList<>(priceRows);
        for(ListIterator<PriceRow> it = ret.listIterator(); it.hasNext(); )
        {
            PriceRow row = it.next();
            long min = row.getMinQuantity();
            Unit unit = row.getUnit();
            if(lastUnit != null && lastUnit.equals(unit) && lastMin == min)
            {
                it.remove();
                continue;
            }
            lastUnit = unit;
            lastMin = min;
        }
        return ret;
    }


    public List matchDiscountRows(Product product, EnumerationValue productGroup, User user, EnumerationValue userGroup, Currency curr, Date date, int maxCount) throws JaloPriceFactoryException
    {
        if(user == null && userGroup == null)
        {
            throw new JaloPriceFactoryException("cannot match discounts without user and user group - at least one must be present", 0);
        }
        if(curr == null)
        {
            throw new JaloPriceFactoryException("cannot match price without currency", 0);
        }
        if(date == null)
        {
            throw new JaloPriceFactoryException("cannot match price without date", 0);
        }
        Collection<? extends AbstractDiscountRow> rows = queryDiscounts4Price(getSession().getSessionContext(), product, productGroup, user, userGroup);
        if(!rows.isEmpty())
        {
            List<? extends AbstractDiscountRow> ret = filterDiscountRows4Price(rows, date);
            if(ret.size() > 1)
            {
                Collections.sort(ret, (Comparator<? super AbstractDiscountRow>)new DiscountRowMatchComparator(this));
            }
            return ret;
        }
        return Collections.EMPTY_LIST;
    }


    protected List<? extends AbstractDiscountRow> filterDiscountRows4Price(Collection<? extends AbstractDiscountRow> rows, Date date)
    {
        if(rows.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<? extends AbstractDiscountRow> ret = new ArrayList<>(rows);
        for(ListIterator<? extends AbstractDiscountRow> it = ret.listIterator(); it.hasNext(); )
        {
            AbstractDiscountRow abstractDiscountRow = it.next();
            StandardDateRange standardDateRange = abstractDiscountRow.getDateRange();
            if(standardDateRange != null && !standardDateRange.encloses(date))
            {
                it.remove();
            }
        }
        return ret;
    }


    protected Collection<? extends AbstractDiscountRow> queryDiscounts4Price(SessionContext ctx, Product product, EnumerationValue productGroup, User user, EnumerationValue userGroup)
    {
        boolean global = (product == null && productGroup == null);
        String discountRowTypeCode = global ? GeneratedEurope1Constants.TC.GLOBALDISCOUNTROW : GeneratedEurope1Constants.TC.DISCOUNTROW;
        PK productPk = (product == null) ? null : product.getPK();
        PK productGroupPk = (productGroup == null) ? null : productGroup.getPK();
        PK userPk = (user == null) ? null : user.getPK();
        PK userGroupPk = (userGroup == null) ? null : userGroup.getPK();
        String productId = extractProductId(ctx, product);
        PDTRowsQueryBuilder builder = getPDTRowsQueryBuilderFor(discountRowTypeCode);
        PDTRowsQueryBuilder.QueryWithParams queryAndParams = builder.withAnyProduct().withAnyUser().withProduct(productPk).withProductId(productId).withProductGroup(productGroupPk).withUser(userPk).withUserGroup(userGroupPk).build();
        return FlexibleSearch.getInstance().search(ctx, queryAndParams.getQuery(), queryAndParams.getParams(), DiscountRow.class)
                        .getResult();
    }


    public PriceRow matchPriceRowForPrice(SessionContext ctx, Product product, EnumerationValue productGroup, User user, EnumerationValue userGroup, long qtd, Unit unit, Currency currency, Date date, boolean net, boolean giveAwayMode) throws JaloPriceFactoryException
    {
        if(product == null && productGroup == null)
        {
            throw new JaloPriceFactoryException("cannot match price without product and product group - at least one must be present", 0);
        }
        if(user == null && userGroup == null)
        {
            throw new JaloPriceFactoryException("cannot match price without user and user group - at least one must be present", 0);
        }
        if(currency == null)
        {
            throw new JaloPriceFactoryException("cannot match price without currency", 0);
        }
        if(date == null)
        {
            throw new JaloPriceFactoryException("cannot match price without date", 0);
        }
        if(unit == null)
        {
            throw new JaloPriceFactoryException("cannot match price without unit", 0);
        }
        Collection<PriceRow> rows = queryPriceRows4Price(ctx, product, productGroup, user, userGroup, date, currency, giveAwayMode);
        if(!rows.isEmpty())
        {
            PriceRowChannel channel = this.retrieveChannelStrategy.getChannel(ctx);
            List<PriceRow> list = filterPriceRows4Price(rows, qtd, unit, currency, date, giveAwayMode, channel);
            if(list.isEmpty())
            {
                return null;
            }
            if(list.size() == 1)
            {
                return list.get(0);
            }
            list.sort((Comparator<? super PriceRow>)new PriceRowMatchComparator(this, currency, net, unit));
            return list.get(0);
        }
        return null;
    }


    protected List<PriceRow> filterPriceRows4Price(Collection<PriceRow> rows, long _quantity, Unit unit, Currency curr, Date date, boolean giveAwayMode, PriceRowChannel channel)
    {
        if(rows.isEmpty())
        {
            return Collections.emptyList();
        }
        Currency base = curr.isBase().booleanValue() ? null : C2LManager.getInstance().getBaseCurrency();
        Set<Unit> convertible = unit.getConvertibleUnits();
        List<PriceRow> ret = new ArrayList<>(rows);
        long quantity = (_quantity == 0L) ? 1L : _quantity;
        boolean hasChannelRowMatching = false;
        ListIterator<PriceRow> it;
        for(it = ret.listIterator(); it.hasNext(); )
        {
            PriceRow priceRow = it.next();
            if(quantity < priceRow.getMinqtdAsPrimitive())
            {
                it.remove();
                continue;
            }
            Currency currency = priceRow.getCurrency();
            if(!curr.equals(currency) && (base == null || !base.equals(currency)))
            {
                it.remove();
                continue;
            }
            Unit user = priceRow.getUnit();
            if(!unit.equals(user) && !convertible.contains(user))
            {
                it.remove();
                continue;
            }
            StandardDateRange standardDateRange = priceRow.getDateRange();
            if(standardDateRange != null && !standardDateRange.encloses(date))
            {
                it.remove();
                continue;
            }
            if(giveAwayMode != priceRow.isGiveAwayPriceAsPrimitive())
            {
                it.remove();
                continue;
            }
            if(channel != null && priceRow.getChannel() != null &&
                            !priceRow.getChannel().getCode().equalsIgnoreCase(channel.getCode()))
            {
                it.remove();
                continue;
            }
            if(channel != null && priceRow.getChannel() != null && priceRow
                            .getChannel().getCode().equalsIgnoreCase(channel.getCode()))
            {
                hasChannelRowMatching = true;
            }
        }
        if(hasChannelRowMatching && ret.size() > 1)
        {
            for(it = ret.listIterator(); it.hasNext(); )
            {
                PriceRow priceRow = it.next();
                if(priceRow.getChannel() == null)
                {
                    it.remove();
                }
            }
        }
        return ret;
    }


    @Deprecated(since = "6.7", forRemoval = false)
    protected Collection<PriceRow> queryPriceRows4Price(SessionContext ctx, Product product, EnumerationValue productGroup, User user, EnumerationValue userGroup)
    {
        PDTRowsQueryBuilder builder = getQueryPriceRow4PriceBuilder(ctx, product, productGroup, user, userGroup);
        PDTRowsQueryBuilder.QueryWithParams queryAndParams = builder.build();
        return FlexibleSearch.getInstance().search(ctx, queryAndParams.getQuery(), queryAndParams.getParams(), PriceRow.class)
                        .getResult();
    }


    protected Collection<PriceRow> queryPriceRows4Price(SessionContext ctx, Product product, EnumerationValue productGroup, User user, EnumerationValue userGroup, Date date, Currency currency, boolean giveAwayMode)
    {
        PDTRowsQueryBuilder builder = getQueryPriceRowBasicData4PriceBuilder(ctx, product, productGroup, user, userGroup);
        PDTRowsQueryBuilder.QueryWithParams queryAndParams = builder.build();
        SearchResult<List<Object>> search = FlexibleSearch.getInstance().search(ctx, queryAndParams.getQuery(), queryAndParams
                        .getParams(), queryAndParams
                        .getResultClassList(), true, true, 0, -1);
        Currency base = currency.isBase().booleanValue() ? null : C2LManager.getInstance().getBaseCurrency();
        List<PK> pks = (List<PK>)search.getResult().stream().map(o -> priceRowResultListToBasicData(o)).filter(priceRowBasicData -> priceRowBasicData.isCurrencyValid(currency, base)).filter(priceRowBasicData -> priceRowBasicData.isDateInRange(date))
                        .filter(priceRowBasicData -> priceRowBasicData.isGiveAwayModeValid(giveAwayMode)).map(priceRowBasicData -> priceRowBasicData.getPK()).collect(Collectors.toList());
        return (Collection<PriceRow>)new LazyLoadItemList(WrapperFactory.getPrefetchLanguages(ctx), pks, 100);
    }


    private PriceRowBasicData priceRowResultListToBasicData(List<Object> o)
    {
        return new PriceRowBasicData((PK)o.get(0), (Date)o.get(1), (Date)o.get(2), (Currency)o.get(3), ((Boolean)o
                        .get(4)).booleanValue());
    }


    @Deprecated(since = "6.7", forRemoval = false)
    private PDTRowsQueryBuilder getQueryPriceRow4PriceBuilder(SessionContext ctx, Product product, EnumerationValue productGroup, User user, EnumerationValue userGroup)
    {
        PK productPk = (product == null) ? null : product.getPK();
        PK productGroupPk = (productGroup == null) ? null : productGroup.getPK();
        PK userPk = (user == null) ? null : user.getPK();
        PK userGroupPk = (userGroup == null) ? null : userGroup.getPK();
        String productId = extractProductId(ctx, product);
        PDTRowsQueryBuilder builder = getPDTRowsQueryBuilderFor(GeneratedEurope1Constants.TC.PRICEROW);
        return builder.withAnyProduct().withAnyUser().withProduct(productPk).withProductId(productId)
                        .withProductGroup(productGroupPk).withUser(userPk).withUserGroup(userGroupPk);
    }


    private PDTRowsQueryBuilder getQueryPriceRowBasicData4PriceBuilder(SessionContext ctx, Product product, EnumerationValue productGroup, User user, EnumerationValue userGroup)
    {
        PK productPk = (product == null) ? null : product.getPK();
        PK productGroupPk = (productGroup == null) ? null : productGroup.getPK();
        PK userPk = (user == null) ? null : user.getPK();
        PK userGroupPk = (userGroup == null) ? null : userGroup.getPK();
        String productId = extractProductId(ctx, product);
        LinkedHashMap<String, Class<?>> columns = new LinkedHashMap<>();
        columns.put(PriceRow.PK, PK.class);
        columns.put("startTime", Date.class);
        columns.put("endTime", Date.class);
        columns.put("currency", Currency.class);
        columns.put("giveAwayPrice", Boolean.class);
        DefaultPDTRowsQueryBuilder defaultPDTRowsQueryBuilder = new DefaultPDTRowsQueryBuilder(GeneratedEurope1Constants.TC.PRICEROW, columns);
        return defaultPDTRowsQueryBuilder.withAnyProduct().withAnyUser().withProduct(productPk).withProductId(productId)
                        .withProductGroup(productGroupPk).withUser(userPk).withUserGroup(userGroupPk);
    }


    protected PDTRowsQueryBuilder getPDTRowsQueryBuilderFor(String type)
    {
        return PDTRowsQueryBuilder.defaultBuilder(type);
    }


    public List<PriceRow> matchPriceRowsForInfo(SessionContext ctx, Product product, EnumerationValue productGroup, User user, EnumerationValue userGroup, Currency currency, Date date, boolean net) throws JaloPriceFactoryException
    {
        if(product == null && productGroup == null)
        {
            throw new JaloPriceFactoryException("cannot match price info without product and product group - at least one must be present", 0);
        }
        if(user == null && userGroup == null)
        {
            throw new JaloPriceFactoryException("cannot match price info without user and user group - at least one must be present", 0);
        }
        if(currency == null)
        {
            throw new JaloPriceFactoryException("cannot match price info without currency", 0);
        }
        if(date == null)
        {
            throw new JaloPriceFactoryException("cannot match price info without date", 0);
        }
        Collection<PriceRow> rows = queryPriceRows4Price(ctx, product, productGroup, user, userGroup, date, currency, false);
        if(rows.isEmpty())
        {
            return Collections.emptyList();
        }
        PriceRowChannel channel = this.retrieveChannelStrategy.getChannel(ctx);
        List<PriceRow> ret = new ArrayList<>(rows);
        if(ret.size() > 1)
        {
            ret.sort((Comparator<? super PriceRow>)new PriceRowInfoComparator(this, currency, net));
        }
        return filterPriceRows4Info(ret, currency, date, channel);
    }


    protected List<PriceRow> filterPriceRows4Info(Collection<PriceRow> rows, Currency curr, Date date, PriceRowChannel channel)
    {
        if(rows.isEmpty())
        {
            return Collections.emptyList();
        }
        Currency base = curr.isBase().booleanValue() ? null : C2LManager.getInstance().getBaseCurrency();
        List<PriceRow> ret = new ArrayList<>(rows);
        boolean hasChannelRowMatching = false;
        ListIterator<PriceRow> it;
        for(it = ret.listIterator(); it.hasNext(); )
        {
            PriceRow priceRow = it.next();
            Currency currency = priceRow.getCurrency();
            if(!curr.equals(currency) && (base == null || !base.equals(currency)))
            {
                it.remove();
                continue;
            }
            StandardDateRange standardDateRange = priceRow.getDateRange();
            if(standardDateRange != null && !standardDateRange.encloses(date))
            {
                it.remove();
                continue;
            }
            if(priceRow.isGiveAwayPriceAsPrimitive())
            {
                it.remove();
                continue;
            }
            if(channel != null && priceRow.getChannel() != null &&
                            !priceRow.getChannel().getCode().equalsIgnoreCase(channel.getCode()))
            {
                it.remove();
                continue;
            }
            if(channel != null && priceRow.getChannel() != null && priceRow
                            .getChannel().getCode().equalsIgnoreCase(channel.getCode()))
            {
                hasChannelRowMatching = true;
            }
        }
        if(hasChannelRowMatching && ret.size() > 1)
        {
            for(it = ret.listIterator(); it.hasNext(); )
            {
                PriceRow priceRow = it.next();
                if(priceRow.getChannel() == null)
                {
                    it.remove();
                }
            }
        }
        return ret;
    }


    private boolean useFastAlg()
    {
        return useFastAlg(getSession().getSessionContext());
    }


    private boolean useFastAlg(SessionContext ctx)
    {
        return (ctx == null || !Boolean.FALSE.equals(ctx.getAttribute("use.fast.algorithms")));
    }


    public Collection getProductPriceRows(Product product, EnumerationValue productGroup)
    {
        return getProductPriceRows(getSession().getSessionContext(), product, productGroup);
    }


    public Collection getProductPriceRows(SessionContext ctx, Product product, EnumerationValue productGroup)
    {
        Collection<PriceRow> ret = getProductPriceRowsFast(ctx, product, productGroup);
        if(!useFastAlg(ctx) && ret.size() > 1)
        {
            List<PriceRow> list = new ArrayList<>(ret);
            Collections.sort(list, PR_COMP);
            ret = list;
        }
        return ret;
    }


    private static final Comparator<PriceRow> PR_COMP = (Comparator<PriceRow>)new Object();


    public Collection getProductPriceRowsFast(SessionContext ctx, Product product, EnumerationValue productGroup)
    {
        if(product == null)
        {
            throw new JaloInvalidParameterException("cannot find price rows without product ", 0);
        }
        PK productGroupPk = (productGroup == null) ? null : productGroup.getPK();
        String productId = extractProductId(ctx, product);
        PDTRowsQueryBuilder builder = getPDTRowsQueryBuilderFor(GeneratedEurope1Constants.TC.PRICEROW);
        PDTRowsQueryBuilder.QueryWithParams queryAndParams = builder.withAnyProduct().withProduct(product.getPK()).withProductId(productId).withProductGroup(productGroupPk).build();
        return getSession().getFlexibleSearch().search(ctx, queryAndParams.getQuery(), queryAndParams.getParams(),
                        Collections.singletonList(PriceRow.class), true, true, 0, -1).getResult();
    }


    public Collection getProductTaxRows(Product product, EnumerationValue productGroup)
    {
        return getProductTaxRows(getSession().getSessionContext(), product, productGroup);
    }


    public Collection getProductTaxRows(SessionContext ctx, Product product, EnumerationValue productGroup)
    {
        Collection<TaxRow> ret = getProductTaxRowsFast(ctx, product, productGroup);
        if(!useFastAlg(ctx) && ret.size() > 1)
        {
            List<TaxRow> list = new ArrayList<>(ret);
            Collections.sort(list, TR_COMP);
            ret = list;
        }
        return ret;
    }


    private static final Comparator<TaxRow> TR_COMP = (Comparator<TaxRow>)new Object();


    public Collection getProductTaxRowsFast(SessionContext ctx, Product product, EnumerationValue productGroup)
    {
        if(product == null)
        {
            throw new JaloInvalidParameterException("cannot find tax rows without product ", 0);
        }
        String typeCode = TypeManager.getInstance().getComposedType(TaxRow.class).getCode();
        PK productGroupPk = (productGroup == null) ? null : productGroup.getPK();
        String productId = extractProductId(ctx, product);
        PDTRowsQueryBuilder builder = getPDTRowsQueryBuilderFor(typeCode);
        PDTRowsQueryBuilder.QueryWithParams queryAndParams = builder.withAnyProduct().withProduct(product.getPK()).withProductId(productId).withProductGroup(productGroupPk).build();
        return getSession()
                        .getFlexibleSearch()
                        .search(ctx, queryAndParams.getQuery(), queryAndParams.getParams(), Collections.singletonList(TaxRow.class), true, true, 0, -1)
                        .getResult();
    }


    public Collection getProductDiscountRows(Product product, EnumerationValue productGroup)
    {
        return getProductDiscountRows(getSession().getSessionContext(), product, productGroup);
    }


    public Collection getProductDiscountRows(SessionContext ctx, Product product, EnumerationValue productGroup)
    {
        Collection<DiscountRow> ret = getProductDiscountRowsFast(ctx, product, productGroup);
        if(!useFastAlg(ctx) && ret.size() > 1)
        {
            List<DiscountRow> list = new ArrayList<>(ret);
            Collections.sort(list, DR_COMP);
            ret = list;
        }
        return ret;
    }


    private static final Comparator<DiscountRow> DR_COMP = (Comparator<DiscountRow>)new Object();


    public Collection getProductDiscountRowsFast(SessionContext ctx, Product product, EnumerationValue productGroup)
    {
        if(product == null)
        {
            throw new JaloInvalidParameterException("cannot find price rows without product ", 0);
        }
        PK productGroupPk = (productGroup == null) ? null : productGroup.getPK();
        String productId = extractProductId(ctx, product);
        PDTRowsQueryBuilder builder = getPDTRowsQueryBuilderFor(GeneratedEurope1Constants.TC.DISCOUNTROW);
        PDTRowsQueryBuilder.QueryWithParams queryAndParams = builder.withAnyProduct().withProduct(product.getPK()).withProductId(productId).withProductGroup(productGroupPk).build();
        return getSession()
                        .getFlexibleSearch()
                        .search(ctx, queryAndParams.getQuery(), queryAndParams.getParams(), Collections.singletonList(DiscountRow.class), true, true, 0, -1)
                        .getResult();
    }


    protected String extractProductId(SessionContext ctx, Product product)
    {
        String idFromContext = (ctx != null) ? (String)ctx.getAttribute("productId") : null;
        if(idFromContext != null)
        {
            return idFromContext;
        }
        return extractProductId(product);
    }


    protected String extractProductId(Product product)
    {
        return (product == null) ? null : product.getCode();
    }


    public Collection getUserGlobalDiscountRows(User user, EnumerationValue userGroup) throws JaloInvalidParameterException
    {
        String LEFT_JOIN = " LEFT JOIN ";
        if(user == null && userGroup == null)
        {
            throw new JaloInvalidParameterException("cannot match price without user and user group - at least one must be present", 0);
        }
        StringBuilder select = new StringBuilder();
        StringBuilder query = new StringBuilder();
        TypeManager typeManager = TypeManager.getInstance();
        select.append("SELECT ");
        select.append("{dr:").append(Item.PK).append("}, ");
        select.append("CASE WHEN {dr:").append("startTime").append("} IS NULL THEN 2 ELSE 1 END as drOrd ");
        select.append("FROM {").append(typeManager.getComposedType(GlobalDiscountRow.class).getCode()).append(" AS dr JOIN ");
        select.append(typeManager.getComposedType(Discount.class).getCode()).append(" AS disc ON {dr:")
                        .append("discount").append("}={disc:").append(Item.PK).append("}  LEFT JOIN ");
        select.append(typeManager.getComposedType(Currency.class).getCode()).append(" AS curr ON {dr:")
                        .append("currency").append("}={curr:").append(Item.PK).append("} } ");
        select.append("WHERE ");
        Map<Object, Object> values = new HashMap<>();
        appendUserConditions(query, "dr", user, userGroup, values, "ug");
        query.append(" ORDER BY ");
        query.append("{disc:").append("code").append("} ASC");
        query.append(",drOrd ASC");
        query.append(", CASE WHEN {").append("currency").append("} IS NULL THEN 0 ELSE 1 END ASC");
        query.append(",{curr:").append("isocode").append("} ASC");
        return FlexibleSearch.getInstance()
                        .search(select.toString() + select.toString(), values,
                                        Collections.singletonList(GlobalDiscountRow.class), true, true, 0, -1)
                        .getResult();
    }


    public List matchTaxRows(Product product, EnumerationValue productGroup, User user, EnumerationValue userGroup, Date date, int maxCount) throws JaloPriceFactoryException
    {
        if(product == null && productGroup == null)
        {
            throw new JaloPriceFactoryException("cannot match taxes without product and product group - at least one must be present", 0);
        }
        if(user == null && userGroup == null)
        {
            throw new JaloPriceFactoryException("cannot match taxes without user and user group - at least one must be present", 0);
        }
        if(date == null)
        {
            throw new JaloPriceFactoryException("cannot match taxes without date", 0);
        }
        Collection<TaxRow> rows = queryTax4Price(getSession().getSessionContext(), product, productGroup, user, userGroup);
        if(!rows.isEmpty())
        {
            List<TaxRow> ret = filterTaxRows4Price(rows, date);
            if(ret.size() > 1)
            {
                Collections.sort(ret, (Comparator<? super TaxRow>)new TaxRowMatchComparator(this));
            }
            return ret;
        }
        return Collections.EMPTY_LIST;
    }


    protected List<TaxRow> filterTaxRows4Price(Collection<TaxRow> rows, Date date)
    {
        if(rows.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<TaxRow> ret = new ArrayList<>(rows);
        for(ListIterator<TaxRow> it = ret.listIterator(); it.hasNext(); )
        {
            TaxRow taxRow = it.next();
            StandardDateRange standardDateRange = taxRow.getDateRange();
            if(standardDateRange != null && !standardDateRange.encloses(date))
            {
                it.remove();
            }
        }
        return ret;
    }


    protected Collection<TaxRow> superQueryTax4Price(SessionContext ctx, Product product, EnumerationValue productGroup, User user, EnumerationValue userGroup)
    {
        String taxRowTypeCode = TypeManager.getInstance().getComposedType(TaxRow.class).getCode();
        PK productPk = (product == null) ? null : product.getPK();
        PK productGroupPk = (productGroup == null) ? null : productGroup.getPK();
        PK userPk = (user == null) ? null : user.getPK();
        PK userGroupPk = (userGroup == null) ? null : userGroup.getPK();
        String productId = extractProductId(ctx, product);
        PDTRowsQueryBuilder builder = getPDTRowsQueryBuilderFor(taxRowTypeCode);
        PDTRowsQueryBuilder.QueryWithParams queryAndParams = builder.withAnyProduct().withAnyUser().withProduct(productPk).withProductGroup(productGroupPk).withProductId(productId).withUser(userPk).withUserGroup(userGroupPk).build();
        return FlexibleSearch.getInstance().search(ctx, queryAndParams.getQuery(), queryAndParams.getParams(), TaxRow.class)
                        .getResult();
    }


    protected Collection<TaxRow> queryTax4Price(SessionContext ctx, Product product, EnumerationValue productGroup, User user, EnumerationValue userGroup)
    {
        Collection<TaxRow> results = null;
        CatalogManager catalogManager = CatalogManager.getInstance();
        for(TaxRow taxRow : superQueryTax4Price(ctx, product, productGroup, user, userGroup))
        {
            CatalogVersion taxRowCatalogVersion = catalogManager.getCatalogVersion((Item)taxRow);
            if(taxRowCatalogVersion == null || taxRowCatalogVersion.equals(catalogManager.getCatalogVersion(product)))
            {
                if(results == null)
                {
                    results = new ArrayList<>();
                }
                results.add(taxRow);
            }
        }
        return (results == null) ? Collections.EMPTY_LIST : results;
    }


    protected void appendProductConditions(StringBuilder query, String alias, Product product, EnumerationValue productGroup, Map<String, Long> values, String pgConstant)
    {
        query.append("{").append(alias).append(":").append("productMatchQualifier").append("} IN ( ?anyP ");
        values.put("anyP", Long.valueOf(PK.NULL_PK.getLongValue()));
        if(product != null)
        {
            query.append(",?product ");
            values.put("product", Long.valueOf(product.getPK().getLongValue()));
        }
        if(productGroup != null)
        {
            query.append(",?pg ");
            values.put("pg", Long.valueOf(productGroup.getPK().getLongValue()));
        }
        query.append(")");
    }


    protected void appendUserConditions(StringBuilder query, String alias, User user, EnumerationValue userGroup, Map<String, Long> values, String ugConst)
    {
        query.append("{").append(alias).append(":").append("userMatchQualifier").append("} IN ( ?anyU ");
        values.put("anyU", Long.valueOf(PK.NULL_PK.getLongValue()));
        if(user != null)
        {
            query.append(",?user ");
            values.put("user", Long.valueOf(user.getPK().getLongValue()));
        }
        if(userGroup != null)
        {
            query.append(",?ug ");
            values.put("ug", Long.valueOf(userGroup.getPK().getLongValue()));
        }
        query.append(")");
    }


    protected EnumerationValue getEnumFromContextOrItem(SessionContext ctx, ExtensibleItem item, String qualifier)
    {
        EnumerationValue enumerationValue = (ctx != null) ? (EnumerationValue)ctx.getAttribute(qualifier) : null;
        if(enumerationValue == null)
        {
            enumerationValue = (item != null) ? (EnumerationValue)item.getProperty(ctx, qualifier) : null;
        }
        return enumerationValue;
    }


    protected EnumerationValue getPTG(SessionContext ctx, AbstractOrderEntry entry)
    {
        EnumerationValue overridePTG = getEurope1PriceFactory_PTG(ctx, entry);
        return (overridePTG == null) ? getPTG(ctx, entry.getProduct()) : overridePTG;
    }


    public EnumerationValue getPTG(SessionContext ctx, Product product)
    {
        return getEnumFromContextOrItem(ctx, (ExtensibleItem)product, "Europe1PriceFactory_PTG");
    }


    protected EnumerationValue getPDG(SessionContext ctx, AbstractOrderEntry entry)
    {
        EnumerationValue overridePDG = getEurope1PriceFactory_PDG(ctx, entry);
        return (overridePDG == null) ? getPDG(ctx, entry.getProduct()) : overridePDG;
    }


    public EnumerationValue getPDG(SessionContext ctx, Product product)
    {
        return getEnumFromContextOrItem(ctx, (ExtensibleItem)product, "Europe1PriceFactory_PDG");
    }


    protected EnumerationValue getPPG(SessionContext ctx, AbstractOrderEntry entry)
    {
        EnumerationValue overridePPG = getEurope1PriceFactory_PPG(ctx, entry);
        return (overridePPG == null) ? getPPG(ctx, entry.getProduct()) : overridePPG;
    }


    public EnumerationValue getPPG(SessionContext ctx, Product product)
    {
        return getEnumFromContextOrItem(ctx, (ExtensibleItem)product, "Europe1PriceFactory_PPG");
    }


    protected EnumerationValue getUTG(SessionContext ctx, AbstractOrderEntry entry) throws JaloPriceFactoryException
    {
        return getUTG(ctx, entry.getOrder());
    }


    protected EnumerationValue getUTG(SessionContext ctx, AbstractOrder order) throws JaloPriceFactoryException
    {
        EnumerationValue overrideUTG = getEurope1PriceFactory_UTG(ctx, order);
        return (overrideUTG == null) ? getUTG(ctx, order.getUser()) : overrideUTG;
    }


    public EnumerationValue getUTG(SessionContext ctx, User user) throws JaloPriceFactoryException
    {
        EnumerationValue enumerationValue = getEnumFromContextOrItem(ctx, (ExtensibleItem)user, "Europe1PriceFactory_UTG");
        return (enumerationValue != null) ? enumerationValue : getEnumFromGroups(user, "userTaxGroup");
    }


    public EnumerationValue getUDG(SessionContext ctx, User user) throws JaloPriceFactoryException
    {
        EnumerationValue enumerationValue = getEnumFromContextOrItem(ctx, (ExtensibleItem)user, "Europe1PriceFactory_UDG");
        return (enumerationValue != null) ? enumerationValue : getEnumFromGroups(user, "userDiscountGroup");
    }


    protected EnumerationValue getUDG(SessionContext ctx, AbstractOrderEntry entry) throws JaloPriceFactoryException
    {
        return getUDG(ctx, entry.getOrder());
    }


    protected EnumerationValue getUDG(SessionContext ctx, AbstractOrder order) throws JaloPriceFactoryException
    {
        EnumerationValue overrideUDG = getEurope1PriceFactory_UDG(ctx, order);
        return (overrideUDG == null) ? getUDG(ctx, order.getUser()) : overrideUDG;
    }


    public EnumerationValue getUPG(SessionContext ctx, User user) throws JaloPriceFactoryException
    {
        EnumerationValue enumerationValue = getEnumFromContextOrItem(ctx, (ExtensibleItem)user, "Europe1PriceFactory_UPG");
        return (enumerationValue != null) ? enumerationValue : getEnumFromGroups(user, "userPriceGroup");
    }


    protected EnumerationValue getUPG(SessionContext ctx, AbstractOrderEntry entry) throws JaloPriceFactoryException
    {
        return getUPG(ctx, entry.getOrder());
    }


    protected EnumerationValue getUPG(SessionContext ctx, AbstractOrder order) throws JaloPriceFactoryException
    {
        EnumerationValue overrideUPG = getEurope1PriceFactory_UPG(ctx, order);
        return (overrideUPG == null) ? getUPG(ctx, order.getUser()) : overrideUPG;
    }


    protected EnumerationValue getEnumFromGroups(User user, String attribute) throws JaloPriceFactoryException
    {
        EnumerationValue enumerationValue = null;
        Set<UserGroup> controlSet = new HashSet();
        Collection groups = user.getGroups();
        while(enumerationValue == null && !groups.isEmpty())
        {
            Collection nextGroups = new HashSet();
            for(Iterator<UserGroup> it = groups.iterator(); it.hasNext(); )
            {
                UserGroup userGroup = it.next();
                controlSet.add(userGroup);
                EnumerationValue ugValue = (EnumerationValue)userGroup.getProperty(attribute);
                if(ugValue != null)
                {
                    if(enumerationValue != null && !ugValue.equals(enumerationValue))
                    {
                        throw new JaloPriceFactoryException("multiple " + attribute + " values found for user " + user.getUID() + " from its groups " + groups + " : " + enumerationValue
                                        .getCode() + " != " + ugValue
                                        .getCode(), 0);
                    }
                    enumerationValue = ugValue;
                    continue;
                }
                if(enumerationValue == null)
                {
                    nextGroups.addAll(userGroup.getGroups());
                }
            }
            if(enumerationValue == null)
            {
                nextGroups.removeAll(controlSet);
                groups = nextGroups;
            }
        }
        return enumerationValue;
    }


    public EnumerationValue getUserPriceGroup(String code)
    {
        try
        {
            return getSession().getEnumerationManager().getEnumerationValue("UserPriceGroup", code);
        }
        catch(JaloItemNotFoundException e)
        {
            return null;
        }
    }


    public EnumerationValue createUserPriceGroup(String code) throws ConsistencyCheckException
    {
        return getSession().getEnumerationManager().createEnumerationValue("UserPriceGroup", code);
    }


    public EnumerationValue getProductTaxGroup(String code)
    {
        try
        {
            return getSession().getEnumerationManager().getEnumerationValue("ProductTaxGroup", code);
        }
        catch(JaloItemNotFoundException e)
        {
            return null;
        }
    }


    public EnumerationValue createProductTaxGroup(String code) throws ConsistencyCheckException
    {
        return getSession().getEnumerationManager().createEnumerationValue("ProductTaxGroup", code);
    }


    public EnumerationValue getUserTaxGroup(String code)
    {
        try
        {
            return getSession().getEnumerationManager().getEnumerationValue("UserTaxGroup", code);
        }
        catch(JaloItemNotFoundException e)
        {
            return null;
        }
    }


    public EnumerationValue createUserTaxGroup(String code) throws ConsistencyCheckException
    {
        return getSession().getEnumerationManager().createEnumerationValue("UserTaxGroup", code);
    }


    public boolean isCreatorDisabled()
    {
        return false;
    }


    public void createProjectData(Map params, JspContext jspc)
    {
    }


    protected Date adjustDate(Date date)
    {
        if(date == null)
        {
            return null;
        }
        String propStr = getTenant().getConfig().getString("europe1.price.accuracy", Europe1Constants.PRICE_ACCURACY_PROPERTY.DEFAULT
                        .getFullName());
        if(propStr == null)
        {
            return date;
        }
        Europe1Constants.PRICE_ACCURACY_PROPERTY.Value propValue = Europe1Constants.PRICE_ACCURACY_PROPERTY.Value.lookUp(propStr);
        if(propValue == null)
        {
            LOG.warn("**********");
            LOG.warn("Invalid value '" + propStr + "' for property 'europe1.price.accuracy', consult project.properties for valid settings.");
            LOG.warn("Using default value '" + Europe1Constants.PRICE_ACCURACY_PROPERTY.DEFAULT.getFullName() + "'.");
            LOG.warn("**********");
            propValue = Europe1Constants.PRICE_ACCURACY_PROPERTY.DEFAULT;
        }
        if(!getTenant().getConfig().getBoolean(Config.Params.BYPASS_HYBRIS_RECOMMENDATIONS, false))
        {
            if(propValue.ordinal() < Europe1Constants.PRICE_ACCURACY_PROPERTY.LIMIT.ordinal())
            {
                LOG.warn("**********");
                LOG.warn("Value '" + propStr + "' for property 'europe1.price.accuracy' is finer than the finest allowed value '" + Europe1Constants.PRICE_ACCURACY_PROPERTY.LIMIT
                                .getFullName() + "'. For more information please contact the hybris support.");
                LOG.warn("Using finest allowed value '" + Europe1Constants.PRICE_ACCURACY_PROPERTY.LIMIT.getFullName() + "'.");
                LOG.warn("**********");
                propValue = Europe1Constants.PRICE_ACCURACY_PROPERTY.LIMIT;
            }
        }
        Calendar now = Utilities.getDefaultCalendar();
        now.setTime(date);
        switch(null.$SwitchMap$de$hybris$platform$europe1$constants$Europe1Constants$PRICE_ACCURACY_PROPERTY$Value[propValue.ordinal()])
        {
            case 1:
                now.set(11, 0);
            case 2:
                now.set(12, 0);
            case 3:
                now.set(13, 0);
            case 4:
                now.set(14, 0);
                break;
        }
        return now.getTime();
    }


    protected Collection<CachedTaxValue> getSuperCachedTaxes(Product product, EnumerationValue productTaxGroup, User user, EnumerationValue userTaxGroup, Date date)
    {
        if(isCachingTaxes())
        {
            long ANY = PK.NULL_PK.getLongValue();
            long pPK = (product != null) ? product.getPK().getLongValue() : ANY;
            long ptgPK = (productTaxGroup != null) ? productTaxGroup.getPK().getLongValue() : ANY;
            long uPK = (user != null) ? user.getPK().getLongValue() : ANY;
            long utgPK = (userTaxGroup != null) ? userTaxGroup.getPK().getLongValue() : ANY;
            Collection<CachedTaxValue> ret = null;
            Set<CachedTax> processed = null;
            Collection<CatalogVersionAwareCachedTax> matches = (Collection<CatalogVersionAwareCachedTax>)this.catalogAwareCachedTaxes.get(pPK);
            if(matches != null)
            {
                processed = new HashSet<>();
                for(CachedTax ct : matches)
                {
                    if(processed.add(ct))
                    {
                        if(ct.matches(pPK, ptgPK, uPK, utgPK, date))
                        {
                            if(ret == null)
                            {
                                ret = new ArrayList<>();
                            }
                            ret.add(ct.value);
                        }
                    }
                }
            }
            matches = (Collection<CatalogVersionAwareCachedTax>)this.catalogAwareCachedTaxes.get(ptgPK);
            if(matches != null)
            {
                if(processed == null)
                {
                    processed = new HashSet<>();
                }
                for(CachedTax ct : matches)
                {
                    if(processed.add(ct))
                    {
                        if(ct.matches(pPK, ptgPK, uPK, utgPK, date))
                        {
                            if(ret == null)
                            {
                                ret = new ArrayList<>();
                            }
                            ret.add(ct.value);
                        }
                    }
                }
            }
            matches = (Collection<CatalogVersionAwareCachedTax>)this.catalogAwareCachedTaxes.get(uPK);
            if(matches != null)
            {
                if(processed == null)
                {
                    processed = new HashSet<>();
                }
                for(CachedTax ct : matches)
                {
                    if(processed.add(ct))
                    {
                        if(ct.matches(pPK, ptgPK, uPK, utgPK, date))
                        {
                            if(ret == null)
                            {
                                ret = new ArrayList<>();
                            }
                            ret.add(ct.value);
                        }
                    }
                }
            }
            matches = (Collection<CatalogVersionAwareCachedTax>)this.catalogAwareCachedTaxes.get(utgPK);
            if(matches != null)
            {
                if(processed == null)
                {
                    processed = new HashSet<>();
                }
                for(CachedTax ct : matches)
                {
                    if(processed.add(ct))
                    {
                        if(ct.matches(pPK, ptgPK, uPK, utgPK, date))
                        {
                            if(ret == null)
                            {
                                ret = new ArrayList<>();
                            }
                            ret.add(ct.value);
                        }
                    }
                }
            }
            if(utgPK != ANY && utgPK != ANY && pPK != ANY && ptgPK != ANY)
            {
                matches = (Collection<CatalogVersionAwareCachedTax>)this.catalogAwareCachedTaxes.get(ANY);
                if(matches != null)
                {
                    if(processed == null)
                    {
                        processed = new HashSet<>();
                    }
                    for(CachedTax ct : matches)
                    {
                        if(processed.add(ct))
                        {
                            if(ct.matches(pPK, ptgPK, uPK, utgPK, date))
                            {
                                if(ret == null)
                                {
                                    ret = new ArrayList<>();
                                }
                                ret.add(ct.value);
                            }
                        }
                    }
                }
            }
            return (ret != null) ? ret : Collections.EMPTY_SET;
        }
        return null;
    }


    protected boolean isCachingTaxes()
    {
        Boolean localCaches = this.cachesTaxes;
        if(localCaches == null)
        {
            synchronized(this)
            {
                localCaches = this.cachesTaxes;
                if(localCaches == null)
                {
                    if(getTenant().getConfig().getBoolean("europe1.cache.taxes", true))
                    {
                        fillTaxCache();
                        localCaches = Boolean.TRUE;
                    }
                    else
                    {
                        localCaches = Boolean.FALSE;
                    }
                    this.cachesTaxes = localCaches;
                }
            }
        }
        return localCaches.booleanValue();
    }


    protected void fillTaxCache()
    {
        SessionContext ctx = null;
        try
        {
            ctx = JaloSession.getCurrentSession().createLocalSessionContext();
            ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            FlexibleSearch flexibleSearch = JaloSession.getCurrentSession().getFlexibleSearch();
            List<TaxRow> taxRows = flexibleSearch.search("SELECT {PK} FROM {" + GeneratedEurope1Constants.TC.TAXROW + "}", TaxRow.class).getResult();
            this.catalogAwareCachedTaxes.clear();
            for(TaxRow tr : taxRows)
            {
                if(tr == null)
                {
                    continue;
                }
                CatalogVersionAwareCachedTax catalogVersionAwareCachedTax = new CatalogVersionAwareCachedTax(tr);
                Collection<CatalogVersionAwareCachedTax> coll = (Collection<CatalogVersionAwareCachedTax>)this.catalogAwareCachedTaxes.get(catalogVersionAwareCachedTax
                                .getProductMatchQualifier());
                if(coll == null)
                {
                    this.catalogAwareCachedTaxes.put(catalogVersionAwareCachedTax.getProductMatchQualifier(), coll = new ArrayList<>());
                }
                coll.add(catalogVersionAwareCachedTax);
                coll = (Collection<CatalogVersionAwareCachedTax>)this.catalogAwareCachedTaxes.get(catalogVersionAwareCachedTax.getUserMatchQualifier());
                if(coll == null)
                {
                    this.catalogAwareCachedTaxes.put(catalogVersionAwareCachedTax.getUserMatchQualifier(), coll = new ArrayList<>());
                }
                coll.add(catalogVersionAwareCachedTax);
            }
        }
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
    }


    protected Collection<CachedTaxValue> getCachedTaxes(Product product, EnumerationValue productTaxGroup, User user, EnumerationValue userTaxGroup, Date date)
    {
        if(isCachingTaxes())
        {
            long pPK = (product != null) ? product.getPK().getLongValue() : MATCH_ANY;
            long ptgPK = (productTaxGroup != null) ? productTaxGroup.getPK().getLongValue() : MATCH_ANY;
            long uPK = (user != null) ? user.getPK().getLongValue() : MATCH_ANY;
            long utgPK = (userTaxGroup != null) ? userTaxGroup.getPK().getLongValue() : MATCH_ANY;
            CatalogVersion catalogVersion = CatalogManager.getInstance().getCatalogVersion(product);
            (new long[1])[0] = catalogVersion
                            .getPK().getLongValue();
            long[] productCatalogVersionPK = (catalogVersion == null) ? ANY_COLLECTION : new long[1];
            Collection<CachedTaxValue> ret = null;
            Set<CachedTax> processed = null;
            Collection<CatalogVersionAwareCachedTax> matches = (Collection<CatalogVersionAwareCachedTax>)this.catalogAwareCachedTaxes.get(pPK);
            if(matches != null)
            {
                processed = new HashSet<>();
                for(CatalogVersionAwareCachedTax ct : matches)
                {
                    if(processed.add(ct))
                    {
                        if(ct.matches(pPK, ptgPK, uPK, utgPK, date, productCatalogVersionPK))
                        {
                            if(ret == null)
                            {
                                ret = new ArrayList<>();
                            }
                            ret.add(ct.getValue());
                        }
                    }
                }
            }
            matches = (Collection<CatalogVersionAwareCachedTax>)this.catalogAwareCachedTaxes.get(ptgPK);
            if(matches != null)
            {
                if(processed == null)
                {
                    processed = new HashSet<>();
                }
                for(CatalogVersionAwareCachedTax ct : matches)
                {
                    if(processed.add(ct))
                    {
                        if(ct.matches(pPK, ptgPK, uPK, utgPK, date, productCatalogVersionPK))
                        {
                            if(ret == null)
                            {
                                ret = new ArrayList<>();
                            }
                            ret.add(ct.getValue());
                        }
                    }
                }
            }
            matches = (Collection<CatalogVersionAwareCachedTax>)this.catalogAwareCachedTaxes.get(uPK);
            if(matches != null)
            {
                if(processed == null)
                {
                    processed = new HashSet<>();
                }
                for(CatalogVersionAwareCachedTax ct : matches)
                {
                    if(processed.add(ct))
                    {
                        if(ct.matches(pPK, ptgPK, uPK, utgPK, date, productCatalogVersionPK))
                        {
                            if(ret == null)
                            {
                                ret = new ArrayList<>();
                            }
                            ret.add(ct.getValue());
                        }
                    }
                }
            }
            matches = (Collection<CatalogVersionAwareCachedTax>)this.catalogAwareCachedTaxes.get(utgPK);
            if(matches != null)
            {
                if(processed == null)
                {
                    processed = new HashSet<>();
                }
                for(CatalogVersionAwareCachedTax ct : matches)
                {
                    if(processed.add(ct))
                    {
                        if(ct.matches(pPK, ptgPK, uPK, utgPK, date, productCatalogVersionPK))
                        {
                            if(ret == null)
                            {
                                ret = new ArrayList<>();
                            }
                            ret.add(ct.getValue());
                        }
                    }
                }
            }
            if(utgPK != MATCH_ANY && utgPK != MATCH_ANY && pPK != MATCH_ANY && ptgPK != MATCH_ANY)
            {
                matches = (Collection<CatalogVersionAwareCachedTax>)this.catalogAwareCachedTaxes.get(MATCH_ANY);
                if(matches != null)
                {
                    if(processed == null)
                    {
                        processed = new HashSet<>();
                    }
                    for(CatalogVersionAwareCachedTax ct : matches)
                    {
                        if(processed.add(ct))
                        {
                            if(ct.matches(pPK, ptgPK, uPK, utgPK, date, productCatalogVersionPK))
                            {
                                if(ret == null)
                                {
                                    ret = new ArrayList<>();
                                }
                                ret.add(ct.getValue());
                            }
                        }
                    }
                }
            }
            return (ret != null) ? ret : Collections.EMPTY_SET;
        }
        return null;
    }


    private void createSearchRestrictions(JspContext jspc)
    {
        String alias = "item";
        TypeManager typeman = getSession().getTypeManager();
        UserGroup catalogViewers = getSession().getUserManager().getUserGroupByGroupID(Constants.USER.CUSTOMER_USERGROUP);
        if(typeman.getSearchRestriction(typeman.getComposedType(PriceRow.class), "Frontend_Group_PriceRows") == null)
        {
            typeman.createRestriction("Frontend_Group_PriceRows", (Principal)catalogViewers, typeman.getComposedType(PriceRow.class),
                            "(  {item:product} IS NOT NULL OR{item:" + Europe1Constants.Attributes.TaxRow.CATALOGVERSION + "} IS NULL OR {item:" + Europe1Constants.Attributes.PriceRow.CATALOGVERSION + "} IN (?session.catalogversions) )");
        }
        if(typeman.getSearchRestriction(typeman.getComposedType(TaxRow.class), "Frontend_Group_TaxRows") == null)
        {
            typeman.createRestriction("Frontend_Group_TaxRows", (Principal)catalogViewers, typeman.getComposedType(TaxRow.class),
                            "(  {item:product} IS NOT NULL OR{item:" + Europe1Constants.Attributes.TaxRow.CATALOGVERSION + "} IS NULL OR {item:" + Europe1Constants.Attributes.TaxRow.CATALOGVERSION + "} IN (?session.catalogversions) )");
        }
        if(typeman.getSearchRestriction(typeman.getComposedType(DiscountRow.class), "Frontend_Group_DiscountRows") == null)
        {
            typeman.createRestriction("Frontend_Group_DiscountRows", (Principal)catalogViewers, typeman.getComposedType(DiscountRow.class),
                            "(  {item:product} IS NOT NULL OR{item:" + Europe1Constants.Attributes.TaxRow.CATALOGVERSION + "} IS NULL OR {item:" + Europe1Constants.Attributes.DiscountRow.CATALOGVERSION + "} IN (?session.catalogversions) )");
        }
    }


    public void createEssentialData(Map params, JspContext jspc) throws Exception
    {
        if("init".equals(params.get("initmethod")))
        {
            createSearchRestrictions(jspc);
        }
    }


    protected PriceRow getCounterpartItem(SessionContext ctx, PriceRow pricerow, CatalogVersion targetVersion)
    {
        Preconditions.checkArgument((pricerow.getProduct() == null));
        EnumerationValue pricegroup = pricerow.getPg();
        User user = pricerow.getUser();
        EnumerationValue usergroup = pricerow.getUg();
        Map<String, Object> params = new HashMap<>();
        if(pricegroup != null)
        {
            params.put("pg", pricegroup);
        }
        if(user != null)
        {
            params.put("u", user);
        }
        if(usergroup != null)
        {
            params.put("ug", usergroup);
        }
        params.put("unit", pricerow.getUnit());
        params.put("min", pricerow.getMinqtd());
        params.put("curr", pricerow.getCurrency());
        params.put("net", pricerow.isNet());
        params.put("tgt", targetVersion);
        List<PriceRow> rows = FlexibleSearch.getInstance()
                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedEurope1Constants.TC.PRICEROW + "} WHERE {pg}" + ((pricegroup == null) ? " IS NULL" : " = ?pg") + " AND {user}" + ((user == null) ? " IS NULL" : " = ?u") + " AND {ug}" + ((usergroup == null) ? " IS NULL" : " = ?ug")
                                        + " AND {unit} = ?unit AND {minqtd} = ?min AND {currency} = ?curr AND {net} = ?net AND {" + Europe1Constants.Attributes.PriceRow.CATALOGVERSION + "} = ?tgt ", params, PriceRow.class).getResult();
        return rows.isEmpty() ? null : rows.get(0);
    }


    protected TaxRow getCounterpartItem(SessionContext ctx, TaxRow taxrow, CatalogVersion targetVersion)
    {
        Preconditions.checkArgument((taxrow.getProduct() == null));
        EnumerationValue pricegroup = taxrow.getPg();
        User user = taxrow.getUser();
        EnumerationValue usergroup = taxrow.getUg();
        Map<String, Object> params = new HashMap<>();
        if(pricegroup != null)
        {
            params.put("pg", pricegroup);
        }
        if(user != null)
        {
            params.put("u", user);
        }
        if(usergroup != null)
        {
            params.put("ug", usergroup);
        }
        params.put("tax", taxrow.getTax());
        params.put("tgt", targetVersion);
        List<TaxRow> rows = FlexibleSearch.getInstance()
                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedEurope1Constants.TC.TAXROW + "} WHERE {pg}" + ((pricegroup == null) ? " IS NULL" : " = ?pg") + " AND {user}" + ((user == null) ? " IS NULL" : " = ?u") + " AND {ug}" + ((usergroup == null) ? " IS NULL" : " = ?ug")
                                        + " AND {tax} = ?tax AND {" + Europe1Constants.Attributes.TaxRow.CATALOGVERSION + "} = ?tgt ", params, TaxRow.class).getResult();
        return rows.isEmpty() ? null : rows.get(0);
    }


    protected DiscountRow getCounterpartItem(SessionContext ctx, DiscountRow discountrow, CatalogVersion targetVersion)
    {
        Preconditions.checkArgument((discountrow.getProduct() == null));
        EnumerationValue pricegroup = discountrow.getPg();
        User user = discountrow.getUser();
        EnumerationValue usergroup = discountrow.getUg();
        Map<String, Object> params = new HashMap<>();
        if(pricegroup != null)
        {
            params.put("pg", pricegroup);
        }
        if(user != null)
        {
            params.put("u", user);
        }
        if(usergroup != null)
        {
            params.put("ug", usergroup);
        }
        params.put("discount", discountrow.getDiscount());
        params.put("tgt", targetVersion);
        List<DiscountRow> rows = FlexibleSearch.getInstance()
                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedEurope1Constants.TC.TAXROW + "} WHERE {pg}" + ((pricegroup == null) ? " IS NULL" : " = ?pg") + " AND {user}" + ((user == null) ? " IS NULL" : " = ?u") + " AND {ug}" + ((usergroup == null) ? " IS NULL" : " = ?ug")
                                        + " AND {tax} = ?tax AND {" + Europe1Constants.Attributes.TaxRow.CATALOGVERSION + "} = ?tgt ", params, DiscountRow.class).getResult();
        return rows.isEmpty() ? null : rows.get(0);
    }


    public void beforeItemCreation(SessionContext ctx, ComposedType type, Item.ItemAttributeMap attributes) throws JaloBusinessException
    {
        super.beforeItemCreation(ctx, type, attributes);
        Class<?> jaloClass = type.getJaloClass();
        if(JobLog.class.isAssignableFrom(jaloClass) || ItemSyncTimestamp.class.isAssignableFrom(jaloClass) || ProductFeature.class
                        .isAssignableFrom(jaloClass) || ChangeDescriptor.class.isAssignableFrom(jaloClass))
        {
            return;
        }
        if(PriceRow.class.isAssignableFrom(jaloClass))
        {
            CatalogVersion catver = (CatalogVersion)attributes.get(Europe1Constants.Attributes.PriceRow.CATALOGVERSION);
            if(catver == null)
            {
                Product prod = (Product)attributes.get("product");
                if(prod != null)
                {
                    catver = CatalogManager.getInstance().getCatalogVersion(null, prod);
                }
                if(catver != null)
                {
                    attributes.put(Europe1Constants.Attributes.PriceRow.CATALOGVERSION, catver);
                }
            }
            attributes.setAttributeMode(Europe1Constants.Attributes.PriceRow.CATALOGVERSION, Item.AttributeMode.INITIAL);
        }
        else if(TaxRow.class.isAssignableFrom(jaloClass))
        {
            CatalogVersion catver = (CatalogVersion)attributes.get(Europe1Constants.Attributes.TaxRow.CATALOGVERSION);
            if(catver == null)
            {
                Product prod = (Product)attributes.get("product");
                if(prod != null)
                {
                    catver = CatalogManager.getInstance().getCatalogVersion(null, prod);
                }
                if(catver != null)
                {
                    attributes.put(Europe1Constants.Attributes.TaxRow.CATALOGVERSION, catver);
                }
            }
            attributes.setAttributeMode(Europe1Constants.Attributes.TaxRow.CATALOGVERSION, Item.AttributeMode.INITIAL);
        }
        else if(DiscountRow.class.isAssignableFrom(jaloClass))
        {
            CatalogVersion catver = (CatalogVersion)attributes.get(Europe1Constants.Attributes.DiscountRow.CATALOGVERSION);
            if(catver == null)
            {
                Product prod = (Product)attributes.get("product");
                if(prod != null)
                {
                    catver = CatalogManager.getInstance().getCatalogVersion(null, prod);
                }
                if(catver != null)
                {
                    attributes.put(Europe1Constants.Attributes.DiscountRow.CATALOGVERSION, catver);
                }
            }
            attributes.setAttributeMode(Europe1Constants.Attributes.DiscountRow.CATALOGVERSION, Item.AttributeMode.INITIAL);
        }
    }


    @Required
    public void setRetrieveChannelStrategy(RetrieveChannelStrategy retrieveChannelStrategy)
    {
        this.retrieveChannelStrategy = retrieveChannelStrategy;
    }
}
