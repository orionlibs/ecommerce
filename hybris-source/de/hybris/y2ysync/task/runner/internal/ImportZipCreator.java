package de.hybris.y2ysync.task.runner.internal;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.logging.Logs;
import de.hybris.platform.util.zip.SafeZipEntry;
import de.hybris.y2ysync.model.media.SyncImpExMediaModel;
import de.hybris.y2ysync.task.dao.Y2YSyncDAO;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class ImportZipCreator
{
    private static final Logger LOG = Logger.getLogger(ImportZipCreator.class);
    private static final String INCLUDE_LINE = "\"#% impex.includeExternalDataMedia(\"\"{0}-{1}-{2}.csv\"\", \"\"UTF-8\"\", '';'', 1, -1);\"";
    private static final String LINE_BREAK = "#-----------------------------------------------------------";
    private Y2YSyncDAO y2YSyncDAO;
    private MediaService mediaService;
    private ModelService modelService;
    private final HashFunction hashFunction = Hashing.md5();


    public ImportPackage createImportMedias(String syncExecutionID)
    {
        Map<String, List<SyncImpExMediaModel>> mediasByHeader = groupSyncMediasByHeader(syncExecutionID);
        CatalogUnawareMediaModel importDataMedia = createImportDataMedia(mediasByHeader, syncExecutionID);
        CatalogUnawareMediaModel importBinariesMedia = createImportBinariesMedia(mediasByHeader, syncExecutionID);
        return new ImportPackage(importDataMedia, importBinariesMedia);
    }


    private CatalogUnawareMediaModel createImportDataMedia(Map<String, List<SyncImpExMediaModel>> mediasByHeader, String syncExecutionID)
    {
        CatalogUnawareMediaModel media = null;
        Path tempZipPath = null;
        try
        {
            String mediaID = "data-" + syncExecutionID;
            ZipBuilder zipBuilder = new ZipBuilder(mediaID);
            try
            {
                zipBuilder.addEntry("importscript.impex");
                writeImportScript(mediasByHeader, zipBuilder);
                writeIncludeFiles(mediasByHeader, zipBuilder);
                tempZipPath = zipBuilder.getTempZipPath();
                zipBuilder.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    zipBuilder.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
            if(isTempZipExist(tempZipPath))
            {
                media = createFinalZipMedia(mediaID, tempZipPath);
            }
        }
        finally
        {
            deleteTempZip(tempZipPath);
        }
        return media;
    }


    private CatalogUnawareMediaModel createImportBinariesMedia(Map<String, List<SyncImpExMediaModel>> mediasByHeader, String syncExecutionID)
    {
        CatalogUnawareMediaModel media = null;
        Path tempZipPath = null;
        try
        {
            boolean zipContainsData;
            String mediaID = "media-" + syncExecutionID;
            ZipBuilder zipBuilder = new ZipBuilder(mediaID);
            try
            {
                writeMediaBinaries(mediasByHeader, zipBuilder);
                tempZipPath = zipBuilder.getTempZipPath();
                zipContainsData = zipBuilder.hasEntries();
                zipBuilder.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    zipBuilder.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
            if(isTempZipExist(tempZipPath) && zipContainsData)
            {
                media = createFinalZipMedia(mediaID, tempZipPath);
            }
        }
        finally
        {
            deleteTempZip(tempZipPath);
        }
        return media;
    }


    private void writeMediaBinaries(Map<String, List<SyncImpExMediaModel>> mediasByHeader, ZipBuilder zipBuilder)
    {
        for(Map.Entry<String, List<SyncImpExMediaModel>> entry : mediasByHeader.entrySet())
        {
            List<SyncImpExMediaModel> syncMedias = entry.getValue();
            for(SyncImpExMediaModel syncMedia : syncMedias)
            {
                addPossibleMediaBinary(zipBuilder, syncMedia);
            }
        }
    }


    private void addPossibleMediaBinary(ZipBuilder zipBuilder, SyncImpExMediaModel syncMedia)
    {
        MediaModel exportedMedia = syncMedia.getMediaArchive();
        if(exportedMedia != null)
        {
            InputStream streamFromMedia = this.mediaService.getStreamFromMedia(exportedMedia);
            try
            {
                ZipInputStream zin = new ZipInputStream(streamFromMedia);
                try
                {
                    ZipEntry mediaEntry;
                    while((mediaEntry = zin.getNextEntry()) != null)
                    {
                        zipBuilder.addEntry((ZipEntry)new SafeZipEntry(mediaEntry));
                        zipBuilder.writeToEntryUnsafe(zin);
                        zipBuilder.closeCurrentEntry();
                    }
                    zin.close();
                }
                catch(Throwable throwable)
                {
                    try
                    {
                        zin.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
            }
            catch(IOException e)
            {
                throw new IllegalStateException(e);
            }
        }
    }


    private Map<String, List<SyncImpExMediaModel>> groupSyncMediasByHeader(String syncExecutionID)
    {
        return (Map<String, List<SyncImpExMediaModel>>)this.y2YSyncDAO.findSyncMediasBySyncCronJob(syncExecutionID).stream()
                        .collect(Collectors.groupingBy(SyncImpExMediaModel::getImpexHeader));
    }


    private void writeImportScript(Map<String, List<SyncImpExMediaModel>> mediasByHeader, ZipBuilder zipBuilder)
    {
        for(Map.Entry<String, List<SyncImpExMediaModel>> entry : mediasByHeader.entrySet())
        {
            String header = entry.getKey();
            List<SyncImpExMediaModel> syncMedias = entry.getValue();
            zipBuilder.writeToEntry("#-----------------------------------------------------------");
            zipBuilder.writeToEntry(header);
            int entryCounter = 0;
            for(SyncImpExMediaModel syncMedia : syncMedias)
            {
                String includeLine = MessageFormat.format("\"#% impex.includeExternalDataMedia(\"\"{0}-{1}-{2}.csv\"\", \"\"UTF-8\"\", '';'', 1, -1);\"", new Object[] {syncMedia.getSyncType().getCode(),
                                getHash(syncMedia.getImpexHeader()),
                                Integer.valueOf(entryCounter)});
                zipBuilder.writeToEntry(includeLine);
                entryCounter++;
            }
            zipBuilder.writeToEntry("#-----------------------------------------------------------\n\n");
        }
        zipBuilder.closeCurrentEntry();
    }


    private void writeIncludeFiles(Map<String, List<SyncImpExMediaModel>> mediasByHeader, ZipBuilder zipBuilder)
    {
        for(Map.Entry<String, List<SyncImpExMediaModel>> entry : mediasByHeader.entrySet())
        {
            List<SyncImpExMediaModel> syncMedias = entry.getValue();
            int entryCounter = 0;
            for(SyncImpExMediaModel syncMedia : syncMedias)
            {
                zipBuilder.addEntry(syncMedia.getSyncType().getCode() + "-" + syncMedia.getSyncType().getCode() + "-" + getHash(syncMedia.getImpexHeader()) + ".csv");
                zipBuilder.writeToEntry(this.mediaService.getStreamFromMedia((MediaModel)syncMedia));
                zipBuilder.closeCurrentEntry();
                entryCounter++;
            }
        }
    }


    private String getHash(String input)
    {
        return this.hashFunction.newHasher().putString(input, Charsets.UTF_8).hash().toString();
    }


    private CatalogUnawareMediaModel createFinalZipMedia(String syncExecutionID, Path tempZipPath)
    {
        CatalogUnawareMediaModel media = (CatalogUnawareMediaModel)this.modelService.create(CatalogUnawareMediaModel.class);
        media.setCode(syncExecutionID);
        this.modelService.save(media);
        try
        {
            this.mediaService.setStreamForMedia((MediaModel)media, new FileInputStream(tempZipPath.toFile()), tempZipPath.getFileName().toString(), "application/zip");
        }
        catch(FileNotFoundException e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
        Logs.debug(LOG, () -> "Main media item " + media.getCode() + " has been created. Binary location: " + media.getLocation());
        return media;
    }


    private void deleteTempZip(Path tempZipPath)
    {
        if(!isTempZipExist(tempZipPath))
        {
            return;
        }
        try
        {
            Files.deleteIfExists(tempZipPath);
            Logs.debug(LOG, () -> "Main zip temp file " + tempZipPath + " has been deleted");
        }
        catch(IOException e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    private boolean isTempZipExist(Path tempZipPath)
    {
        return (tempZipPath != null && tempZipPath.toFile().exists());
    }


    @Required
    public void setY2YSyncDAO(Y2YSyncDAO y2YSyncDAO)
    {
        this.y2YSyncDAO = y2YSyncDAO;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
