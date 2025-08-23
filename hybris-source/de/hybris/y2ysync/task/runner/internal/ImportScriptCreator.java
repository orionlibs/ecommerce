package de.hybris.y2ysync.task.runner.internal;

import com.google.common.collect.ImmutableList;
import de.hybris.platform.core.PK;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.jalo.exp.Export;
import de.hybris.platform.impex.jalo.exp.converter.DefaultExportResultHandler;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.servicelayer.impex.ExportConfig;
import de.hybris.platform.servicelayer.impex.ExportResult;
import de.hybris.platform.servicelayer.impex.ExportService;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class ImportScriptCreator
{
    private final ModelService modelService;
    private final ExportService exportService;
    private final ExportScriptCreator exportScriptCreator;
    private final UserService userService;


    public ImportScriptCreator(ModelService modelService, ExportService exportService, ExportScriptCreator exportScriptCreator, UserService userService)
    {
        Objects.requireNonNull(modelService, "modelService can't be null");
        Objects.requireNonNull(exportService, "exportService can't be null");
        Objects.requireNonNull(exportScriptCreator, "exportScriptCreator can't be null");
        Objects.requireNonNull(userService, "userService can't be null");
        this.modelService = modelService;
        this.exportService = exportService;
        this.exportScriptCreator = exportScriptCreator;
        this.userService = userService;
    }


    public Collection<ImportScript> createImportScripts()
    {
        String exportScript = this.exportScriptCreator.createInsertUpdateExportScript();
        ExportConfig config = new ExportConfig();
        config.setValidationMode(ExportConfig.ValidationMode.STRICT);
        config.setEncoding("UTF-8");
        config.setScript((ImpExResource)new StreamBasedImpExResource(IOUtils.toInputStream(exportScript), "UTF-8"));
        config.setSessionUser(this.userService.getAdminUser());
        ExportResult exportResult = this.exportService.exportData(config);
        if(exportResult.isError() || !exportResult.isSuccessful())
        {
            throw new RuntimeException("Export has failed.");
        }
        try
        {
            ImmutableList.Builder<ImportScript> resultBuilder = extractImportScripts(exportResult);
            addDeleteScriptIfNeeded(resultBuilder);
            return (Collection<ImportScript>)resultBuilder.build();
        }
        catch(IOException | JaloBusinessException e)
        {
            throw new RuntimeException("Export has failed.", e);
        }
    }


    private void addDeleteScriptIfNeeded(ImmutableList.Builder<ImportScript> resultBuilder)
    {
        String deleteCsv = this.exportScriptCreator.createDeleteCsv();
        if(StringUtils.isNotBlank(deleteCsv))
        {
            resultBuilder
                            .add(new ImportScript(this.exportScriptCreator.getTypeCode(), this.exportScriptCreator.getRemoveHeader(), deleteCsv, null));
        }
    }


    private ImmutableList.Builder<ImportScript> extractImportScripts(ExportResult exportResult) throws IOException, JaloBusinessException
    {
        ImmutableList.Builder<ImportScript> resultBuilder = ImmutableList.builder();
        DefaultExportResultHandler handler = new DefaultExportResultHandler();
        try
        {
            handler.setExport((Export)this.modelService.getSource(exportResult.getExport()));
            List<ZipEntry> entries = handler.getZipEntries((ImpExMedia)this.modelService.getSource(exportResult.getExportedData()));
            for(ZipEntry entry : entries)
            {
                if(entry.getName().equalsIgnoreCase(this.exportScriptCreator.getFileNameForInsertedOrUpdatedItems()))
                {
                    resultBuilder.add(new ImportScript(this.exportScriptCreator
                                    .getTypeCode(), this.exportScriptCreator.getInsertUpdateHeader(), handler
                                    .getDataContent(entry).toString(), getMediaArchivePk(exportResult)));
                }
            }
            handler.close();
        }
        catch(Throwable throwable)
        {
            try
            {
                handler.close();
            }
            catch(Throwable throwable1)
            {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
        return resultBuilder;
    }


    private PK getMediaArchivePk(ExportResult exportResult)
    {
        ImpExMediaModel exportedMedia = exportResult.getExportedMedia();
        return (exportedMedia != null) ? exportedMedia.getPk() : null;
    }
}
