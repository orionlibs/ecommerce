package de.hybris.platform.util;

import com.google.common.base.Suppliers;
import de.hybris.platform.core.LazyLoadItemSet;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.internal.BaseLazyLoadItemList;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.RelationDescriptor;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.polyglot.TypeInfoFactory;
import de.hybris.platform.persistence.polyglot.config.ItemStateRepositoryFactory;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

public class OneToManyHandler<T extends Item>
{
    public static final String IGNORE_PART_OF_CONSTRAINT_FOR_TYPE_CODE = "ignore.part.of.constraint.for.type.code";
    private static final Logger log = Logger.getLogger(OneToManyHandler.class.getName());
    private static final String WHERE = "WHERE {";
    private static final String GET = "GET {";
    private static final String FALSE = "false";
    private static final String SYNC = "sync";
    private static final String ALWAYS = "always";
    protected final String handlerMode = "relation.handle.legacy";
    protected final String foreignKeyAttr;
    protected String reorderingEnabledFlagName;
    protected String relationQualifier;
    protected final Supplier<RelationsData> relationsInfo = (Supplier<RelationsData>)Suppliers.memoize(this::getRelationsInfo);
    private final String orderNumberAttr;
    private final boolean reOrderable;
    private final boolean partOf;
    private final boolean orderDirectionAsc;
    private final int typeOfCollection;
    private final String conditionQuery;
    private final String targetItemType;
    private final Supplier<HandlerQueries> handlerQueries = (Supplier<HandlerQueries>)Suppliers.memoize(this::getHandlerQueries);


    public OneToManyHandler(String targetItemType, boolean partOf, String foreignKeyAttr, String orderNumberAttr, boolean reorderable, boolean asc)
    {
        this(targetItemType, partOf, foreignKeyAttr, orderNumberAttr, reorderable, asc, 0);
    }


    public OneToManyHandler(String targetItemType, boolean partOf, String foreignKeyAttr, String orderNumberAttr, boolean reorderable, boolean asc, int typeOfCollection)
    {
        this(targetItemType, partOf, foreignKeyAttr, orderNumberAttr, reorderable, asc, typeOfCollection, null);
    }


    public OneToManyHandler(String targetItemType, boolean partOf, String foreignKeyAttr, String orderNumberAttr, boolean reorderable, boolean asc, int typeOfCollection, String conditionQuery)
    {
        verifyParameters(targetItemType, foreignKeyAttr, orderNumberAttr, reorderable);
        this.typeOfCollection = typeOfCollection;
        this.foreignKeyAttr = foreignKeyAttr;
        this.orderNumberAttr = orderNumberAttr;
        this.partOf = partOf;
        this.reOrderable = reorderable;
        this.orderDirectionAsc = asc;
        this.conditionQuery = conditionQuery;
        this.targetItemType = targetItemType;
    }


    private RelationsData getRelationsInfo()
    {
        ComposedType type = TypeManager.getInstance().getComposedType(this.targetItemType);
        RelationDescriptor relationDescriptor = (RelationDescriptor)type.getAttributeDescriptorIncludingPrivate(this.foreignKeyAttr);
        RelationType relationType = relationDescriptor.getRelationType();
        String flagName = (this.reorderingEnabledFlagName != null) ? this.reorderingEnabledFlagName : generateReorderingEnabledFlagNameFromRelationCode(relationDescriptor
                        .getRelationName());
        String relQualifier = (this.relationQualifier != null) ? this.relationQualifier : getQualifierFromRelationType(relationType);
        return new RelationsData(flagName, relQualifier);
    }


    private String getQualifierFromRelationType(RelationType relationType)
    {
        return relationType.isSourceTypeOne() ?
                        relationType.getSourceAttributeDescriptor().getQualifier() :
                        relationType.getTargetAttributeDescriptor().getQualifier();
    }


    private void verifyConfIfPolyglotType(Boolean isPolyglotType)
    {
        if(isPolyglotType.booleanValue() && StringUtils.isNotBlank(this.conditionQuery))
        {
            throw new InvalidParameterException("OneToManyHandler: Setting attribute \"condition.query\" is not supported for types configured in Polyglot - Exception occurs in targetItemType: '" + this.targetItemType + "' for foreignKeyAttribute '" + this.foreignKeyAttr + "'");
        }
    }


