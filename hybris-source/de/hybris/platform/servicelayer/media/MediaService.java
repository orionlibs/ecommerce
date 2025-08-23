package de.hybris.platform.servicelayer.media;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaContextModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;

public interface MediaService
{
    boolean hasData(MediaModel paramMediaModel);


    @Deprecated(since = "ages", forRemoval = true)
    DataInputStream getDataStreamFromMedia(MediaModel paramMediaModel) throws NoDataAvailableException;


    InputStream getStreamFromMedia(MediaModel paramMediaModel) throws NoDataAvailableException, IllegalArgumentException;


    byte[] getDataFromMedia(MediaModel paramMediaModel) throws NoDataAvailableException;


    void setDataForMedia(MediaModel paramMediaModel, byte[] paramArrayOfbyte) throws MediaIOException, IllegalArgumentException;


    @Deprecated(since = "ages", forRemoval = true)
    void setDataStreamForMedia(MediaModel paramMediaModel, DataInputStream paramDataInputStream);


    void setStreamForMedia(MediaModel paramMediaModel, InputStream paramInputStream) throws MediaIOException, IllegalArgumentException;


    boolean removeDataFromMediaQuietly(MediaModel paramMediaModel);


    void removeDataFromMedia(MediaModel paramMediaModel);


    void addVersionStreamForMedia(MediaModel paramMediaModel, String paramString, InputStream paramInputStream);


    void addVersionStreamForMedia(MediaModel paramMediaModel, String paramString1, String paramString2, InputStream paramInputStream);


    void removeVersionForMedia(MediaModel paramMediaModel, String paramString);


    String getUrlForMediaVersion(MediaModel paramMediaModel, String paramString);


    InputStream getStreamForMediaVersion(MediaModel paramMediaModel, String paramString);


    void setStreamForMedia(MediaModel paramMediaModel, InputStream paramInputStream, String paramString1, String paramString2) throws MediaIOException, IllegalArgumentException;


    void setStreamForMedia(MediaModel paramMediaModel, InputStream paramInputStream, String paramString1, String paramString2, MediaFolderModel paramMediaFolderModel) throws MediaIOException, IllegalArgumentException;


    void setUrlForMedia(MediaModel paramMediaModel, String paramString);


    String getUrlForMedia(MediaModel paramMediaModel);


    @Deprecated(since = "ages", forRemoval = true)
    void setFolderForMedia(MediaModel paramMediaModel, MediaFolderModel paramMediaFolderModel) throws MediaIOException, IllegalArgumentException;


    void moveMediaToFolder(MediaModel paramMediaModel, MediaFolderModel paramMediaFolderModel) throws MediaIOException, IllegalArgumentException;


    void moveData(MediaModel paramMediaModel1, MediaModel paramMediaModel2) throws MediaIOException, IllegalArgumentException;


    void copyData(MediaModel paramMediaModel1, MediaModel paramMediaModel2) throws MediaIOException, IllegalArgumentException;


    void duplicateData(MediaModel paramMediaModel1, MediaModel paramMediaModel2) throws MediaIOException, IllegalArgumentException;


    MediaModel getMediaByContext(MediaModel paramMediaModel, MediaContextModel paramMediaContextModel) throws ModelNotFoundException, IllegalArgumentException;


    MediaModel getMediaByFormat(MediaModel paramMediaModel, MediaFormatModel paramMediaFormatModel) throws ModelNotFoundException, IllegalArgumentException;


    MediaModel getMediaByFormat(MediaContainerModel paramMediaContainerModel, MediaFormatModel paramMediaFormatModel) throws ModelNotFoundException, IllegalArgumentException;


    MediaModel getMedia(CatalogVersionModel paramCatalogVersionModel, String paramString) throws UnknownIdentifierException, AmbiguousIdentifierException;


    MediaModel getMedia(String paramString) throws UnknownIdentifierException, AmbiguousIdentifierException;


    @Deprecated(since = "ages", forRemoval = true)
    Collection<File> getFiles(MediaModel paramMediaModel) throws IllegalArgumentException, NoDataAvailableException;


    MediaFolderModel getFolder(String paramString) throws UnknownIdentifierException, AmbiguousIdentifierException;


    MediaFormatModel getFormat(String paramString) throws UnknownIdentifierException, AmbiguousIdentifierException;


    MediaFolderModel getRootFolder() throws ModelNotFoundException;


    Collection<MediaModel> getMediaWithSameDataReference(MediaModel paramMediaModel);
}
