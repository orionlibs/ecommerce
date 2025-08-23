package de.hybris.platform.hac.facade;

import de.hybris.platform.hac.data.dto.ExportDataResult;
import de.hybris.platform.hac.data.dto.ImportDataResult;
import de.hybris.platform.hac.data.dto.ValidationDataResult;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import de.hybris.platform.impex.jalo.ScriptValidationReader;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.impex.ExportConfig;
import de.hybris.platform.servicelayer.impex.ExportResult;
import de.hybris.platform.servicelayer.impex.ExportService;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.util.CSVReader;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class HacImpexFacade
{
    private static final Logger LOG = Logger.getLogger(HacImpexFacade.class);
    private ImportService importService;
    private ExportService exportService;
    private MediaService mediaService;


    public ImportDataResult importData(ImportConfig dataInput)
    {
        ImportResult importResult = this.importService.importData(dataInput);
        ImportDataResult result = new ImportDataResult();
        result.setSuccesss(importResult.isSuccessful());
        result.setUnresolvedLines(importResult.hasUnresolvedLines());
        String resultString = "";
        if(importResult.hasUnresolvedLines())
        {
            resultString = resultString + resultString + "\n\n";
        }
        if(importResult.getCronJob() != null)
        {
            resultString = resultString + resultString;
        }
        result.setUnresolvedData(resultString);
        return result;
    }


    public ExportDataResult exportData(ExportConfig dataInput)
    {
        ExportResult exportResult = this.exportService.exportData(dataInput);
        ImpExMediaModel exportedData = exportResult.getExportedData();
        ExportDataResult result = new ExportDataResult();
        result.setSuccess(exportResult.isSuccessful());
        result.setDownloadUrl(getDownloadUrlForExportData(exportedData));
        result.setExportDataName(getExportDataName(exportedData));
        return result;
    }


    public ValidationDataResult validateScript(byte[] bytes, String encodingIsoCode, ImpExValidationModeEnum mode, boolean enableCodeExecution)
    {
        ValidationDataResult result = new ValidationDataResult();
        try
        {
            ScriptValidationReader validator = new ScriptValidationReader(new CSVReader(new ByteArrayInputStream(bytes), encodingIsoCode), mode.getCode());
            validator.enableCodeExecution(enableCodeExecution);
            validator.validateScript();
            LOG.info("ImpEx script is valid");
            result.setValid(true);
        }
        catch(UnsupportedEncodingException e)
        {
            String msg = "unsupported encoding";
            if(LOG.isDebugEnabled())
            {
                LOG.debug("unsupported encoding", e);
            }
            LOG.error("unsupported encoding");
            result.setValid(false);
            result.setMessage("unsupported encoding");
        }
        catch(Exception e)
        {
            String msg = e.getMessage();
            if(LOG.isDebugEnabled())
            {
                LOG.debug(msg, e);
            }
            LOG.error(msg);
            result.setValid(false);
            result.setMessage(msg);
        }
        return result;
    }


    private String getExportDataName(ImpExMediaModel media)
    {
        if(media != null)
        {
            return media.getRealFileName();
        }
        return "";
    }


    private String getDownloadUrlForExportData(ImpExMediaModel media)
    {
        if(media != null)
        {
            return media.getDownloadURL();
        }
        return "";
    }


    @Required
    public void setImportService(ImportService importService)
    {
        this.importService = importService;
    }


    @Required
    public void setExportService(ExportService exportService)
    {
        this.exportService = exportService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
