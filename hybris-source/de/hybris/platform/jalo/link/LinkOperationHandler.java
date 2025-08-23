package de.hybris.platform.jalo.link;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

public class LinkOperationHandler
{
    private final boolean localized;
    private final boolean itemIsSource;
    private final String qualifier;
    private List<LinkData> gatheredData;
    private boolean replace = false;
    private boolean preserveHiddenLanguages = true;
    private boolean unlink = false;
    private boolean shift = true;
    private boolean skipGatheringExistingLinks = false;
    private int position = -1;
    protected boolean done = false;
    private boolean touchItemsIfRelationChanged = true;
    private Set<Item> itemsToTouch = null;
    private boolean sortTgtEnd = true;
    private boolean sortSrcEnd = true;
    private boolean allowDuplicates = true;
    private final boolean oracleMode;
    private final boolean sqlServerMode;
    private Boolean isAdmin = null;
    private final List<Item> items;
    private final Map<PK, List<Item>> toBeLinked;
    private final Set<Language> languages;
    private Set<Item> unlinkedItems;


    public LinkOperationHandler(Item item, List<Item> toBeLinked, boolean isSource, String qualifier)
    {
        this(Collections.singletonList(item), Collections.singletonMap((Language)null, toBeLinked), isSource, qualifier, false);
    }


    public LinkOperationHandler(Item item, List<Item> toBeLinked, boolean isSource, String qualifier, Language lang)
    {
        this(item, toBeLinked, isSource, qualifier, lang, true);
    }


    public LinkOperationHandler(Item item, List<Item> toBeLinked, boolean isSource, String qualifier, Language lang, boolean markModified)
    {
        this(Collections.singletonList(item), Collections.singletonMap(lang, toBeLinked), isSource, qualifier, (lang != null), markModified);
    }


    public LinkOperationHandler(Item item, Map<Language, List<Item>> toBeLinked, boolean isSource, String qualifier)
    {
        this(Collections.singletonList(item), toBeLinked, isSource, qualifier, true);
    }


    public LinkOperationHandler(List<Item> items, List<Item> toBeLinked, boolean isSource, String qualifier)
    {
        this(items, Collections.singletonMap((Language)null, toBeLinked), isSource, qualifier, false);
    }


    public LinkOperationHandler(List<Item> items, List<Item> toBeLinked, boolean isSource, String qualifier, Language lang)
    {
        this(items, toBeLinked, isSource, qualifier, lang, true);
    }


    public LinkOperationHandler(List<Item> items, List<Item> toBeLinked, boolean isSource, String qualifier, Language lang, boolean markModified)
    {
        this(items, Collections.singletonMap(lang, toBeLinked), isSource, qualifier, (lang != null), markModified);
    }


    public LinkOperationHandler(List<Item> items, Map<Language, List<Item>> toBeLinked, boolean isSource, String qualifier, boolean localized)
    {
        this(items, toBeLinked, isSource, qualifier, localized, true);
    }


    public LinkOperationHandler(List<Item> items, Map<Language, List<Item>> toBeLinked, boolean isSource, String qualifier, boolean localized, boolean markModified)
    {
        this.oracleMode = Config.isOracleUsed();
        this.sqlServerMode = Config.isSQLServerUsed();
        this.qualifier = qualifier;
        this.itemIsSource = isSource;
        this.localized = localized;
        this.items = new ArrayList<>(items);
        this.toBeLinked = new HashMap<>();
        this.languages = localized ? new HashSet<>() : Collections.<Language>singleton((Language)null);
        this.touchItemsIfRelationChanged = markModified;
        for(Map.Entry<Language, List<Item>> e : toBeLinked.entrySet())
        {
            if(e.getKey() != null)
            {
                this.languages.add(e.getKey());
            }
            this.toBeLinked.put(
                            (e.getKey() != null) ? ((Language)e.getKey()).getPK() : null,
                            (e.getValue() != null) ? new ArrayList<>(e.getValue()) : null);
            checkForInvalidItems();
        }
    }


    protected void checkForInvalidItems()
    {
        for(Map.Entry<PK, List<Item>> entry : this.toBeLinked.entrySet())
        {
            List<Item> list = entry.getValue();
            if(list != null)
            {
                for(int i = 0, s = list.size(); i < s; i++)
                {
                    if(list.get(i) == null)
                    {
                        throw new IllegalArgumentException("found NULL item in link set " + entry.getKey() + "->" + list);
                    }
                }
            }
        }
    }


    private void addItemsToTouch(Item... items)
    {
        if(isMarkItemsModified())
        {
            if(this.itemsToTouch == null)
            {
                this.itemsToTouch = new HashSet<>();
            }
            if(items.length == 1)
            {
                this.itemsToTouch.add(items[0]);
            }
            else
            {
                this.itemsToTouch.addAll(Arrays.asList(items));
            }
        }
    }


    private boolean hasItemsToTouch()
    {
        return CollectionUtils.isNotEmpty(this.itemsToTouch);
    }


    public List<Item> getItems()
    {
        return this.items;
    }


    public Collection<Item> getToBeLinked(Language lang)
    {
        return getToBeLinked((lang != null) ? lang.getPK() : null);
    }


    protected Collection<Item> getToBeLinked(PK langPK)
    {
        List<Item> ret = this.toBeLinked.get(langPK);
        return (ret != null) ? (Collection<Item>)ImmutableList.copyOf(ret) : Collections.EMPTY_LIST;
    }


    protected Set<Item> getAllToBeLinked()
    {
        ImmutableSet.Builder<Item> ret = new ImmutableSet.Builder();
        for(Map.Entry<PK, List<Item>> e : this.toBeLinked.entrySet())
        {
            if(e.getValue() != null && !((List)e.getValue()).isEmpty())
            {
                ret.addAll(e.getValue());
            }
        }
        return (Set<Item>)ret.build();
    }


    public boolean hasItemsToLink()
    {
        for(Map.Entry<PK, List<Item>> e : this.toBeLinked.entrySet())
        {
            if(e.getValue() != null && !((List)e.getValue()).isEmpty())
            {
                return true;
            }
        }
        return false;
    }


    protected Set<Item> getUnlikedItems()
    {
        return (this.unlinkedItems != null) ? this.unlinkedItems : Collections.EMPTY_SET;
    }


    public boolean perform(SessionContext ctx)
    {
        if(!this.done)
        {
            this.done = true;
            return (mustPerform() && doPerform(ctx));
        }
        throw new IllegalStateException("link operation " + this + " already performed -cannot call twice");
    }