    private void verifyParameters(String targetItemType, String foreignKeyAttr, String orderNumberAttr, boolean reorderable)
    {
        if(reorderable && (orderNumberAttr == null || orderNumberAttr.length() == 0))
        {
            throw new InvalidParameterException(
                            "OneToManyHandler: If you set the attribute \"reorderable\" to true you also have to provide the attribute \"orderNumberAttr\" (not null and not empty)! - Exception occurs in targetItemType: '" + targetItemType + "' for foreignKeyAttribute '" + foreignKeyAttr + "'");
        }
    }


    private String generateQuery(boolean canBePolyglotQuery)
    {
        StringBuilder sbQuery = new StringBuilder();
        if(canBePolyglotQuery)
        {
            sbQuery.append("GET {");
        }
        else
        {
            sbQuery.append("SELECT {").append(Item.PK).append("} FROM {");
        }
        sbQuery.append(this.targetItemType).append("} ").append("WHERE {").append(this.foreignKeyAttr).append("}=?key ");
        if(StringUtils.isNotBlank(this.conditionQuery))
        {
            sbQuery.append("AND ").append(this.conditionQuery.trim()).append(" ");
        }
        sbQuery.append("ORDER BY ");
        if(this.orderNumberAttr != null)
        {
            sbQuery.append("{").append(this.orderNumberAttr).append("} ").append(this.orderDirectionAsc ? "ASC" : "DESC");
            if(!Item.CREATION_TIME.equals(this.orderNumberAttr))
            {
                sbQuery.append(",");
            }
        }
        if(!Item.CREATION_TIME.equals(this.orderNumberAttr))
        {
            sbQuery.append("{").append(Item.CREATION_TIME).append("} ASC ");
        }
        return sbQuery.toString().intern();
    }


    private HandlerQueries getHandlerQueries()
    {
        boolean canBePolyglotQuery = StringUtils.isBlank(this.conditionQuery);
        boolean isPolyglotType = canBePolyglotQuery;
        try
        {
            isPolyglotType = !((ItemStateRepositoryFactory)Registry.getCoreApplicationContext().getBean("itemStateRepositoryFactory", ItemStateRepositoryFactory.class)).getRepository(TypeInfoFactory.getTypeInfo(TypeManager.getInstance().getComposedType(this.targetItemType))).isNotSupported();
        }
        catch(Exception e)
        {
            log.error("Unable to check if type is configured in Polyglot ", e);
        }
        verifyConfIfPolyglotType(Boolean.valueOf(isPolyglotType));
        return new HandlerQueries(generateQuery((canBePolyglotQuery || isPolyglotType)), generateMaxNumberQuery(isPolyglotType), isPolyglotType);
    }


    private String generateMaxNumberQueryInternal(boolean isPolyglotType)
    {
        StringBuilder sbMaxNumberQuery = new StringBuilder();
        if(isPolyglotType)
        {
            sbMaxNumberQuery.append("GET {").append(this.targetItemType).append("} ");
            sbMaxNumberQuery.append("WHERE {")
                            .append(this.foreignKeyAttr)
                            .append("}=?key ORDER BY {")
                            .append(this.orderNumberAttr)
                            .append("} DESC");
        }
        else
        {
            sbMaxNumberQuery.append("SELECT MAX({")
                            .append(this.orderNumberAttr)
                            .append("}) FROM {")
                            .append(this.targetItemType)
                            .append("} ");
            sbMaxNumberQuery.append("WHERE {").append(this.foreignKeyAttr).append("}=?key ");
            if(StringUtils.isNotBlank(this.conditionQuery))
            {
                sbMaxNumberQuery.append("AND ").append(this.conditionQuery.trim()).append(" ");
            }
        }
        return sbMaxNumberQuery.toString();
    }


    private String generateMaxNumberQuery(boolean isPolyglotType)
    {
        if(this.reOrderable)
        {
            return generateMaxNumberQueryInternal(isPolyglotType);
        }
        return null;
    }


