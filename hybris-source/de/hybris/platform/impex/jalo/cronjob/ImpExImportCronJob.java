package de.hybris.platform.impex.jalo.cronjob;

import de.hybris.platform.core.Constants;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.MediaUtil;
import de.hybris.platform.util.zip.SafeZipEntry;
import de.hybris.platform.util.zip.SafeZipInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class ImpExImportCronJob extends GeneratedImpExImportCronJob
{
    private static final Logger LOG = Logger.getLogger(ImpExImportCronJob.class);


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        allAttributes.setAttributeMode("dumpFileEncoding", Item.AttributeMode.INITIAL);
        if(allAttributes.get("dumpFileEncoding") == null)
        {
            try
            {
                allAttributes.addInitialProperty("dumpFileEncoding", EnumerationManager.getInstance()
                                .getEnumerationValue(Constants.ENUMS.ENCODINGENUM, CSVConstants.DEFAULT_ENCODING));
            }
            catch(Exception e)
            {
                LOG.warn("Encoding not found! Unable to set default encoding and dumpFileEncoding to '" + CSVConstants.DEFAULT_ENCODING + "'");
            }
        }
        if(allAttributes.containsKey("maxThreads"))
        {
            allAttributes.put("maxThreads", adjustMaxThreads((Integer)allAttributes.get("maxThreads")));
        }
        return super.createItem(ctx, type, allAttributes);
    }


    public ImpExMedia createUnresolvedDataStore()
    {
        Map<Object, Object> attributes = new HashMap<>();
        attributes.put(ImpExMedia.OWNER, this);
        attributes.put("code", "unresolved_lines_" + getPK().toString());
        attributes.put("mime", ImpExConstants.File.MIME_TYPE_CSV);
        attributes.put("realFileName", "unresolved_lines_" +
                        getPK().toString() + ".csv");
        return ImpExManager.getInstance().createImpExMedia(attributes);
    }


    public ImpExMedia createWorkMedia()
    {
        Map<Object, Object> attributes = new HashMap<>();
        attributes.put(ImpExMedia.OWNER, this);
        attributes.put("code", "work_media_" + getPK().toString());
        attributes.put("mime", ImpExConstants.File.MIME_TYPE_CSV);
        attributes.put("realFileName", "work_media_" + getPK().toString() + ".csv");
        return ImpExManager.getInstance().createImpExMedia(attributes);
    }


    public boolean extractZip() throws ImpExException
    {
        boolean result = false;
        ImpExMedia oldJobMedia = getJobMedia();
        String oldJobMediaMime = (oldJobMedia != null) ? oldJobMedia.getMime() : null;
        if(oldJobMedia != null && MediaUtil.isZipRelatedMime(oldJobMediaMime))
        {
            ImpExMedia newJobMedia = null;
            Collection<ImpExMedia> newExternalDataColl = new ArrayList<>(getExternalDataCollection());
            ImpExManager impexManager = ImpExManager.getInstance();
            String zipJobMediaName = (getZipentry() != null && getZipentry().length() > 0) ? getZipentry() : "importscript.impex";
            ZipFile zFile = null;
            SafeZipInputStream zin = null;
            try
            {
                Enumeration<? extends ZipEntry> entries = null;
                if(oldJobMedia.hasData())
                {
                    zFile = new ZipFile(oldJobMedia.getFiles().iterator().next());
                    entries = zFile.entries();
                }
                else
                {
                    zin = new SafeZipInputStream(oldJobMedia.getDataFromStreamSure());
                }
                for(SafeZipEntry entry = getNext(entries, zin); entry != null; entry = getNext(entries, zin))
                {
                    String extractionId = "-" + UUID.randomUUID().toString();
                    String code = getCode() + "-" + getCode() + entry.getName();
                    ImpExMedia entryMedia = impexManager.createImpExMediaForCodeAndExtractionId(code, extractionId);
                    oldJobMedia.copySettings(entryMedia);
                    if(zFile != null)
                    {
                        entryMedia.setData(new DataInputStream(zFile.getInputStream((ZipEntry)entry)), entryMedia.getRealFileName(), null);
                    }
                    else
                    {
                        File temp = File.createTempFile("zipentry", "impex");
                        try
                        {
                            MediaUtil.copy((InputStream)zin, new FileOutputStream(temp));
                            FileInputStream in = new FileInputStream(temp);
                            try
                            {
                                entryMedia.setData(new DataInputStream(in), entryMedia.getRealFileName(), null);
                                in.close();
                            }
                            catch(Throwable throwable)
                            {
                                try
                                {
                                    in.close();
                                }
                                catch(Throwable throwable1)
                                {
                                    throwable.addSuppressed(throwable1);
                                }
                                throw throwable;
                            }
                        }
                        finally
                        {
                            if(!FileUtils.deleteQuietly(temp))
                            {
                                LOG.warn("Can not delete temporary file " + temp.getAbsolutePath());
                            }
                        }
                    }
                    if(entry.getName().equalsIgnoreCase(zipJobMediaName))
                    {
                        newJobMedia = entryMedia;
                    }
                    else
                    {
                        newExternalDataColl.add(entryMedia);
                    }
                }
                setExternalDataCollection(newExternalDataColl);
                if(newJobMedia == null)
                {
                    throw new ImpExException("Can not find ImpEx file with name " + (
                                    (getZipentry() == null) ? "importscript.impex" : getZipentry()) + " within ZIP-media");
                }
                setJobMedia(newJobMedia);
                if(oldJobMedia.isRemoveOnSuccessAsPrimitive())
                {
                    oldJobMedia.remove();
                }
                result = true;
            }
            catch(JaloBusinessException e)
            {
                throw new ImpExException(e);
            }
            catch(IOException e)
            {
                throw new ImpExException(e);
            }
            finally
            {
                if(zFile != null)
                {
                    try
                    {
                        zFile.close();
                    }
                    catch(IOException e)
                    {
                        throw new JaloSystemException(e);
                    }
                }
                if(zin != null)
                {
                    try
                    {
                        zin.close();
                        zin = null;
                    }
                    catch(IOException e)
                    {
                        throw new JaloSystemException(e);
                    }
                }
            }
        }
        return result;
    }


    private SafeZipEntry getNext(Enumeration<? extends ZipEntry> entriesFromFile, SafeZipInputStream zin) throws IOException
    {
        if(entriesFromFile != null)
        {
            return entriesFromFile.hasMoreElements() ? new SafeZipEntry(entriesFromFile.nextElement()) : null;
        }
        return zin.getNextEntry();
    }


    @ForceJALO(reason = "abstract method implementation")
    public String getZipentry(SessionContext ctx)
    {
        if(getJobMedia() != null)
        {
            return getJobMedia().getZipentry(ctx);
        }
        return null;
    }


    @ForceJALO(reason = "abstract method implementation")
    public void setZipentry(SessionContext ctx, String value)
    {
        if(getJobMedia() != null)
        {
            getJobMedia().setZipentry(ctx, value);
        }
    }


    @ForceJALO(reason = "something else")
    public Integer getMaxThreads(SessionContext ctx)
    {
        Integer ret = super.getMaxThreads(ctx);
        if(ret == null || ret.intValue() < 1)
        {
            ret = ((ImpExImportJob)getJob()).getMaxThreads(ctx);
        }
        return ret;
    }


    @ForceJALO(reason = "something else")
    public void setMaxThreads(SessionContext ctx, Integer value)
    {
        super.setMaxThreads(ctx, adjustMaxThreads(value));
    }


    protected Integer adjustMaxThreads(Integer value)
    {
        return (value != null && value.intValue() < 1) ? null : value;
    }
}
