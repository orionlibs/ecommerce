package de.hybris.platform.media.web;

import com.google.common.collect.Iterables;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.media.services.MediaLocationHashService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMediaFilterLogic
{
    private static final Pattern VALID_PK_PATTERN = Pattern.compile("\\d{13,20}");
    private static final Logger LOG = LoggerFactory.getLogger(DefaultMediaFilterLogic.class);
    private static final int N_BYTES = 20;
    private final FlexibleSearchService flexibleSearchService;
    private final MediaService mediaService;


    public DefaultMediaFilterLogic(FlexibleSearchService flexibleSearchService, MediaService mediaService)
    {
        this.flexibleSearchService = flexibleSearchService;
        this.mediaService = mediaService;
    }


    public void addContentType(HttpServletResponse httpResponse, MediaFilterLogicContext ctx)
    {
        String contentType = getContentType(ctx.getMediaContext(), ctx.getResourcePath(), ctx.getHashType());
        httpResponse.setContentType(contentType);
    }


    private String getContentType(Iterable<String> mediaContext, String resourcePath, MediaLocationHashService.HashType hashType)
    {
        String contextMime = getContextPart(MediaFilterLogicContext.ContextPart.MIME, mediaContext);
        String location = getContextPart(MediaFilterLogicContext.ContextPart.LOCATION, mediaContext);
        String folderQualifier = getContextPart(MediaFilterLogicContext.ContextPart.FOLDER, mediaContext);
        if(isMimeVerified(hashType, contextMime))
        {
            return contextMime;
        }
        boolean dbDetectionEnabled = Config.getBoolean("media.filter.contentType.fromDB", true);
        if(!dbDetectionEnabled)
        {
            if(StringUtils.isNotBlank(contextMime))
            {
                return contextMime;
            }
            return detectContentType(mediaContext, resourcePath);
        }
        String contentType = getPersistedContentType(folderQualifier, location).orElseGet(() -> detectContentType(mediaContext, resourcePath));
        if(!StringUtils.equals(contextMime, contentType) && !isLegacyPrettyUrlSupport())
        {
            LOG.warn("Requested mime type  of {} is different from the persisted/detected one!", location);
        }
        return contentType;
    }


    private boolean isLegacyPrettyUrlSupport()
    {
        return Config.getBoolean("media.legacy.prettyURL", false);
    }


    private boolean isMimeVerified(MediaLocationHashService.HashType hashType, String contextMime)
    {
        if(StringUtils.isBlank(contextMime) || contextMime.equals("-"))
        {
            return false;
        }
        return (MediaLocationHashService.HashType.LOCATION_MIME_SIZE == hashType);
    }


    public void verifyHash(MediaFilterLogicContext ctx)
    {
        Iterable<String> mediaContext = ctx.getMediaContext();
        String folderQualifier = getContextPart(MediaFilterLogicContext.ContextPart.FOLDER, mediaContext);
        String location = getContextPart(MediaFilterLogicContext.ContextPart.LOCATION, mediaContext);
        String locationHash = getContextPart(MediaFilterLogicContext.ContextPart.LOCATION_HASH, mediaContext);
        String mime = getContextPart(MediaFilterLogicContext.ContextPart.MIME, mediaContext);
        long size = Long.parseLong(getContextPart(MediaFilterLogicContext.ContextPart.SIZE, mediaContext));
        MediaLocationHashService.HashType hashType = getMediaManager().verifyMediaHash(folderQualifier, location, locationHash, size, mime);
        ctx.setHashType(hashType);
    }


    protected Optional<String> getPersistedContentType(String folderQualifier, String location)
    {
        Optional<PK> optionalDataPK = extractDataPKFromLocation(location);
        if(optionalDataPK.isEmpty())
        {
            return Optional.empty();
        }
        List<String> mimeResult = loadMimesFromDb(optionalDataPK.get());
        if(mimeResult.isEmpty())
        {
            LOG.debug("No mime type persisted for: {}", location);
            return Optional.empty();
        }
        if(CollectionUtils.isNotEmpty(mimeResult))
        {
            Optional<String> persistedMimeType = extractPersistedMimeType(mimeResult);
            if(persistedMimeType.isPresent())
            {
                return persistedMimeType;
            }
            LOG.debug("Could not establish persisted mime type for: {}", location);
            return Optional.empty();
        }
        LOG.debug("No mime type persisted for: {}", location);
        return Optional.empty();
    }


    protected Optional<PK> extractDataPKFromLocation(String location)
    {
        Matcher matcher = VALID_PK_PATTERN.matcher(location);
        Set<PK> dataPKs = new HashSet<>();
        matcher.results().forEach(result -> {
            try
            {
                dataPKs.add(PK.parse(result.group()));
            }
            catch(de.hybris.platform.core.PK.PKException e)
            {
                LOG.debug("Error while parsing PK from location: {}", location, e);
            }
        });
        if(dataPKs.size() == 1)
        {
            return dataPKs.stream().findFirst();
        }
        LOG.debug("Expected 1 unique PK in location {} but got: {}", location, Integer.valueOf(dataPKs.size()));
        return Optional.empty();
    }


    protected List<String> loadMimesFromDb(PK dataPK)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {mime} FROM {Media} WHERE {dataPK}=?dataPK");
        fQuery.addQueryParameter("dataPK", dataPK);
        fQuery.setResultClassList(Collections.singletonList(String.class));
        SearchResult<String> mimeSearch = this.flexibleSearchService.search(fQuery);
        return mimeSearch.getResult();
    }


    @Deprecated(since = "2105", forRemoval = true)
    protected List<String> loadMimesFromDb(String location, MediaFolderModel folder)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {m.mime} FROM {media as m} WHERE {location}=?loc AND {folder}=?folder");
        fQuery.addQueryParameter("loc", location);
        fQuery.addQueryParameter("folder", folder);
        fQuery.setResultClassList(Collections.singletonList(String.class));
        SearchResult<String> mimeSearch = this.flexibleSearchService.search(fQuery);
        return mimeSearch.getResult();
    }


    private Optional<String> extractPersistedMimeType(List<String> mimeResult)
    {
        String firstMime = mimeResult.get(0);
        if(StringUtils.isBlank(firstMime))
        {
            return Optional.empty();
        }
        if(mimeResult.size() == 1)
        {
            return Optional.of(firstMime);
        }
        boolean areMimesConsistent = IntStream.range(1, mimeResult.size()).allMatch(i -> StringUtils.equals(firstMime, mimeResult.get(i)));
        if(areMimesConsistent)
        {
            return Optional.of(firstMime);
        }
        return Optional.empty();
    }


    protected String detectContentType(Iterable<String> mediaContext, String resourcePath)
    {
        String folderQualifier = getContextPart(MediaFilterLogicContext.ContextPart.FOLDER, mediaContext);
        String location = getContextPart(MediaFilterLogicContext.ContextPart.LOCATION, mediaContext);
        byte[] nBytes = null;
        try
        {
            MediaManager.InputStreamWithSize streamWithSize = getMediaAsStreamWithSize(folderQualifier, location);
            try
            {
                if(streamWithSize.getSize() >= 20L)
                {
                    nBytes = streamWithSize.getInputStream().readNBytes(20);
                }
                if(streamWithSize != null)
                {
                    streamWithSize.close();
                }
            }
            catch(Throwable throwable)
            {
                if(streamWithSize != null)
                {
                    try
                    {
                        streamWithSize.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException | de.hybris.platform.media.exceptions.MediaNotFoundException e)
        {
            LOG.info("Failed to detect mime type for : {}", resourcePath);
            return "application/octet-stream";
        }
        return getMediaManager().getBestMime(location, nBytes, "");
    }


    MediaManager.InputStreamWithSize getMediaAsStreamWithSize(String folderQualifier, String location)
    {
        return getMediaManager().getMediaAsStreamWithSize(folderQualifier, location);
    }


    protected MediaManager getMediaManager()
    {
        return MediaManager.getInstance();
    }


    protected String getContextPart(MediaFilterLogicContext.ContextPart contextPart, Iterable<String> mediaContext)
    {
        return (String)Iterables.get(mediaContext, contextPart.getPartNumber());
    }
}