    protected boolean useLegacyMode()
    {
        return Config.getBoolean("relation.handle.legacy", false);
    }


    public SearchResult searchForLinkedItems(PK pk)
    {
        return getSearchResult(JaloSession.getCurrentSession().getSessionContext(), pk);
    }


    public Collection<T> getValues(SessionContext ctx, Item key)
    {
        BaseLazyLoadItemList<T> pkList;
        Collection<T> directResult = key.getRelatedItems(RelationsInfo.builder()
                        .withForeignKeyAttr(
                                        Pair.of(this.foreignKeyAttr, key.getPK()))
                        .withTargetItemType(this.targetItemType)
                        .withRelationQualifier(((RelationsData)this.relationsInfo
                                        .get()).getRelationQualifier())
                        .withTypeOfCollection(this.typeOfCollection)
                        .withOrdering(this.orderNumberAttr, this.orderDirectionAsc)
                        .build());
        if(directResult != null)
        {
            return directResult;
        }
        StandardSearchResult<T> sr = (StandardSearchResult<T>)getSearchResult(ctx, key.getPK());
        switch(this.typeOfCollection)
        {
            case 1:
            case 3:
                pkList = (BaseLazyLoadItemList<T>)sr.getOriginalResultList();
                return (Collection<T>)new LazyLoadItemSet(pkList.getPrefetchLanguages(), pkList.getPKList(), pkList.getPreFetchSize());
        }
        return sr.getResult();
    }


    private SearchResult getSearchResult(SessionContext ctx, PK pk)
    {
        return FlexibleSearch.getInstance()
                        .search(ctx, ((HandlerQueries)this.handlerQueries.get()).query, Collections.singletonMap("key", pk), Item.class);
    }


    public int getNextOrderNumber(SessionContext ctx, Item key)
    {
        if(!this.reOrderable)
        {
            throw new IllegalStateException("cannot call getNextOrderNumber in non-reorderable handler " + this);
        }
        HandlerQueries queries = this.handlerQueries.get();
        if(queries.isPolyglotQuery)
        {
            return executePolygolotQuery(queries.numberQuery, ctx, key);
        }
        return executeFlexibleSearchQuery(queries.numberQuery, ctx, key);
    }


    private <MAXTYPE> Integer queryNext(String query, SessionContext ctx, Item key, Class<MAXTYPE> clazz, Function<MAXTYPE, Integer> unwrap)
    {
        SessionContext localCtx = (ctx != null) ? JaloSession.getCurrentSession().createLocalSessionContext(ctx) : JaloSession.getCurrentSession().createLocalSessionContext();
        localCtx.setAttribute("disableCache", Boolean.TRUE);
        try
        {
            List<MAXTYPE> items = FlexibleSearch.getInstance().search(localCtx, query, Collections.singletonMap("key", key), Collections.singletonList(clazz), true, true, 0, 1).getResult();
            if(isEmpty(items))
            {
                return Integer.valueOf(0);
            }
            return Integer.valueOf(((Integer)unwrap.apply(items.get(0))).intValue() + 1);
        }
        finally
        {
            JaloSession.getCurrentSession().removeLocalSessionContext();
        }
    }


    private int executeFlexibleSearchQuery(String query, SessionContext ctx, Item key)
    {
        return queryNext(query, ctx, key, Integer.class, Function.identity()).intValue();
    }


    private int executePolygolotQuery(String query, SessionContext ctx, Item key)
    {
        return queryNext(query, ctx, key, ExtensibleItem.class, this::getValueAsPrimitive).intValue();
    }


    private boolean isEmpty(List items)
    {
        return (items.isEmpty() || (items.size() == 1 && items.get(0) == null));
    }


    private Integer getValueAsPrimitive(ExtensibleItem item)
    {
        Integer value = getOrderNumberAttributeValue(item);
        return Integer.valueOf((value != null) ? value.intValue() : 0);
    }


    private Integer getOrderNumberAttributeValue(ExtensibleItem item)
    {
        return (Integer)item.getProperty(this.orderNumberAttr);
    }


