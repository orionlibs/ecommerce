package de.hybris.platform.promotions.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.util.Comparators;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.map.Flat3Map;
import org.apache.log4j.Logger;

public abstract class AbstractPromotion extends GeneratedAbstractPromotion
{
    private static final Logger LOG = Logger.getLogger(AbstractPromotion.class.getName());


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(!allAttributes.containsKey("enabled"))
        {
            allAttributes.put("enabled", Boolean.FALSE);
        }
        if(!allAttributes.containsKey("PromotionGroup"))
        {
            allAttributes.put("PromotionGroup", PromotionsManager.getInstance().getDefaultPromotionGroup(ctx));
        }
        return super.createItem(ctx, type, allAttributes);
    }


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        setRestrictions(ctx, Collections.emptyList());
        super.remove(ctx);
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return getClass().getSimpleName() + " '" + getClass().getSimpleName() + "' (" + getCode() + ")";
    }


    public String getPromotionType(SessionContext ctx)
    {
        return getComposedType().getName(ctx);
    }


    public Map getAllPromotionType(SessionContext ctx)
    {
        return getComposedType().getAllNames(ctx);
    }


    public void setPromotionGroup(SessionContext ctx, PromotionGroup promotionGroup)
    {
        if(promotionGroup == null)
        {
            throw new JaloInvalidParameterException("Cannot set promotionGroup to NULL", 999);
        }
        super.setPromotionGroup(ctx, promotionGroup);
    }


    public abstract List<PromotionResult> evaluate(SessionContext paramSessionContext, PromotionEvaluationContext paramPromotionEvaluationContext);


    public abstract String getResultDescription(SessionContext paramSessionContext, PromotionResult paramPromotionResult, Locale paramLocale);


    protected final Double getPriceForOrder(SessionContext ctx, Collection<PromotionPriceRow> prices, AbstractOrder order, String fieldLabel)
    {
        if(order == null)
        {
            return null;
        }
        Currency currency = order.getCurrency(ctx);
        if(currency == null)
        {
            LOG.warn("Order [" + order + "] has null currency");
            return null;
        }
        if(prices != null)
        {
            for(PromotionPriceRow ppr : prices)
            {
                if(currency.equals(ppr.getCurrency(ctx)))
                {
                    return ppr.getPrice(ctx);
                }
            }
        }
        LOG.warn("Missing currency row [" + currency.getName(ctx) + "] for [" + fieldLabel + "] on promotion [" + this + "]");
        return null;
    }


    protected static final String formatMessage(String pattern, Object[] arguments, Locale locale)
    {
        if(pattern == null)
        {
            return "Error, message not found";
        }
        MessageFormat mf = new MessageFormat(pattern, locale);
        return mf.format(arguments);
    }


    protected final AbstractPromotion findOrCreateImmutableClone(SessionContext ctx)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("findImmutableClone [" + this + "]");
        }
        if(getImmutableKeyHash(ctx) != null)
        {
            return this;
        }
        String immutableKey = getDataUniqueKey(ctx);
        String immutableKeyHash = buildMD5Hash(immutableKey);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("findImmutableClone [" + this + "] immutableKey=[" + immutableKey + "] immutableKeyHash=[" + immutableKeyHash + "]");
        }
        AbstractPromotion immutableInstance = findImmutablePromotionByUniqueKey(getSession(), ctx, immutableKeyHash, immutableKey);
        if(immutableInstance == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("findImmutableClone [" + this + "] creating new clone of promotion");
            }
            immutableInstance = createImmutableDeepClone(ctx, immutableKeyHash, immutableKey);
        }
        return immutableInstance;
    }


    protected static final String buildMD5Hash(String message)
    {
        return DigestUtils.md5Hex(message);
    }


    protected final AbstractPromotion createImmutableDeepClone(SessionContext ctx, String immutableKeyHash, String immutableKey)
    {
        try
        {
            Map<String, String> values = getAllAttributes(ctx);
            values.remove(Item.PK);
            values.remove(Item.MODIFIED_TIME);
            values.remove(Item.CREATION_TIME);
            values.remove("savedvalues");
            values.remove("customLinkQualifier");
            values.remove("synchronizedCopies");
            values.remove("synchronizationSources");
            values.remove("alldocuments");
            values.remove(Item.TYPE);
            values.remove(Item.OWNER);
            values.put("immutableKey", immutableKey);
            values.put("immutableKeyHash", immutableKeyHash);
            values.put("enabled", Boolean.FALSE);
            deepCloneAttributes(ctx, values);
            ComposedType type = TypeManager.getInstance().getComposedType(getClass());
            AbstractPromotion dupAbstractPromotion = null;
            try
            {
                dupAbstractPromotion = (AbstractPromotion)type.newInstance(ctx, values);
            }
            catch(JaloGenericCreationException | de.hybris.platform.jalo.type.JaloAbstractTypeException ex)
            {
                LOG.warn("createDeepClone failed to create instance of AbstractPromotion", (Throwable)ex);
            }
            return dupAbstractPromotion;
        }
        catch(JaloSecurityException ex)
        {
            LOG.warn("createDeepClone failed to get attributes from [" + this + "]", (Throwable)ex);
            return null;
        }
    }


    protected static final AbstractPromotion findImmutablePromotionByUniqueKey(JaloSession jaloSession, SessionContext ctx, String immutableKeyHash, String immutableKey)
    {
        String query = "SELECT {" + Item.PK + "} FROM   {" + TypeManager.getInstance().getComposedType(AbstractPromotion.class).getCode() + "} WHERE  {immutableKeyHash} = ?immutableKeyHash";
        HashMap<Object, Object> args = new HashMap<>();
        args.put("immutableKeyHash", immutableKeyHash);
        List<AbstractPromotion> matchingPromotions = jaloSession.getFlexibleSearch().search(ctx, query, args, AbstractPromotion.class).getResult();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("findImmutablePromotionByUniqueKey found [" + matchingPromotions.size() + "] promotions with immutable key hash=[" + immutableKeyHash + "]");
        }
        if(!matchingPromotions.isEmpty())
        {
            for(AbstractPromotion promo : matchingPromotions)
            {
                if(immutableKey.equals(promo.getImmutableKey(ctx)))
                {
                    return promo;
                }
            }
        }
        return null;
    }


    protected final String getDataUniqueKey(SessionContext ctx)
    {
        StringBuilder builder = new StringBuilder();
        buildDataUniqueKey(ctx, builder);
        return builder.toString();
    }


    protected void buildDataUniqueKey(SessionContext ctx, StringBuilder builder)
    {
        builder.append(getClass().getSimpleName()).append('|').append(getPromotionGroup(ctx).getIdentifier(ctx)).append('|')
                        .append(getCode(ctx)).append('|').append(getPriority(ctx)).append('|').append(ctx.getLanguage().getIsocode())
                        .append('|');
        Date startDate = getStartDate(ctx);
        if(startDate == null)
        {
            builder.append("x|");
        }
        else
        {
            builder.append(startDate.getTime()).append('|');
        }
        Date endDate = getEndDate(ctx);
        if(endDate == null)
        {
            builder.append("x|");
        }
        else
        {
            builder.append(endDate.getTime()).append('|');
        }
        Collection<AbstractPromotionRestriction> restrictions = getRestrictions(ctx);
        if(restrictions != null && !restrictions.isEmpty())
        {
            for(AbstractPromotionRestriction restriction : restrictions)
            {
                restriction.buildDataUniqueKey(ctx, builder);
            }
        }
        builder.append('|');
    }


    protected void deepCloneAttributes(SessionContext ctx, Map<String, Collection<AbstractPromotionRestriction>> values)
    {
        values.remove("promotionType");
        values.remove("restrictions");
        Collection<AbstractPromotionRestriction> dupRestrictions = new ArrayList<>();
        Collection<AbstractPromotionRestriction> restrictions = getRestrictions(ctx);
        if(restrictions != null && !restrictions.isEmpty())
        {
            for(AbstractPromotionRestriction restriction : restrictions)
            {
                dupRestrictions.add(restriction.deepClone(ctx));
            }
        }
        values.put("restrictions", dupRestrictions);
    }


    protected static final Collection<PromotionPriceRow> deepClonePriceRows(SessionContext ctx, Collection<PromotionPriceRow> priceRows)
    {
        Collection<PromotionPriceRow> dupPriceRows = new ArrayList<>();
        if(priceRows != null && !priceRows.isEmpty())
        {
            for(PromotionPriceRow row : priceRows)
            {
                dupPriceRows.add(PromotionsManager.getInstance().createPromotionPriceRow(ctx, row.getCurrency(ctx), row
                                .getPrice(ctx).doubleValue()));
            }
        }
        return dupPriceRows;
    }


    protected static final void buildDataUniqueKeyForPriceRows(SessionContext ctx, StringBuilder builder, Collection<PromotionPriceRow> priceRows)
    {
        if(priceRows != null && !priceRows.isEmpty())
        {
            List<PromotionPriceRow> sortedPriceRows = new ArrayList<>(priceRows);
            Collections.sort(sortedPriceRows, Comparators.promotionPriceRowComparator);
            for(PromotionPriceRow row : sortedPriceRows)
            {
                builder.append(row.getCurrency(ctx).getIsoCode(ctx)).append('=').append(row.getPrice(ctx)).append(',');
            }
        }
        builder.append('|');
    }


    protected static final void buildDataUniqueKeyForProducts(SessionContext ctx, StringBuilder builder, Collection<Product> products)
    {
        StringBuilder productBuilder = new StringBuilder();
        if(products != null && !products.isEmpty())
        {
            List<Product> sortedProducts = new ArrayList<>(products);
            Collections.sort(sortedProducts, Comparators.productComparator);
            for(Product p : sortedProducts)
            {
                productBuilder.append(p.getCode(ctx)).append(',');
            }
        }
        builder.append(buildMD5Hash(productBuilder.toString()));
        builder.append('|');
    }


    protected static final void buildDataUniqueKeyForCategories(SessionContext ctx, StringBuilder builder, Collection<Category> categories)
    {
        StringBuilder categoryBuilder = new StringBuilder();
        if(categories != null && !categories.isEmpty())
        {
            List<Category> sortedProducts = new ArrayList<>(categories);
            Collections.sort(sortedProducts, Comparators.categoryComparator);
            for(Category category : sortedProducts)
            {
                categoryBuilder.append(category.getCode(ctx)).append(',');
            }
        }
        builder.append(buildMD5Hash(categoryBuilder.toString()));
        builder.append('|');
    }


    public final Collection getRestrictions(SessionContext ctx)
    {
        String query = "SELECT {" + Item.PK + "} FROM   {" + TypeManager.getInstance().getComposedType(AbstractPromotionRestriction.class).getCode() + "} WHERE  {promotion} = ?promotion";
        Flat3Map args = new Flat3Map();
        args.put("promotion", this);
        Collection<?> results = getSession().getFlexibleSearch().search(ctx, query, (Map)args, AbstractPromotionRestriction.class).getResult();
        return Collections.unmodifiableCollection(results);
    }


    public final void setRestrictions(SessionContext ctx, Collection restrictions)
    {
        ArrayList<AbstractPromotionRestriction> newRestrictions = new ArrayList<>();
        if(restrictions != null && !restrictions.isEmpty())
        {
            for(Object obj : restrictions)
            {
                if(obj instanceof AbstractPromotionRestriction)
                {
                    newRestrictions.add((AbstractPromotionRestriction)obj);
                }
            }
        }
        Collection<AbstractPromotionRestriction> oldRestrictions = getRestrictions(ctx);
        if(oldRestrictions != null && !oldRestrictions.isEmpty())
        {
            for(AbstractPromotionRestriction oldRestriction : oldRestrictions)
            {
                boolean keepItem = newRestrictions.remove(oldRestriction);
                if(!keepItem)
                {
                    try
                    {
                        oldRestriction.remove(ctx);
                    }
                    catch(ConsistencyCheckException ex)
                    {
                        LOG.error("setRestrictions failed to remove [" + oldRestriction + "] from database", (Throwable)ex);
                    }
                }
            }
        }
        if(!newRestrictions.isEmpty())
        {
            for(AbstractPromotionRestriction newRestriction : newRestrictions)
            {
                newRestriction.setPromotion(ctx, this);
            }
        }
    }


    public static void deletePromotionPriceRows(SessionContext ctx, Collection<PromotionPriceRow> prices) throws ConsistencyCheckException
    {
        if(prices != null && !prices.isEmpty())
        {
            for(PromotionPriceRow row : prices)
            {
                row.remove(ctx);
            }
        }
    }


    protected final String getPromotionResultDataUnigueKey(SessionContext ctx, PromotionResult promotionResult)
    {
        StringBuilder builder = new StringBuilder(255);
        builder.append(getClass().getSimpleName()).append('|');
        builder.append(getCode(ctx)).append('|');
        buildPromotionResultDataUnigueKey(ctx, promotionResult, builder);
        return builder.toString();
    }


    protected void buildPromotionResultDataUnigueKey(SessionContext ctx, PromotionResult promotionResult, StringBuilder builder)
    {
        builder.append(promotionResult.getCertainty(ctx)).append('|');
        builder.append(promotionResult.getCustom(ctx)).append('|');
        Collection<PromotionOrderEntryConsumed> entries = promotionResult.getConsumedEntries(ctx);
        if(entries != null && !entries.isEmpty())
        {
            for(PromotionOrderEntryConsumed entry : entries)
            {
                builder.append(entry.getOrderEntry(ctx).getProduct(ctx).getCode(ctx)).append(',');
                builder.append(entry.getQuantity(ctx)).append('|');
            }
        }
        Collection<AbstractPromotionAction> actions = promotionResult.getActions(ctx);
        if(actions != null && !actions.isEmpty())
        {
            for(AbstractPromotionAction action : actions)
            {
                builder.append(action.getClass().getSimpleName()).append('|');
            }
        }
    }
}
