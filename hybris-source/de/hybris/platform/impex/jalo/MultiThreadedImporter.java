package de.hybris.platform.impex.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.cronjob.ImpExImportJob;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.impex.jalo.imp.MultiThreadedImpExImportReader;
import de.hybris.platform.impex.jalo.imp.MultiThreadedImportProcessor;
import de.hybris.platform.util.CSVReader;
import org.apache.log4j.Logger;

public class MultiThreadedImporter extends Importer
{
    private static final Logger LOG = Logger.getLogger(MultiThreadedImporter.class);
    private int maxThreads;


    public MultiThreadedImporter(CSVReader source)
    {
        super(source);
        setMaxThreads(ImpExImportJob.getDefaultMaxThreads(Registry.getCurrentTenant()));
    }


    public MultiThreadedImporter(MultiThreadedImpExImportReader importReader, int maxThreads)
    {
        super((ImpExImportReader)importReader);
        setMaxThreads(maxThreads);
    }


    protected ImpExImportReader createImportReader(CSVReader csvReader)
    {
        return (ImpExImportReader)new MultiThreadedImpExImportReader(csvReader);
    }


    protected ImpExImportReader createImportReaderForNextPass()
    {
        MultiThreadedImpExImportReader currentReader = (MultiThreadedImpExImportReader)getReader();
        MultiThreadedImpExImportReader reader = new MultiThreadedImpExImportReader(getDumpHandler().getReaderOfLastDump(), getDumpHandler().getWriterOfCurrentDump(), currentReader.getDocumentIDRegistry(), (MultiThreadedImportProcessor)currentReader.getImportProcessor(),
                        currentReader.getValidationMode());
        reader.setMaxThreads(getMaxThreads());
        reader.setIsSecondPass();
        reader.setLocale(currentReader.getLocale());
        reader.setLogFilter(getLogFilter());
        return (ImpExImportReader)reader;
    }


    protected void init() throws ImpExException
    {
        super.init();
        ((MultiThreadedImpExImportReader)getReader()).setLogFilter(getLogFilter());
    }


    public void setLogFilter(ImpExLogFilter filter)
    {
        super.setLogFilter(filter);
        ((MultiThreadedImpExImportReader)getReader()).setLogFilter(getLogFilter());
    }


    public int getMaxThreads()
    {
        return this.maxThreads;
    }


    public void setMaxThreads(int maxThreads)
    {
        this.maxThreads = maxThreads;
        ((MultiThreadedImpExImportReader)getReader()).setMaxThreads(getMaxThreads());
    }


    public String getCurrentLocation()
    {
        return "";
    }
}
