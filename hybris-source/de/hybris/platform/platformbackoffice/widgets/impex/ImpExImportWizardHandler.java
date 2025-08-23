package de.hybris.platform.platformbackoffice.widgets.impex;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.cronjob.model.JobLogModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.MediaBasedImpExResource;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class ImpExImportWizardHandler implements FlowActionHandler
{
    private static final String FORM = "importForm";
    private ImportService importService;


    public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        ImpExImportForm impExImportForm = (ImpExImportForm)adapter.getWidgetInstanceManager().getModel().getValue("importForm", ImpExImportForm.class);
        if(impExImportForm.getImpExMedia() != null)
        {
            ImpExMediaModel impExMedia = impExImportForm.getImpExMedia();
            MediaBasedImpExResource mediaBasedImpExResource = new MediaBasedImpExResource(impExMedia);
            importData(impExImportForm, (ImpExResource)mediaBasedImpExResource);
        }
        adapter.next();
    }


    private void importData(ImpExImportForm impExImportForm, ImpExResource impExResource)
    {
        ImportConfig config = new ImportConfig();
        config.setScript(impExResource);
        impExImportForm.mapImportConfig(config);
        ImportResult result = this.importService.importData(config);
        StringBuilder logs = new StringBuilder();
        if(result.isFinished())
        {
            if(result.getCronJob() != null && result.getCronJob().getLogs() != null)
            {
                for(JobLogModel jobLogModel : result.getCronJob().getLogs())
                {
                    logs.append(jobLogModel.getMessage()).append("\n");
                }
            }
            impExImportForm.setImportLog(logs.toString());
        }
        else
        {
            impExImportForm.setImportLog("");
        }
        renderResultInfo(impExImportForm, result);
        impExImportForm.setImportExecutionCronJob(result.getCronJob());
        impExImportForm.setFilename(impExResource.getMedia().getCode());
        impExImportForm.setFinished(true);
    }


    private void renderResultInfo(ImpExImportForm impExImportForm, ImportResult result)
    {
        if(impExImportForm.isDistributedImpex())
        {
            impExImportForm.setStatus("Distributed import started");
        }
        else if(impExImportForm.isAsyncExecution())
        {
            impExImportForm.setStatus("Async import started");
        }
        else if(result.isFinished() && result.isSuccessful())
        {
            impExImportForm.setStatus("Sync import successful");
        }
        else
        {
            impExImportForm.setStatus("Sync import failed");
        }
    }


    @Required
    public void setImportService(ImportService importService)
    {
        this.importService = importService;
    }
}
