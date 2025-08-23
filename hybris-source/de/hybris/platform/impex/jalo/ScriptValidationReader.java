package de.hybris.platform.impex.jalo;

import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.apache.log4j.Logger;

public class ScriptValidationReader extends ImpExReader
{
    private static final Logger LOG = Logger.getLogger(ScriptValidationReader.class);


    public ScriptValidationReader(CSVReader reader, String mode)
    {
        this(reader, EnumerationManager.getInstance().getEnumerationValue("ImpExValidationModeEnum", mode));
    }


    public ScriptValidationReader(CSVReader reader, EnumerationValue mode)
    {
        this(reader, mode, new DocumentIDRegistry(new CSVWriter(new StringWriter())));
    }


    public ScriptValidationReader(CSVReader reader, EnumerationValue mode, DocumentIDRegistry docIdRegistry)
    {
        super(reader, true, mode, docIdRegistry);
        enableCodeExecution(false);
        enableExternalCodeExecution(false);
        enableExternalSyntaxParsing(false);
    }


    public Object readLine() throws ImpExException
    {
        boolean _enableExternalSyntaxParsing = isExternalSyntaxParsingEnabled();
        if(!_enableExternalSyntaxParsing)
        {
            enableExternalSyntaxParsing(true);
        }
        Map line = null;
        boolean gotOne = false;
        while(true)
        {
            CSVReader csvReader = getCurrentReader();
            gotOne = csvReader.readNextLine();
            if(!gotOne)
            {
                if(isIncludingExternalData())
                {
                    try
                    {
                        close();
                    }
                    catch(IOException e)
                    {
                        LOG.error(e.getMessage());
                    }
                }
                else
                {
                    return null;
                }
            }
            if(gotOne)
            {
                line = replaceDefinitions(readNextCSVLine());
                if(!isEmptyLine(line))
                {
                    if(isHeaderLine(line))
                    {
                        setCurrentHeader(createNewHeader(line));
                    }
                    else if(isDefinition(line))
                    {
                        addDefinition((String)line.get(FIRST));
                    }
                }
                if(!gotOne)
                {
                    break;
                }
            }
        }
        enableExternalSyntaxParsing(_enableExternalSyntaxParsing);
        return line;
    }


    public static void validateScript(CSVReader reader) throws ImpExException
    {
        ScriptValidationReader validation = new ScriptValidationReader(reader, ImpExManager.getImportStrictMode());
        if(validation.readLine() == null)
        {
            throw new ImpExException("Script does not contain a header definitiion");
        }
    }


    public static void validateScript(InputStream inputStream, String encoding) throws ImpExException
    {
        try
        {
            validateScript(new CSVReader(inputStream, encoding));
        }
        catch(UnsupportedEncodingException e)
        {
            throw new ImpExException(e);
        }
    }


    public void validateScript() throws ImpExException
    {
        readLine();
    }
}
