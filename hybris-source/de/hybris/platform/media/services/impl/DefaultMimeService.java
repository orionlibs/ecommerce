package de.hybris.platform.media.services.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.media.services.MimeService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.MediaUtil;
import de.hybris.platform.util.config.ConfigIntf;
import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil2;
import eu.medsea.mimeutil.detector.ExtensionMimeDetector;
import eu.medsea.mimeutil.detector.MagicMimeMimeDetector;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

public class DefaultMimeService implements MimeService, InitializingBean
{
    public static final String FALLBACK_FILE_EXT = "bin";
    public static final String FALLBACK_MIME = "application/octet-stream";
    public static final String ZIP_MIME = "application/zip";
    public static final String X_ZIP_MIME = "application/x-zip";
    public static final String X_ZIP_COMPRESSED_MIME = "application/x-zip-compressed";
    protected static final String CUSTOM_EXTENSION_PREFIX = "media.customextension.";
    protected static final String CUSTOM_MIMETYPE_PREFIX = "mediatype.by.fileextension.";
    private MimeUtil2 mimeUtil;
    private volatile Set<String> cachedSupportedMimeTypes;


    private void registerMimeCacheInvalidationListeners()
    {
        CacheInvalidationListener cacheInvalidationListener = new CacheInvalidationListener(this);
        Tenant currentTenant = Registry.getCurrentTenantNoFallback();
        if(currentTenant != null)
        {
            currentTenant.getConfig().registerConfigChangeListener((ConfigIntf.ConfigChangeListener)cacheInvalidationListener);
            if(!currentTenant.getTenantID().equals("master"))
            {
                Registry.getMasterTenant().getConfig().registerConfigChangeListener((ConfigIntf.ConfigChangeListener)cacheInvalidationListener);
            }
        }
    }


    public void afterPropertiesSet() throws Exception
    {
        this.mimeUtil = new MimeUtil2();
        this.mimeUtil.registerMimeDetector(ExtensionMimeDetector.class.getName());
        this.mimeUtil.registerMimeDetector(MagicMimeMimeDetector.class.getName());
        this.cachedSupportedMimeTypes = extractSupportedMimeTypes();
        registerMimeCacheInvalidationListeners();
    }


    public String getFileExtensionFromMime(String mime)
    {
        return StringUtils.isNotBlank(mime) ? getConfiguredFileExtensionForMime(mime) : null;
    }


    private String getConfiguredFileExtensionForMime(String mime)
    {
        String mimeKeyPart = mime.replace('/', '.').toLowerCase();
        return getConfigParameter("media.customextension." + mimeKeyPart);
    }


    public String getBestExtensionFromMime(String mime)
    {
        String fileExtension = getFileExtensionFromMime(mime);
        return (fileExtension == null) ? "bin" : fileExtension;
    }


    public String getMimeFromFileExtension(String fileName)
    {
        String fileExtension = MediaUtil.getFileExtension(fileName);
        return StringUtils.isNotBlank(fileExtension) ? getConfiguredMimeForFileExtension(fileExtension) : null;
    }


    private String getConfiguredMimeForFileExtension(String fileExtension)
    {
        return getConfigParameter("mediatype.by.fileextension." + fileExtension.toLowerCase());
    }


    protected String getConfigParameter(String configKey)
    {
        return Config.getParameter(configKey);
    }


    public String getBestMime(String fileName, byte[] firstBytes, String overrideMime)
    {
        String mime = getMimeFromFileExtension(fileName);
        if(StringUtils.isBlank(mime) && firstBytes != null)
        {
            mime = getMimeFromFirstBytes(firstBytes);
        }
        if(StringUtils.isBlank(mime) && getSupportedMimeTypes().contains(overrideMime))
        {
            mime = overrideMime;
        }
        if(StringUtils.isBlank(mime))
        {
            mime = "application/octet-stream";
        }
        return mime;
    }


    public Set<String> getSupportedMimeTypes()
    {
        return this.cachedSupportedMimeTypes;
    }


    private Set<String> extractSupportedMimeTypes()
    {
        Set<String> supportedMimeTypes = new HashSet<>();
        Set<String> keys = getConfigParametersByPattern("media.customextension.").keySet();
        for(Iterator<String> it = keys.iterator(); it.hasNext(); )
        {
            String key = it.next();
            int idx = key.indexOf("media.customextension.");
            key = key.substring("media.customextension.".length() + idx);
            key = key.replace('.', '/');
            supportedMimeTypes.add(key);
        }
        return Collections.unmodifiableSet(supportedMimeTypes);
    }


    protected Map<String, String> getConfigParametersByPattern(String keyPrefix)
    {
        return Config.getParametersByPattern(keyPrefix);
    }


    public String getMimeFromFirstBytes(byte[] firstBytes)
    {
        String candidate = getMimeFromStream(new ByteArrayInputStream(firstBytes));
        if(candidate != null)
        {
            return candidate;
        }
        return getMimeFromStream((InputStream)new BOMInputStream(new ByteArrayInputStream(firstBytes)));
    }


    private String getMimeFromStream(InputStream stream)
    {
        Collection<MimeType> mimeTypes = this.mimeUtil.getMimeTypes(stream);
        MimeType mimeType = MimeUtil2.getMostSpecificMimeType(mimeTypes);
        if(mimeType.equals(MimeUtil2.UNKNOWN_MIME_TYPE))
        {
            return null;
        }
        return mimeType.toString();
    }


    public boolean isZipRelatedMime(String mime)
    {
        return ("application/zip".equalsIgnoreCase(mime) || "application/x-zip".equalsIgnoreCase(mime) || "application/x-zip-compressed".equalsIgnoreCase(mime));
    }
}
