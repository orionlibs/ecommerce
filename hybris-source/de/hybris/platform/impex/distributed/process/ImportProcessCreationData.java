package de.hybris.platform.impex.distributed.process;

import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.impex.distributed.batch.ImportDataDumpStrategy;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.processing.distributed.ProcessCreationData;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class ImportProcessCreationData implements ProcessCreationData
{
    private final String processCode;
    private final InputStream input;
    private final ImportDataDumpStrategy dumpStrategy;
    private final ImportProcessContext ctx;


    public ImportProcessCreationData(String processCode, InputStream input, ImportDataDumpStrategy importDataDumpStrategy)
    {
        this(processCode, input, importDataDumpStrategy, null);
    }


    public ImportProcessCreationData(String processCode, InputStream input, ImportDataDumpStrategy importDataDumpStrategy, ImportProcessContext ctx)
    {
        this.processCode = Objects.<String>requireNonNull(processCode, "processCode mustn't be null");
        this.input = Objects.<InputStream>requireNonNull(input, "input data is required");
        this.dumpStrategy = Objects.<ImportDataDumpStrategy>requireNonNull(importDataDumpStrategy, "importDataDumpStrategy is required");
        this.ctx = ctx;
    }


    public Stream<ImportBatchCreationData> initialBatches()
    {
        return StreamSupport.stream((Spliterator<ImportBatchCreationData>)new ImpexBatchSpliterator(this, getImportReader()), false);
    }


    private BatchingImportReader getImportReader()
    {
        try
        {
            BatchingImportReader reader = new BatchingImportReader(this.input, this.dumpStrategy, this.processCode);
            setUpReader(reader);
            return reader;
        }
        catch(UnsupportedEncodingException e)
        {
            throw new SystemException(e);
        }
    }


    private void setUpReader(BatchingImportReader reader)
    {
        if(this.ctx == null)
        {
            return;
        }
        reader.setLocale(this.ctx.getLocale());
        List<ImpExMediaModel> impExMediaModels = this.ctx.getImpExMediaModels();
        if(CollectionUtils.isNotEmpty(impExMediaModels))
        {
            reader.addExternalDataMedias(convertImpExMediaModelsToItems(impExMediaModels));
        }
        String validationMode = this.ctx.getValidationMode();
        if(StringUtils.isNotBlank(validationMode))
        {
            reader.setValidationMode(ImpExManager.getValidationMode(validationMode));
        }
        else
        {
            reader.setValidationMode(ImpExManager.getImportStrictMode());
        }
    }


    private List<ImpExMedia> convertImpExMediaModelsToItems(List<ImpExMediaModel> impExMediaModels)
    {
        return (List<ImpExMedia>)impExMediaModels.stream().map(m -> (ImpExMedia)JaloSession.getCurrentSession().getItem(m.getPk()))
                        .collect(Collectors.toList());
    }


    public String getHandlerBeanId()
    {
        return "importProcessHandler";
    }


    public String getNodeGroup()
    {
        if(this.ctx == null || this.ctx.getNodeGroup() == null)
        {
            return null;
        }
        return this.ctx.getNodeGroup();
    }


    public String getProcessCode()
    {
        return this.processCode;
    }


    public JobLogLevel getEffectiveLogLevel()
    {
        if(this.ctx == null || this.ctx.getLogLevel() == null)
        {
            return JobLogLevel.WARNING;
        }
        return this.ctx.getLogLevel();
    }


    public ImportMetadata getImportProcessMetadata()
    {
        ImportMetadata metadata = ImportMetadata.empty();
        metadata.set("sld.enabled", Boolean.toString(isSldEnabled()));
        metadata.set("code.execution", Boolean.toString(isCodeExecutionEnabled()));
        metadata.set("process.code", getProcessCode());
        return metadata;
    }


    private boolean isSldEnabled()
    {
        return (this.ctx != null && this.ctx.isSldEnabled());
    }


    private boolean isCodeExecutionEnabled()
    {
        return (this.ctx != null && this.ctx.isCodeExecutionEnabled());
    }


    public void connectToCronJob(ImpExImportCronJobModel model)
    {
        if(this.ctx != null)
        {
            model.setExternalDataCollection(this.ctx.getImpExMediaModels());
        }
    }
}
