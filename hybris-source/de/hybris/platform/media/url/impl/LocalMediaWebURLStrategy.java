package de.hybris.platform.media.url.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.Registry;
import de.hybris.platform.media.MediaSource;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.url.MediaURLStrategy;
import de.hybris.platform.media.url.PrettyUrlStrategy;
import de.hybris.platform.util.MediaUtil;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Value;

public class LocalMediaWebURLStrategy implements MediaURLStrategy
{
    public static final String CONTEXT_PARAM_DELIM = "|";
    public static final String NO_CTX_PART_MARKER = "-";
    public static final String CONTEXT_PARAM = "context";
    public static final String ATTACHEMENT_PARAM = "attachment";
    public static final String MEDIA_LEGACY_PRETTY_URL = "media.legacy.prettyURL";
    public static final String PRETTY_URL_TENANT_PREFIX = "sys_";
    public static final String URL_SEPARATOR = "/";
    private static final String MEDIAWEB_DEFAULT_CONTEXT = "/medias";
    @Value("${mediaweb.webroot:/medias}")
    private String mediaWebRoot;
    private Boolean overridePrettyUrlEnabled;


    public String getUrlForMedia(MediaStorageConfigService.MediaFolderConfig config, MediaSource mediaSource)
    {
        Preconditions.checkArgument((config != null), "Folder config is required to perform this operation");
        Preconditions.checkArgument((mediaSource != null), "MediaSource is required to perform this operation");
        return assembleUrl(config.getFolderQualifier(), mediaSource);
    }


    private String assembleUrl(String folderQualifier, MediaSource mediaSource)
    {
        if(!GenericValidator.isBlankOrNull(folderQualifier) && !GenericValidator.isBlankOrNull(mediaSource.getLocation()))
        {
            if(isPrettyUrlEnabled())
            {
                Optional<String> prettyUrl = assembleLegacyURL(folderQualifier, mediaSource);
                if(prettyUrl.isPresent())
                {
                    return prettyUrl.get();
                }
            }
            return assembleURLWithMediaContext(folderQualifier, mediaSource);
        }
        return "";
    }


    private Optional<String> assembleLegacyURL(String folderQualifier, MediaSource mediaSource)
    {
        StringBuilder sb = new StringBuilder(MediaUtil.addTrailingFileSepIfNeeded(getMediaWebRootContext()));
        PrettyUrlStrategy.MediaData mediaData = new PrettyUrlStrategy.MediaData(getTenantId(), folderQualifier, mediaSource.getLocation());
        mediaData.setRealFileName(mediaSource.getRealFileName());
        Optional<String> prettyPath = PrettyUrlStrategyFactory.getDefaultPrettyUrlStrategy().assemblePath(mediaData);
        Objects.requireNonNull(sb);
        return prettyPath.map(sb::append).map(Object::toString);
    }


    private String assembleURLWithMediaContext(String folderQualifier, MediaSource mediaSource)
    {
        StringBuilder builder = new StringBuilder(MediaUtil.addTrailingFileSepIfNeeded(getMediaWebRootContext()));
        String realFilename = getRealFileNameForMedia(mediaSource);
        if(realFilename != null)
        {
            builder.append(realFilename);
        }
        builder.append("?").append("context").append("=");
        builder.append(assembleMediaLocationContext(folderQualifier, mediaSource));
        return builder.toString();
    }


    private String getRealFileNameForMedia(MediaSource mediaSource)
    {
        String realFileName = mediaSource.getRealFileName();
        return StringUtils.isNotBlank(realFileName) ? MediaUtil.normalizeRealFileName(realFileName) : null;
    }


    public String getMediaWebRootContext()
    {
        return MediaUtil.addLeadingFileSepIfNeeded(
                        (StringUtils.isBlank(this.mediaWebRoot) || isMediaWebRootDisabled()) ? "/medias" : this.mediaWebRoot);
    }


    private boolean isMediaWebRootDisabled()
    {
        return StringUtils.containsIgnoreCase(this.mediaWebRoot, "<disabled>");
    }


    private String assembleMediaLocationContext(String folderQualifier, MediaSource mediaSource)
    {
        StringBuilder builder = new StringBuilder(getTenantId());
        builder.append("|").append(folderQualifier.replace("|", ""));
        builder.append("|").append(mediaSource.getSize());
        builder.append("|").append(getCtxPartOrNullMarker(mediaSource.getMime()));
        builder.append("|").append(getCtxPartOrNullMarker(mediaSource.getLocation()));
        builder.append("|").append(getCtxPartOrNullMarker(mediaSource.getLocationHash()));
        return (new Base64(-1, null, true)).encodeAsString(builder.toString().getBytes());
    }


    private String getCtxPartOrNullMarker(String ctxPart)
    {
        return StringUtils.isNotBlank(ctxPart) ? ctxPart : "-";
    }


    protected String getTenantId()
    {
        return Registry.getCurrentTenantNoFallback().getTenantID();
    }


    public String getDownloadUrlForMedia(MediaStorageConfigService.MediaFolderConfig config, MediaSource mediaSource)
    {
        StringBuilder url = new StringBuilder(getUrlForMedia(config, mediaSource));
        if(url.indexOf("?") < 0)
        {
            url.append("?");
        }
        else
        {
            url.append("&");
        }
        url.append("attachment").append("=").append("true");
        return url.toString();
    }


    protected boolean isPrettyUrlEnabled()
    {
        Boolean localOverride = this.overridePrettyUrlEnabled;
        if(localOverride != null)
        {
            return localOverride.booleanValue();
        }
        return PrettyUrlStrategyFactory.getDefaultPrettyUrlStrategy().isPrettyUrlEnabled();
    }


    public void setPrettyUrlEnabled(boolean prettyUrlEnabled)
    {
        this.overridePrettyUrlEnabled = Boolean.valueOf(prettyUrlEnabled);
    }
}
