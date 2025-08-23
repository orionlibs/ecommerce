package de.hybris.platform.testframework.seed;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

public class MediaTestDataCreator extends TestDataCreator
{
    private final MediaService mediaService;


    public MediaTestDataCreator(ModelService modelService, MediaService mediaService)
    {
        super(modelService);
        this.mediaService = mediaService;
    }


    public MediaFolderModel createMediaFolder()
    {
        return createMediaFolder(uniqueId());
    }


    public MediaFolderModel createMediaFolder(String qualifier)
    {
        return createMediaFolder(qualifier, qualifier);
    }


    public MediaFolderModel createMediaFolder(String qualifier, String path)
    {
        MediaFolderModel mediaFolder = (MediaFolderModel)getModelService().create(MediaFolderModel.class);
        mediaFolder.setQualifier(qualifier);
        mediaFolder.setPath(path);
        getModelService().save(mediaFolder);
        return mediaFolder;
    }


    public MediaModel createMedia(MediaFolderModel mediaFolder, CatalogVersionModel catalogVersion)
    {
        return createMedia(uniqueId(), mediaFolder, catalogVersion);
    }


    public MediaModel createMedia(CatalogVersionModel catalogVersion)
    {
        return createMedia(uniqueId(), catalogVersion);
    }


    public MediaModel createMedia(String code, CatalogVersionModel catalogVersion)
    {
        return createMedia(code, null, catalogVersion);
    }


    public MediaModel createMedia(String code, MediaFolderModel mediaFolder, CatalogVersionModel catalogVersion)
    {
        MediaModel media = (MediaModel)getModelService().create(MediaModel.class);
        media.setCode(code);
        media.setCatalogVersion(catalogVersion);
        media.setFolder(mediaFolder);
        getModelService().save(media);
        InputStream sampleInputStream = null;
        try
        {
            sampleInputStream = getSampleInputStream(randomStringOfLength(60).getBytes(StandardCharsets.UTF_8));
            this.mediaService.setStreamForMedia(media, sampleInputStream);
        }
        finally
        {
            IOUtils.closeQuietly(sampleInputStream);
        }
        return media;
    }


    public MediaModel createMediaWithExternalUrl(CatalogVersionModel catalogVersion)
    {
        String code = uniqueId();
        String url = randomUrl() + randomUrl();
        return createMediaWithExternalUrl(code, url, catalogVersion);
    }


    public MediaModel createMediaWithExternalUrl(String code, String url, CatalogVersionModel catalogVersion)
    {
        MediaModel media = (MediaModel)getModelService().create(MediaModel.class);
        media.setCode(code);
        media.setCatalogVersion(catalogVersion);
        media.setURL(url);
        getModelService().save(media);
        return media;
    }


    protected InputStream getSampleInputStream(byte[] data)
    {
        return new DataInputStream(new ByteArrayInputStream(data));
    }


    protected String randomUrl()
    {
        return "http://" + randomStringOfLength(10) + "/";
    }
}
