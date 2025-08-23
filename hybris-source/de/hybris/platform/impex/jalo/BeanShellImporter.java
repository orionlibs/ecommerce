package de.hybris.platform.impex.jalo;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import org.apache.log4j.Logger;

public class BeanShellImporter implements BeanShellImportable
{
    private static final Logger LOG = Logger.getLogger(BeanShellImporter.class.getName());
    private final ImpExReader impExReader;
    private boolean SLModeEnabled;
    private final ModelService modelService;


    public BeanShellImporter(ImpExReader impExReader, ModelService modelService)
    {
        this.impExReader = impExReader;
        this.modelService = modelService;
    }


    public void enableSLModeForImport(boolean value)
    {
        this.SLModeEnabled = value;
    }


    public void setLocale(Locale locale)
    {
        this.impExReader.setLocale(locale);
    }


    public void importQuery(String url, String user, String password, String className, String sqlStatement)
    {
        this.impExReader.includeSQLData(url, user, password, className, sqlStatement);
    }


    public void importFile(File file, String encoding, int linesToSkip) throws UnsupportedEncodingException, FileNotFoundException, ImpExException
    {
        this.impExReader.includeExternalData(file, encoding, linesToSkip);
    }


    public void importStream(InputStream inputStream, String encoding, char[] delimiter, int linesToSkip, int columnOffset) throws UnsupportedEncodingException
    {
        char[] _delimiter = delimiter;
        if(_delimiter == null || _delimiter.length < 1)
        {
            _delimiter = new char[] {';'};
        }
        this.impExReader.includeExternalData(inputStream, encoding, _delimiter, linesToSkip, columnOffset);
    }


    public void importMedia(ImpExMedia media, Integer columnOffset, String encoding, Character delimiter, Integer linesToSkip) throws UnsupportedEncodingException
    {
        int _columnOffset = (columnOffset != null) ? columnOffset.intValue() : -1;
        int _linesToSkip = (linesToSkip != null) ? linesToSkip.intValue() : media.getLinesToSkipAsPrimitive();
        char _delimiter = (delimiter != null) ? delimiter.charValue() : media.getFieldSeparatorAsPrimitive();
        this.impExReader.includeExternalDataMedia(media, encoding, _delimiter, _linesToSkip, _columnOffset);
    }


    public void enableBeanShellCodeExecution(boolean enable)
    {
        this.impExReader.enableExternalCodeExecution(enable);
    }


    public void enableBeanShellSyntaxParsing(boolean enable)
    {
        this.impExReader.enableExternalSyntaxParsing(enable);
    }


    public void enableResolving(boolean enable)
    {
        if(this.impExReader instanceof ImpExImportReader)
        {
            ((ImpExImportReader)this.impExReader).setDumpingAllowed(enable);
        }
        else
        {
            LOG.warn("Resolving feature is not supported  for this ImpExReader " + this.impExReader);
        }
    }


    public ItemModel getLastImportedData()
    {
        if(this.impExReader instanceof ImpExImportReader)
        {
            if(this.SLModeEnabled)
            {
                return (ItemModel)this.modelService.get(((ImpExImportReader)this.impExReader).getLastImportedItem());
            }
            return null;
        }
        return null;
    }


    public boolean isSLModeEnabled()
    {
        return this.SLModeEnabled;
    }


    public Locale getLocale()
    {
        return this.impExReader.getLocale();
    }


    public boolean isBeanShellCodeExecutionEnabled()
    {
        return this.impExReader.isExternalCodeExecutionEnabled();
    }


    public boolean isBeanShellSyntaxParsingEnabled()
    {
        return this.impExReader.isExternalSyntaxParsingEnabled();
    }


    public boolean isResolvingEnabled()
    {
        if(this.impExReader instanceof ImpExImportReader)
        {
            return ((ImpExImportReader)this.impExReader).isDumpingAllowed();
        }
        LOG.warn("Resolving feature is not supported  for this ImpExReader " + this.impExReader);
        return false;
    }
}
