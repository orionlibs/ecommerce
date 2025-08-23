package de.hybris.platform.persistence.property;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.persistence.ItemHome;
import de.hybris.platform.persistence.c2l.LocalizableItemRemote;
import de.hybris.platform.persistence.framework.PersistencePool;
import de.hybris.platform.util.jeeapi.YFinderException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

class HJMPLpCachePopulator
{
    private static final Logger LOG = LoggerFactory.getLogger(HJMPLpCachePopulator.class);


    void populateLpCache()
    {
        Arrays.<TypeSystemTypeCode>stream(TypeSystemTypeCode.values()).forEach(t -> cacheLpForTypeCode(t.getTypeCode()));
    }


    Multimap<PK, EJBPropertyRowCache> cacheLpForTypeCode(int typeCode)
    {
        Collection<LocalizableItemRemote> items = findAllItemsByTypeCode(typeCode);
        String tableName = getLpTableName(items, typeCode);
        if(items.isEmpty() || StringUtils.isEmpty(tableName))
        {
            return (Multimap<PK, EJBPropertyRowCache>)ArrayListMultimap.create();
        }
        Map<PK, Long> pkTimestampMap = getPkTimestampMap(items);
        JdbcTemplate jdbcTemplate = new JdbcTemplate((DataSource)Registry.getCurrentTenant().getDataSource());
        List<EJBPropertyRowCache> result = jdbcTemplate.query(String.format("SELECT * FROM %s", new Object[] {tableName}), (RowMapper)new EJBPropertyRowCacheMapper(pkTimestampMap));
        List<EJBPropertyRowCache> ejbPropertyRowCaches = removeItemsNonMatchingMainTable(result);
        Multimap<PK, EJBPropertyRowCache> rowCacheMultimap = prepareMultimapBasingOnResult(ejbPropertyRowCaches, items, pkTimestampMap);
        populateLpCache(items, rowCacheMultimap);
        return rowCacheMultimap;
    }


    private List<EJBPropertyRowCache> removeItemsNonMatchingMainTable(List<EJBPropertyRowCache> result)
    {
        result.removeIf(ejb -> (ejb.getVersion() == -1L));
        return result;
    }


    private String getLpTableName(Collection<LocalizableItemRemote> items, int typeCode)
    {
        PersistenceManager pm = Registry.getPersistenceManager();
        for(LocalizableItemRemote i : items)
        {
            TypeInfoMap info = pm.getPersistenceInfo(i.getTypeKey());
            if(info != null && info.hasInfos(true))
            {
                return info.getTableName(true);
            }
        }
        String warnMsg = String.format("Could not find lp table for given type code: %d", new Object[] {Integer.valueOf(typeCode)});
        LOG.warn(warnMsg);
        return "";
    }


    private void populateLpCache(Collection<LocalizableItemRemote> items, Multimap<PK, EJBPropertyRowCache> rowCacheMultimap)
    {
        for(LocalizableItemRemote i : items)
        {
            PK itemPk = i.getPK();
            rowCacheMultimap.get(itemPk).forEach(r -> i.hintPropertyCache(new ItemLocalizedPropertyCacheKey(r)));
        }
    }


    private Multimap<PK, EJBPropertyRowCache> prepareMultimapBasingOnResult(List<EJBPropertyRowCache> result, Collection<LocalizableItemRemote> items, Map<PK, Long> pkTimestampMap)
    {
        ArrayListMultimap arrayListMultimap = ArrayListMultimap.create();
        result.forEach(e -> pkPropertyRowCacheMultimap.put(e.getItemPK(), e));
        if(items.size() > arrayListMultimap.keySet().size())
        {
            populateMultimapForMissingPks((Multimap<PK, EJBPropertyRowCache>)arrayListMultimap, items, pkTimestampMap);
        }
        return (Multimap<PK, EJBPropertyRowCache>)arrayListMultimap;
    }


    private void populateMultimapForMissingPks(Multimap<PK, EJBPropertyRowCache> pkPropertyRowCacheMultimap, Collection<LocalizableItemRemote> items, Map<PK, Long> pkTimestampMap)
    {
        List<LocalizableItemRemote> missingItems = (List<LocalizableItemRemote>)items.stream().filter(i -> !pkPropertyRowCacheMultimap.containsKey(i.getPK())).collect(
                        Collectors.toList());
        List<PK> languagesPkList = getSupportedLanguagesPkList();
        for(LocalizableItemRemote item : missingItems)
        {
            PK itemPk = item.getPK();
            long timeStamp = ((Long)pkTimestampMap.get(item.getPK())).longValue();
            List sortedNames = item.getTypeInfoMap().getSortedNames(true);
            for(PK langPk : languagesPkList)
            {
                EJBPropertyRowCache propertyRowCache = EJBPropertyRowCache.createLocalized(langPk, timeStamp, sortedNames);
                propertyRowCache.setItemPK(itemPk);
                pkPropertyRowCacheMultimap.put(itemPk, propertyRowCache);
            }
        }
    }


    List<PK> getSupportedLanguagesPkList()
    {
        return (List<PK>)C2LManager.getInstance().getAllLanguages().stream().map(Item::getPK).collect(Collectors.toList());
    }


    private Collection<LocalizableItemRemote> findAllItemsByTypeCode(int typeCode)
    {
        Collection<LocalizableItemRemote> items;
        ItemHome itemHome = getItemHome(typeCode);
        try
        {
            items = itemHome.findAll();
        }
        catch(YFinderException e)
        {
            logException(String.format("Could not find items for given type code: %d", new Object[] {Integer.valueOf(typeCode)}), (Exception)e);
            return Collections.emptyList();
        }
        if(items.isEmpty())
        {
            LOG.warn("Could not find items for given type code: {}", Integer.valueOf(typeCode));
            return Collections.emptyList();
        }
        return items;
    }


    ItemHome getItemHome(int typeCode)
    {
        return (ItemHome)getPersistencePool().getHomeProxy(typeCode);
    }


    private PersistencePool getPersistencePool()
    {
        return Registry.getCurrentTenant().getPersistencePool();
    }


    private void logException(String msg, Exception ex)
    {
        LOG.warn(msg);
        LOG.debug(msg, ex);
    }


    private Map<PK, Long> getPkTimestampMap(Collection<LocalizableItemRemote> items)
    {
        Map<PK, Long> pkTimestampMap = new HashMap<>();
        items.forEach(i -> pkTimestampMap.put(i.getPK(), Long.valueOf(i.getPropertyTimestamp())));
        return pkTimestampMap;
    }
}
