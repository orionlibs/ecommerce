package de.hybris.platform.jalo.media;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.media.MediaSource;
import de.hybris.platform.media.exceptions.MediaNotFoundException;
import de.hybris.platform.media.exceptions.MediaRemovalException;
import de.hybris.platform.media.impl.JaloMediaSource;
import de.hybris.platform.media.storage.impl.StoredMediaData;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.MediaUtil;
import de.hybris.platform.util.zip.SafeZipEntry;
import de.hybris.platform.util.zip.SafeZipInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;

public class Media extends GeneratedMedia
{
    public static final String URL = "url";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String FROM_JAR = "fromjar";
    private static final Logger LOG = Logger.getLogger(Media.class);
    @Deprecated(since = "ages", forRemoval = false)
    public static final String THUMBNAIL = "media.thumbnail";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String PICTURE = "media.picture";
    public static final String URL2 = "url2";
    public static final String HASDATA = "hasdata";
    public static final String ISONSERVER = "isonserver";


    @SLDSafe(portingClass = "de.hybris.platform.media.interceptors.MediaModelPrepareInterceptor", portingMethod = "onPrepare")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!checkMandatoryAttribute("code", allAttributes, missing))
        {
            throw new JaloInvalidParameterException("missing parameters " + missing + " to create a media", 0);
        }
        MediaFolder folder = (MediaFolder)allAttributes.get("folder");
        if(folder == null)
        {
            allAttributes.put("folder", MediaManager.getInstance().getRootMediaFolder());
        }
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("folder", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    public boolean setFile(File file) throws JaloBusinessException
    {
        return setFile(file, getFolder());
    }


    public boolean setFile(File file, MediaFolder newFolder) throws JaloBusinessException
    {
        if(file != null && file.isFile() && file.canRead())
        {
            String fileName = file.getName();
            int index = fileName.lastIndexOf(File.pathSeparator);
            if(index > 0)
            {
                fileName = fileName.substring(index + 1);
            }
            try
            {
                setData(new DataInputStream(new FileInputStream(file)), fileName, null, newFolder);
                return true;
            }
            catch(FileNotFoundException e)
            {
                throw new JaloInternalException(e, "file " + file + " was removed wile trying to store in media", 0);
            }
        }
        return false;
    }


    public String getURL()
    {
        return getURL(getSession().getSessionContext());
    }


    public String getURL(SessionContext ctx)
    {
        return MediaManager.getInstance().getURLForMedia(getFolder().getQualifier(), (MediaSource)new JaloMediaSource((AbstractMedia)this));
    }


    @SLDSafe
    @Deprecated(since = "ages", forRemoval = false)
    public String getURL2(SessionContext ctx)
    {
        return getURL(ctx);
    }


    @SLDSafe
    @Deprecated(since = "ages", forRemoval = false)
    public String getURL2()
    {
        return getURL2(getSession().getSessionContext());
    }


    @SLDSafe
    @Deprecated(since = "ages", forRemoval = false)
    public void setURL2(SessionContext ctx, String url) throws JaloBusinessException
    {
        setURL(ctx, url);
    }


    @SLDSafe
    @Deprecated(since = "ages", forRemoval = false)
    public void setURL2(String url) throws JaloBusinessException
    {
        setURL2(getSession().getSessionContext(), url);
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.media.impl.MediaDownloadUrlHandler", portingMethod = "get")
    public String getDownloadURL(SessionContext ctx)
    {
        return getDownloadURL();
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.media.impl.MediaDownloadUrlHandler", portingMethod = "get")
    public String getDownloadURL()
    {
        return MediaManager.getInstance().getDownloadURLForMedia(getFolder().getQualifier(), (MediaSource)new JaloMediaSource((AbstractMedia)this));
    }


    protected File copyToTempFile(InputStream inputStream) throws IOException
    {
        File tempFile = File.createTempFile("relocate-" + getPK(), "bin");
        OutputStream outputStream = null;
        try
        {
            outputStream = new FileOutputStream(tempFile);
            IOUtils.copy(inputStream, outputStream);
        }
        catch(IOException | RuntimeException e)
        {
            FileUtils.deleteQuietly(tempFile);
            throw e;
        }
        finally
        {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
        }
        return tempFile;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean relocateData(MediaFolder folder)
    {
        try
        {
            Collection<Media> foreignDataOwners = getForeignDataOwners();
            boolean result = moveToFolder(folder, true);
            for(Media otherMedia : foreignDataOwners)
            {
                copyMetaData(otherMedia, this);
                otherMedia.setFolder(folder);
            }
            return result;
        }
        catch(JaloBusinessException e)
        {
            LOG.error(e.getMessage(), (Throwable)e);
            return false;
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setData(DataInputStream stream)
    {
        setData(stream, null, null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public final void setData(DataInputStream stream, String originalName, String mimeType)
    {
        setData(stream, originalName, mimeType, getFolder());
    }


    public final void setData(InputStream stream, String originalName, String mimeType)
    {
        setData(stream, originalName, mimeType, getFolder());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setData(DataInputStream stream, String originalName, String mimeType, MediaFolder folder)
    {
        setData(stream, originalName, mimeType, folder);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setData(InputStream stream)
    {
        setData(stream, null, null);
    }


    public void setData(InputStream stream, String originalName, String mimeType, MediaFolder folder)
    {
        try
        {
            Transaction.current().execute((TransactionBody)new Object(this, folder, originalName, mimeType, stream));
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    private MediaFolder determineAndSetFolder(MediaFolder folder)
    {
        MediaFolder myFolder = getFolder();
        MediaFolder newFolder = (folder == null) ? MediaManager.getInstance().getRootMediaFolder() : folder;
        if(!myFolder.equals(newFolder))
        {
            setFolder(newFolder);
        }
        return getFolder();
    }


    private void setAllMetadata(StoredMediaData mediaData, String realFileName) throws JaloBusinessException
    {
        setMime(mediaData.getMime());
        setRealFileName(realFileName);
        setDataPK(mediaData.getDataPk());
        setLocation(mediaData.getLocation());
        setLocationHash(mediaData.getHashForLocation());
        setSize(mediaData.getSize());
    }


    private void notifyGottenData() throws JaloBusinessException
    {
        setInternalURL(JaloSession.getCurrentSession().getSessionContext(), "replicated273654712");
    }


    public void setURL(String url) throws JaloBusinessException
    {
        setURL(getSession().getSessionContext(), url);
    }


    public void setURL(SessionContext ctx, String url) throws JaloBusinessException
    {
        if(url != null && url.contains("/fromjar"))
        {
            String resourceName = url.substring(url.indexOf("/fromjar") + 1 + "fromjar".length());
            URL fileurl = Media.class.getResource(resourceName);
            if(fileurl != null)
            {
                try
                {
                    File file = new File(fileurl.toURI());
                    if(file.exists())
                    {
                        setSize(Long.valueOf(file.length()));
                    }
                }
                catch(URISyntaxException e)
                {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        if(hasData())
        {
            removeData(true);
        }
        if(getMime() == null)
        {
            setMime(ctx, MediaManager.getInstance().getBestMime(url));
        }
        setInternalURL(ctx, url);
    }


    public boolean hasData()
    {
        return "replicated273654712".equals(getInternalURL(JaloSession.getCurrentSession().getSessionContext()));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isOnServer()
    {
        return hasData();
    }


    @SLDSafe
    public void setSize(Long size)
    {
        setProperty(null, "size", size);
    }


    @SLDSafe
    public boolean isRemovableAsPrimitive(SessionContext context)
    {
        Boolean value = isRemovable(context);
        return (value == null || value.booleanValue());
    }


    @SLDSafe(portingClass = "de.hybris.platform.media.interceptors.CheckIfRemovableMediaInterceptor", portingMethod = "onRemove")
    protected void checkRemovable(SessionContext ctx) throws ConsistencyCheckException
    {
        if(!isRemovableAsPrimitive())
        {
            throw new ConsistencyCheckException("Could not remove data because it is not removable at the moment", -1);
        }
        super.checkRemovable(ctx);
    }


    @SLDSafe(portingClass = "de.hybris.platform.media.interceptors.RemoveDataOnMediaModelRemoveInterceptor", portingMethod = "onRemove")
    public void remove(SessionContext context) throws ConsistencyCheckException
    {
        if(isOnServer())
        {
            try
            {
                removeData(false);
            }
            catch(JaloBusinessException e)
            {
                LOG.error("could not remove data due to " + e.getMessage(), (Throwable)e);
            }
        }
        super.remove(context);
    }


    public void setDataByURL() throws IOException, JaloBusinessException
    {
        if(hasData())
        {
            throw new JaloBusinessException("media has already data", -1);
        }
        String url = getURL();
        if(url == null || url.trim().isEmpty())
        {
            throw new JaloInvalidParameterException("url is null or empty - cannot set data by it", 0);
        }
        setData(new DataInputStream((new URL(url))
                                        .openConnection().getInputStream()),
                        (getMime() == null) ? MediaManager.getInstance().getBestMime(url) : getMime(),
                        getRealFileName());
    }


    public void removeData(boolean failOnError) throws JaloBusinessException
    {
        if(hasData())
        {
            try
            {
                MediaManager.getInstance().deleteMediaDataUnlessReferenced(getPK(), getDataPK(), getFolder().getQualifier(),
                                getLocation());
            }
            catch(MediaRemovalException e)
            {
                if(failOnError)
                {
                    throw new JaloBusinessException(e);
                }
                if(LOG.isInfoEnabled())
                {
                    LOG.info("Removal of media has failed.");
                }
            }
            setLocation(null);
            setLocationHash(null);
            setDataPK(null);
            setSize(null);
            setMime(JaloSession.getCurrentSession().getSessionContext(), null);
            setInternalURL(JaloSession.getCurrentSession().getSessionContext(), null);
        }
    }


    @SLDSafe(portingClass = "de.hybris.platform.media.impl.ForeignDataOwnerHandler", portingMethod = "get")
    public Collection<Media> getForeignDataOwners()
    {
        return getForeignDataOwners(getSession().getSessionContext());
    }


    @SLDSafe(portingClass = "de.hybris.platform.media.impl.ForeignDataOwnerHandler", portingMethod = "get")
    public Collection<Media> getForeignDataOwners(SessionContext ctx)
    {
        if(!hasData())
        {
            return Collections.EMPTY_LIST;
        }
        SessionContext myCtx = ctx;
        boolean useLocalCtx = (ctx == null || !Boolean.TRUE.equals(myCtx.getAttribute("disableRestrictions")));
        if(useLocalCtx)
        {
            myCtx = getSession().createLocalSessionContext(myCtx);
            myCtx.setAttribute("disableRestrictions", Boolean.TRUE);
        }
        try
        {
            Long myDataPK = (Long)getProperty(myCtx, "dataPK");
            Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
            params.put("myDataPK", myDataPK);
            params.put("myPK", getPK());
            params.put("hasdata", "replicated273654712");
            if(myDataPK == null)
            {
                return
                                FlexibleSearch.getInstance()
                                                .search("SELECT {" + Item.PK + "} FROM {MEDIA} WHERE {" + PK + "}<>?myPK AND {internalURL}=?hasdata AND {dataPK}=?myPK", params, Media.class)
                                                .getResult();
            }
            return
                            FlexibleSearch.getInstance()
                                            .search("SELECT {" + PK + "} FROM {MEDIA} WHERE {" + PK + "}<>?myPK AND {internalURL}=?hasdata AND ({dataPK}=?myDataPK OR ({dataPK} IS NULL AND {" + PK + "}=?myDataPK))", params, Media.class)
                                            .getResult();
        }
        finally
        {
            if(useLocalCtx)
            {
                getSession().removeLocalSessionContext();
            }
        }
    }


    public boolean hasForeignDataOwners()
    {
        if(!hasData())
        {
            return false;
        }
        return MediaManager.getInstance().hasForeignDataOwners(getPK(), getDataPK());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public DataInputStream getDataFromStream() throws JaloBusinessException
    {
        InputStream inputStream = getDataFromStreamInternal();
        return (inputStream == null) ? null : new DataInputStream(inputStream);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public InputStream getDataFromInputStream() throws JaloBusinessException
    {
        return getDataFromStreamInternal();
    }


    private InputStream getDataFromStreamInternal() throws JaloBusinessException
    {
        String location = getLocation();
        InputStream dataFromStream = null;
        if(location != null)
        {
            try
            {
                dataFromStream = MediaManager.getInstance().getMediaAsStream(getFolder().getQualifier(), location);
                return dataFromStream;
            }
            catch(MediaNotFoundException e)
            {
                closeStreamQuietly(dataFromStream);
                throw new JaloBusinessException(e);
            }
        }
        return null;
    }


    private void closeStreamQuietly(InputStream inputStream)
    {
        if(inputStream != null)
        {
            try
            {
                inputStream.close();
            }
            catch(IOException iOException)
            {
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public DataInputStream getDataFromStreamSure() throws JaloBusinessException
    {
        InputStream inputStream = getDataFromInputStreamSure();
        return (inputStream == null) ? null : new DataInputStream(inputStream);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public InputStream getDataFromInputStreamSure() throws JaloBusinessException
    {
        return MediaManager.getInstance().getMediaAsStream((MediaSource)new JaloMediaSource((AbstractMedia)this));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setDataFromStream(DataInputStream stream) throws JaloBusinessException
    {
        setData(stream, null, null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setDataFromStream(InputStream stream) throws JaloBusinessException
    {
        setData(stream, null, null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public byte[] getData() throws JaloBusinessException
    {
        String location = getLocation();
        if(location == null)
        {
            throw new JaloBusinessException("Media has not data uploaded");
        }
        try
        {
            return MediaManager.getInstance().getMediaAsByteArray(getFolder().getQualifier(), location);
        }
        catch(IOException e)
        {
            throw new JaloBusinessException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setData(Media sourceMedia)
    {
        if(sourceMedia == null)
        {
            throw new JaloInvalidParameterException("media was null", 0);
        }
        if(!sourceMedia.hasData())
        {
            if(GenericValidator.isBlankOrNull(sourceMedia.getURL()))
            {
                throw new JaloInvalidParameterException("source media " + sourceMedia + " has no data and no URL - cannot copy data to " + this, 0);
            }
            try
            {
                setURL(sourceMedia.getURL());
            }
            catch(JaloBusinessException e)
            {
                throw new JaloSystemException(e);
            }
            copyMetaData(this, sourceMedia);
        }
        else
        {
            MediaFolder myFolder = getFolder();
            MediaFolder otherFolder = sourceMedia.getFolder();
            if(otherFolder == null)
            {
                otherFolder = MediaManager.getInstance().getRootMediaFolder();
            }
            if(myFolder == otherFolder || (myFolder != null && myFolder.equals(otherFolder)))
            {
                try
                {
                    Transaction.current().execute((TransactionBody)new Object(this, sourceMedia));
                }
                catch(Exception e)
                {
                    throw new JaloSystemException(e);
                }
            }
            else
            {
                MediaUtil.copyMediaData(sourceMedia, this);
            }
        }
    }


    private void reuseDataInternal(Media sourceMedia)
    {
        Preconditions.checkArgument(sourceMedia.hasData(), "Source media has no data");
        if(hasData())
        {
            try
            {
                removeData(false);
            }
            catch(JaloBusinessException e)
            {
                throw new JaloSystemException(e);
            }
        }
        copyMetaData(this, sourceMedia);
    }


    private void copyMetaData(Media targetMedia, Media sourceMedia)
    {
        try
        {
            if(sourceMedia.hasData())
            {
                Long dataPK = sourceMedia.getDataPK();
                targetMedia.setDataPK((dataPK == null) ? Long.valueOf(sourceMedia.getPK().getLongValue()) : dataPK);
            }
            targetMedia.setMime(sourceMedia.getMime());
            targetMedia.setLocation(sourceMedia.getLocation());
            targetMedia.setLocationHash(sourceMedia.getLocationHash());
            targetMedia.setRealFileName(sourceMedia.getRealFileName());
            targetMedia.setSize(sourceMedia.getSize());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setData(byte[] data) throws JaloBusinessException
    {
        setData(new DataInputStream(new ByteArrayInputStream(data)), null, null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getFiles() throws JaloSystemException
    {
        return Lists.newArrayList((Object[])new File[] {getFile()});
    }


    public File getFile() throws JaloSystemException
    {
        return hasData() ? MediaManager.getInstance().getMediaAsFile(getFolder().getQualifier(), getLocation()) : null;
    }


    public String getFileName()
    {
        File file = getFile();
        return (file != null) ? file.getName() : null;
    }


    protected String getFileExtensionFromMime()
    {
        return MediaManager.getInstance().getFileExtensionFromMime(getMime());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void moveData(Media destin) throws JaloBusinessException
    {
        moveDataTo(destin);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void moveDataTo(Media destin) throws JaloBusinessException
    {
        destin.setData(this);
        removeData(true);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean moveMedia(MediaFolder newFolder) throws JaloBusinessException
    {
        return moveMedia(newFolder, false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected boolean moveMedia(MediaFolder newFolder, boolean forOverwriting) throws JaloBusinessException
    {
        return moveToFolder(newFolder, forOverwriting);
    }


    public String getFileNameForFileBasedSoftware()
    {
        if(hasData())
        {
            return getFileName();
        }
        String prefix = MediaUtil.getLocalMediaWebRootUrl();
        String str = getURL();
        if(GenericValidator.isBlankOrNull(str))
        {
            return str;
        }
        if(!str.startsWith(prefix))
        {
            if(str.charAt(0) == '/')
            {
                str = str.substring(1);
            }
            return str.replace("/", File.separator);
        }
        str = str.substring(prefix.length());
        if(str.charAt(0) == '/')
        {
            str = str.substring(1);
        }
        if(str.startsWith("fromjar"))
        {
            str = str.substring("fromjar".length());
        }
        if(str.charAt(0) == '/')
        {
            str = str.substring(1);
        }
        return str.replace("/", File.separator);
    }


    public List<String> getZipEntryNames() throws IOException, JaloBusinessException
    {
        if(MediaManager.getInstance().isZipRelatedMime(getMime()))
        {
            List<String> names = new ArrayList<>();
            ZipFile zFile = null;
            Enumeration<? extends ZipEntry> zEntries = null;
            SafeZipInputStream zin = null;
            if(hasData())
            {
                zFile = new ZipFile(getFiles().iterator().next());
                zEntries = zFile.entries();
            }
            else
            {
                zin = new SafeZipInputStream(getDataFromStreamSure());
            }
            try
            {
                for(SafeZipEntry entry = getNext(zEntries, zin); entry != null; entry = getNext(zEntries, zin))
                {
                    names.add(entry.getName());
                }
            }
            catch(IOException e)
            {
                throw new JaloBusinessException("Error while retrieving zip entries of file: " + e.getMessage());
            }
            finally
            {
                if(zFile != null)
                {
                    try
                    {
                        zFile.close();
                    }
                    catch(IOException iOException)
                    {
                    }
                }
                IOUtils.closeQuietly((InputStream)zin);
            }
            return names;
        }
        return Collections.EMPTY_LIST;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List<ZipEntry> getZipEntries() throws IOException, JaloBusinessException
    {
        return Collections.EMPTY_LIST;
    }


    private SafeZipEntry getNext(Enumeration<? extends ZipEntry> entriesFromFile, SafeZipInputStream zin) throws IOException
    {
        if(entriesFromFile != null)
        {
            return entriesFromFile.hasMoreElements() ? new SafeZipEntry(entriesFromFile.nextElement()) : null;
        }
        return zin.getNextEntry();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Media getInFormat(MediaFormat format)
    {
        if(format == null)
        {
            return null;
        }
        if(format.equals(getMediaFormat()))
        {
            return this;
        }
        MediaContainer myContainer = getMediaContainer();
        if(myContainer == null)
        {
            return null;
        }
        return myContainer.getMedia(format);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Media getForContext(MediaContext context)
    {
        if(context == null)
        {
            return null;
        }
        return getInFormat(context.getTargetFormat(getMediaFormat()));
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return getCode() + "(" + getCode() + ")";
    }


    private boolean moveToFolder(MediaFolder newFolder, boolean forOverwriting) throws JaloBusinessException
    {
        MediaFolder sourceFolder = getFolder();
        MediaFolder destFolder = (newFolder == null) ? MediaManager.getInstance().getRootMediaFolder() : newFolder;
        if(sourceFolder != null && sourceFolder.equals(destFolder))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Will not move media " + getCode() + " because current folder is same as target folder");
            }
            return false;
        }
        if(hasData())
        {
            if(!hasForeignDataOwners() || forOverwriting)
            {
                moveToFolderInternal(destFolder, true);
            }
            else
            {
                moveToFolderInternal(destFolder, false);
            }
        }
        else
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Will not move data of media " + getCode() + " because it has no data or data is not managed in media directory. However new folder " + destFolder
                                .getQualifier() + " will be set.");
            }
            setFolder(destFolder);
        }
        return true;
    }


    private void moveToFolderInternal(MediaFolder destFolder, boolean removeOldData) throws JaloBusinessException
    {
        String oldFolderQualifier = getFolder().getQualifier();
        String oldLocation = getLocation();
        InputStream sourceStream = MediaManager.getInstance().getMediaAsStream(getFolder().getQualifier(), getLocation());
        String sourceMime = getMime();
        String sourceRealFileName = getRealFileName();
        setData(sourceStream, sourceRealFileName, sourceMime, destFolder);
        if(removeOldData)
        {
            MediaManager.getInstance().deleteMedia(oldFolderQualifier, oldLocation);
        }
    }


    @SLDSafe
    public void setDataPK(Long dataPK)
    {
        setProperty(null, "dataPK", dataPK);
    }


    @SLDSafe
    public MediaFolder getFolder(SessionContext ctx)
    {
        MediaFolder folder = super.getFolder(ctx);
        if(folder == null)
        {
            folder = MediaManager.getInstance().getRootMediaFolder();
        }
        return folder;
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.media.LegacyMediaMigrator")
    public String getLocation(SessionContext ctx)
    {
        String location = (String)getProperty("location");
        if(hasData())
        {
            Long dataPK = (Long)getProperty("dataPK");
            if(dataPK == null)
            {
                String mimeExtension = getFileExtensionFromMime();
                String folderPath = Strings.nullToEmpty(getFolder().getPath());
                location = MediaUtil.addTrailingFileSepIfNeeded(folderPath) + MediaUtil.addTrailingFileSepIfNeeded(folderPath) + "." + getPK();
            }
            else if(location == null)
            {
                String subFolderPath = Strings.nullToEmpty((String)getProperty("subFolderPath"));
                String mimeExtension = getFileExtensionFromMime();
                String folderPath = Strings.nullToEmpty(getFolder().getPath());
                location = MediaUtil.addTrailingFileSepIfNeeded(folderPath) + MediaUtil.addTrailingFileSepIfNeeded(folderPath) + MediaUtil.addTrailingFileSepIfNeeded(subFolderPath) + "." + dataPK;
            }
        }
        return location;
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.media.impl.MediaDeniedPrincipalsHandler", portingMethod = "get")
    public Collection<Principal> getDeniedPrincipals()
    {
        return getDeniedPrincipals(getSession().getSessionContext());
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.media.impl.MediaDeniedPrincipalsHandler", portingMethod = "set")
    public void setDeniedPrincipals(Collection<Principal> value)
    {
        setDeniedPrincipals(getSession().getSessionContext(), value);
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.media.impl.MediaDeniedPrincipalsHandler", portingMethod = "get")
    public Collection<Principal> getDeniedPrincipals(SessionContext ctx)
    {
        List<UserRight> userRights = Collections.singletonList(AccessManager.getInstance().getUserRightByCode("read"));
        Map<Principal, List<Boolean>> permissionMap = getPermissionMap(userRights);
        Collection<Principal> deniedPrincipals = new HashSet<>();
        for(Principal principal : permissionMap.keySet())
        {
            if(Boolean.TRUE.equals(((List)permissionMap.get(principal)).get(0)))
            {
                deniedPrincipals.add(principal);
            }
        }
        return deniedPrincipals;
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.media.impl.MediaDeniedPrincipalsHandler", portingMethod = "set")
    public void setDeniedPrincipals(SessionContext ctx, Collection<Principal> value)
    {
        Collection<Principal> existingDeniedPrincipals = getDeniedPrincipals();
        Collection<Principal> newPrincipals = value;
        if(newPrincipals == null)
        {
            newPrincipals = Collections.EMPTY_LIST;
        }
        UserRight userRight = AccessManager.getInstance().getUserRightByCode("read");
        for(Principal existingDeniedPrincipal : existingDeniedPrincipals)
        {
            if(!newPrincipals.contains(existingDeniedPrincipal))
            {
                try
                {
                    AccessManager.getInstance()
                                    .removePermissionOn((Item)this, (Principal)getSession().getUser(), existingDeniedPrincipal, userRight);
                }
                catch(JaloSecurityException e)
                {
                    LOG.error("Removing permissions for the principal " + existingDeniedPrincipal + " failed...");
                }
            }
        }
        for(Principal principal : newPrincipals)
        {
            addNegativePermission(principal, userRight);
        }
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.media.impl.MediaPermittedPrincipalsHandler", portingMethod = "get")
    public Collection<Principal> getPermittedPrincipals()
    {
        return getPermittedPrincipals(getSession().getSessionContext());
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.media.impl.MediaPermittedPrincipalsHandler", portingMethod = "get")
    public Collection<Principal> getPermittedPrincipals(SessionContext ctx)
    {
        List<UserRight> userRights = Collections.singletonList(AccessManager.getInstance().getUserRightByCode("read"));
        Map<Principal, List<Boolean>> permissionMap = getPermissionMap(userRights);
        Collection<Principal> permittedPrincipals = new HashSet<>();
        for(Principal principal : permissionMap.keySet())
        {
            if(Boolean.FALSE.equals(((List)permissionMap.get(principal)).get(0)))
            {
                permittedPrincipals.add(principal);
            }
        }
        return permittedPrincipals;
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.media.impl.MediaDeniedPrincipalsHandler", portingMethod = "set")
    public void setPermittedPrincipals(Collection<Principal> value)
    {
        setPermittedPrincipals(getSession().getSessionContext(), value);
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.media.impl.MediaDeniedPrincipalsHandler", portingMethod = "set")
    public void setPermittedPrincipals(SessionContext ctx, Collection<Principal> value)
    {
        Collection<Principal> existingGrantedPrincipals = getPermittedPrincipals();
        Collection<Principal> newPrincipals = value;
        if(newPrincipals == null)
        {
            newPrincipals = Collections.EMPTY_LIST;
        }
        UserRight userRight = AccessManager.getInstance().getUserRightByCode("read");
        for(Principal existingGrantedPrincipal : existingGrantedPrincipals)
        {
            if(!newPrincipals.contains(existingGrantedPrincipal))
            {
                try
                {
                    AccessManager.getInstance()
                                    .removePermissionOn((Item)this, (Principal)getSession().getUser(), existingGrantedPrincipal, userRight);
                }
                catch(JaloSecurityException e)
                {
                    LOG.error("Removing permissions for the principal " + existingGrantedPrincipal + " failed...");
                }
            }
        }
        for(Principal principal : newPrincipals)
        {
            addPositivePermission(principal, userRight);
        }
    }
}
