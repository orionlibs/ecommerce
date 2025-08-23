package de.hybris.platform.catalog.jalo;

import de.hybris.bootstrap.util.RequirementSolver;
import de.hybris.bootstrap.util.RequirementSolverException;
import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.jalo.MultiThreadedImporter;
import de.hybris.platform.impex.jalo.exp.ImpExCSVExportWriter;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class RemoveCatalogVersionJob extends GeneratedRemoveCatalogVersionJob
{
    private static final Logger LOG = Logger.getLogger(RemoveCatalogVersionJob.class.getName());


    protected CronJob.CronJobResult performCronJob(CronJob cronJob)
    {
        RemoveCatalogVersionCronJob removeCVCJ = (RemoveCatalogVersionCronJob)cronJob;
        Catalog catalog = removeCVCJ.getCatalog();
        if(catalog == null || !catalog.isAlive())
        {
            LOG.error("The Catalog attribute is null or already deleted. Aborting! ");
            removeCVCJ.setCronJobResult(cronJob.getFinishedResult(false));
            return cronJob.getFinishedResult(false);
        }
        CatalogVersion version = removeCVCJ.getCatalogVersion();
        List<ComposedType> sortedComposedTypeList = calculateComposedTypeOrder(CatalogManager.getInstance()
                        .getAllCatalogItemTypes());
        try
        {
            if(version == null)
            {
                return removeCatalog(sortedComposedTypeList, catalog, removeCVCJ);
            }
            return removeCatalogVersion(sortedComposedTypeList, version, removeCVCJ);
        }
        catch(JaloBusinessException e)
        {
            LOG.error("Got JaloBusinessException " + e);
            removeCVCJ.setCronJobResult(cronJob.getFinishedResult(false));
            return cronJob.getFinishedResult(false);
        }
        catch(IOException e)
        {
            LOG.error("Got IOException " + e);
            removeCVCJ.setCronJobResult(cronJob.getFinishedResult(false));
            return cronJob.getFinishedResult(false);
        }
    }


    private CronJob.CronJobResult removeCatalog(List<ComposedType> sortedComposedTypeList, Catalog catalog, RemoveCatalogVersionCronJob removeCVCJ) throws IOException, JaloBusinessException
    {
        if(LOG.isInfoEnabled())
        {
            LOG.info("start " + removeCVCJ.getCode() + " with removing catalog: " + catalog.getId());
        }
        removeCatalogVersionCollection(sortedComposedTypeList, new HashSet<>(catalog.getCatalogVersions()), removeCVCJ);
        boolean noVersionsLeft = catalog.getCatalogVersions().isEmpty();
        int remainingItemsCount = getItemInstanceCount(sortedComposedTypeList, catalog);
        if(noVersionsLeft && remainingItemsCount == 0)
        {
            try
            {
                catalog.remove();
                removeCVCJ.setCronJobResult(removeCVCJ.getFinishedResult(true));
                return removeCVCJ.getFinishedResult(true);
            }
            catch(ConsistencyCheckException e)
            {
                LOG.error("Could not remove Catalog " + catalog.getId() + " due to " + e.getMessage());
                removeCVCJ.setCronJobResult(removeCVCJ.getFinishedResult(false));
                return removeCVCJ.getFinishedResult(false);
            }
        }
        LOG.warn("Could not remove Catalog " + catalog.getId() + " ( versions removed:" + noVersionsLeft + " remaining items:" + remainingItemsCount + ")");
        removeCVCJ.setCronJobResult(removeCVCJ.getFinishedResult(false));
        return removeCVCJ.getFinishedResult(false);
    }


    private CronJob.CronJobResult removeCatalogVersion(List<ComposedType> sortedComposedTypeList, CatalogVersion version, RemoveCatalogVersionCronJob removeCVCJ) throws IOException, JaloBusinessException
    {
        if(LOG.isInfoEnabled())
        {
            LOG.info("start " + removeCVCJ.getCode() + " with removing catalogversion: " + version.toString());
        }
        removeCatalogVersionCollection(sortedComposedTypeList, Collections.singletonList(version), removeCVCJ);
        if(version.isAlive())
        {
            LOG.warn("Could not Remove CatalogVersion " + version);
            removeCVCJ.setCronJobResult(removeCVCJ.getFinishedResult(false));
            return removeCVCJ.getFinishedResult(false);
        }
        removeCVCJ.setCronJobResult(removeCVCJ.getFinishedResult(true));
        return removeCVCJ.getFinishedResult(true);
    }


    private void removeCatalogVersionCollection(List<ComposedType> sortedComposedTypeList, Collection<CatalogVersion> cvcollection, RemoveCatalogVersionCronJob removeCVCJ) throws IOException, JaloBusinessException
    {
        int maxItemCount = getItemInstanceCount(sortedComposedTypeList, cvcollection);
        removeCVCJ.setTotalDeleteItemCount(maxItemCount);
        if(LOG.isInfoEnabled())
        {
            LOG.info("first step: start removing " + maxItemCount + " items.");
        }
        for(CatalogVersion catver : cvcollection)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("catalogversion " + catver + " contains " + getItemInstanceCount(sortedComposedTypeList, catver) + " items");
            }
            removeItemsInCatalogVersion(sortedComposedTypeList, catver, removeCVCJ, false);
            if(getItemInstanceCount(sortedComposedTypeList, catver) == 0)
            {
                if(LOG.isInfoEnabled())
                {
                    LOG.info("removing catalogversion " + catver);
                }
                try
                {
                    catver.remove();
                }
                catch(ConsistencyCheckException e)
                {
                    if(LOG.isInfoEnabled())
                    {
                        LOG.info("could not delete catalogversion " + catver + " in the first step. Trying again in the second step");
                    }
                }
            }
        }
        int leftItems = getItemInstanceCount(sortedComposedTypeList, cvcollection);
        if(leftItems > 0)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("second step: start removing " + leftItems + " items.");
            }
            for(CatalogVersion catver : cvcollection)
            {
                if(catver.isAlive())
                {
                    if(LOG.isInfoEnabled())
                    {
                        LOG.info("catalogversion " + catver + " contains " + getItemInstanceCount(sortedComposedTypeList, catver) + " items");
                    }
                    removeItemsInCatalogVersion(sortedComposedTypeList, catver, removeCVCJ, true);
                    if(getItemInstanceCount(sortedComposedTypeList, catver) == 0)
                    {
                        if(LOG.isInfoEnabled())
                        {
                            LOG.info("removing catalogversion " + catver);
                        }
                        try
                        {
                            catver.remove();
                        }
                        catch(ConsistencyCheckException e)
                        {
                            LOG.warn("Could not remove CatalogVersion " + catver);
                        }
                        continue;
                    }
                    LOG.warn("Could not remove CatalogVersion " + catver + " since there are still contained items left");
                }
            }
        }
    }


    private void removeItemsInCatalogVersion(List<ComposedType> sortedComposedTypeList, CatalogVersion version, RemoveCatalogVersionCronJob removeCVCJ, boolean secondStep) throws JaloBusinessException, IOException
    {
        String cvname = version.getCatalog().getId() + "/" + version.getCatalog().getId();
        int itemCountVersion = getItemInstanceCount(sortedComposedTypeList, version);
        if(version.isActive().booleanValue())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("deactivate catalogversion: " + cvname);
            }
            version.setActive(false);
        }
        for(ComposedType currentct : sortedComposedTypeList)
        {
            int composedTypeCount = getItemInstanceCount(currentct, version);
            if(composedTypeCount > 0)
            {
                MultiThreadedImporter imp;
                if(LOG.isInfoEnabled())
                {
                    LOG.info("starting to Remove " + composedTypeCount + " " + currentct.getCode() + " items");
                }
                ImpExMedia impexmedia = createImpexMediaForComposedType(currentct, version, removeCVCJ);
                DataInputStream dataFromStream = null;
                try
                {
                    dataFromStream = impexmedia.getDataFromStream();
                    imp = new MultiThreadedImporter(new CSVReader(dataFromStream, CSVConstants.DEFAULT_ENCODING));
                }
                catch(UnsupportedEncodingException ex)
                {
                    if(dataFromStream != null)
                    {
                        dataFromStream.close();
                    }
                    throw ex;
                }
                imp.getReader().enableCodeExecution(true);
                Item item = null;
                try
                {
                    do
                    {
                        item = imp.importNext();
                        removeCVCJ.setCurrentProcessingItemCount(imp.getProcessedItemsCountOverall());
                        removeCVCJ.setCurrentProcess(cvname + " " + cvname);
                    }
                    while(item != null);
                }
                catch(ImpExException e)
                {
                    if(secondStep && e.getErrorCode() == 123)
                    {
                        if(LOG.isInfoEnabled())
                        {
                            LOG.info("add remaining items to the cronjob dumpmedia");
                        }
                        if(removeCVCJ.getNotRemovedItems() == null)
                        {
                            ImpExMedia media = ImpExManager.getInstance().createImpExMedia(removeCVCJ
                                            .getPK().toString() + "_dumpfile");
                            File dumpfile = imp.getDumpHandler().getDumpAsFile();
                            media.setFile(dumpfile);
                            removeCVCJ.setNotRemovedItems(media);
                        }
                        else
                        {
                            File dumpfile = imp.getDumpHandler().getDumpAsFile();
                            ImpExMedia media = removeCVCJ.getNotRemovedItems();
                            InputStream inputstream = null;
                            FileOutputStream fos = null;
                            try
                            {
                                inputstream = media.getDataFromInputStreamSure();
                                fos = new FileOutputStream(dumpfile, true);
                                IOUtils.copy(inputstream, fos);
                            }
                            finally
                            {
                                IOUtils.closeQuietly(inputstream);
                                IOUtils.closeQuietly(fos);
                            }
                            media.setData(new DataInputStream(new FileInputStream(dumpfile)));
                        }
                    }
                }
                finally
                {
                    imp.close();
                }
                removeCVCJ.addDeletedItemCount(composedTypeCount - getItemInstanceCount(currentct, version));
            }
        }
        removeCVCJ.addHistory(cvname, itemCountVersion - getItemInstanceCount(sortedComposedTypeList, version), itemCountVersion);
    }


    private ImpExMedia createImpexMediaForComposedType(ComposedType comptype, CatalogVersion version, RemoveCatalogVersionCronJob removeCVCJ) throws IOException, ImpExException
    {
        File tempfile = File.createTempFile(removeCVCJ.getPK().toString() + "_" + removeCVCJ.getPK().toString() + "_", ".csv");
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("using tempfile \"" + tempfile.getAbsolutePath() + "\" for composedType " + comptype.getCode());
            }
            ImpExCSVExportWriter impExCSVExportWriter = new ImpExCSVExportWriter(new CSVWriter(tempfile, CSVConstants.DEFAULT_ENCODING));
            impExCSVExportWriter.writeSrcLine("#% de.hybris.platform.jalo.JaloSession.getCurrentSession().getSessionContext().setAttribute( de.hybris.platform.jalo.Item.DISABLE_ITEMCHECK_BEFORE_REMOVABLE, Boolean.TRUE);");
            impExCSVExportWriter.setCurrentHeader("REMOVE " + comptype.getCode() + CSVConstants.DEFAULT_FIELD_SEPARATOR + "pk[unique=true]",
                            ImpExManager.getExportReimportRelaxedMode());
            impExCSVExportWriter.writeCurrentHeader(false);
            for(PK pk : getPKList(comptype, version))
            {
                impExCSVExportWriter.writeSrcLine("" + CSVConstants.DEFAULT_FIELD_SEPARATOR + CSVConstants.DEFAULT_FIELD_SEPARATOR);
            }
            impExCSVExportWriter.comment(" end of pk list for " + comptype.getCode());
            impExCSVExportWriter.comment(" ");
            impExCSVExportWriter.close();
            ImpExMedia media = ImpExManager.getInstance().createImpExMedia(tempfile.getName());
            media.setFieldSeparator(CSVConstants.DEFAULT_FIELD_SEPARATOR);
            media.setData(new DataInputStream(new FileInputStream(tempfile)), tempfile.getName(), ImpExConstants.File.MIME_TYPE_CSV);
            return media;
        }
        finally
        {
            FileUtils.deleteQuietly(tempfile);
        }
    }


    private List<PK> getPKList(ComposedType comptype, CatalogVersion version)
    {
        return FlexibleSearch.getInstance().search("SELECT {PK} FROM {" + comptype.getCode() + "!} WHERE {" +
                                                        CatalogManager.getInstance()
                                                                        .getCatalogVersionAttribute(comptype)
                                                                        .getQualifier() + "}  = ?version ORDER BY {PK} DESC",
                                        Collections.singletonMap("version", version), PK.class)
                        .getResult();
    }


    private int getItemInstanceCount(List<ComposedType> sortedComposedTypeList, Catalog catalog)
    {
        return getItemInstanceCount(sortedComposedTypeList, catalog.getCatalogVersions());
    }


    private int getItemInstanceCount(List<ComposedType> sortedComposedTypeList, Collection<CatalogVersion> cvcoll)
    {
        int itemcount = 0;
        for(CatalogVersion catver : cvcoll)
        {
            itemcount += getItemInstanceCount(sortedComposedTypeList, catver);
        }
        return itemcount;
    }


    private int getItemInstanceCount(List<ComposedType> sortedComposedTypeList, CatalogVersion version)
    {
        int itemcount = 0;
        for(ComposedType comptyp : sortedComposedTypeList)
        {
            itemcount += getItemInstanceCount(comptyp, version);
        }
        return itemcount;
    }


    private int getItemInstanceCount(ComposedType comptype, CatalogVersion version)
    {
        Map<String, Object> values = new HashMap<>();
        String query = "SELECT count({PK}) FROM {" + comptype.getCode() + "!} WHERE {" + CatalogManager.getInstance().getCatalogVersionAttribute(comptype).getQualifier() + "}  = ?version";
        values.put("version", version);
        List<Integer> reslist = FlexibleSearch.getInstance().search(query, values, Integer.class).getResult();
        if(!reslist.isEmpty())
        {
            return ((Integer)reslist.get(0)).intValue();
        }
        return 0;
    }


    private void createNodes(Set<ComposedType> origSet, Map<ComposedType, ComposedTypeNode> ct_ctn_map, Map<ComposedType, Integer> countMap)
    {
        for(ComposedType ct : origSet)
        {
            for(AttributeDescriptor ad : ct.getAttributeDescriptors())
            {
                if(ad.isPartOf())
                {
                    ComposedType nested;
                    Type attrType = ad.getRealAttributeType();
                    if(attrType instanceof ComposedType)
                    {
                        nested = (ComposedType)attrType;
                    }
                    else if(attrType instanceof CollectionType && ((CollectionType)attrType)
                                    .getElementType() instanceof ComposedType)
                    {
                        nested = (ComposedType)((CollectionType)attrType).getElementType();
                    }
                    else if(attrType instanceof MapType && ((MapType)attrType).getReturnType() instanceof ComposedType)
                    {
                        nested = (ComposedType)((MapType)attrType).getReturnType();
                    }
                    else
                    {
                        nested = null;
                    }
                    if(ct_ctn_map.containsKey(nested))
                    {
                        Set<ComposedType> sub = new HashSet<>();
                        sub.add(ct);
                        sub.addAll(ct.getAllSubTypes());
                        for(ComposedType t : sub)
                        {
                            if(!t.equals(nested) && ct_ctn_map.containsKey(t))
                            {
                                ((ComposedTypeNode)ct_ctn_map.get(t)).addRequirement(ct_ctn_map.get(nested));
                                int count = ((Integer)countMap.get(nested)).intValue();
                                countMap.put(nested, Integer.valueOf(count + 1));
                            }
                        }
                    }
                }
            }
        }
    }


    private List<ComposedType> calculateComposedTypeOrder(Set<ComposedType> originalComposedTypeSet)
    {
        List<ComposedType> returnlist = new ArrayList<>();
        Set<ComposedType> origSet = new HashSet<>();
        for(ComposedType ct : originalComposedTypeSet)
        {
            if(!ct.isAbstract())
            {
                origSet.add(ct);
            }
        }
        Map<ComposedType, Integer> countMap = new HashMap<>();
        Map<ComposedType, ComposedTypeNode> ct_ctn_map = new HashMap<>();
        while(true)
        {
            countMap.clear();
            ct_ctn_map.clear();
            for(ComposedType ct : origSet)
            {
                ct_ctn_map.put(ct, new ComposedTypeNode(this, ct));
                countMap.put(ct, Integer.valueOf(0));
            }
            createNodes(origSet, ct_ctn_map, countMap);
            try
            {
                List<ComposedTypeNode> sortedList = RequirementSolver.solve(ct_ctn_map.values());
                for(ComposedTypeNode ctn : sortedList)
                {
                    returnlist.add(ctn.getComposedType());
                }
                return returnlist;
            }
            catch(RequirementSolverException e)
            {
                ComposedType firstInList = getCTwithHighestCount(countMap);
                returnlist.add(firstInList);
                origSet.remove(firstInList);
            }
        }
    }


    private ComposedType getCTwithHighestCount(Map<ComposedType, Integer> countMap)
    {
        int counter = 0;
        ComposedType retct = null;
        for(Map.Entry<ComposedType, Integer> me : countMap.entrySet())
        {
            if(retct == null)
            {
                retct = me.getKey();
            }
            if(((Integer)me.getValue()).intValue() > counter)
            {
                counter = ((Integer)me.getValue()).intValue();
                retct = me.getKey();
            }
        }
        return retct;
    }
}
