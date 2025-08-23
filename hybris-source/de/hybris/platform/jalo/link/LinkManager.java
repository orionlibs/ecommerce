package de.hybris.platform.jalo.link;

import com.codahale.metrics.Meter;
import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.metrics.dropwizard.MetricUtils;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.PolyglotPersistenceGenericItemSupport;
import de.hybris.platform.persistence.link.LinkManagerEJB;
import de.hybris.platform.persistence.links.NonNavigableRelationsDAO;
import de.hybris.platform.persistence.polyglot.TypeInfoFactory;
import de.hybris.platform.persistence.polyglot.config.TypeInfo;
import de.hybris.platform.persistence.type.ComposedTypeEJBImpl;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.SearchTools;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.config.PropertyActionReader;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

public class LinkManager extends Manager
{
    public static final String BEAN_NAME = "core.linkManager";
    private static final Logger LOG = Logger.getLogger(LinkManager.class.getName());
    private static final List LINK_SEARCH_RES_CLASSES = Collections.singletonList(Item.class);
    private final MetricUtils.CachedMetrics<Pair<String, Boolean>> metrics = MetricUtils.metricCache();
    public static final String DISABLE_LINK_REMOVAL_NOTIFICATION = "disable.link.removal.notification";


    protected boolean skipChangingExistingLinks(SessionContext ctx)
    {
        return (ctx != null && Boolean.TRUE.equals(ctx.getAttribute("dont.change.existing.links")));
    }


    private final LinkOperationFactory factory = (LinkOperationFactory)Registry.getCoreApplicationContext().getBean("linkOperationFactory", LinkOperationFactory.class);


    public Class getRemoteManagerClass()
    {
        return LinkManagerEJB.class;
    }


    public static LinkManager getInstance()
    {
        return Registry.getCurrentTenant().getJaloConnection().getLinkManager();
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
    }


    private boolean isRootRelationType(String qualifier)
    {
        return getTenant().getPersistenceManager().isRootRelationType(qualifier);
    }


    private String getLinkTypeCode(String qualifier)
    {
        try
        {
            return isRootRelationType(qualifier) ? qualifier : TypeManager.getInstance()
                            .getRootComposedType(Link.class)
                            .getCode();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloInternalException(e, "cannot find link item type for jalo class name 'de.hybris.platform.jalo.link.Link' - check code", 0);
        }
    }


    protected boolean enableRelationRestrictions()
    {
        return Config.getBoolean("relation.restrictions.enabled", true);
    }


    public List getLinkedItems(Item item, boolean itemIsSource, String qualifier, Language lang)
    {
        return getLinkedItems(getSession().getSessionContext(), item, itemIsSource, qualifier, lang);
    }


    public List getLinkedItems(Item item, boolean itemIsSource, String qualifier, Language lang, int start, int count)
    {
        return getLinkedItems(getSession().getSessionContext(), item, itemIsSource, qualifier, lang, start, count);
    }


    public long getLinkedItemsCount(Item item, boolean itemIsSource, String qualifier, Language lang)
    {
        return getLinkedItemsCount(getSession().getSessionContext(), item, itemIsSource, qualifier, lang);
    }


