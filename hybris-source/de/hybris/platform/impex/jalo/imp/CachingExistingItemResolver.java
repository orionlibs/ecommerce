package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.core.PK;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.header.UnresolvedValueException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class CachingExistingItemResolver extends DefaultExistingItemResolver
{
    private static final Logger LOG = Logger.getLogger(CachingExistingItemResolver.class);
    private static final float CACHE_LOAD_FACTOR = 0.6F;
    private final HeaderDescriptor header;
    private final boolean caseInsensitive;
    private final boolean testMode;
    private boolean languageMissing = false;
    private final Map<UniqueKey, Object> cache;


    protected Map<UniqueKey, Object> createCache() throws HeaderValidationException
    {
        Set<String> tcs = this.header.getPermittedTypeCodes();
        int totalMappingsCount = 0;
        int cacheSize = -1;
        HashMap[] arrayOfHashMap = new HashMap[tcs.size()];
        for(String code : tcs)
        {
            List rows;
            Set<StandardColumnDescriptor> uniqueKeyColumns = this.header.getUniqueAttributeColumns(code);
            QueryParameters queryParams = new QueryParameters(null, true, uniqueKeyColumns, Collections.emptyMap());
            List<StandardColumnDescriptor> searchable = new ArrayList<>(queryParams.getSearchableColumns());
            List<StandardColumnDescriptor> nonsearchable = new ArrayList<>(queryParams.getNonSearchableColumns());
            if(LOG.isDebugEnabled())
            {
                LOG.debug("filling cache for header type " + code + " ...");
                LOG.debug("searchable = " + searchable + ", non-searchable = " + nonsearchable);
            }
            try
            {
                long l1 = System.currentTimeMillis();
                rows = fetchData(code, searchable);
                long endTime = System.currentTimeMillis();
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("fetched " + rows.size() + " rows in " + endTime - l1 + "ms for creating item-llokup cache for " + this.header);
                }
            }
            catch(UnresolvedValueException e1)
            {
                throw new HeaderValidationException(this.header, "some virtual column of " + searchable + " could not be translated due to " + e1
                                .getMessage(), 0);
            }
            if(this.languageMissing)
            {
                LOG.warn("there is at least one column language missing - cannot fill cache");
                return null;
            }
            if(rows.isEmpty())
            {
                continue;
            }
            long startTime = System.currentTimeMillis();
            arrayOfHashMap[++cacheSize] = new HashMap<>(rows.size(), 0.6F);
            int counter = 1;
            Iterator<List> it;
            label80:
            for(it = rows.iterator(); it.hasNext(); counter++)
            {
                if(LOG.isDebugEnabled() && counter % 10000 == 0)
                {
                    long endTime = System.currentTimeMillis();
                    LOG.debug("added 10000 rows to cache in " + endTime - startTime + "ms");
                    startTime = endTime;
                }
                List<PK> row = it.next();
                PK pkStr = row.get(0);
                Item item = null;
                Map<StandardColumnDescriptor, Object> valueMappings = new HashMap<>();
                for(int c = 1, j = 0; j < searchable.size(); j++)
                {
                    StandardColumnDescriptor columnDesc = searchable.get(j);
                    if(!columnDesc.isVirtual())
                    {
                        valueMappings.put(columnDesc, row.get(c));
                        c++;
                    }
                }
                if(!nonsearchable.isEmpty())
                {
                    item = JaloSession.getCurrentSession().getItem(pkStr);
                    for(Iterator<StandardColumnDescriptor> iterator = nonsearchable.iterator(); iterator.hasNext(); )
                    {
                        StandardColumnDescriptor columnDesc = iterator.next();
                        Object value = null;
                        try
                        {
                            if(columnDesc.isLocalized() && columnDesc.getLanguageIso() != null)
                            {
                                try
                                {
                                    Language lang = columnDesc.getLanguage();
                                    SessionContext ctx = JaloSession.getCurrentSession().createSessionContext();
                                    ctx.setLanguage(lang);
                                    value = item.getAttribute(ctx, columnDesc.getQualifier());
                                }
                                catch(HeaderValidationException e)
                                {
                                    this.languageMissing = true;
                                    return null;
                                }
                            }
                            else
                            {
                                value = item.getAttribute(columnDesc.getQualifier());
                            }
                            if(columnDesc.isVirtual())
                            {
                                Object defaultValue = columnDesc.getDefaultValue();
                                if(defaultValue != value)
                                {
                                    if(defaultValue != null)
                                    {
                                        if(!defaultValue.equals(value))
                                        {
                                            continue label80;
                                        }
                                        continue;
                                    }
                                    continue label80;
                                }
                                continue;
                            }
                            valueMappings.put(columnDesc, value);
                        }
                        catch(UnresolvedValueException e)
                        {
                            throw new HeaderValidationException(this.header, "some virtual column of " + nonsearchable + " could not be translated due to " + e
                                            .getMessage(), 0);
                        }
                        catch(Exception e)
                        {
                            throw new JaloSystemException(e);
                        }
                    }
                }
                UniqueKey uniqueKey = new UniqueKey(code, valueMappings, this.caseInsensitive);
                Object cached = arrayOfHashMap[cacheSize].get(uniqueKey);
                if(cached != null)
                {
                    if(this.header.isBatchMode())
                    {
                        Collection<PK> itemPKs = (Collection)cached;
                        itemPKs.add(pkStr);
                    }
                    else
                    {
                        throw new HeaderValidationException("found more than one item for unique key values " + valueMappings + " : new one = " + pkStr + ", existing = " + cached, 0);
                    }
                }
                else
                {
                    arrayOfHashMap[cacheSize].put(uniqueKey,
                                    this.header.isBatchMode() ? new ArrayList(Collections.singletonList(pkStr)) :
                                                    pkStr);
                    totalMappingsCount++;
                }
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("done filling cache for header type " + code + ".");
            }
        }
        Map<UniqueKey, Object> ret = new HashMap<>(totalMappingsCount, 0.6F);
        for(int i = 0; i < arrayOfHashMap.length && arrayOfHashMap[i] != null; i++)
        {
            ret.putAll(arrayOfHashMap[i]);
        }
        return ret;
    }


    protected List fetchData(String typeCode, List<StandardColumnDescriptor> searchableUniqueColumns) throws UnresolvedValueException
    {
        List<Class<?>> sig = new ArrayList<>(Collections.singletonList(PK.class));
        StringBuilder queryBuffer = new StringBuilder();
        StringBuilder whereBuffer = null;
        Map<String, Object> values = null;
        queryBuffer.append("SELECT {").append(Item.PK).append("}");
        for(Iterator<StandardColumnDescriptor> iter = searchableUniqueColumns.iterator(); iter.hasNext(); )
        {
            StandardColumnDescriptor columnDesc = iter.next();
            if(columnDesc.isVirtual())
            {
                if(whereBuffer == null)
                {
                    whereBuffer = new StringBuilder();
                    values = new HashMap<>();
                }
                else
                {
                    whereBuffer.append(" AND ");
                }
                boolean isString = false;
                String langQualifier = null;
                if(columnDesc.isLocalized())
                {
                    isString = "java.lang.String".equalsIgnoreCase(((MapType)columnDesc.getAttributeDescriptor()
                                    .getRealAttributeType()).getReturnType()
                                    .getCode());
                    if(columnDesc.getLanguageIso() != null)
                    {
                        try
                        {
                            Language lang = columnDesc.getLanguage();
                            langQualifier = "[" + lang.getIsoCode() + "]";
                        }
                        catch(HeaderValidationException e)
                        {
                            this.languageMissing = true;
                            return Collections.EMPTY_LIST;
                        }
                    }
                }
                else
                {
                    isString = "java.lang.String".equalsIgnoreCase(columnDesc.getAttributeDescriptor().getRealAttributeType()
                                    .getCode());
                }
                whereBuffer.append((this.caseInsensitive && isString) ? " LOWER(" : "").append("{").append(columnDesc.getQualifier());
                if(langQualifier != null)
                {
                    whereBuffer.append(langQualifier);
                }
                whereBuffer.append("}").append((this.caseInsensitive && isString) ? ")" : "");
                whereBuffer.append(this.isMySQl ? " = BINARY " : " = ");
                whereBuffer.append((this.caseInsensitive && isString) ? " LOWER( ?" : " ?").append(columnDesc.getQualifier())
                                .append((this.caseInsensitive && isString) ? " ) " : " ");
                values.put(columnDesc.getQualifier(), columnDesc.getDefaultValue());
                continue;
            }
            queryBuffer.append(",{").append(columnDesc.getQualifier());
            AttributeDescriptor attrDesc = columnDesc.getAttributeDescriptor();
            Type adType = attrDesc.getRealAttributeType();
            if(columnDesc.isLocalized())
            {
                adType = ((MapType)adType).getReturnType();
                if(columnDesc.getLanguageIso() != null)
                {
                    try
                    {
                        Language lang = columnDesc.getLanguage();
                        queryBuffer.append("[").append(lang.getIsoCode()).append("]");
                    }
                    catch(HeaderValidationException e)
                    {
                        this.languageMissing = true;
                        return Collections.EMPTY_LIST;
                    }
                }
            }
            else if(adType instanceof AtomicType)
            {
                sig.add(((AtomicType)adType).getJavaClass());
            }
            else if(adType instanceof ComposedType)
            {
                sig.add(((ComposedType)adType).getJaloClass());
            }
            else
            {
                throw new IllegalStateException("unexpected attribute type " + adType + " - muste be either atomic or composed");
            }
            queryBuffer.append("}");
        }
        queryBuffer.append(" FROM {").append(typeCode).append("!}");
        return
                        FlexibleSearch.getInstance()
                                        .search((whereBuffer != null) ? (queryBuffer.toString() + " WHERE " + queryBuffer.toString()) : queryBuffer.toString(),
                                                        (values != null) ? values : Collections.EMPTY_MAP, sig, true, true, 0, -1).getResult();
    }


    public CachingExistingItemResolver(HeaderDescriptor header) throws HeaderValidationException
    {
        this.header = header;
        this.testMode = "true".equalsIgnoreCase(header.getDescriptorData().getModifier("testMode"));
        this.caseInsensitive = "true".equalsIgnoreCase(header.getDescriptorData().getModifier("ignoreKeyCase"));
        boolean atLeastOneColumnIsUnique = false;
        for(StandardColumnDescriptor desc : header.getSpecificColumns(StandardColumnDescriptor.class))
        {
            if(desc.isUnique())
            {
                atLeastOneColumnIsUnique = true;
                break;
            }
        }
        if(header.isInsertMode() && !atLeastOneColumnIsUnique)
        {
            throw new HeaderValidationException("You can not use cacheUnique modifier in INSERT mode when no column is unique, use UPDATE / INSERT_UPDATE mode or declare at least one column as unique", 78);
        }
        long startTime = System.currentTimeMillis();
        this.cache = createCache();
        long endTime = System.currentTimeMillis();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("filling cache took " + endTime - startTime + "ms");
        }
    }


    public void notifyItemCreatedOrRemoved(ValueLineTranslator valueTranlator, ValueLine line)
    {
        super.notifyItemCreatedOrRemoved(valueTranlator, line);
        if(this.header != line.getHeader())
        {
            throw new IllegalStateException("wrong caching existing item resolver: expected " + this.header + " but got " + line
                            .getHeader());
        }
        PK pkStr = line.getProcessedItemPK();
        if(pkStr == null)
        {
            throw new IllegalStateException("cannot cache line " + line + " since itemPK was null");
        }
        try
        {
            Set<StandardColumnDescriptor> uniqueColumns = this.header.getUniqueAttributeColumns(line.getComposedType());
            if(!uniqueColumns.isEmpty())
            {
                Map<StandardColumnDescriptor, Object> valueMappings = translateUniqueKeys(valueTranlator, line,
                                filterVirtualColumns(uniqueColumns));
                UniqueKey uniqueKey = new UniqueKey(line.getComposedType().getCode(), valueMappings, this.caseInsensitive);
                boolean remove = line.getHeader().isRemoveMode();
                if(remove)
                {
                    this.cache.remove(uniqueKey);
                }
                else
                {
                    Object cached = this.cache.get(uniqueKey);
                    if(cached != null)
                    {
                        if(!this.header.isBatchMode())
                        {
                            ((Collection<PK>)cached).add(pkStr);
                        }
                        else
                        {
                            throw new IllegalStateException("cannot cache line " + line + " since unique key is already mapped to " + cached);
                        }
                    }
                    else
                    {
                        this.cache.put(uniqueKey,
                                        this.header.isBatchMode() ? new ArrayList(Collections.singletonList(pkStr)) : pkStr);
                    }
                }
            }
        }
        catch(InsufficientDataException e)
        {
            throw new JaloSystemException(e);
        }
        catch(UnresolvedValueException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public Collection findExisting(ValueLineTranslator valueTranslator, ValueLine line) throws InsufficientDataException, UnresolvedValueException, AmbiguousItemException
    {
        long prepStartTime = System.currentTimeMillis();
        if(this.languageMissing)
        {
            throw new UnresolvedValueException("language for some localized column is missing");
        }
        Collection<Item> ret = Collections.EMPTY_LIST;
        if(line.getProcessedItemPK() != null)
        {
            ret = Collections.singletonList(JaloSession.getCurrentSession().getItem(line.getProcessedItemPK()));
        }
        else
        {
            if(!this.header.equals(line.getHeader()))
            {
                throw new IllegalStateException("wrong caching existing item resolver: expected " + this.header + " but got " + line
                                .getHeader());
            }
            Set<StandardColumnDescriptor> uniqueColumns = this.header.getUniqueAttributeColumns(line.getComposedType());
            if(!uniqueColumns.isEmpty())
            {
                long transStartTime = System.currentTimeMillis();
                Map<StandardColumnDescriptor, Object> valueMappings = translateUniqueKeys(valueTranslator, line,
                                filterVirtualColumns(uniqueColumns));
                long lookupStartTime = System.currentTimeMillis();
                Object cached = this.cache.get(new UniqueKey(line
                                .getComposedType().getCode(), valueMappings, this.caseInsensitive));
                long resultStartTime = System.currentTimeMillis();
                if(cached instanceof PK)
                {
                    ret = Collections.singletonList(JaloSession.getCurrentSession().getItem((PK)cached));
                }
                else if(cached instanceof Collection)
                {
                    JaloSession session = JaloSession.getCurrentSession();
                    ret = new ArrayList<>(((Collection)cached).size());
                    for(Iterator<PK> iter = ((Collection)cached).iterator(); iter.hasNext(); )
                    {
                        ret.add(session.getItem(iter.next()));
                    }
                }
                long endTime = System.currentTimeMillis();
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("findExisting stats (cache size:" + this.cache.size() + ", prep:" + transStartTime - prepStartTime + "ms, trans:" + lookupStartTime - transStartTime + "ms, lookup:" + resultStartTime - lookupStartTime + "ms, result:" + endTime - resultStartTime + " )");
                }
            }
        }
        if(this.testMode)
        {
            Collection uncachedRet = super.findExisting(valueTranslator, line);
            if(!uncachedRet.equals(ret))
            {
                throw new IllegalStateException("detected cache error for line " + line + " : uncached = " + uncachedRet + ", cached = " + ret);
            }
        }
        return ret;
    }


    protected Set<StandardColumnDescriptor> filterVirtualColumns(Set<StandardColumnDescriptor> uniqueColumns)
    {
        Set<StandardColumnDescriptor> ret = new HashSet<>(uniqueColumns);
        for(Iterator<StandardColumnDescriptor> iter = ret.iterator(); iter.hasNext(); )
        {
            StandardColumnDescriptor columnDesc = iter.next();
            if(columnDesc.isVirtual())
            {
                iter.remove();
            }
        }
        return ret;
    }
}
