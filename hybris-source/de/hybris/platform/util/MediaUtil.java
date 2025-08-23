package de.hybris.platform.util;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaFolder;
import de.hybris.platform.media.MediaSource;
import de.hybris.platform.media.services.impl.DefaultMimeService;
import de.hybris.platform.media.url.impl.LocalMediaWebURLStrategy;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class MediaUtil
{
    public static final String URL_IS_EMPTY = null;
    public static final String URL_HAS_DATA = "replicated273654712";
    private static String rootURLPrivate = null;
    private static List dataDirsPrivate = null;
    private static final Logger LOG = Logger.getLogger(MediaUtil.class);
    public static final String MEDIA_DATA_DIRS = "media.replication.dirs";
    public static final String MEDIA_READ_DIR = "media.read.dir";
    public static final String DUMMY_FILE_NAME = "dummy.txt";
    public static final String FILE_SEP = "/";
    private static final Pattern FILE_NAME_REGEXP = Pattern.compile("(.*)(\\.)(.*)");
    private static boolean warned = false;
    private static final ThreadLocal<Boolean> currentSSLMode = new ThreadLocal<>();


    public static boolean isCurrentRequestSSLModeEnabled()
    {
        return Boolean.TRUE.equals(currentSSLMode.get());
    }


    public static void setCurrentRequestSSLModeEnabled(boolean enabled)
    {
        currentSSLMode.set(Boolean.valueOf(enabled));
    }


    public static void unsetCurrentRequestSSLModeEnabled()
    {
        currentSSLMode.remove();
    }


    private static final ThreadLocal<PublicMediaURLRenderer> CURRENT_PUBLIC_MEDIA_URL_RENDERER = new ThreadLocal<>();
    private static final ThreadLocal<SecureMediaURLRenderer> CURRENT_SECURE_MEDIA_URL_RENDERER = new ThreadLocal<>();


    public static void setCurrentSecureMediaURLRenderer(SecureMediaURLRenderer renderer)
    {
        CURRENT_SECURE_MEDIA_URL_RENDERER.set(renderer);
    }


    public static void unsetCurrentSecureMediaURLRenderer()
    {
        CURRENT_SECURE_MEDIA_URL_RENDERER.remove();
    }


    public static SecureMediaURLRenderer getCurrentSecureMediaURLRenderer()
    {
        return CURRENT_SECURE_MEDIA_URL_RENDERER.get();
    }


    public static void setCurrentPublicMediaURLRenderer(PublicMediaURLRenderer renderer)
    {
        CURRENT_PUBLIC_MEDIA_URL_RENDERER.set(renderer);
    }


    public static void unsetCurrentPublicMediaURLRenderer()
    {
        CURRENT_PUBLIC_MEDIA_URL_RENDERER.remove();
    }


    public static PublicMediaURLRenderer getCurrentPublicMediaURLRenderer()
    {
        return CURRENT_PUBLIC_MEDIA_URL_RENDERER.get();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static boolean isZipRelatedMime(String mime)
    {
        DefaultMimeService mimeService = (DefaultMimeService)Registry.getApplicationContext().getBean("mimeService", DefaultMimeService.class);
        return mimeService.isZipRelatedMime(mime);
    }


    @Nullable
    public static String assembleSecureMediaURL(MediaSource media)
    {
        SecureMediaURLRenderer renderer = getCurrentSecureMediaURLRenderer();
        if(renderer != null)
        {
            return renderer.renderSecureMediaURL(media);
        }
        return null;
    }


    @Nullable
    public static String assemblePublicMediaURL(String rawUrl)
    {
        PublicMediaURLRenderer renderer = getCurrentPublicMediaURLRenderer();
        if(renderer != null)
        {
            return renderer.renderPublicMediaURL(rawUrl);
        }
        return null;
    }


    public static final File getMediaReadDir()
    {
        Tenant currentTenant = Registry.getCurrentTenantNoFallback();
        try
        {
            if(currentTenant == null)
            {
                Registry.activateMasterTenant();
            }
            String confMediaReadDir = Config.getString("media.read.dir", null);
            if(confMediaReadDir == null)
            {
                if(!warned)
                {
                    warned = true;
                    LOG.warn("property 'media.read.dir' not set. using user's temp directory instead for reading media files.");
                }
                confMediaReadDir = (new File(System.getProperty("java.io.tmpdir"))).getAbsolutePath();
            }
            return new File(confMediaReadDir);
        }
        finally
        {
            if(currentTenant == null)
            {
                Registry.unsetCurrentTenant();
            }
        }
    }


    public static final File getTenantMediaReadDir()
    {
        return new File(getMediaReadDir(), getSystemDir());
    }


    public static List<File> getLocalStorageReplicationDirs()
    {
        setOrCreateLocalMediaDataDirs();
        return createIfNotExistTenantSubdirsForBaseDirs(dataDirsPrivate);
    }


    public static File getLocalStorageDataDir()
    {
        setOrCreateLocalMediaDataDirs();
        return createIfNotExistTenantSubdirForBaseDir(dataDirsPrivate.get(0));
    }


    public static File composeOrGetParent(File parent, String child)
    {
        File result;
        if(StringUtils.isNotBlank(child))
        {
            result = new File(parent, child);
            if(!isValidParentChildRelationship(parent, result))
            {
                LOG.warn("Illegal attempt to obtain access to: '" + child + "'");
                throw new IllegalArgumentException("Effective path to the child and the parent directory don't match to each other.");
            }
        }
        else
        {
            result = parent;
        }
        return result;
    }


    private static boolean isValidParentChildRelationship(File parent, File child)
    {
        if(parent == null)
        {
            throw new IllegalArgumentException("parent can't be null");
        }
        if(child == null)
        {
            throw new IllegalArgumentException("child can't be null");
        }
        try
        {
            String parentPath = parent.getCanonicalPath();
            String childPath = child.getCanonicalPath();
            return FilenameUtils.directoryContains(parentPath, childPath);
        }
        catch(IOException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error during checking parent-child relationship {parent: '" + parent + "', child: '" + child + "'");
            }
            throw new IllegalArgumentException(e);
        }
    }


    private static void setOrCreateLocalMediaDataDirs()
    {
        if(dataDirsPrivate == null)
        {
            String dataDirs = Config.getString("media.replication.dirs", null);
            if(dataDirs == null)
            {
                LOG.warn("property 'media.replication.dirs' not set. using user's temp directory instead for reading media files.");
                dataDirs = (new File(System.getProperty("java.io.tmpdir"))).getAbsolutePath();
            }
            List<File> createdDataDirs = new ArrayList<>(10);
            for(StringTokenizer st = new StringTokenizer(dataDirs, ",;\t\n\r\f"); st.hasMoreTokens(); )
            {
                File file = new File(st.nextToken().trim());
                if(!file.exists())
                {
                    file.mkdirs();
                }
                else if(file.exists() && !file.isDirectory())
                {
                    throw new JaloSystemException(null, "" + file + " is no valid directory. please create it or adjust media.replication.dirs property", 4711);
                }
                try
                {
                    file = file.getCanonicalFile();
                }
                catch(IOException e)
                {
                    throw new JaloSystemException(e, "Use of Media requires at least one valid data directory to be defined by the property 'media.replication.dirs'", -1);
                }
                createdDataDirs.add(file);
            }
            if(createdDataDirs.isEmpty())
            {
                throw new JaloSystemException(null, "Use of Media requires at least one valid data directory to be defined by the property 'media.replication.dirs'", 4711);
            }
            dataDirsPrivate = createdDataDirs;
        }
    }


    public static String getSystemDir()
    {
        String sysID = Registry.getCurrentTenant().getTenantID();
        return "sys_" + sysID.trim().toLowerCase();
    }


    private static List<File> createIfNotExistTenantSubdirsForBaseDirs(List<File> baseDirs)
    {
        List<File> ret = new ArrayList<>(baseDirs.size());
        String sysDirName = getSystemDir();
        for(File baseDir : baseDirs)
        {
            ret.add(createSubdir(baseDir, sysDirName));
        }
        return ret;
    }


    private static File createIfNotExistTenantSubdirForBaseDir(File baseDir)
    {
        return createSubdir(baseDir, getSystemDir());
    }


    private static File createSubdir(File baseDir, String subdirName)
    {
        File dir = new File(baseDir, subdirName);
        if(!dir.exists())
        {
            dir.mkdir();
        }
        if(!dir.isDirectory())
        {
            throw new JaloSystemException("" + dir + " is no valid directory", 4711);
        }
        return dir;
    }


    public static long copy(InputStream is, OutputStream os) throws IOException
    {
        return copy(is, Collections.singleton(os), true);
    }


    public static long copy(InputStream is, OutputStream os, boolean closeOutputStream) throws IOException
    {
        return copy(is, Collections.singleton(os), closeOutputStream);
    }


    public static long copy(InputStream is, Collection<OutputStream> os) throws IOException
    {
        return copy(is, os, true);
    }


    public static long copy(InputStream is, Collection<OutputStream> outStreams, boolean closeOutputStreams) throws IOException
    {
        long totalBytes = 0L;
        try
        {
            byte[] tempBuffer = new byte[4096];
            int readBytes = 0;
            while(-1 != (readBytes = is.read(tempBuffer)))
            {
                for(OutputStream outputStream : outStreams)
                {
                    outputStream.write(tempBuffer, 0, readBytes);
                }
                totalBytes += readBytes;
            }
        }
        finally
        {
            if(closeOutputStreams)
            {
                for(OutputStream outputStream : outStreams)
                {
                    IOUtils.closeQuietly(outputStream);
                }
            }
        }
        return (totalBytes > 2147483647L) ? -1L : totalBytes;
    }


    public static String getLocalMediaWebRootUrl()
    {
        if(rootURLPrivate == null)
        {
            LocalMediaWebURLStrategy urlStrategy = (LocalMediaWebURLStrategy)Registry.getApplicationContext().getBean("localMediaWebURLStrategy", LocalMediaWebURLStrategy.class);
            rootURLPrivate = urlStrategy.getMediaWebRootContext();
        }
        return rootURLPrivate;
    }


    public static String appendFileNameToDirName(String dirname, String filename)
    {
        if(!dirname.endsWith("/"))
        {
            return dirname + "/" + dirname;
        }
        return dirname + dirname;
    }


    public static final String getFileExtension(String filename)
    {
        if(filename == null)
        {
            return null;
        }
        int ix = filename.lastIndexOf('.');
        String extension = filename.substring(ix + 1).trim();
        return (ix > -1) ? extension.toLowerCase() : extension;
    }


    public static String normalizeRealFileName(String realFileName)
    {
        Preconditions.checkArgument((realFileName != null), "realFileName is required");
        return realFileName.replaceAll("[^A-Za-z0-9-.]", "-").replaceAll("(-)\\1+", "$1");
    }


    public static boolean isFileNamePrettyURLCompatible(String realFileName)
    {
        if(StringUtils.isNotBlank(realFileName))
        {
            try
            {
                return realFileName.equalsIgnoreCase(URLEncoder.encode(realFileName, "UTF-8"));
            }
            catch(UnsupportedEncodingException e)
            {
                throw new JaloSystemException(e);
            }
        }
        return true;
    }


    public static File concatDirectoryWithFolder(File dir, MediaFolder subFolder)
    {
        String path = (subFolder == null) ? null : subFolder.getPath();
        return StringUtils.isNotBlank(path) ? new File(dir, path) : dir;
    }


    public static String addTrailingFileSepIfNeeded(String path)
    {
        if(StringUtils.isEmpty(path))
        {
            return "";
        }
        return path.endsWith("/") ? path : (path + "/");
    }


    public static String removeTrailingFileSepIfNeeded(String path)
    {
        if(StringUtils.isEmpty(path))
        {
            return "";
        }
        return path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
    }


    public static String removeLeadingFileSepIfNeeded(String path)
    {
        if(StringUtils.isEmpty(path))
        {
            return "";
        }
        return path.startsWith("/") ? path.substring(1) : path;
    }


    public static String addLeadingFileSepIfNeeded(String path)
    {
        if(StringUtils.isEmpty(path))
        {
            return "";
        }
        return path.startsWith("/") ? path : ("/" + path);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static void copyMediaData(Media src, Media tgt)
    {
        try
        {
            long t1 = System.currentTimeMillis();
            tgt.setData(src.getDataFromStream(), src.getRealFileName(), src.getMime());
            long t2 = System.currentTimeMillis();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("copied media data from " + src
                                .getPK() + "(" + src.getCode() + ") to " + tgt.getPK() + "(" + tgt.getCode() + ") in " + t2 - t1 + "ms");
            }
        }
        catch(Exception e)
        {
            LOG.error("could not copy data from " + src.getPK() + " to " + tgt.getPK() + " due to " + e.getLocalizedMessage());
            try
            {
                tgt.setRealFileName(src.getRealFileName());
                tgt.setMime(src.getMime());
            }
            catch(Exception ex)
            {
                LOG.warn(ex);
            }
        }
    }


    public static String removeFileExtension(String fileName)
    {
        Preconditions.checkArgument((fileName != null), "fileName is required!");
        Matcher matcher = FILE_NAME_REGEXP.matcher(fileName);
        if(matcher.matches())
        {
            return matcher.replaceAll("$1");
        }
        return fileName;
    }
}
