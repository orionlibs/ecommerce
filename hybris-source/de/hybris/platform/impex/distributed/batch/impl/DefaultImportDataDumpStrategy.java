package de.hybris.platform.impex.distributed.batch.impl;

import de.hybris.platform.impex.distributed.batch.ImportDataDumpStrategy;
import de.hybris.platform.impex.jalo.AbstractCodeLine;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.util.CSVWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultImportDataDumpStrategy implements ImportDataDumpStrategy
{
    public String dump(List<ValueLine> valueLines)
    {
        Objects.requireNonNull(valueLines, "valueLines cannot be null");
        if(valueLines.isEmpty())
        {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(valueLines.stream().map(this::dumpLine).collect(Collectors.joining()));
        return sb.toString();
    }


    public String dump(ValueLine valueLine)
    {
        return dumpLine(valueLine);
    }


    public String dump(HeaderDescriptor headerDescriptor)
    {
        return dump(headerDescriptor.dump());
    }


    public String dump(AbstractCodeLine codeLine)
    {
        return dump(codeLine.getSourceLine());
    }


    private String dumpLine(ValueLine valueLine)
    {
        return dump(valueLine.dump());
    }


    private String dump(Map<Integer, String> dump)
    {
        StringWriter writer = new StringWriter();
        try
        {
            CSVWriter csvWriter = getCsvWriter(writer);
            try
            {
                csvWriter.write(dump);
                if(csvWriter != null)
                {
                    csvWriter.close();
                }
            }
            catch(Throwable throwable)
            {
                if(csvWriter != null)
                {
                    try
                    {
                        csvWriter.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            throw new IllegalStateException(e);
        }
        return writer.toString();
    }


    CSVWriter getCsvWriter(StringWriter writer)
    {
        return new CSVWriter(writer);
    }
}
