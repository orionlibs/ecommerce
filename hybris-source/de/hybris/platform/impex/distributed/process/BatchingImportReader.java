package de.hybris.platform.impex.distributed.process;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.distributed.batch.ImportDataDumpStrategy;
import de.hybris.platform.impex.jalo.AbstractCodeLine;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.jalo.ImpExReader;
import de.hybris.platform.impex.jalo.InvalidHeaderPolicy;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.impex.model.DistributedImportSplitErrorDumpModel;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchingImportReader extends ImpExReader
{
    private static final Logger LOG = LoggerFactory.getLogger(BatchingImportReader.class);
    private static final int DEFAULT_BATCH_SIZE = 100;
    private final ImportDataDumpStrategy dumpStrategy;
    private final String processCode;
    private int batchLineCounter = 0;
    private boolean stopCurrentBatch = false;
    private boolean previousHeaderValid = true;
    private boolean currentHeaderValid = true;
    private String previousHeaderDump = null;
    private String currentHeaderDump = null;
    private String codeLineDump = null;
    private boolean skipLinesToNextHeader = false;
    private final int batchSize;


    public BatchingImportReader(InputStream input, ImportDataDumpStrategy dumpStrategy, String processCode) throws UnsupportedEncodingException
    {
        super(Objects.<InputStream>requireNonNull(input), "UTF-8");
        this.dumpStrategy = dumpStrategy;
        super.enableCodeExecution(true);
        setInvalidHeaderPolicy(InvalidHeaderPolicy.DUMP_VALUE_LINES);
        this.batchSize = getConfiguredBatchSize();
        this.processCode = processCode;
    }


    int getConfiguredBatchSize()
    {
        return Config.getInt("impex.distributed.batch.size", 100);
    }


    private boolean isBatchSizeNotExceeded()
    {
        return (this.batchLineCounter < this.batchSize);
    }


    public ImpExMedia findExternalDataMedia(String code) throws JaloBusinessException
    {
        ImpExMedia ret = super.findExternalDataMedia(code);
        if(ret == null)
        {
            ret = findExternalDataMedia(this.processCode + "-", code);
        }
        return ret;
    }


    public void enableCodeExecution(boolean isOn)
    {
        throw new UnsupportedOperationException("Code execution is not enabled in a pre-process phase");
    }


    protected final void processCodeLine(AbstractCodeLine line) throws ImpExException
    {
        if(!isExternalCodeExecutionEnabled() && isIncludingExternalData())
        {
            LOG.warn("blocked code execution from external data ( line: {})", Integer.valueOf(line.getLineNumber()));
        }
        else if(line.getMarker() != null)
        {
            this.codeLineDump = this.dumpStrategy.dump(line);
        }
        else
        {
            execute(line, line.getSourceLine());
        }
    }


    public void setCurrentHeader(HeaderDescriptor header)
    {
        super.setCurrentHeader(header);
        backupCurrentHeader();
        this.currentHeaderValid = (header.getInvalidHeaderException() == null);
        if(this.currentHeaderValid)
        {
            this.currentHeaderDump = this.dumpStrategy.dump(getCurrentHeader());
        }
        if(this.batchLineCounter > 0 && isBatchSizeNotExceeded())
        {
            this.stopCurrentBatch = true;
            this.previousHeaderDump = composeHeader(this.previousHeaderDump);
            this.codeLineDump = null;
        }
        else
        {
            this.codeLineDump = null;
        }
    }


    private void backupCurrentHeader()
    {
        if(this.currentHeaderDump != null)
        {
            this.previousHeaderDump = this.currentHeaderDump;
        }
        this.previousHeaderValid = this.currentHeaderValid;
    }


    public ImportBatchCreationData getNextBatch()
    {
        List<String> valueLines = new ArrayList<>();
        List<String> invalidLines = new ArrayList<>();
        do
        {
            Object line = null;
            try
            {
                line = readLine();
                if(line == null && valueLines.isEmpty())
                {
                    resetCurrentBatch();
                    storeInvalidLines(invalidLines);
                    return null;
                }
                if(line == null && !valueLines.isEmpty())
                {
                    resetCurrentBatch();
                    storeInvalidLines(invalidLines);
                    return new ImportBatchCreationData(composeHeader(this.currentHeaderDump), valueLines);
                }
                if(line instanceof HeaderDescriptor && !((HeaderDescriptor)line).isValid())
                {
                    addToInvalidLines(invalidLines, line);
                    this.skipLinesToNextHeader = true;
                }
                if(this.skipLinesToNextHeader)
                {
                    if(!(line instanceof HeaderDescriptor))
                    {
                        addToInvalidLines(invalidLines, line);
                        break;
                    }
                    if(!((HeaderDescriptor)line).isValid())
                    {
                        break;
                    }
                    this.stopCurrentBatch = true;
                }
                if(this.stopCurrentBatch)
                {
                    resetCurrentBatch();
                    storeInvalidLines(invalidLines);
                    if(this.previousHeaderDump != null)
                    {
                        if(this.previousHeaderValid)
                        {
                            return new ImportBatchCreationData(this.previousHeaderDump, valueLines);
                        }
                    }
                }
                else
                {
                    addToValueLines(valueLines, line);
                }
            }
            catch(ImpExException e)
            {
                LOG.error("Error creating batch - reason: {}", e.getMessage());
                addToInvalidLines(invalidLines, line);
                this.skipLinesToNextHeader = true;
            }
        }
        while(isBatchSizeNotExceeded());
        resetCurrentBatch();
        storeInvalidLines(invalidLines);
        return new ImportBatchCreationData(composeHeader(this.currentHeaderDump), valueLines);
    }


    private void resetCurrentBatch()
    {
        this.batchLineCounter = 0;
        this.stopCurrentBatch = false;
        this.skipLinesToNextHeader = false;
    }


    private void storeInvalidLines(List<String> invalidLines)
    {
        if(CollectionUtils.isEmpty(invalidLines))
        {
            return;
        }
        DistributedImportSplitErrorDumpModel dump = (DistributedImportSplitErrorDumpModel)getModelService().create(DistributedImportSplitErrorDumpModel.class);
        dump.setCode(UUID.randomUUID().toString());
        dump.setProcessCode(this.processCode);
        dump.setContent(invalidLines.stream().collect(Collectors.joining("\n")));
        getModelService().save(dump);
    }


    private ModelService getModelService()
    {
        return (ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class);
    }


    private String composeHeader(String headerDump)
    {
        String result = stripLineEndings(headerDump);
        if(this.codeLineDump != null)
        {
            result = result + "\n" + result;
        }
        return result;
    }


    private void addToValueLines(List<String> valueLines, Object line)
    {
        if(line instanceof ValueLine)
        {
            valueLines.add(stripLineEndings(this.dumpStrategy.dump((ValueLine)line)));
            this.batchLineCounter++;
        }
    }


    private void addToInvalidLines(List<String> lines, Object line)
    {
        if(line instanceof ValueLine)
        {
            lines.add(stripLineEndings(this.dumpStrategy.dump((ValueLine)line)));
        }
        else if(line instanceof HeaderDescriptor)
        {
            lines.add(stripLineEndings(this.dumpStrategy.dump((HeaderDescriptor)line)));
        }
    }


    private String stripLineEndings(String input)
    {
        return input.replaceAll("\\r\\n|\\r|\\n", "");
    }
}