    protected boolean setValuesInternal(SessionContext ctx, Item key, Collection<T> values)
    {
        try
        {
            return unlinkPreviusValues(ctx, key, values) | linkNewValues(ctx, key, values);
        }
        catch(RuntimeException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    private boolean linkNewValues(SessionContext ctx, Item key, Collection<T> values) throws JaloInvalidParameterException, JaloSecurityException, JaloBusinessException
    {
        if(CollectionUtils.isEmpty(values))
        {
            return false;
        }
        boolean modified = false;
        Set<PK> linkedPKs = new HashSet<>(values.size());
        int i = 0;
        for(Item item : values)
        {
            if(item == null)
            {
                continue;
            }
            PK myPK = item.getPK();
            if(linkedPKs.add(myPK))
            {
                if(this.reOrderable)
                {
                    setPosAttributeValue(ctx, (T)item, i);
                    i++;
                }
                Item prev = (Item)item.getAttribute(ctx, this.foreignKeyAttr);
                if(this.partOf && prev != null && !prev.equals(key) && !ignorePartOfConstraint(ctx, item))
                {
                    throw new PartOfItemAlreadyAssignedToTheParentException(item, key, prev);
                }
                if(prev == null || !prev.equals(key))
                {
                    tryToSetForeignKeyValue(ctx, key, (T)item);
                }
            }
            modified = true;
        }
        return modified;
    }


    private void tryToSetForeignKeyValue(SessionContext ctx, Item key, T toLink)
    {
        try
        {
            setForeignKeyValue(ctx, key, toLink);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, "cannot assign " + toLink + " to " + key + " due to " + e
                            .getMessage(), 0);
        }
    }


    private boolean unlinkPreviusValues(SessionContext ctx, Item key, Collection<T> values) throws JaloInvalidParameterException, JaloSecurityException, JaloBusinessException
    {
        boolean modified = false;
        Collection<? extends Item> previous = getValues(ctx, key);
        if(!previous.isEmpty())
        {
            Collection<? extends Item> toUnlink = new HashSet<>(previous);
            if(CollectionUtils.isNotEmpty(values))
            {
                toUnlink.removeAll(values);
            }
            if(!toUnlink.isEmpty())
            {
                if(this.partOf)
                {
                    toUnlink = Utilities.sortItemsByPK(toUnlink);
                }
                for(Item oldOne : toUnlink)
                {
                    if(oldOne != null)
                    {
                        if(this.partOf)
                        {
                            oldOne.remove(ctx);
                        }
                        else
                        {
                            oldOne.setAttribute(ctx, this.foreignKeyAttr, null);
                        }
                        modified = true;
                    }
                }
            }
        }
        return modified;
    }


    public void setValues(SessionContext ctx, Item key, Collection<T> values)
    {
        if(key.setRelatedItems(((RelationsData)this.relationsInfo.get()).getRelationQualifier(), values))
        {
            return;
        }
        if(setValuesInternal(ctx, key, values) && !Item.isCurrentlyRemoving(key))
        {
            key.setModificationTime(new Date());
        }
    }


    public void addValue(SessionContext ctx, Item key, T toLink)
    {
        if(toLink == null)
        {
            return;
        }
        try
        {
            Item prev = (Item)toLink.getAttribute(ctx, this.foreignKeyAttr);
            if(this.partOf && prev != null && !prev.equals(key) && !ignorePartOfConstraint(ctx, (Item)toLink))
            {
                throw new PartOfItemAlreadyAssignedToTheParentException(toLink, key, prev);
            }
            if(prev == null || !prev.equals(key))
            {
                try
                {
                    setForeignKeyValue(ctx, key, toLink);
                    setOrderNumber(ctx, key, toLink);
                }
                catch(Exception e)
                {
                    throw new JaloSystemException(e, "cannot assign " + toLink + " to " + key + " due to " + e.getMessage(), 0);
                }
            }
        }
        catch(RuntimeException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    private void setOrderNumber(SessionContext ctx, Item key, T toLink) throws JaloBusinessException
    {
        if(this.reOrderable && key != null && reorderingEnabled(ctx, true))
        {
            setPosAttributeValue(ctx, toLink, getNextOrderNumber(ctx, key));
        }
    }


    public void newInstance(SessionContext ctx, Item.ItemAttributeMap attributes)
    {
        Item ref = (Item)attributes.get(this.foreignKeyAttr);
        if(ref != null && this.reOrderable && reorderingEnabled(ctx, (attributes.get(this.orderNumberAttr) == null)))
        {
            attributes.put(this.orderNumberAttr, Integer.valueOf(getNextOrderNumber(ctx, ref)));
        }
    }


    private boolean reorderingEnabled(SessionContext ctx, boolean orderNumberUnknown)
    {
        String currentReorderingEnabled = Config.getString(((RelationsData)this.relationsInfo.get()).getReorderingEnabledFlagName(), "");
        switch(currentReorderingEnabled)
        {
            case "always":
                return false;
            case "sync":
                return (!isCatalogSyncActive(ctx) && orderNumberUnknown);
            case "false":
                return true;
        }
        return orderNumberUnknown;
    }


    private boolean isCatalogSyncActive(SessionContext ctx)
    {
        return (ctx.getAttribute("catalog.sync.active") != null && ((Boolean)ctx.getAttribute("catalog.sync.active")).booleanValue());
    }


    protected void setForeignKeyValue(SessionContext ctx, Item key, T toLink) throws JaloSecurityException, JaloBusinessException
    {
        try
        {
            ((ExtensibleItem)toLink).setProperty(ctx, this.foreignKeyAttr, key);
        }
        catch(ClassCastException cce)
        {
            log.warn("For given type " + toLink + " a foreign key " + this.foreignKeyAttr + " could not be set as property, please provide own handler implementation to handle this.");
            if(log.isDebugEnabled())
            {
                log.debug(cce);
            }
        }
    }


    protected void setPosAttributeValue(SessionContext ctx, T toLink, int index) throws JaloSecurityException, JaloBusinessException
    {
        toLink.setAttribute(ctx, this.orderNumberAttr, Integer.valueOf(index));
    }


    public void removeValue(SessionContext ctx, Item key, T linked)
    {
        try
        {
            if(linked == null)
            {
                return;
            }
            if(this.partOf)
            {
                linked.remove(ctx);
            }
            else
            {
                linked.setAttribute(ctx, this.foreignKeyAttr, null);
            }
        }
        catch(RuntimeException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    public void notifyKeyRemoval(SessionContext ctx, Item key)
    {
        setValuesInternal(ctx, key, null);
    }


    public String getForeignKeyAttr()
    {
        return this.foreignKeyAttr;
    }


    public String getOrderNumberAttr()
    {
        return this.orderNumberAttr;
    }


    public boolean isReOrderable()
    {
        return this.reOrderable;
    }


    public boolean isPartOf()
    {
        return this.partOf;
    }


    public String getConditionQuery()
    {
        return this.conditionQuery;
    }


    public OneToManyHandler<T> withRelationQualifier(String qualifier)
    {
        this.relationQualifier = qualifier;
        return this;
    }


    public OneToManyHandler<T> withRelationCode(String relationCode)
    {
        if(StringUtils.isNotEmpty(relationCode))
        {
            this.reorderingEnabledFlagName = generateReorderingEnabledFlagNameFromRelationCode(relationCode);
        }
        return this;
    }


    private String generateReorderingEnabledFlagNameFromRelationCode(String relationCode)
    {
        return "relation." + relationCode + ".reordered";
    }


    private boolean ignorePartOfConstraint(SessionContext ctx, Item item)
    {
        ComposedType typeToIgnore;
        if(ctx == null || item == null)
        {
            return false;
        }
        String typeCode = (String)ctx.getAttribute("ignore.part.of.constraint.for.type.code");
        if(typeCode == null)
        {
            return false;
        }
        try
        {
            typeToIgnore = TypeManager.getInstance().getComposedType(typeCode);
        }
        catch(JaloItemNotFoundException e)
        {
            String message = "Couldn't find type '" + typeCode + "'. Ignoring 'ignore.part.of.constraint.for.type.code' attribute.";
            log.warn(message);
            if(log.isDebugEnabled())
            {
                log.debug(message, (Throwable)e);
            }
            return false;
        }
        return typeToIgnore.isInstance(item);
    }
}
