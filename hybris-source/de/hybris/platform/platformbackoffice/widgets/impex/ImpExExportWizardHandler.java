package de.hybris.platform.platformbackoffice.widgets.impex;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.core.enums.EncodingEnum;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.impex.ExportConfig;
import de.hybris.platform.servicelayer.impex.ExportResult;
import de.hybris.platform.servicelayer.impex.ExportService;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.impl.ExportCronJobResult;
import de.hybris.platform.servicelayer.impex.impl.MediaBasedImpExResource;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class ImpExExportWizardHandler implements FlowActionHandler
{
    private static final String FORM = "exportForm";
    private ExportService exportService;
    private static final String DEFAULT_ENCODING = "UTF-8";


    public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        ImpExExportForm impExExportForm = (ImpExExportForm)adapter.getWidgetInstanceManager().getModel().getValue("exportForm", ImpExExportForm.class);
        ImpExMediaModel impExMedia = impExExportForm.getImpExMedia();
        if(impExMedia != null)
        {
            MediaBasedImpExResource mediaBasedImpExResource = new MediaBasedImpExResource(impExMedia);
            ExportConfig exportConfig = mapExportConfiguration(impExExportForm);
            exportConfig.setScript((ImpExResource)mediaBasedImpExResource);
            ExportResult result = this.exportService.exportData(exportConfig);
            handleExportResult(adapter, impExExportForm, result);
        }
        adapter.next();
    }


    private ExportConfig mapExportConfiguration(ImpExExportForm form)
    {
        ExportConfig exportConfig = new ExportConfig();
        exportConfig.setSynchronous(!form.isAsyncExecution());
        if(form.getValidationMode() != null)
        {
            ImpExValidationModeEnum validationMode = form.getValidationMode();
            if(validationMode == ImpExValidationModeEnum.EXPORT_REIMPORT_STRICT)
            {
                exportConfig.setValidationMode(ExportConfig.ValidationMode.STRICT);
            }
            else if(validationMode == ImpExValidationModeEnum.EXPORT_REIMPORT_RELAXED)
            {
                exportConfig.setValidationMode(ExportConfig.ValidationMode.RELAXED);
            }
            else
            {
                exportConfig.setValidationMode(ExportConfig.ValidationMode.WITHOUT);
            }
        }
        EncodingEnum encoding = form.getEncoding();
        if(encoding != null)
        {
            exportConfig.setEncoding(encoding.getCode());
        }
        else
        {
            exportConfig.setEncoding("UTF-8");
        }
        exportConfig.setFieldSeparator(form.getFieldSeparator());
        exportConfig.setQuoteCharacter(form.getEscapeCharacter());
        exportConfig.setCommentCharacter(form.getCommentCharacter());
        exportConfig.setSingleFile(!form.isExportAsZip());
        return exportConfig;
    }


    private void handleExportResult(FlowActionHandlerAdapter adapter, ImpExExportForm impExExportForm, ExportResult result)
    {
        if(result instanceof ExportCronJobResult)
        {
            ExportCronJobResult cronJobResult = (ExportCronJobResult)result;
            impExExportForm.setExportCronJob(cronJobResult.getCronJob());
        }
        if(!impExExportForm.isAsyncExecution())
        {
            ImpExMediaModel exportedData = result.getExportedData();
            ImpExMediaModel exportedMedia = result.getExportedMedia();
            if(exportedData != null)
            {
                impExExportForm.setDataDownloadMedia(exportedData);
                impExExportForm.setDataDownloadUrl(exportedData.getDownloadURL());
            }
            if(exportedMedia != null)
            {
                impExExportForm.setMediaDownloadMedia(exportedMedia);
                impExExportForm.setMediaDownloadUrl(exportedMedia.getDownloadURL());
            }
            if(result.isSuccessful())
            {
                String label = adapter.getWidgetInstanceManager().getLabel("impex.export.sync.success");
                impExExportForm.setExportStatus(label);
            }
            else
            {
                String label = adapter.getWidgetInstanceManager().getLabel("impex.export.sync.fail");
                impExExportForm.setExportStatus(label);
            }
        }
        else
        {
            String label = adapter.getWidgetInstanceManager().getLabel("impex.export.async.started");
            impExExportForm.setExportStatus(label);
        }
    }


    @Required
    public void setExportService(ExportService exportService)
    {
        this.exportService = exportService;
    }
}
