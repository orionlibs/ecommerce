package de.hybris.platform.catalog.jalo.synccompare;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.ItemSyncTimestamp;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncJob;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class CompareSynchronization
{
    private static final Logger log = Logger.getLogger(CompareSynchronization.class.getName());
    private final CatalogManager catalogmanager;
    private final CatalogVersionSyncJob syncjob;
    private final Map<AttributeDescriptor, Boolean> syncJobADs;


    public CompareSynchronization(CatalogVersionSyncJob cvsj)
    {
        this.syncjob = cvsj;
        this.catalogmanager = CatalogManager.getInstance();
        this.syncJobADs = this.syncjob.getAllExportAttributeDescriptors();
    }


    public List<ItemCompareResult> compareSyncRootType(ComposedType root_ct, int skip)
    {
        List<ItemCompareResult> resultlist = new ArrayList<>();
        for(PK srcPK : getSourceItemList(root_ct, skip))
        {
            Item srcItem = JaloSession.getCurrentSession().getItem(srcPK);
            ItemCompareResult icr = new ItemCompareResult(srcItem);
            Item trgItem = searchTargetItem(srcItem);
            resultlist.add(icr);
            icr.setTargetItem(trgItem);
            if(srcItem == null)
            {
                icr.setResult(CompareSyncUtils.Status.NOT_EQUAL, "Source Item is null");
                continue;
            }
            if(trgItem == null)
            {
                icr.setResult(CompareSyncUtils.Status.NOT_EQUAL, "Target Item not found");
                continue;
            }
            try
            {
                processItem(icr, srcItem, trgItem, false);
            }
            catch(JaloInvalidParameterException e)
            {
                log.error("got JaloInvalidParameterException");
                icr.setResult(CompareSyncUtils.Status.NOT_EQUAL, "got JaloInvalidParameterException");
            }
            catch(JaloSecurityException e)
            {
                log.error("got JaloSecurityException");
                icr.setResult(CompareSyncUtils.Status.NOT_EQUAL, "got JaloSecurityException");
            }
        }
        return resultlist;
    }


    private List<PK> getSourceItemList(ComposedType composedType, int skip)
    {
        int step = skip;
        Map<String, Item> values = new HashMap<>();
        values.put("cv", this.syncjob.getSourceVersion());
        String query = "SELECT {PK} FROM {" + composedType.getCode() + "} WHERE {CatalogVersion} = ?cv";
        List<PK> erglist = FlexibleSearch.getInstance().search(query, values, PK.class).getResult();
        if(step == 1)
        {
            return erglist;
        }
        int listsize = erglist.size();
        if(step <= 0)
        {
            step = (int)(Math.random() * listsize + 1.0D);
        }
        int offset = (int)(Math.random() * ((step < listsize) ? step : listsize));
        List<PK> retlist = new ArrayList<>();
        int i;
        for(i = offset; i < listsize; i += step)
        {
            retlist.add(erglist.get(i));
        }
        return retlist;
    }


    private Item searchTargetItem(Item srcItem)
    {
        Map<String, Item> values = new HashMap<>();
        values.put("src", srcItem);
        StringBuilder query = new StringBuilder();
        query.append("SELECT {" + ItemSyncTimestamp.PK + "}");
        query.append(" FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + "}");
        query.append(" WHERE {sourceItem} = ?src");
        if(this.syncjob.isExclusiveMode().booleanValue())
        {
            values.put("sync", this.syncjob);
            query.append(" AND {syncJob} = ?sync");
        }
        else
        {
            values.put("srcCV", this.syncjob.getSourceVersion());
            values.put("trgCV", this.syncjob.getTargetVersion());
            query.append(" AND {sourceVersion} = ?srcCV");
            query.append(" AND {targetVersion} = ?trgCV");
        }
        List<ItemSyncTimestamp> list = FlexibleSearch.getInstance().search(query.toString(), values, ItemSyncTimestamp.class).getResult();
        if(list.size() == 1)
        {
            return ((ItemSyncTimestamp)list.get(0)).getTargetItem();
        }
        return null;
    }


    private void processItem(ItemCompareResult icr, Item srcItem, Item trgItem, boolean recursive) throws JaloInvalidParameterException, JaloSecurityException
    {
        ComposedType src_ct = srcItem.getComposedType();
        if(recursive && this.syncjob.getRootTypes().contains(src_ct))
        {
            if(compareItem((AbstractCompareResult)icr, srcItem, trgItem, false))
            {
                icr.setResult(CompareSyncUtils.Status.EQUAL);
            }
            else
            {
                icr.setResult(CompareSyncUtils.Status.NOT_EQUAL);
            }
        }
        else
        {
            SessionContext localctx = JaloSession.getCurrentSession().createLocalSessionContext();
            localctx.setLanguage(null);
            for(Map.Entry<String, Object> sourceME : (Iterable<Map.Entry<String, Object>>)srcItem.getAllAttributes()
                            .entrySet())
            {
                AttributeDescriptor sourceAD = src_ct.getAttributeDescriptor(sourceME.getKey());
                if((sourceAD.isWritable() || sourceAD.isInitial()) && this.syncJobADs
                                .get(sourceAD) != null && ((Boolean)this.syncJobADs.get(sourceAD))
                                .booleanValue())
                {
                    AttributeDescriptorCompareResult adcr = null;
                    Object sourceContentObject = sourceME.getValue();
                    Object targetContentObject = trgItem.getAttribute(sourceME.getKey());
                    if(sourceAD.isLocalized())
                    {
                        for(Language lang : this.syncjob.getEffectiveSyncLanguages())
                        {
                            adcr = icr.addADCR(sourceAD, lang);
                            Map srcMap = (Map)sourceContentObject;
                            Map trgMap = (Map)targetContentObject;
                            Type returntype = ((MapType)sourceAD.getRealAttributeType()).getReturnType();
                            processAttribute(adcr, sourceAD, srcMap.get(lang), trgMap.get(lang), returntype);
                        }
                        continue;
                    }
                    adcr = icr.addADCR(sourceAD);
                    processAttribute(adcr, sourceAD, sourceContentObject, targetContentObject, null);
                }
            }
            JaloSession.getCurrentSession().removeLocalSessionContext();
        }
    }


    private void processAttribute(AttributeDescriptorCompareResult adcr, AttributeDescriptor sourceAD, Object sourceContentObject, Object targetContentObject, Type type) throws JaloInvalidParameterException, JaloSecurityException
    {
        if(sourceContentObject == null && targetContentObject == null)
        {
            adcr.setResult(CompareSyncUtils.Status.EQUAL);
        }
        else if(sourceContentObject == null || targetContentObject == null)
        {
            adcr.setResult(CompareSyncUtils.Status.NOT_EQUAL);
            adcr.setDifference(sourceContentObject, targetContentObject);
        }
        else
        {
            Type srcAttributeType = sourceAD.getRealAttributeType();
            if(type != null)
            {
                srcAttributeType = type;
            }
            if(srcAttributeType instanceof de.hybris.platform.jalo.type.AtomicType)
            {
                if(compareAtomicTypeValues(sourceContentObject, targetContentObject))
                {
                    adcr.setResult(CompareSyncUtils.Status.EQUAL);
                }
                else
                {
                    adcr.setResult(CompareSyncUtils.Status.NOT_EQUAL);
                    adcr.setDifference(sourceContentObject, targetContentObject);
                }
            }
            else if(srcAttributeType instanceof ComposedType)
            {
                Item srcItem = (Item)sourceContentObject;
                Item trgItem = (Item)targetContentObject;
                if(compareItem((AbstractCompareResult)adcr, srcItem, trgItem, sourceAD.isPartOf()))
                {
                    adcr.setResult(CompareSyncUtils.Status.EQUAL);
                    ItemCompareResult icr = adcr.addICR(srcItem);
                    icr.setTargetItem(trgItem);
                    processItem(icr, srcItem, trgItem, true);
                }
                else
                {
                    adcr.setResult(CompareSyncUtils.Status.NOT_EQUAL);
                }
            }
            else if(srcAttributeType instanceof CollectionType)
            {
                Collection srcColl = (Collection)sourceContentObject;
                Collection trgColl = (Collection)targetContentObject;
                Type collElementType = ((CollectionType)srcAttributeType).getElementType();
                EnumerationValue typeOfColl = ((CollectionType)srcAttributeType).getTypeOfCollectionEnum();
                processCollection(adcr, sourceAD.isPartOf(), collElementType, typeOfColl, srcColl, trgColl);
            }
            else if(srcAttributeType instanceof MapType)
            {
                Map srcMap = (Map)sourceContentObject;
                Map trgMap = (Map)targetContentObject;
                Type mapreturntype = ((MapType)srcAttributeType).getReturnType();
                processMap(adcr, sourceAD.isPartOf(), mapreturntype, srcMap, trgMap);
            }
            else
            {
                adcr.setResult(CompareSyncUtils.Status.IGNORE, "unknown attribute type: " + srcAttributeType.toString());
            }
        }
    }


    private boolean compareItem(AbstractCompareResult acr, Item srcItem, Item trgItem, boolean partof) throws JaloInvalidParameterException, JaloSecurityException
    {
        if(partof)
        {
            if(srcItem.equals(trgItem))
            {
                acr.setResult(CompareSyncUtils.Status.NOT_EQUAL, "items are partof and are identical! Should be different items");
                return false;
            }
            if(acr instanceof ItemCompareResult)
            {
                processItem((ItemCompareResult)acr, srcItem, trgItem, true);
            }
            return true;
        }
        if(srcItem.equals(trgItem))
        {
            if(this.catalogmanager.isCatalogItem(srcItem))
            {
                CatalogVersion catver = this.catalogmanager.getCatalogVersion(srcItem);
                if(catver.equals(this.syncjob.getSourceVersion()) || catver.equals(this.syncjob.getTargetVersion()))
                {
                    acr.setResult(CompareSyncUtils.Status.NOT_EQUAL, "items are idencial and also belongs to catalog version of the sync process");
                    return false;
                }
                return true;
            }
            return true;
        }
        if(this.catalogmanager.isCatalogItem(srcItem) && this.catalogmanager.isCatalogItem(trgItem))
        {
            Item syncTrgItem = searchTargetItem(srcItem);
            if(syncTrgItem.equals(trgItem))
            {
                return true;
            }
            acr.setResult(CompareSyncUtils.Status.NOT_EQUAL, "items are different synced items");
            acr.setDifference("src refs to " + syncTrgItem.toString(), "target item is " + trgItem.toString());
            return false;
        }
        if(!this.catalogmanager.isCatalogItem(srcItem) && !this.catalogmanager.isCatalogItem(trgItem))
        {
            acr.setResult(CompareSyncUtils.Status.NOT_EQUAL, "items are different and and are no catalog items");
            return false;
        }
        acr.setDifference("source item is catalogitem: " + this.catalogmanager.isCatalogItem(srcItem), "target item is catalogitem: " + this.catalogmanager
                        .isCatalogItem(trgItem));
        acr.setResult(CompareSyncUtils.Status.NOT_EQUAL, "items are idencial and also belongs to catalog version of the sync process");
        return false;
    }


    private void processCollection(AttributeDescriptorCompareResult adcr, boolean partof, Type collElementType, EnumerationValue typeOfColl, Collection srcColl, Collection trgColl) throws JaloInvalidParameterException, JaloSecurityException
    {
        if(srcColl.isEmpty() && trgColl.isEmpty())
        {
            adcr.setResult(CompareSyncUtils.Status.EQUAL);
        }
        else if(srcColl.size() != trgColl.size())
        {
            adcr.setResult(CompareSyncUtils.Status.NOT_EQUAL, "size of the source collection is different from the size of the target collection");
            adcr.setDifference(Integer.valueOf(srcColl.size()), Integer.valueOf(trgColl.size()));
        }
        else if(typeOfColl.getCode().equalsIgnoreCase("list"))
        {
            List<Item> srcList = (List)srcColl;
            List<Item> trgList = (List)trgColl;
            if(collElementType instanceof de.hybris.platform.jalo.type.AtomicType)
            {
                for(int i = 0; i < srcList.size(); i++)
                {
                    if(!compareAtomicTypeValues(srcList.get(i), trgList.get(i)))
                    {
                        adcr.setResult(CompareSyncUtils.Status.NOT_EQUAL, "Aborting check collection due first mismatch");
                        adcr.setDifference(srcList.get(i), trgList.get(i));
                        break;
                    }
                }
            }
            else if(collElementType instanceof ComposedType)
            {
                for(int i = 0; i < srcList.size(); i++)
                {
                    Item srcItem = srcList.get(i);
                    Item trgItem = trgList.get(i);
                    ItemCompareResult icr = adcr.addICR(srcItem);
                    icr.setTargetItem(trgItem);
                    if(compareItem((AbstractCompareResult)icr, srcItem, trgItem, partof))
                    {
                        icr.setResult(CompareSyncUtils.Status.EQUAL);
                        processItem(icr, srcItem, trgItem, true);
                    }
                    else
                    {
                        icr.setResult(CompareSyncUtils.Status.NOT_EQUAL, "items in list are not equal");
                        icr.setDifference(srcItem, trgItem);
                    }
                }
            }
            else if(collElementType instanceof CollectionType)
            {
                adcr.setResult(CompareSyncUtils.Status.IGNORE, "list contains collection elements  - not supported");
            }
            else if(collElementType instanceof MapType)
            {
                adcr.setResult(CompareSyncUtils.Status.IGNORE, "list contains map elements  - not supported");
            }
            else
            {
                adcr.setResult(CompareSyncUtils.Status.IGNORE, "collection element type not supported");
            }
        }
        else if(collElementType instanceof de.hybris.platform.jalo.type.AtomicType)
        {
            for(Object srcObject : srcColl)
            {
                if(trgColl.contains(srcObject))
                {
                    adcr.setResult(CompareSyncUtils.Status.EQUAL);
                    continue;
                }
                adcr.setResult(CompareSyncUtils.Status.NOT_EQUAL, "value " + srcObject + " was not fount in target collection");
            }
        }
        else if(collElementType instanceof ComposedType)
        {
            for(Item srcItem : srcColl)
            {
                ItemCompareResult icr = adcr.addICR(srcItem);
                if(trgColl.contains(srcItem))
                {
                    icr.setTargetItem(srcItem);
                    if(compareItem((AbstractCompareResult)icr, srcItem, srcItem, partof))
                    {
                        icr.setResult(CompareSyncUtils.Status.EQUAL);
                        continue;
                    }
                    icr.setResult(CompareSyncUtils.Status.NOT_EQUAL);
                    continue;
                }
                Item syncTrgItem = searchTargetItem(srcItem);
                icr.setTargetItem(syncTrgItem);
                if(trgColl.contains(syncTrgItem))
                {
                    if(compareItem((AbstractCompareResult)icr, srcItem, syncTrgItem, partof))
                    {
                        icr.setResult(CompareSyncUtils.Status.EQUAL);
                        processItem(icr, srcItem, syncTrgItem, true);
                        continue;
                    }
                    icr.setResult(CompareSyncUtils.Status.NOT_EQUAL);
                    continue;
                }
                icr.setResult(CompareSyncUtils.Status.NOT_EQUAL, "no target item in target collection found");
            }
        }
        else if(collElementType instanceof CollectionType)
        {
            adcr.setResult(CompareSyncUtils.Status.IGNORE, "collection contains collection elements  - not supported");
        }
        else if(collElementType instanceof MapType)
        {
            adcr.setResult(CompareSyncUtils.Status.IGNORE, "collection contains map elements  - not supported");
        }
        else
        {
            adcr.setResult(CompareSyncUtils.Status.IGNORE, "collection element type not supported");
        }
    }


    private void processMap(AttributeDescriptorCompareResult adcr, boolean partof, Type mapreturntype, Map srcMap, Map trgMap) throws JaloInvalidParameterException, JaloSecurityException
    {
        if(srcMap.isEmpty() && trgMap.isEmpty())
        {
            adcr.setResult(CompareSyncUtils.Status.EQUAL);
        }
        else if(srcMap.size() != trgMap.size())
        {
            adcr.setResult(CompareSyncUtils.Status.NOT_EQUAL, "size of the source map is different from the size of the target map");
            adcr.setDifference(Integer.valueOf(srcMap.size()), Integer.valueOf(trgMap.size()));
        }
        else
        {
            for(Map.Entry<Object, Object> srcME : (Iterable<Map.Entry<Object, Object>>)srcMap.entrySet())
            {
                if(mapreturntype instanceof de.hybris.platform.jalo.type.AtomicType)
                {
                    if(!compareAtomicTypeValues(srcME.getValue(), trgMap.get(srcME.getKey())))
                    {
                        adcr.setResult(CompareSyncUtils.Status.NOT_EQUAL, "values are different");
                        adcr.setDifference(srcME.getValue(), trgMap.get(srcME.getKey()));
                        break;
                    }
                    continue;
                }
                if(mapreturntype instanceof ComposedType)
                {
                    Item srcItem = (Item)srcME.getValue();
                    Item trgItem = (Item)trgMap.get(srcME.getKey());
                    ItemCompareResult icr = adcr.addICR(srcItem);
                    icr.setTargetItem(trgItem);
                    if(compareItem((AbstractCompareResult)icr, srcItem, trgItem, partof))
                    {
                        icr.setResult(CompareSyncUtils.Status.EQUAL);
                        processItem(icr, srcItem, trgItem, true);
                        continue;
                    }
                    icr.setResult(CompareSyncUtils.Status.NOT_EQUAL, "items in list are not equal");
                    icr.setDifference(srcItem, trgItem);
                    continue;
                }
                if(mapreturntype instanceof MapType)
                {
                    adcr.setResult(CompareSyncUtils.Status.IGNORE, "map contains map elements  - not supported");
                    continue;
                }
                if(mapreturntype instanceof CollectionType)
                {
                    adcr.setResult(CompareSyncUtils.Status.IGNORE, "map contains collection elements  - not supported");
                    continue;
                }
                adcr.setResult(CompareSyncUtils.Status.IGNORE, "map element type not supported");
            }
        }
    }


    private boolean compareAtomicTypeValues(Object src, Object trg)
    {
        if(src == null && trg == null)
        {
            return true;
        }
        if(src == null || trg == null)
        {
            return false;
        }
        return src.equals(trg);
    }
}