    private boolean mustPerform()
    {
        return ((isReplace() && (
                        !isLocalized() || !isPreserveHiddenLanguages() || CollectionUtils.isNotEmpty(getLanguages()))) ||
                        hasItemsToLink());
    }


    private boolean doPerform(SessionContext ctx)
    {
        gatherData(ctx);
        prefetchItems(getGatheredData());
        boolean isAnythingChanged = false;
        boolean isAnyLinkToRemove = false;
        for(LinkData link : getGatheredData())
        {
            if(link.isPositionsChanged())
            {
                isAnythingChanged |= shiftLink(ctx, link);
                continue;
            }
            if(link.isRemove())
            {
                isAnyLinkToRemove = true;
                continue;
            }
            createNewLink(ctx, link);
            isAnythingChanged = true;
        }
        try
        {
            if(isAnyLinkToRemove)
            {
                isAnythingChanged |= (new LinksToDelete(this, getGatheredData())).removeAll(ctx);
            }
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e);
        }
        markItemsModifiedIfNecessary();
        return isAnythingChanged;
    }


    private void markItemsModifiedIfNecessary()
    {
        if(isMarkItemsModified() && hasItemsToTouch())
        {
            Date timeStamp = new Date();
            for(Item item : Utilities.sortItemsByPK(this.itemsToTouch))
            {
                item.setModificationTime(timeStamp);
            }
        }
    }


    private void gatherData(SessionContext ctx)
    {
        if(isReplace())
        {
            setGatheredData(gatherReplaceAllData(ctx));
        }
        else if(isUnlink())
        {
            setGatheredData(gatherUnlinkData(ctx));
        }
        else
        {
            setGatheredData((getPosition() >= 0) ? gatherInsertData(ctx) : gatherAppendOnlyData(ctx));
        }
    }


    private boolean isRootRelationType(String qualifier)
    {
        return LinkManager.getInstance().getTenant().getPersistenceManager().isRootRelationType(qualifier);
    }


    protected String getLinkTypeCode(String qualifier)
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


    private void prefetchItems(Collection<LinkData> dataSet)
    {
        Map<PK, LinkData> linkPKMap = new HashMap<>(dataSet.size());
        List<PK> itemPKs = new ArrayList<>(dataSet.size());
        Set<Item> unlinkedAlreadyLoaded = new HashSet<>();
        for(LinkData data : dataSet)
        {
            if(data.exists())
            {
                linkPKMap.put(data.getLinkPK(), data);
                if(data.isRemove() && isMarkItemsModified())
                {
                    if(isItemSource())
                    {
                        if(data.getTgtItem() != null)
                        {
                            unlinkedAlreadyLoaded.add(data.getTgtItem());
                            continue;
                        }
                        itemPKs.add(data.getTgt());
                        continue;
                    }
                    if(data.getSrcItem() != null)
                    {
                        unlinkedAlreadyLoaded.add(data.getSrcItem());
                        continue;
                    }
                    itemPKs.add(data.getSrc());
                }
            }
        }
        boolean ignoreMissing = true;
        boolean returnMissingAsNull = false;
        Collection<Link> links = JaloSession.lookupItems(JaloSession.CTX_NO_PREFTCH_LANGUAGES, linkPKMap
                        .keySet(), true, false);
        for(Link l : links)
        {
            LinkData singleData = linkPKMap.get(l.getPK());
            singleData.setLinkItem(l);
        }
        if(!itemPKs.isEmpty() || !unlinkedAlreadyLoaded.isEmpty())
        {
            this.unlinkedItems = new HashSet<>();
            this.unlinkedItems.addAll(unlinkedAlreadyLoaded);
            this.unlinkedItems.addAll(JaloSession.lookupItems(JaloSession.CTX_NO_PREFTCH_LANGUAGES, itemPKs, true, false));
        }
    }


    private boolean shiftLink(SessionContext ctx, LinkData data)
    {
        Link item = data.getLinkItem();
        if(item == null)
        {
            throw new IllegalStateException("no item for link record " + data);
        }
        boolean changed = false;
        if(isItemSource() && isSortTgtEnd())
        {
            if(item.getSequenceNumber() != data.getSrcPosition())
            {
                item.setSequenceNumber(data.getSrcPosition());
                addItemsToTouch(new Item[] {data.getSrcItem()});
                changed = true;
            }
        }
        else if(!isItemSource() && isSortSrcEnd())
        {
            if(item.getReverseSequenceNumber() != data.getTgtPosition())
            {
                item.setReverseSequenceNumber(data.getTgtPosition());
                addItemsToTouch(new Item[] {data.getTgtItem()});
                changed = true;
            }
        }
        return changed;
    }


    private void createNewLink(SessionContext ctx, LinkData data)
    {
        try
        {
            if(data.getSrcItem() == null)
            {
                throw new IllegalArgumentException("link data " + data + " got no src item");
            }
            if(data.getTgtItem() == null)
            {
                throw new IllegalArgumentException("link data " + data + " got no tgt item");
            }
            if(data.isLocalized())
            {
                LinkManager.getInstance().createLinkNoWrap(getQualifier(), data.getLang(), data.getSrcItem(), data.getTgtItem(),
                                isSortTgtEnd() ? data.getSrcPosition() : 0,
                                isSortSrcEnd() ? data.getTgtPosition() : 0);
            }
            else
            {
                LinkManager.getInstance().createLinkNoWrap(getQualifier(), null, data.getSrcItem(), data.getTgtItem(),
                                isSortTgtEnd() ? data.getSrcPosition() : 0,
                                isSortSrcEnd() ? data.getTgtPosition() : 0);
            }
            addItemsToTouch(new Item[] {data.getSrcItem(), data.getTgtItem()});
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public boolean isReplace()
    {
        return this.replace;
    }


    public void setReplace(boolean replace)
    {
        this.replace = replace;
    }


    public int getPosition()
    {
        return this.position;
    }


    public void setPosition(int position)
    {
        this.position = position;
    }


    public boolean isItemSource()
    {
        return this.itemIsSource;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public boolean isLocalized()
    {
        return this.localized;
    }


    public Set<Language> getLanguages()
    {
        return this.languages;
    }


    protected List<LinkData> getGatheredData()
    {
        if(this.gatheredData == null)
        {
            throw new IllegalStateException("no data gathered yet");
        }
        return this.gatheredData;
    }


    protected void setGatheredData(List<LinkData> data)
    {
        if(this.gatheredData != null)
        {
            throw new IllegalStateException("data already gathered");
        }
        this.gatheredData = data;
    }


    private List<List<?>> queryExistingLinks4Unlink(SessionContext ctx)
    {
        Map<String, List<Item>> params = Maps.newHashMapWithExpectedSize(3);
        params.put("items", getItems());
        params.put("quali", getQualifier());
        if(isLocalized())
        {
            params.put("languages", getLanguages());
        }
        return
                        FlexibleSearch.getInstance()
                                        .search(ctx, "SELECT {" + (
                                                                        isItemSource() ? "source" : "target") + "} as itemPK,{language} as langPK, {" + (
                                                                        isItemSource() ? "target" : "source") + "} AS linkedItemPK, {" + Item.PK + "} AS linkPK FROM {" +
                                                                        getLinkTypeCode(getQualifier()) + "*} WHERE {" + (
                                                                        isItemSource() ? "source" : "target") + "} IN ( ?items ) AND {qualifier}=?quali AND {language}" + (
                                                                        isLocalized() ? " IN ( ?languages ) " : " IS NULL ") + "ORDER BY {" + (
                                                                        isItemSource() ? "sequenceNumber" : "reverseSequenceNumber") + "} ASC ", params,
                                                        Arrays.asList((Class<?>[][])new Class[] {PK.class, PK.class, PK.class, PK.class}, ), true, true, 0, -1).getResult();
    }


    private Collection<Item> prepareToBeLinked(boolean localized)
    {
        if(localized)
        {
            return getAllToBeLinked();
        }
        return getToBeLinked((PK)null);
    }


    protected List<LinkData> gatherUnlinkData(SessionContext ctx)
    {
        int ITEM_PK = 0;
        int LANG_PK = 1;
        int LINKED_ITEM_PK = 2;
        int LINK_PK = 3;
        List<List<?>> rows = this.skipGatheringExistingLinks ? Collections.EMPTY_LIST : queryExistingLinks4Unlink(ctx);
        List<LinkData> data = new ArrayList<>();
        for(Language lang : getLanguages())
        {
            PK langPK = (lang != null) ? lang.getPK() : null;
            Map<PK, Map<PK, List<PK>>> records = new HashMap<>();
            for(List<?> row : rows)
            {
                PK myLangPK = (PK)row.get(1);
                if(myLangPK != langPK && (myLangPK == null || !myLangPK.equals(langPK)))
                {
                    continue;
                }
                PK itemPK = (PK)row.get(0);
                PK linkedItemPK = (PK)row.get(2);
                PK linkPK = (PK)row.get(3);
                Map<PK, List<PK>> recordsForItem = records.get(itemPK);
                if(recordsForItem == null)
                {
                    records.put(itemPK, recordsForItem = new HashMap<>());
                }
                List<PK> linkPKs = recordsForItem.get(linkedItemPK);
                if(linkPKs == null)
                {
                    recordsForItem.put(linkedItemPK, linkPKs = new ArrayList<>(3));
                }
                linkPKs.add(linkPK);
            }
            for(Item item : getItems())
            {
                PK itemPK = item.getPK();
                for(Item toUnlink : getToBeLinked(lang))
                {
                    Map<PK, List<PK>> linksForItem = records.get(itemPK);
                    PK linkedItemPK = toUnlink.getPK();
                    List<PK> linksToRemove = (linksForItem != null) ? linksForItem.get(linkedItemPK) : null;
                    PK linkToRemove = (linksToRemove != null && !linksToRemove.isEmpty()) ? linksToRemove.remove(0) : null;
                    if(linkToRemove != null)
                    {
                        LinkData removeData = new LinkData(linkToRemove, isItemSource() ? itemPK : linkedItemPK, -1, isItemSource() ? linkedItemPK : itemPK, -1, lang);
                        removeData.setRemove(true);
                        if(isItemSource())
                        {
                            removeData.setSrcItem(item);
                            removeData.setTgtItem(toUnlink);
                        }
                        else
                        {
                            removeData.setSrcItem(toUnlink);
                            removeData.setTgtItem(item);
                        }
                        data.add(removeData);
                    }
                }
            }
        }
        return data;
    }


    protected List<List<?>> queryExistingLinks4ReplaceAll(SessionContext ctx, boolean sort, boolean sortReverse)
    {
        String reversePositionsQuery;
        ImmutableMap.Builder<String, Object> params = new ImmutableMap.Builder();
        params.put("items", getItems());
        params.put("quali", getQualifier());
        if(isLocalized())
        {
            params.put("languages", isPreserveHiddenLanguages() ? getLanguages() : C2LManager.getInstance().getAllLanguages());
        }
        Collection<Item> toBeLinked = prepareToBeLinked(isLocalized());
        int oraclePages = 1;
        boolean inline4SqlServer = false;
        if(!toBeLinked.isEmpty())
        {
            if(this.oracleMode && toBeLinked.size() > 1000)
            {
                List<Item> list = new ArrayList<>(toBeLinked);
                for(int i = 0; i < toBeLinked.size(); i += 1000)
                {
                    params.put("toBeLinked" + oraclePages, list.subList(i, Math.min(i + 1000, toBeLinked.size())));
                    oraclePages++;
                }
            }
            else if(this.sqlServerMode && toBeLinked.size() + getLanguages().size() + getItems().size() + 1 > 2000)
            {
                inline4SqlServer = true;
            }
            else
            {
                params.put("toBeLinked", toBeLinked);
            }
        }
        String existingLinksQuery =
                        "SELECT {" + (isItemSource() ? "source" : "target") + "} as itemPK,{language} as langPK, {" + (isItemSource() ? "target" : "source") + "} AS linkedItemPK, {" + Item.PK + "} AS linkPK, {" + (isItemSource() ? "sequenceNumber" : "reverseSequenceNumber") + "} AS cnt FROM {"
                                        + getLinkTypeCode(getQualifier()) + "*} WHERE {" + (isItemSource() ? "source" : "target") + "} IN ( ?items ) AND {qualifier}=?quali " + (isLocalized() ? " AND {language} IN ( ?languages ) " : " AND {language} IS NULL ");
        if(!sortReverse || toBeLinked.isEmpty())
        {
            reversePositionsQuery = null;
        }
        else
        {
            String tmp = "SELECT null as itemPK, {language} as langPK, {" + (isItemSource() ? "target" : "source") + "} AS linkedItemPK, null as linkPK, MAX({" + (isItemSource() ? "reverseSequenceNumber" : "sequenceNumber") + "}) AS cnt FROM {" + getLinkTypeCode(getQualifier()) + "*} WHERE ";
            if(oraclePages > 1)
            {
                tmp = tmp + "(";
                for(int i = 1; i < oraclePages; i++)
                {
                    tmp = tmp + tmp + "{" + ((i > 1) ? " OR " : "") + "} IN ( ?toBeLinked" + (isItemSource() ? "target" : "source") + ") ";
                }
                tmp = tmp + " ) AND ";
            }
            else if(inline4SqlServer)
            {
                tmp = tmp + "{" + tmp + "} IN (";
                boolean first = true;
                for(Item toLink : toBeLinked)
                {
                    if(first)
                    {
                        first = false;
                    }
                    else
                    {
                        tmp = tmp + ",";
                    }
                    tmp = tmp + tmp;
                }
                tmp = tmp + " ) AND ";
            }
            else
            {
                tmp = tmp + "{" + tmp + "} IN ( ?toBeLinked ) AND ";
            }
            tmp = tmp + "{qualifier}=?quali " + tmp + "GROUP BY {language}, {" + (isLocalized() ? " AND {language} IN ( ?languages ) " : " AND {language} IS NULL ") + "}";
            reversePositionsQuery = tmp;
        }
        return
                        FlexibleSearch.getInstance()
                                        .search(ctx,
                                                        (reversePositionsQuery == null) ? (existingLinksQuery + existingLinksQuery) : ("SELECT itemPK, langPK, linkedItemPK, linkPK, cnt FROM ({{ " +
                                                                        existingLinksQuery + " }} UNION ALL {{ " + reversePositionsQuery + " }}) xyz ORDER BY cnt ASC"), (Map)params.build(),
                                                        Arrays.asList((Class<?>[][])new Class[] {PK.class, PK.class, PK.class, PK.class, Integer.class}, ), true, true, 0, -1).getResult();
    }


    protected List<LinkData> gatherReplaceAllData(SessionContext ctx)
    {
        int ITEM_PK = 0;
        int LANG_PK = 1;
        int LINKED_ITEM_PK = 2;
        int LINK_PK = 3;
        int POS = 4;
        boolean sort = isItemSource() ? isSortTgtEnd() : isSortSrcEnd();
        boolean sortReverse = isItemSource() ? isSortSrcEnd() : isSortTgtEnd();
        boolean uniqueMode = !isAllowDuplicates();
        List<List<?>> rows = this.skipGatheringExistingLinks ? Collections.EMPTY_LIST : queryExistingLinks4ReplaceAll(ctx, sort, sortReverse);
        List<LinkData> data = new ArrayList<>();
        Set<PK> langPKs = new HashSet<>();
        if(!isLocalized())
        {
            langPKs.add(null);
        }
        else
        {
            for(Language lang : getLanguages())
            {
                langPKs.add(lang.getPK());
            }
            if(isReplace())
            {
                for(List<?> row : rows)
                {
                    langPKs.add((PK)row.get(1));
                }
            }
        }
        for(PK langPK : langPKs)
        {
            Map<PK, Integer> linkedItemsPositions = new HashMap<>();
            Map<PK, Map<PK, List<Object[]>>> existingLinks = new HashMap<>();
            for(List<?> row : rows)
            {
                PK myLangPK = (PK)row.get(1);
                if(myLangPK != langPK && (myLangPK == null || !myLangPK.equals(langPK)))
                {
                    continue;
                }
                PK linkPK = (PK)row.get(3);
                if(linkPK == null)
                {
                    linkedItemsPositions.put((PK)row.get(2), (Integer)row.get(4));
                    continue;
                }
                PK itemPK = (PK)row.get(0);
                Map<PK, List<Object[]>> existingLinksForItem = existingLinks.get(itemPK);
                if(existingLinksForItem == null)
                {
                    existingLinks.put(itemPK, existingLinksForItem = new HashMap<>());
                }
                PK linkedItemPK = (PK)row.get(2);
                List<Object[]> records = existingLinksForItem.get(linkedItemPK);
                if(records == null)
                {
                    existingLinksForItem.put(linkedItemPK, records = new ArrayList(3));
                }
                records.add(new Object[] {linkPK, row
                                .get(4)});
            }
            Language lang = null;
            for(Item item : getItems())
            {
                if(langPK != null && lang == null)
                {
                    lang = (Language)JaloSession.getCurrentSession().getItem(langPK);
                }
                PK itemPK = item.getPK();
                Map<PK, List<Object[]>> existingLinksForItem = existingLinks.get(itemPK);
                int position = 0;
                Set<PK> uniqueControlSet = uniqueMode ? new HashSet<>() : null;
                for(Item toLink : getToBeLinked(langPK))
                {
                    PK linkedItemPK = toLink.getPK();
                    if(uniqueMode && !uniqueControlSet.add(linkedItemPK))
                    {
                        continue;
                    }
                    List<Object[]> links = (existingLinksForItem != null) ? existingLinksForItem.get(linkedItemPK) : null;
                    if(links != null && !links.isEmpty())
                    {
                        Object[] lnk = links.remove(0);
                        if(sort)
                        {
                            PK linkPK = (PK)lnk[0];
                            int currentPos = ((Integer)lnk[1]).intValue();
                            if(currentPos != position)
                            {
                                if(isItemSource())
                                {
                                    LinkData shiftedLink = new LinkData(linkPK, itemPK, position, linkedItemPK, -1, lang);
                                    shiftedLink.setPositionsChanged(true);
                                    shiftedLink.setSrcItem(item);
                                    data.add(shiftedLink);
                                }
                                else
                                {
                                    LinkData shiftedLink = new LinkData(linkPK, linkedItemPK, -1, itemPK, position, lang);
                                    shiftedLink.setPositionsChanged(true);
                                    shiftedLink.setTgtItem(item);
                                    data.add(shiftedLink);
                                }
                            }
                        }
                    }
                    else
                    {
                        int linkedItemEndPosition = (sortReverse && linkedItemsPositions.containsKey(linkedItemPK)) ? ((Integer)linkedItemsPositions.get(linkedItemPK)).intValue() : -1;
                        if(isItemSource())
                        {
                            LinkData newOne = new LinkData(null, itemPK, sort ? position : -1, linkedItemPK, sortReverse ? ++linkedItemEndPosition : -1, lang);
                            newOne.setSrcItem(item);
                            newOne.setTgtItem(toLink);
                            data.add(newOne);
                        }
                        else
                        {
                            LinkData newOne = new LinkData(null, linkedItemPK, sortReverse ? ++linkedItemEndPosition : -1, itemPK, sort ? position : -1, lang);
                            newOne.setSrcItem(toLink);
                            newOne.setTgtItem(item);
                            data.add(newOne);
                        }
                        if(sortReverse)
                        {
                            linkedItemsPositions.put(linkedItemPK, Integer.valueOf(linkedItemEndPosition));
                        }
                    }
                    if(sort)
                    {
                        position++;
                    }
                }
            }
            for(Map.Entry<PK, Map<PK, List<Object[]>>> e : existingLinks.entrySet())
            {
                PK itemPK = e.getKey();
                for(Map.Entry<PK, List<Object[]>> e2 : (Iterable<Map.Entry<PK, List<Object[]>>>)((Map)e.getValue()).entrySet())
                {
                    PK linkedItemPK = e2.getKey();
                    for(Object[] rec : e2.getValue())
                    {
                        LinkData removedLink = new LinkData((PK)rec[0], isItemSource() ? itemPK : linkedItemPK, -1, isItemSource() ? linkedItemPK : itemPK, -1, lang);
                        removedLink.setRemove(true);
                        data.add(removedLink);
                    }
                }
            }
        }
        return data;
    }


    private List<List<?>> queryExistingLinks4Insert(SessionContext ctx, boolean uniqueMode, boolean sort, boolean sortReverse)
    {
        String existingLinksToAdjustQuery, reversePositionsQuery;
        Map<Object, Object> params = new HashMap<>();
        params.put("items", getItems());
        params.put("quali", getQualifier());
        params.put("pos", Integer.valueOf(getPosition()));
        if(isLocalized())
        {
            params.put("languages", getLanguages());
        }
        Collection<Item> toBeLinked = prepareToBeLinked(isLocalized());
        params.put("toBeLinked", toBeLinked);
        int oraclePages = 1;
        boolean inline4SqlServer = false;
        if(this.oracleMode && toBeLinked.size() > 1000)
        {
            params.remove("toBeLinked");
            List<Item> list = new ArrayList<>(toBeLinked);
            for(int i = 0; i < toBeLinked.size(); i += 1000)
            {
                params.put("toBeLinked" + oraclePages, list.subList(i, Math.min(i + 1000, toBeLinked.size())));
                oraclePages++;
            }
        }
        else if(this.sqlServerMode && toBeLinked.size() + getLanguages().size() + getItems().size() + 2 > 2000)
        {
            params.remove("toBeLinked");
            inline4SqlServer = true;
        }
        if((sort && isShift()) || uniqueMode)
        {
            String tmp = "SELECT {" + (isItemSource() ? "source" : "target") + "} as itemPK, {language} as langPK, {" + (isItemSource() ? "target" : "source") + "} AS linkedItemPK, {" + Item.PK + "} AS linkPK, {" + (isItemSource() ? "sequenceNumber" : "reverseSequenceNumber") + "} AS cnt FROM {"
                            + getLinkTypeCode(getQualifier()) + "*} WHERE {" + (isItemSource() ? "source" : "target") + "} IN ( ?items ) AND {qualifier}=?quali AND {language}" + (isLocalized() ? " IN ( ?languages ) " : " IS NULL ") + " AND (" + ((sort && isShift()) ? ("{" + (isItemSource()
                            ? "sequenceNumber"
                            : "reverseSequenceNumber") + "}>=?pos ") : "");
            if(uniqueMode)
            {
                if(sort && isShift())
                {
                    tmp = tmp + " OR ";
                }
                if(oraclePages > 1)
                {
                    for(int i = 1; i < oraclePages; i++)
                    {
                        if(i > 1)
                        {
                            tmp = tmp + " OR ";
                        }
                        tmp = tmp + " {" + tmp + "} IN (?toBeLinked" + (isItemSource() ? "target" : "source") + ")";
                    }
                }
                else if(inline4SqlServer)
                {
                    tmp = tmp + " {" + tmp + "} IN (";
                    int index = 0;
                    for(Item toLink : toBeLinked)
                    {
                        if(index++ > 0)
                        {
                            tmp = tmp + ",";
                        }
                        tmp = tmp + tmp;
                    }
                    tmp = tmp + ")";
                }
                else
                {
                    tmp = tmp + "{" + tmp + "} IN (?toBeLinked)";
                }
            }
            tmp = tmp + ")";
            existingLinksToAdjustQuery = tmp;
        }
        else
        {
            existingLinksToAdjustQuery = null;
        }
        if(sortReverse)
        {
            String tmp = "SELECT null as itemPK, {language} as langPK, {" + (isItemSource() ? "target" : "source") + "} AS linkedItemPK, null as linkPK, MAX({" + (isItemSource() ? "reverseSequenceNumber" : "sequenceNumber") + "}) AS cnt FROM {" + getLinkTypeCode(getQualifier()) + "*} WHERE ";
            if(oraclePages > 1)
            {
                tmp = tmp + " ( ";
                for(int i = 1; i < oraclePages; i++)
                {
                    tmp = tmp + tmp + "{" + ((i > 1) ? " OR " : "") + "} IN (?toBeLinked" + (isItemSource() ? "target" : "source") + ") ";
                }
                tmp = tmp + " ) AND ";
            }
            else if(inline4SqlServer)
            {
                tmp = tmp + " {" + tmp + "} IN (";
                int index = 0;
                for(Item toLink : toBeLinked)
                {
                    if(index++ > 0)
                    {
                        tmp = tmp + ",";
                    }
                    tmp = tmp + tmp;
                }
                tmp = tmp + ") AND ";
            }
            else
            {
                tmp = tmp + "{" + tmp + "} IN (?toBeLinked) AND ";
            }
            tmp = tmp + "{qualifier}=?quali AND {language}" + tmp + "GROUP BY {language},{" + (isLocalized() ? " IN ( ?languages ) " : " IS NULL ") + "}";
            reversePositionsQuery = tmp;
        }
        else
        {
            reversePositionsQuery = null;
        }
        String query = null;
        if(existingLinksToAdjustQuery != null && reversePositionsQuery != null)
        {
            query = "SELECT itemPK, langPK, linkedItemPK, linkPK, cnt FROM ({{" + existingLinksToAdjustQuery + " }} UNION ALL {{" + reversePositionsQuery + " }} ) xyz ORDER BY cnt ASC ";
        }
        else if(existingLinksToAdjustQuery != null)
        {
            query = existingLinksToAdjustQuery;
        }
        else if(reversePositionsQuery != null)
        {
            query = reversePositionsQuery;
        }
        return (query != null) ? FlexibleSearch.getInstance()
                        .search(ctx, query, params,
                                        Arrays.asList((Class<?>[][])new Class[] {PK.class, PK.class, PK.class, PK.class, Integer.class}, ), true, true, 0, -1)
                        .getResult() : Collections.EMPTY_LIST;
    }


    protected List<LinkData> gatherInsertData(SessionContext ctx)
    {
        int ITEM_PK = 0;
        int LANG_PK = 1;
        int LINKED_ITEM_PK = 2;
        int LINK_PK = 3;
        int POS = 4;
        boolean sort = isItemSource() ? isSortTgtEnd() : isSortSrcEnd();
        boolean sortReverse = isItemSource() ? isSortSrcEnd() : isSortTgtEnd();
        boolean uniqueMode = !isAllowDuplicates();
        List<List<?>> rows = this.skipGatheringExistingLinks ? Collections.EMPTY_LIST : queryExistingLinks4Insert(ctx, uniqueMode, sort, sortReverse);
        List<LinkData> data = new ArrayList<>();
        for(Language lang : getLanguages())
        {
            PK langPK = (lang != null) ? lang.getPK() : null;
            Map<PK, Integer> linkedItemsPositions = new HashMap<>();
            Map<PK, Map<PK, List<Object[]>>> existingLinks = new HashMap<>();
            for(List<?> row : rows)
            {
                PK myLangPK = (PK)row.get(1);
                if(myLangPK != langPK && (myLangPK == null || !myLangPK.equals(langPK)))
                {
                    continue;
                }
                PK linkPK = (PK)row.get(3);
                if(linkPK == null)
                {
                    linkedItemsPositions.put((PK)row.get(2), (Integer)row.get(4));
                    continue;
                }
                PK itemPK = (PK)row.get(0);
                Map<PK, List<Object[]>> existingLinksForItem = existingLinks.get(itemPK);
                if(existingLinksForItem == null)
                {
                    existingLinks.put(itemPK, existingLinksForItem = new HashMap<>());
                }
                PK linkedItemPK = (PK)row.get(2);
                List<Object[]> existing = existingLinksForItem.get(linkedItemPK);
                if(existing == null)
                {
                    existingLinksForItem.put(linkedItemPK, existing = new ArrayList(3));
                }
                existing.add(new Object[] {linkPK, row
                                .get(4)});
            }
            for(Item item : getItems())
            {
                PK itemPK = item.getPK();
                int position = getPosition();
                Set<PK> uniqueControlSet = uniqueMode ? new HashSet<>() : null;
                Map<PK, List<Object[]>> myExistingLinks = existingLinks.get(itemPK);
                for(Item toLink : getToBeLinked(lang))
                {
                    PK linkedItemPK = toLink.getPK();
                    if(uniqueMode && !uniqueControlSet.add(linkedItemPK))
                    {
                        continue;
                    }
                    List<Object[]> currentLinksTo = (uniqueMode && myExistingLinks != null) ? myExistingLinks.get(linkedItemPK) : null;
                    if(currentLinksTo != null && !currentLinksTo.isEmpty())
                    {
                        myExistingLinks.remove(linkedItemPK);
                        Object[] lnk = currentLinksTo.get(0);
                        PK linkPK = (PK)lnk[0];
                        int currentPos = ((Integer)lnk[1]).intValue();
                        if(sort && currentPos != position)
                        {
                            LinkData shiftedLink = isItemSource() ? new LinkData(linkPK, itemPK, position++, linkedItemPK, -1, lang) : new LinkData(linkPK, linkedItemPK, -1, itemPK, position++, lang);
                            shiftedLink.setPositionsChanged(true);
                            if(isItemSource())
                            {
                                shiftedLink.setSrcItem(item);
                            }
                            else
                            {
                                shiftedLink.setTgtItem(item);
                            }
                            data.add(shiftedLink);
                        }
                        for(int i = 1, s = currentLinksTo.size(); i < s; i++)
                        {
                            Object[] rec = currentLinksTo.get(i);
                            LinkData removedLink = new LinkData((PK)rec[0], isItemSource() ? itemPK : linkedItemPK, -1, isItemSource() ? linkedItemPK : itemPK, -1, lang);
                            removedLink.setRemove(true);
                            data.add(removedLink);
                        }
                        continue;
                    }
                    int linkedItemEndPosition = linkedItemsPositions.containsKey(linkedItemPK) ? ((Integer)linkedItemsPositions.get(linkedItemPK)).intValue() : -1;
                    if(isItemSource())
                    {
                        LinkData newLink = new LinkData(null, itemPK, sort ? position++ : -1, linkedItemPK, sortReverse ? ++linkedItemEndPosition : -1, lang);
                        newLink.setSrcItem(item);
                        newLink.setTgtItem(toLink);
                        data.add(newLink);
                    }
                    else
                    {
                        LinkData newLink = new LinkData(null, linkedItemPK, sortReverse ? ++linkedItemEndPosition : -1, itemPK, sort ? position++ : -1, lang);
                        newLink.setSrcItem(toLink);
                        newLink.setTgtItem(item);
                        data.add(newLink);
                    }
                    if(sortReverse)
                    {
                        linkedItemsPositions.put(linkedItemPK, Integer.valueOf(linkedItemEndPosition));
                    }
                }
                int offset = position - getPosition();
                if(sort && isShift() && myExistingLinks != null && !myExistingLinks.isEmpty())
                {
                    for(Map.Entry<PK, List<Object[]>> e : myExistingLinks.entrySet())
                    {
                        PK linkedItemPK = e.getKey();
                        for(Object[] rec : e.getValue())
                        {
                            int currentPos = ((Integer)rec[1]).intValue();
                            LinkData shiftedLink = isItemSource() ? new LinkData((PK)rec[0], itemPK, currentPos + offset, linkedItemPK, -1, lang) : new LinkData((PK)rec[0], linkedItemPK, -1, itemPK, currentPos + offset, lang);
                            shiftedLink.setPositionsChanged(true);
                            if(isItemSource())
                            {
                                shiftedLink.setSrcItem(item);
                            }
                            else
                            {
                                shiftedLink.setTgtItem(item);
                            }
                            data.add(shiftedLink);
                        }
                    }
                }
            }
        }
        return data;
    }


    private List<List<?>> queryExistingLinks4AppendOnly(SessionContext ctx, boolean uniqueMode, boolean sort, boolean sortReverse)
    {
        String maxReverseIndexQuery, existingLinksQuery;
        Map<Object, Object> params = new HashMap<>();
        params.put("items", getItems());
        params.put("quali", getQualifier());
        if(isLocalized())
        {
            params.put("languages", getLanguages());
        }
        Collection<Item> toBeLinked = prepareToBeLinked(isLocalized());
        params.put("toBeLinked", toBeLinked);
        int oraclePages = 1;
        boolean inline4SqlServer = false;
        if(this.oracleMode && toBeLinked.size() > 1000)
        {
            params.remove("toBeLinked");
            List<Item> list = new ArrayList<>(toBeLinked);
            for(int i = 0; i < toBeLinked.size(); i += 1000)
            {
                params.put("toBeLinked" + oraclePages, list.subList(i, Math.min(i + 1000, toBeLinked.size())));
                oraclePages++;
            }
        }
        else if(this.sqlServerMode && toBeLinked.size() + this.languages.size() + this.items.size() + 1 > 2000)
        {
            params.remove("toBeLinked");
            inline4SqlServer = true;
        }
        String maxIndexQuery = sort ? ("SELECT {" + (isItemSource() ? "source" : "target") + "} as itemPK, {language} as langPK, null AS linkedItemPK, null AS linkPK, MAX({" + (isItemSource() ? "sequenceNumber" : "reverseSequenceNumber") + "}) AS cnt FROM {" + getLinkTypeCode(getQualifier())
                        + "*} WHERE {" + (isItemSource() ? "source" : "target") + "} IN (?items) AND {qualifier}=?quali AND {language}" + (isLocalized() ? " IN ( ?languages ) " : " IS NULL ") + "GROUP BY {language}, {" + (isItemSource() ? "source" : "target") + "} ") : null;
        if(sortReverse)
        {
            String tmp = "SELECT null as itemPK, {language} as langPK, {" + (isItemSource() ? "target" : "source") + "} AS linkedItemPK, null AS linkPK, MAX({" + (isItemSource() ? "reverseSequenceNumber" : "sequenceNumber") + "}) AS cnt FROM {" + getLinkTypeCode(getQualifier()) + "*} WHERE ";
            if(oraclePages > 1)
            {
                tmp = tmp + " ( ";
                for(int i = 1; i < oraclePages; i++)
                {
                    tmp = tmp + tmp + "{" + ((i > 1) ? " OR " : "") + "} IN ( ?toBeLinked" + (isItemSource() ? "target" : "source") + " ) ";
                }
                tmp = tmp + " ) AND ";
            }
            else if(inline4SqlServer)
            {
                tmp = tmp + "{" + tmp + "} IN ( ";
                boolean first = true;
                for(Item toLink : toBeLinked)
                {
                    if(first)
                    {
                        first = false;
                    }
                    else
                    {
                        tmp = tmp + ",";
                    }
                    tmp = tmp + tmp;
                }
                tmp = tmp + " ) AND ";
            }
            else
            {
                tmp = tmp + "{" + tmp + "} IN ( ?toBeLinked ) AND ";
            }
            tmp = tmp + "{qualifier}=?quali AND {language}" + tmp + "GROUP BY {language}, {" + (isLocalized() ? " IN (?languages ) " : " IS NULL ") + "}";
            maxReverseIndexQuery = tmp;
        }
        else
        {
            maxReverseIndexQuery = null;
        }
        if(uniqueMode)
        {
            String tmp = "SELECT {" + (isItemSource() ? "source" : "target") + "} as itemPK, {language} as langPK, {" + (isItemSource() ? "target" : "source") + "} AS linkedItemPK, {" + Item.PK + "} AS linkPK, {" + (isItemSource() ? "sequenceNumber" : "reverseSequenceNumber") + "} AS cnt FROM {"
                            + getLinkTypeCode(getQualifier()) + "*} WHERE {" + (isItemSource() ? "source" : "target") + "} IN (?items) AND ";
            if(oraclePages > 1)
            {
                tmp = tmp + " ( ";
                for(int i = 1; i < oraclePages; i++)
                {
                    tmp = tmp + tmp + "{" + ((i > 1) ? " OR " : "") + "} IN ( ?toBeLinked" + (isItemSource() ? "target" : "source") + " ) ";
                }
                tmp = tmp + " ) AND ";
            }
            else if(inline4SqlServer)
            {
                tmp = tmp + "{" + tmp + "} IN ( ";
                boolean first = true;
                for(Item toLink : toBeLinked)
                {
                    if(first)
                    {
                        first = false;
                    }
                    else
                    {
                        tmp = tmp + ",";
                    }
                    tmp = tmp + tmp;
                }
                tmp = tmp + " ) AND ";
            }
            else
            {
                tmp = tmp + "{" + tmp + "} IN ( ?toBeLinked ) AND ";
            }
            tmp = tmp + "{qualifier}=?quali AND {language}" + tmp;
            existingLinksQuery = tmp;
        }
        else
        {
            existingLinksQuery = null;
        }
        int queries = (maxIndexQuery != null) ? 1 : 0;
        queries += (maxReverseIndexQuery != null) ? 1 : 0;
        queries += (existingLinksQuery != null) ? 1 : 0;
        String query = null;
        if(queries > 1)
        {
            boolean addUnion = false;
            query = "SELECT itemPK, langPK, linkedItemPK, linkPK, cnt FROM (";
            if(maxIndexQuery != null)
            {
                query = query + "{{" + query + "}} ";
                addUnion = true;
            }
            if(maxReverseIndexQuery != null)
            {
                if(addUnion)
                {
                    query = query + " UNION ALL ";
                }
                else
                {
                    addUnion = true;
                }
                query = query + "{{" + query + "}} ";
            }
            if(existingLinksQuery != null)
            {
                if(addUnion)
                {
                    query = query + " UNION ALL ";
                }
                query = query + "{{" + query + "}} ";
            }
            query = query + ") xyz";
        }
        else if(queries == 1)
        {
            if(maxIndexQuery != null)
            {
                query = maxIndexQuery;
            }
            else if(maxReverseIndexQuery != null)
            {
                query = maxReverseIndexQuery;
            }
            else
            {
                query = existingLinksQuery;
            }
        }
        return (query == null) ? Collections.EMPTY_LIST : FlexibleSearch.getInstance()
                        .search(ctx, query, params,
                                        Arrays.asList((Class<?>[][])new Class[] {PK.class, PK.class, PK.class, PK.class, Integer.class}, ), true, true, 0, -1)
                        .getResult();
    }


    protected List<LinkData> gatherAppendOnlyData(SessionContext ctx)
    {
        int ITEM_PK = 0;
        int LANG_PK = 1;
        int LINKED_ITEM_PK = 2;
        int LINK_PK = 3;
        int POS = 4;
        boolean sort = isItemSource() ? isSortTgtEnd() : isSortSrcEnd();
        boolean sortReverse = isItemSource() ? isSortSrcEnd() : isSortTgtEnd();
        boolean uniqueMode = !isAllowDuplicates();
        List<List<?>> rows = this.skipGatheringExistingLinks ? Collections.EMPTY_LIST : queryExistingLinks4AppendOnly(ctx, uniqueMode, sort, sortReverse);
        List<LinkData> data = new ArrayList<>();
        for(Language lang : getLanguages())
        {
            PK langPK = (lang != null) ? lang.getPK() : null;
            Map<PK, Integer> currentMaxPositions = sort ? new HashMap<>() : null;
            Map<PK, Integer> reversePositions = sortReverse ? new HashMap<>() : null;
            Map<PK, Map<PK, List<Object[]>>> existingLinks = uniqueMode ? new HashMap<>() : null;
            for(List<?> row : rows)
            {
                PK myLangPK = (PK)row.get(1);
                if(myLangPK != langPK && (myLangPK == null || !myLangPK.equals(langPK)))
                {
                    continue;
                }
                PK linkPK = (PK)row.get(3);
                PK itemPK = (PK)row.get(0);
                PK linkedItemPK = (PK)row.get(2);
                if(linkPK == null)
                {
                    if(linkedItemPK != null)
                    {
                        reversePositions.put(linkedItemPK, (Integer)row.get(4));
                        continue;
                    }
                    currentMaxPositions.put(itemPK, (Integer)row.get(4));
                    continue;
                }
                Map<PK, List<Object[]>> itemLinks = existingLinks.get(itemPK);
                if(itemLinks == null)
                {
                    existingLinks.put(itemPK, itemLinks = new HashMap<>());
                }
                List<Object[]> links = itemLinks.get(linkedItemPK);
                if(links == null)
                {
                    itemLinks.put(linkedItemPK, links = new ArrayList());
                }
                links.add(new Object[] {linkPK, row
                                .get(4)});
            }
            for(Item item : getItems())
            {
                PK itemPK = item.getPK();
                int position = (sort && currentMaxPositions.containsKey(itemPK)) ? ((Integer)currentMaxPositions.get(itemPK)).intValue() : -1;
                Set<PK> uniqueSet = uniqueMode ? new HashSet<>() : null;
                Map<PK, List<Object[]>> itemLinks = uniqueMode ? existingLinks.get(itemPK) : null;
                for(Item toLink : getToBeLinked(lang))
                {
                    LinkData newLink;
                    PK linkedItemPK = toLink.getPK();
                    if(uniqueMode && !uniqueSet.add(linkedItemPK))
                    {
                        continue;
                    }
                    List<Object[]> myLinks = (uniqueMode && itemLinks != null) ? itemLinks.get(linkedItemPK) : null;
                    int reversePosition = (sortReverse && reversePositions.containsKey(linkedItemPK)) ? ((Integer)reversePositions.get(linkedItemPK)).intValue() : -1;
                    if(myLinks != null && !myLinks.isEmpty())
                    {
                        existingLinks.remove(linkedItemPK);
                        Object[] lnk = myLinks.get(0);
                        PK linkPK = (PK)lnk[0];
                        int currentPos = ((Integer)lnk[1]).intValue();
                        if(sort && currentPos != position)
                        {
                            LinkData shiftedLink = isItemSource() ? new LinkData(linkPK, itemPK, position++, linkedItemPK, -1, lang) : new LinkData(linkPK, linkedItemPK, -1, itemPK, position++, lang);
                            shiftedLink.setPositionsChanged(true);
                            if(isItemSource())
                            {
                                shiftedLink.setSrcItem(item);
                            }
                            else
                            {
                                shiftedLink.setTgtItem(item);
                            }
                            data.add(shiftedLink);
                        }
                        for(int i = 1, s = myLinks.size(); i < s; i++)
                        {
                            Object[] rec = myLinks.get(i);
                            LinkData removedLink = new LinkData((PK)rec[0], isItemSource() ? itemPK : linkedItemPK, -1, isItemSource() ? linkedItemPK : itemPK, -1, lang);
                            removedLink.setRemove(true);
                            data.add(removedLink);
                        }
                        continue;
                    }
                    if(isItemSource())
                    {
                        newLink = new LinkData(null, itemPK, sort ? ++position : -1, linkedItemPK, sortReverse ? (reversePosition + 1) : -1, lang);
                        newLink.setSrcItem(item);
                        newLink.setTgtItem(toLink);
                    }
                    else
                    {
                        newLink = new LinkData(null, linkedItemPK, sortReverse ? (reversePosition + 1) : -1, itemPK, sort ? ++position : -1, lang);
                        newLink.setSrcItem(toLink);
                        newLink.setTgtItem(item);
                    }
                    if(sortReverse)
                    {
                        reversePositions.put(linkedItemPK, Integer.valueOf(reversePosition + 1));
                    }
                    data.add(newLink);
                }
            }
        }
        return data;
    }


    public boolean isMarkItemsModified()
    {
        return this.touchItemsIfRelationChanged;
    }


    public void setMarkItemsModified(boolean markItemsModified)
    {
        this.touchItemsIfRelationChanged = markItemsModified;
    }


    public boolean isUnlink()
    {
        return this.unlink;
    }


    public void setUnlink(boolean unlink)
    {
        this.unlink = unlink;
    }


    public boolean isShift()
    {
        return this.shift;
    }


    public void setShift(boolean shift)
    {
        this.shift = shift;
    }


    public void setSkipQueryExistingLinks(boolean skip)
    {
        this.skipGatheringExistingLinks = skip;
    }


    public boolean isPreserveHiddenLanguages()
    {
        return this.preserveHiddenLanguages;
    }


    public void setPreserveHiddenLanguages(boolean preserveHiddenLanguages)
    {
        this.preserveHiddenLanguages = preserveHiddenLanguages;
    }


    public boolean isSortTgtEnd()
    {
        return this.sortTgtEnd;
    }


    public void setSortTgtEnd(boolean sortTgtEnd)
    {
        this.sortTgtEnd = sortTgtEnd;
    }


    public boolean isSortSrcEnd()
    {
        return this.sortSrcEnd;
    }


    public void setSortSrcEnd(boolean sortSrcEnd)
    {
        this.sortSrcEnd = sortSrcEnd;
    }


    public boolean isAllowDuplicates()
    {
        return this.allowDuplicates;
    }


    public void setAllowDuplicates(boolean allowDuplicates)
    {
        this.allowDuplicates = allowDuplicates;
    }
}