    public List getLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language lang)
    {
        return getLinkedItems(ctx, item, itemIsSource, qualifier, lang, enableRelationRestrictions());
    }


    public List getLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language lang, int start, int count)
    {
        return getLinkedItems(ctx, item, itemIsSource, qualifier, lang, enableRelationRestrictions(), start, count);
    }


    public List getLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language lang, int start, int count, boolean sortSrc2Tgt, boolean sortTgt2Src)
    {
        return getLinkedItems(ctx, item, itemIsSource, qualifier, lang, enableRelationRestrictions(), start, count, sortSrc2Tgt, sortTgt2Src);
    }


    public long getLinkedItemsCount(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language lang)
    {
        return getLinkedItemsCount(ctx, item, itemIsSource, qualifier, lang, enableRelationRestrictions());
    }


    public List getLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language lang, boolean enableRestrictions)
    {
        return getLinkedItems(ctx, item, itemIsSource, qualifier, lang, enableRestrictions, 0, -1);
    }


    public List getLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language lang, boolean enableRestrictions, int start, int count)
    {
        return getLinkedItems(ctx, item, itemIsSource, qualifier, lang, enableRestrictions, start, count, true, true);
    }


    public List getLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language lang, boolean enableRestrictions, int start, int count, boolean sortSrc2Tgt, boolean sortTgt2Src)
    {
        return getLinkedItems(ctx, (item == null) ? null : item.getPK(), itemIsSource, qualifier,
                        (lang == null) ? null : lang.getPK(), enableRestrictions, start, count, sortSrc2Tgt, sortTgt2Src)
                        .getResult();
    }


    public <T> SearchResult<T> getLinkedItems(PK itemPk, boolean itemIsSource, String qualifier, PK langPk, boolean enableRestrictions, int start, int count, boolean sortSrc2Tgt, boolean sortTgt2Src)
    {
        return getLinkedItems(getSession().getSessionContext(), itemPk, itemIsSource, qualifier, langPk, enableRestrictions, start, count, sortSrc2Tgt, sortTgt2Src);
    }


    public <T> SearchResult<T> getLinkedItems(SessionContext ctx, PK itemPk, boolean itemIsSource, String qualifier, PK langPk, boolean enableRestrictions, int start, int count, boolean sortSrc2Tgt, boolean sortTgt2Src)
    {
        StringBuilder buffer = new StringBuilder();
        boolean isTypeNotSearchRestricted = isTypeNotSearchRestricted(qualifier);
        ComposedType type = null;
        try
        {
            type = (enableRestrictions && !isTypeNotSearchRestricted) ? TypeManager.getInstance().getComposedType(qualifier) : null;
        }
        catch(JaloItemNotFoundException jaloItemNotFoundException)
        {
        }
        List<Class<?>> resultClasses = LINK_SEARCH_RES_CLASSES;
        if(type instanceof RelationType)
        {
            RelationType relationType = (RelationType)type;
            ComposedType itemType = itemIsSource ? relationType.getTargetType() : relationType.getSourceType();
            if(isFullyBackedByTheEJB(itemType))
            {
                resultClasses = Collections.singletonList(itemType.getJaloClass());
                buffer.append("SELECT {item:").append(Item.PK).append("} ");
                buffer.append("FROM {").append(relationType.getCode()).append("* AS rel JOIN ").append(itemType.getCode())
                                .append(" AS item ");
                buffer.append("ON {rel:").append(itemIsSource ? "target" : "source").append("}={item:").append(Item.PK)
                                .append("} } ");
            }
            else
            {
                buffer.append("SELECT {").append(itemIsSource ? "target" : "source").append("} ");
                buffer.append("FROM {").append(getLinkTypeCode(qualifier)).append("* AS rel} ");
            }
        }
        else
        {
            buffer.append("SELECT {").append(itemIsSource ? "target" : "source").append("} ");
            buffer.append("FROM {").append(getLinkTypeCode(qualifier)).append("* AS rel} ");
        }
        buffer.append("WHERE {rel:").append("qualifier").append("} = ?quali AND ");
        buffer.append("{rel:").append(itemIsSource ? "source" : "target").append("} = ?item AND ");
        buffer.append("{rel:").append("language").append("} ").append((langPk == null) ? "IS NULL " : "=?lang ");
        buffer.append("ORDER BY ");
        if((itemIsSource && sortSrc2Tgt) || (!itemIsSource && sortTgt2Src))
        {
            buffer.append("{rel:").append(itemIsSource ? "sequenceNumber" : "reverseSequenceNumber")
                            .append("} ASC ,");
        }
        buffer.append("{rel:").append(Item.PK).append("} ASC");
        SessionContext myCtx = ctx;
        boolean useLocalCtx = (ctx == null || !Boolean.TRUE.equals(myCtx.getAttribute("disableRestrictions")));
        if(useLocalCtx)
        {
            myCtx = getSession().createLocalSessionContext(myCtx);
            myCtx.setAttribute("disableRestrictions", Boolean.TRUE);
        }
        try
        {
            Map<Object, Object> args = new HashMap<>();
            args.put("item", itemPk);
            args.put("quali", qualifier);
            if(langPk != null)
            {
                args.put("lang", langPk);
            }
            return getSession().getFlexibleSearch().search(myCtx, buffer.toString(), args, resultClasses, true, true, start, count);
        }
        catch(FlexibleSearchException e)
        {
            throw new JaloInternalException(e, "flexible search error for link search query '" + buffer.toString() + "' ( item='" + itemPk + "',quali='" + qualifier + "',lang='" + langPk + "')", 0);
        }
        finally
        {
            this.metrics.getMeter(Pair.of(qualifier, Boolean.valueOf(itemIsSource)), getTenant().getTenantID(),
                            mnsCtx -> MetricUtils.metricName("linkManager", new String[] {"getLinkedItems", "invocations"}).tag("relation", (String)((Pair)mnsCtx.getMetricKey()).getLeft()).tag("direction", ((Boolean)((Pair)mnsCtx.getMetricKey()).getValue()).booleanValue() ? "source" : "target")
                                            .tenant(mnsCtx.getTenantId()).module("jalo").extension("core").build()).ifPresent(Meter::mark);
            if(useLocalCtx)
            {
                getSession().removeLocalSessionContext();
            }
        }
    }


    public List getLinkedItems(SessionContext ctx, PK itemPk, boolean itemIsSource, PK langPk, String relationTypeCode, String relatedItemCode, int start, int count, boolean sortSrc2Tgt, boolean sortTgt2Src)
    {
        StringBuilder buffer = new StringBuilder();
        if(!isTypeNotSearchRestricted(relationTypeCode) && enableRelationRestrictions() && isFullyBackedByTheEJB(relatedItemCode))
        {
            buffer.append("SELECT {item:").append(Item.PK).append("} ");
            buffer.append("FROM {").append(relationTypeCode).append("* AS rel JOIN ").append(relatedItemCode)
                            .append(" AS item ");
            buffer.append("ON {rel:").append(itemIsSource ? "target" : "source").append("}={item:").append(Item.PK)
                            .append("} } ");
        }
        else
        {
            buffer.append("SELECT {").append(itemIsSource ? "target" : "source").append("} ");
            buffer.append("FROM {").append(getLinkTypeCode(relationTypeCode)).append("* AS rel} ");
        }
        buffer.append("WHERE {rel:").append("qualifier").append("} = ?quali AND ");
        buffer.append("{rel:").append(itemIsSource ? "source" : "target").append("} = ?item AND ");
        buffer.append("{rel:").append("language").append("} ").append((langPk == null) ? "IS NULL " : "=?lang ");
        buffer.append("ORDER BY ");
        if((itemIsSource && sortSrc2Tgt) || (!itemIsSource && sortTgt2Src))
        {
            buffer.append("{rel:").append(itemIsSource ? "sequenceNumber" : "reverseSequenceNumber")
                            .append("} ASC ,");
        }
        buffer.append("{rel:").append(Item.PK).append("} ASC");
        SessionContext myCtx = ctx;
        boolean useLocalCtx = (ctx == null || !Boolean.TRUE.equals(myCtx.getAttribute("disableRestrictions")));
        if(useLocalCtx)
        {
            myCtx = getSession().createLocalSessionContext(myCtx);
            myCtx.setAttribute("disableRestrictions", Boolean.TRUE);
        }
        try
        {
            Map<Object, Object> args = new HashMap<>();
            args.put("item", itemPk);
            args.put("quali", relationTypeCode);
            if(langPk != null)
            {
                args.put("lang", langPk);
            }
            return getSession().getFlexibleSearch()
                            .search(ctx, buffer.toString(), args, LINK_SEARCH_RES_CLASSES, true, true, start, count)
                            .getResult();
        }
        catch(FlexibleSearchException e)
        {
            throw new JaloInternalException(e, "flexible search error for link search query '" + buffer.toString() + "' ( item='" + itemPk + "',quali='" + relationTypeCode + "',lang='" + langPk + "')", 0);
        }
        finally
        {
            if(useLocalCtx)
            {
                getSession().removeLocalSessionContext();
            }
        }
    }


    private boolean isFullyBackedByTheEJB(String typeCode)
    {
        ComposedType type;
        try
        {
            type = TypeManager.getInstance().getComposedType(typeCode);
        }
        catch(JaloItemNotFoundException e)
        {
            return true;
        }
        return isFullyBackedByTheEJB(type);
    }


    private boolean isFullyBackedByTheEJB(ComposedType type)
    {
        if(type == null)
        {
            return true;
        }
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfo(type);
        return PolyglotPersistenceGenericItemSupport.isFullyBackedByTheEJBPersistence(getTenant(), typeInfo);
    }


    private boolean isTypeNotSearchRestricted(String relationTypeCode)
    {
        return GeneratedCoreConstants.Relations.PRINCIPALGROUPRELATION.equals(relationTypeCode);
    }


    public Collection getAllLinkedItems(SessionContext ctx, Collection<?> items, boolean itemsAreSource, String qualifier, Language lang)
    {
        if(items == null || items.isEmpty())
        {
            throw new JaloInvalidParameterException("item collection was null or empty", 0);
        }
        StringBuilder buffer = new StringBuilder();
        ComposedType type = null;
        try
        {
            type = enableRelationRestrictions() ? TypeManager.getInstance().getComposedType(qualifier) : null;
        }
        catch(JaloItemNotFoundException jaloItemNotFoundException)
        {
        }
        if(type instanceof RelationType)
        {
            RelationType relationType = (RelationType)type;
            ComposedType itemType = itemsAreSource ? relationType.getTargetType() : relationType.getSourceType();
            buffer.append("SELECT DISTINCT {item:").append(Item.PK).append("} ");
            buffer.append("FROM {").append(relationType.getCode()).append("* AS rel JOIN ").append(itemType.getCode())
                            .append(" AS item ");
            buffer.append("ON {rel:").append(itemsAreSource ? "target" : "source").append("}={item:").append(Item.PK)
                            .append("} } ");
        }
        else
        {
            buffer.append("SELECT DISTINCT {").append(itemsAreSource ? "target" : "source").append("} ");
            buffer.append("FROM {").append(getLinkTypeCode(qualifier)).append("* AS rel} ");
        }
        buffer.append("WHERE {rel:").append("qualifier").append("} = '").append(qualifier).append("' AND ");
        buffer.append("{rel:").append("language").append("} ").append((lang == null) ? "IS NULL " : "= ?lang ").append(" AND ");
        buffer.append("{rel:").append(itemsAreSource ? "source" : "target").append("} IN ( ?pklist )");
        Map<String, Object> values = new HashMap<>();
        if(lang != null)
        {
            values.put("lang", lang.getPK());
        }
        try
        {
            boolean argIsLazy = items instanceof LazyLoadItemList;
            List itemList = argIsLazy ? (List)items : new ArrayList(items);
            FlexibleSearch flexibleSearch = FlexibleSearch.getInstance();
            Set<PK> pkSet = new LinkedHashSet<>();
            int pageSize = getTenant().getDataSource().getMaxPreparedParameterCount();
            if(pageSize == -1)
            {
                pageSize = itemList.size();
            }
            else if(lang != null)
            {
                pageSize--;
            }
            int offset = 0;
            while(offset < itemList.size())
            {
                int currentPageEnd = Math.min(itemList.size(), offset + pageSize);
                if(argIsLazy)
                {
                    LazyLoadItemList lazyList = (LazyLoadItemList)itemList;
                    List<PK> pkRange = new ArrayList<>(currentPageEnd - offset + 1);
                    for(int i = offset; i < currentPageEnd; i++)
                    {
                        pkRange.add(lazyList.getPK(i));
                    }
                    values.put("pklist", pkRange);
                }
                else
                {
                    values.put("pklist", itemList.subList(offset, currentPageEnd));
                }
                SearchResult result = flexibleSearch.search(ctx, buffer.toString(), values, PK.class);
                if(result.getResult() != null)
                {
                    pkSet.addAll(result.getResult());
                }
                offset += pageSize;
            }
            return pkSet.isEmpty() ? (Collection)new LazyLoadItemList() :
                            (Collection)new LazyLoadItemList(WrapperFactory.getPrefetchLanguages(ctx), new ArrayList<>(pkSet), 100);
        }
        catch(FlexibleSearchException e)
        {
            throw new JaloInternalException(e, "flexible search error for link search query '" + buffer.toString() + "' ( items='" + items + "',quali='" + qualifier + "',lang='" + lang + "')", 0);
        }
    }


    public long getLinkedItemsCount(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language lang, boolean enableRestrictions)
    {
        StringBuilder buffer = new StringBuilder();
        ComposedType type = null;
        try
        {
            type = enableRestrictions ? getSession().getTypeManager().getComposedType(qualifier) : null;
        }
        catch(JaloItemNotFoundException jaloItemNotFoundException)
        {
        }
        if(type instanceof RelationType)
        {
            RelationType relationType = (RelationType)type;
            ComposedType itemType = itemIsSource ? relationType.getTargetType() : relationType.getSourceType();
            buffer.append("SELECT COUNT({item:").append(Item.PK).append("}) ");
            buffer.append("FROM {").append(relationType.getCode()).append("* AS rel JOIN ").append(itemType.getCode())
                            .append(" AS item ");
            buffer.append("ON {rel:").append(itemIsSource ? "target" : "source").append("}={item:").append(Item.PK)
                            .append("} } ");
            buffer.append("WHERE {rel:").append("qualifier").append("} = ?quali AND ");
            buffer.append("{rel:").append(itemIsSource ? "source" : "target").append("} = ?item AND ");
            buffer.append("{rel:").append("language").append("} ").append((lang == null) ? "IS NULL " : "=?lang ");
        }
        else
        {
            buffer.append("SELECT COUNT({").append(itemIsSource ? "target" : "source").append("}) ");
            buffer.append("FROM {").append(getLinkTypeCode(qualifier)).append("*} ");
            buffer.append("WHERE {").append("qualifier").append("} = ?quali AND ");
            buffer.append("{").append(itemIsSource ? "source" : "target").append("} = ?item AND ");
            buffer.append("{").append("language").append("} ").append((lang == null) ? "IS NULL " : "=?lang ");
        }
        try
        {
            Map<Object, Object> args = new HashMap<>();
            args.put("item", item);
            args.put("quali", qualifier);
            if(lang != null)
            {
                args.put("lang", lang);
            }
            return ((Long)getSession().getFlexibleSearch()
                            .search(ctx, buffer.toString(), args, Collections.singletonList(Long.class), true, true, 0, -1)
                            .getResult().iterator().next()).longValue();
        }
        catch(FlexibleSearchException e)
        {
            throw new JaloInternalException(e, "flexible search error for link search query '" + buffer.toString() + "' ( item='" + item + "',quali='" + qualifier + "',lang='" + lang + "')", 0);
        }
    }


    public long getLinkedItemsCount(SessionContext ctx, PK itemPk, boolean itemIsSource, String relationTypeCode, String relatedItemCode, PK langPk)
    {
        StringBuilder buffer = new StringBuilder();
        if(enableRelationRestrictions())
        {
            buffer.append("SELECT COUNT({item:").append(Item.PK).append("}) ");
            buffer.append("FROM {").append(relationTypeCode).append("* AS rel JOIN ").append(relatedItemCode).append(" AS item ");
            buffer.append("ON {rel:").append(itemIsSource ? "target" : "source").append("}={item:").append(Item.PK)
                            .append("} } ");
            buffer.append("WHERE {rel:").append("qualifier").append("} = ?quali AND ");
            buffer.append("{rel:").append(itemIsSource ? "source" : "target").append("} = ?item AND ");
            buffer.append("{rel:").append("language").append("} ").append((langPk == null) ? "IS NULL " : "=?lang ");
        }
        else
        {
            buffer.append("SELECT COUNT({").append(itemIsSource ? "target" : "source").append("}) ");
            buffer.append("FROM {").append(getLinkTypeCode(relationTypeCode)).append("*} ");
            buffer.append("WHERE {").append("qualifier").append("} = ?quali AND ");
            buffer.append("{").append(itemIsSource ? "source" : "target").append("} = ?item AND ");
            buffer.append("{").append("language").append("} ").append((langPk == null) ? "IS NULL " : "=?lang ");
        }
        try
        {
            Map<Object, Object> args = new HashMap<>();
            args.put("item", itemPk);
            args.put("quali", relationTypeCode);
            if(langPk != null)
            {
                args.put("lang", langPk);
            }
            return ((Long)getSession().getFlexibleSearch()
                            .search(ctx, buffer.toString(), args, Collections.singletonList(Long.class), true, true, 0, -1)
                            .getResult().iterator().next()).longValue();
        }
        catch(FlexibleSearchException e)
        {
            throw new JaloInternalException(e, "flexible search error for link search query '" + buffer.toString() + "' ( item='" + itemPk + "',quali='" + relationTypeCode + "',lang='" + langPk + "')", 0);
        }
    }


    private static final List ALL_LINKS_SEARCH_RES_CLASSES = Arrays.asList((Class<?>[][])new Class[] {Item.class, Language.class});


    public Map getAllLinkedItems(Item item, boolean itemIsSource, String qualifier)
    {
        return getAllLinkedItems(item, itemIsSource, qualifier, enableRelationRestrictions());
    }


    public Map getAllLinkedItems(Item item, boolean itemIsSource, String qualifier, boolean enableRestrictions)
    {
        StringBuilder buffer = new StringBuilder();
        ComposedType type = null;
        try
        {
            type = enableRestrictions ? getSession().getTypeManager().getComposedType(qualifier) : null;
        }
        catch(JaloItemNotFoundException jaloItemNotFoundException)
        {
        }
        if(type instanceof RelationType)
        {
            RelationType relationType = (RelationType)type;
            ComposedType itemType = itemIsSource ? relationType.getTargetType() : relationType.getSourceType();
            buffer.append("SELECT {item:").append(Item.PK).append("}, {rel:").append("language").append("}");
            buffer.append("FROM {").append(relationType.getCode()).append("* AS rel JOIN ").append(itemType.getCode())
                            .append(" AS item ");
            buffer.append("ON {rel:").append(itemIsSource ? "target" : "source").append("}={item:").append(Item.PK)
                            .append("} } ");
        }
        else
        {
            buffer.append("SELECT {rel:")
                            .append(itemIsSource ? "target" : "source")
                            .append("}, {rel:")
                            .append("language")
                            .append("}");
            buffer.append("FROM {").append(getLinkTypeCode(qualifier)).append("* AS rel} ");
        }
        buffer.append("WHERE {rel:").append("qualifier").append("} = ?quali AND ");
        buffer.append("{rel:").append(itemIsSource ? "source" : "target").append("} = ?item AND ");
        buffer.append("{rel:").append("language").append("} IS NOT NULL ");
        buffer.append("ORDER BY {rel:").append("language").append("} ASC,");
        buffer.append("{rel:").append(itemIsSource ? "sequenceNumber" : "reverseSequenceNumber").append("} ASC ,");
        buffer.append("{rel:").append(Item.CREATION_TIME).append("} ASC, {rel:").append(Item.PK).append("} ASC");
        try
        {
            Map<Object, Object> args = new HashMap<>();
            args.put("quali", qualifier);
            args.put("item", item);
            Collection rows = getSession().getFlexibleSearch().search(buffer.toString(), args, ALL_LINKS_SEARCH_RES_CLASSES, true, true, 0, -1).getResult();
            if(!rows.isEmpty())
            {
                Map<Object, Object> map = new HashMap<>();
                Language currentLang = null;
                List<Item> currentList = null;
                for(Iterator<List> it = rows.iterator(); it.hasNext(); )
                {
                    List<Item> row = it.next();
                    Item linkedItem = row.get(0);
                    Language lang = (Language)row.get(1);
                    if(currentLang == null || !currentLang.equals(lang))
                    {
                        if(currentLang != null && currentList != null && !currentList.isEmpty())
                        {
                            map.put(currentLang, currentList);
                        }
                        currentLang = lang;
                        currentList = new LinkedList();
                    }
                    currentList.add(linkedItem);
                }
                if(currentLang != null && currentList != null && !currentList.isEmpty())
                {
                    map.put(currentLang, currentList);
                }
                return map;
            }
            return Collections.EMPTY_MAP;
        }
        catch(FlexibleSearchException e)
        {
            throw new JaloInternalException(e, "flexible search error for all link search query '" + buffer.toString() + "' ( item='" + item + "',quali='" + qualifier + "')", 0);
        }
    }


    public void setLinkedItems(Item item, boolean itemIsSource, String qualifier, Language lang, List<? extends Item> itemsToLink)
    {
        setLinkedItems(getSession().getSessionContext(), item, itemIsSource, qualifier, lang, itemsToLink);
    }


    public void setLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, Collection<? extends Item> items)
    {
        setLinkedItems(ctx, item, itemIsSource, qualifier, language,
                        (items instanceof List) ? (List<? extends Item>)items : ((items == null) ? null : new ArrayList<>(items)));
    }


    public void setLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, Collection<? extends Item> items, boolean markModified)
    {
        setLinkedItems(ctx, item, itemIsSource, qualifier, language,
                        (items instanceof List) ? items : ((items == null) ? null : new ArrayList<>(items)), markModified);
    }


    public void setLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List<? extends Item> items)
    {
        setLinkedItems(ctx, item, itemIsSource, qualifier, language, items, true, true, true);
    }


    public void setLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, Collection<? extends Item> items, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        setLinkedItems(ctx, item, itemIsSource, qualifier, language,
                        (items instanceof List) ? (List<? extends Item>)items : ((items == null) ? null : new ArrayList<>(items)), sortSrc2Tgt, sortTgt2Src, markModified);
    }


    public void setLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List<? extends Item> items, boolean sortSrc2Tgt, boolean sortTgt2Src)
    {
        setLinkedItems(ctx, item, itemIsSource, qualifier, language, items, sortSrc2Tgt, sortTgt2Src, true);
    }


    public void setLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List<? extends Item> items, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        getLinkOperationFactory().createSetOperation(ctx, item, itemIsSource, qualifier, language, items, sortSrc2Tgt, sortTgt2Src, markModified)
                        .perform();
    }


    public void setAllLinkedItems(SessionContext ctx, Collection<Item> itemColl, boolean itemsAreSource, String qualifier, Language language, List<Item> items, boolean ordered)
    {
        setAllLinkedItems(ctx, itemColl, itemsAreSource, qualifier, language, items, ordered, true);
    }


    public void setAllLinkedItems(SessionContext ctx, Collection<Item> itemColl, boolean itemsAreSource, String qualifier, Language language, List<Item> items, boolean ordered, boolean markModified)
    {
        getLinkOperationFactory().createSetOperation(ctx, itemColl, itemsAreSource, qualifier, language, items, markModified)
                        .perform();
    }


    public void setAllLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Map<Language, List<Item>> items)
    {
        setAllLinkedItems(ctx, item, itemIsSource, qualifier, items, true, true, true, true);
    }


    public void setAllLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Map<Language, List<Item>> items, boolean markMpdified)
    {
        setAllLinkedItems(ctx, item, itemIsSource, qualifier, items, true, true, true, markMpdified);
    }


    public void setAllLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Map<Language, List<Item>> items, boolean sortSrc2Tgt, boolean sortTgt2Src)
    {
        setAllLinkedItems(ctx, item, itemIsSource, qualifier, items, sortSrc2Tgt, sortTgt2Src, false, true);
    }


    public void setAllLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Map<Language, List<Item>> items, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean preserveHiddenLanguages, boolean markModified)
    {
        getLinkOperationFactory().createSetOperation(ctx, item, itemIsSource, qualifier, items, sortSrc2Tgt, sortTgt2Src, preserveHiddenLanguages, markModified)
                        .perform();
    }


    public void addLinkedItems(Item item, boolean itemIsSource, String qualifier, Language language, List items)
    {
        addLinkedItems(item, itemIsSource, qualifier, language, items, -1);
    }


    public void addLinkedItems(Item item, boolean itemIsSource, String qualifier, Language language, List items, int position)
    {
        addLinkedItems(item, itemIsSource, qualifier, language, items, position, true);
    }


    public void addLinkedItems(Item item, boolean itemIsSource, String qualifier, Language language, List items, int position, boolean shift)
    {
        addLinkedItems(getSession().getSessionContext(), item, itemIsSource, qualifier, language, items, position, shift);
    }


    public void addLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List items)
    {
        addLinkedItems(ctx, item, itemIsSource, qualifier, language, items, -1);
    }


    public void addLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List items, boolean markModified)
    {
        addLinkedItems(ctx, item, itemIsSource, qualifier, language, items, -1, true, markModified);
    }


    public void addLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List items, int position)
    {
        addLinkedItems(ctx, item, itemIsSource, qualifier, language, items, position, true);
    }


    public void addLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List items, int position, boolean shift)
    {
        addLinkedItems(ctx, item, itemIsSource, qualifier, language, items, position, shift, true, true, true);
    }


    public void addLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List items, int position, boolean shift, boolean markModified)
    {
        addLinkedItems(ctx, item, itemIsSource, qualifier, language, items, position, shift, true, true, markModified);
    }


    public void addLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List items, int position, boolean shift, boolean sortSrc2Tgt, boolean sortTgt2Src)
    {
        addLinkedItems(ctx, item, itemIsSource, qualifier, language, items, position, shift, sortSrc2Tgt, sortTgt2Src, true);
    }


    public void addLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List items, int position, boolean shift, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        getLinkOperationFactory().createInsertOperation(ctx, item, itemIsSource, qualifier, language, items, position, shift, sortSrc2Tgt, sortTgt2Src, markModified)
                        .perform();
    }


    public void removeLinkedItems(Item item, boolean itemIsSource, String qualifier, Language lang, List<Item> itemsToUnlink)
    {
        removeLinkedItems(getSession().getSessionContext(), item, itemIsSource, qualifier, lang, itemsToUnlink);
    }


    private Set<RelationType> filterRelationsWithoutOwnDeploymentAndAbstract(Set<RelationType> types, int plainLinkTC)
    {
        if(types == null || types.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<RelationType> ret = null;
        for(RelationType rt : types)
        {
            if(!rt.isAbstract() && rt.getItemTypeCode() != plainLinkTC)
            {
                String jndi = rt.getJNDIName();
                String table = ((ComposedTypeEJBImpl)rt.getImplementation()).getTable();
                if(jndi != null && jndi.length() > 0 && table == null)
                {
                    continue;
                }
                if(ret == null)
                {
                    ret = new HashSet<>(types.size());
                }
                ret.add(rt);
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    public void removeAllLinksFor(SessionContext ctx, Item item)
    {
        long time1 = System.currentTimeMillis();
        ComposedType linkType = TypeManager.getInstance().getComposedType(Link.class);
        int linkTC = linkType.getItemTypeCode();
        Set<RelationType> relations = new HashSet<>(item.getComposedType().getRelations());
        relations.addAll(getNonNavigableRelations(item));
        if(hasDisabledRelations(item))
        {
            relations.stream()
                            .filter(rel -> isRelationDisabled(item, rel))
                            .findFirst()
                            .ifPresent(rel -> relations.remove(rel));
        }
        Map<PK, RelationType> relMap = (Map<PK, RelationType>)relations.stream().collect(Collectors.toMap(Item::getPK, Function.identity()));
        Set<RelationType> relTypes = filterRelationsWithoutOwnDeploymentAndAbstract(relations, linkTC);
        long time2 = System.currentTimeMillis();
        boolean needUnion = !relTypes.isEmpty();
        boolean dontNotify = (ctx != null && Boolean.TRUE.equals(ctx.getAttribute("disable.link.removal.notification")));
        StringBuilder stringBuilder = new StringBuilder();
        if(needUnion)
        {
            stringBuilder.append("SELECT res.linkPK ");
            if(!dontNotify)
            {
                stringBuilder.append(" , res.src, res.tgt, res.rel ");
            }
            stringBuilder.append(" FROM ( {{");
        }
        stringBuilder.append("SELECT {").append(Item.PK).append("} AS linkPK ");
        if(!dontNotify)
        {
            stringBuilder.append(",{").append("source").append("} AS src,");
            stringBuilder.append("{").append("target").append("} AS tgt, ");
            stringBuilder.append("{").append(Item.TYPE).append("} AS rel ");
        }
        stringBuilder.append("FROM {").append(linkType.getCode()).append("*} ");
        stringBuilder.append("WHERE {").append("source").append("}= ?item  OR ");
        stringBuilder.append("{").append("target").append("}= ?item ");
        if(item instanceof Language)
        {
            stringBuilder.append(" OR {").append("language").append("} = ?item ");
        }
        if(needUnion)
        {
            stringBuilder.append("}}");
            for(RelationType rt : relTypes)
            {
                stringBuilder.append(" UNION ");
                stringBuilder.append("{{");
                stringBuilder.append("SELECT {").append(Item.PK).append("} AS linkPK ");
                if(!dontNotify)
                {
                    stringBuilder.append(",{").append("source").append("} AS src, ");
                    stringBuilder.append("{").append("target").append("} AS tgt, ");
                    stringBuilder.append("{").append(Item.TYPE).append("} AS rel ");
                }
                stringBuilder.append("FROM {").append(rt.getCode()).append("*} ");
                stringBuilder.append("WHERE {").append("source").append("}= ?item  OR ");
                stringBuilder.append("{").append("target").append("}= ?item ");
                if(item instanceof Language)
                {
                    stringBuilder.append(" OR {").append("language").append("} = ?item ");
                }
                stringBuilder.append("}}");
            }
            stringBuilder.append(") res ");
        }
        ctx = new SessionContext(ctx);
        ctx.setAttribute("disableRestrictions", Boolean.TRUE);
        List<PK> rows = FlexibleSearch.getInstance()
                        .search(ctx, stringBuilder.toString(), Collections.singletonMap("item", item), dontNotify ? Collections.<Class<PK>>singletonList(PK.class) : Arrays.<Class<?>[]>asList((Class<?>[][])new Class[] {PK.class, PK.class, PK.class, PK.class}, ), true, true, 0, -1).getResult();
        long time3 = System.currentTimeMillis();
        try
        {
            Map<PK, RelationType> otherItems = dontNotify ? null : new HashMap<>(rows.size());
            Collection<PK> linkPKs = new ArrayList<>(rows.size());
            if(dontNotify)
            {
                linkPKs = rows;
            }
            else
            {
                for(List<PK> row : rows)
                {
                    linkPKs.add(row.get(0));
                    RelationType relationType = relMap.get(row.get(3));
                    PK sourcePk = row.get(1);
                    if(sourcePk != null)
                    {
                        otherItems.put(sourcePk, relationType);
                    }
                    PK targetPk = row.get(2);
                    if(targetPk != null)
                    {
                        otherItems.put(targetPk, relationType);
                    }
                }
                otherItems.remove(item.getPK());
            }
            Map<PK, String> failed = null;
            Collection<Link> linkItems = JaloSession.lookupItems(ctx, linkPKs, true, false);
            for(Link l : linkItems)
            {
                try
                {
                    l.remove(ctx);
                }
                catch(Exception e)
                {
                    if(failed == null)
                    {
                        failed = new HashMap<>(linkItems.size());
                    }
                    failed.put(l.getPK(), e.getMessage());
                }
            }
            if(failed != null)
            {
                for(Link notRemoved : JaloSession.lookupItems(ctx, failed.keySet(), true, false))
                {
                    if(notRemoved != null)
                    {
                        PK pk = notRemoved.getPK();
                        LOG.error("Got exception when removing link " + pk + ". Exception is: " + (String)failed.get(pk));
                    }
                }
            }
            if(!dontNotify)
            {
                failed = null;
                Date now = new Date();
                Collection<PK> otherItemsToModify = (Collection<PK>)otherItems.entrySet().stream().filter(e -> Utilities.getMarkModifiedOverride((RelationType)e.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());
                for(Item i : JaloSession.lookupItems(ctx, otherItemsToModify, true, false))
                {
                    try
                    {
                        i.setModificationTime(now);
                    }
                    catch(Exception e)
                    {
                        if(failed == null)
                        {
                            failed = new HashMap<>(otherItems.size());
                        }
                        failed.put(i.getPK(), e.getMessage());
                    }
                }
                if(failed != null)
                {
                    for(Link notRemoved : JaloSession.lookupItems(ctx, failed.keySet(), true, false))
                    {
                        if(notRemoved != null)
                        {
                            PK pk = notRemoved.getPK();
                            LOG.error("Got exception when marking item " + pk + " modified. Exception is: " + (String)failed.get(pk));
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new JaloSystemException(e);
        }
        long time4 = System.currentTimeMillis();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("removing links for " + item.getPK() + " took " + time4 - time1 + "ms (typesystem:" + time2 - time1 + ",search:" + time3 - time2 + ",remove:" + time4 - time3 + ")");
        }
    }


    protected boolean hasDisabledRelations(Item item)
    {
        return PropertyActionReader.getPropertyActionReader().isActionDisabledForType(".+?relation.removal", item.getComposedType().getCode());
    }


    protected boolean isRelationDisabled(Item item, RelationType rel)
    {
        return PropertyActionReader.getPropertyActionReader().isActionDisabledForType(rel.getCode() + ".relation.removal", item
                        .getComposedType().getCode());
    }


    private Collection<RelationType> getNonNavigableRelations(Item item)
    {
        NonNavigableRelationsDAO nonNavigableRelationsDAO = (NonNavigableRelationsDAO)Registry.getApplicationContext().getBean("nonNavigableRelationsDAO", NonNavigableRelationsDAO.class);
        return nonNavigableRelationsDAO.getNonNavigableRelationsForTypeCode(item.getComposedType().getCode());
    }


    public void removeLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List<Item> items)
    {
        removeLinkedItems(ctx, item, itemIsSource, qualifier, language, items, true, true);
    }


    public void removeLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List<Item> items, boolean sortSrc2Tgt, boolean sortTgt2Src)
    {
        removeLinkedItems(ctx, item, itemIsSource, qualifier, language, items, sortSrc2Tgt, sortTgt2Src, true);
    }


    public void removeLinkedItems(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List<Item> items, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        getLinkOperationFactory()
                        .createRemoveOperation(ctx, item, itemIsSource, qualifier, language, items, sortSrc2Tgt, sortTgt2Src, markModified)
                        .perform();
    }


    private LinkOperationFactory getLinkOperationFactory()
    {
        return this.factory;
    }


    public Collection<Link> getLinks(String qualifier, Language language, Item source, Item target)
    {
        boolean isLikePattern = SearchTools.isLIKEPattern(qualifier);
        boolean isRootRelation = (!isLikePattern && isRootRelationType(qualifier));
        boolean empty = true;
        StringBuilder stringBuilder = new StringBuilder();
        Map<Object, Object> params = new HashMap<>();
        stringBuilder.append("SELECT {").append(Item.PK).append("} FROM {")
                        .append(isRootRelation ? qualifier : (isLikePattern ? "Link" : "Link!")).append("} ");
        if(!isRootRelation)
        {
            stringBuilder.append("WHERE ");
            empty = false;
            stringBuilder.append("{").append("qualifier").append("}").append(isLikePattern ? " LIKE ?quali" : " = ?quali");
            params.put("quali", qualifier);
        }
        if(!Link.ANYLANGUAGE.equals(language))
        {
            if(empty)
            {
                stringBuilder.append("WHERE ");
                empty = false;
            }
            else
            {
                stringBuilder.append(" AND ");
            }
            stringBuilder.append("{").append("language").append("}");
            if(language == null)
            {
                stringBuilder.append(" IS NULL");
            }
            else
            {
                stringBuilder.append(" = ?language ");
                params.put("language", language.getPK());
            }
        }
        if(!Link.ANYITEM.equals(source))
        {
            if(empty)
            {
                stringBuilder.append("WHERE ");
                empty = false;
            }
            else
            {
                stringBuilder.append(" AND ");
            }
            stringBuilder.append("{").append("source").append("}");
            if(source == null)
            {
                stringBuilder.append(" IS NULL");
            }
            else
            {
                stringBuilder.append("= ?source ");
                params.put("source", source.getPK());
            }
        }
        if(!Link.ANYITEM.equals(target))
        {
            if(empty)
            {
                stringBuilder.append("WHERE ");
                empty = false;
            }
            else
            {
                stringBuilder.append(" AND ");
            }
            stringBuilder.append("{").append("target").append("}");
            if(target == null)
            {
                stringBuilder.append(" IS NULL");
            }
            else
            {
                stringBuilder.append("= ?target ");
                params.put("target", target.getPK());
            }
        }
        SessionContext myCtx = getSession().createSessionContext();
        myCtx.setAttribute("disableRestrictions", Boolean.TRUE);
        myCtx.setAttribute("disableSessionAttributes", Boolean.TRUE);
        return FlexibleSearch.getInstance()
                        .search(stringBuilder.toString(), params, Collections.singletonList(Link.class), true, true, 0, -1)
                        .getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Link createLink(String qualifier, Language language, Item source, Item target, int sequenceNumber) throws ConsistencyCheckException
    {
        return createLink(qualifier, language, source, target, sequenceNumber, 0);
    }


    public Link createLink(String qualifier, Language language, Item source, Item target, int sequenceNumber, int reverseSequenceNumber) throws ConsistencyCheckException
    {
        try
        {
            Link link = (Link)wrap(((LinkManagerEJB)
                            getRemote()).createLink(qualifier, (source == null) ? null : source.getPK(),
                            (target == null) ? null : target.getPK(), sequenceNumber, reverseSequenceNumber));
            link.setLanguage(language);
            return link;
        }
        catch(EJBInvalidParameterException e)
        {
            throw new ConsistencyCheckException(e.getMessage(), 0);
        }
    }


    public Link createLink(String qualifier, Language language, PK sourcePK, PK targetPK, int sequenceNumber, int reverseSequenceNumber) throws ConsistencyCheckException
    {
        try
        {
            Link link = (Link)wrap(((LinkManagerEJB)
                            getRemote()).createLink(qualifier, sourcePK, targetPK, sequenceNumber, reverseSequenceNumber));
            link.setLanguage(language);
            return link;
        }
        catch(EJBInvalidParameterException e)
        {
            throw new ConsistencyCheckException(e.getMessage(), 0);
        }
    }


    public void createLinkNoWrap(String qualifier, Language language, Item source, Item target, int sequenceNumber, int reverseSequenceNumber) throws ConsistencyCheckException
    {
        createLink(qualifier, language,
                        (source == null) ? null : source.getPK(),
                        (target == null) ? null : target.getPK(), sequenceNumber, reverseSequenceNumber);
    }


    public void createLinkNoWrap(String qualifier, Language language, PK sourcePK, PK targetPK, int sequenceNumber, int reverseSequenceNumber) throws ConsistencyCheckException
    {
        createLink(qualifier, language, sourcePK, targetPK, sequenceNumber, reverseSequenceNumber);
    }


    public Collection<Link> getLinks(String qualifier, Item source, Item target)
    {
        return getLinks(qualifier, null, source, target);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Link createLink(String qualifier, Item source, Item target, int sequenceNumber) throws ConsistencyCheckException
    {
        return createLink(qualifier, source, target, sequenceNumber, 0);
    }


    public Link createLink(String qualifier, Item source, Item target, int sequenceNumber, int reverseSequenceNumber) throws ConsistencyCheckException
    {
        return createLink(qualifier, null, source, target, sequenceNumber, reverseSequenceNumber);
    }


    public Object writeReplace() throws ObjectStreamException
    {
        return new LinkManagerSerializableDTO(getTenant());
    }
}
