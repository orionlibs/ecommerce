package de.hybris.deltadetection.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import de.hybris.deltadetection.ChangesCollector;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.exception.ChangesColletorException;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.CSVUtils;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class CsvReportChangesCollector implements ChangesCollector
{
    private static final Logger LOG = Logger.getLogger(CsvReportChangesCollector.class.getName());
    private static final Map<Integer, String> csvHeader;
    private final Writer csvWriter;

    static
    {
        Map<Integer, String> tmp = new HashMap<>();
        tmp.put(Integer.valueOf(0), "STREAM_ID");
        tmp.put(Integer.valueOf(1), "PK");
        tmp.put(Integer.valueOf(2), "ITEM_TYPE");
        tmp.put(Integer.valueOf(3), "CHANGE_TYPE");
        tmp.put(Integer.valueOf(4), "VERSION_TS");
        tmp.put(Integer.valueOf(5), "INFO");
        csvHeader = Collections.unmodifiableMap(tmp);
    }

    public CsvReportChangesCollector(Writer writer)
    {
        Preconditions.checkNotNull(writer);
        this.csvWriter = writer;
        try
        {
            writeCsvLine(csvHeader);
        }
        catch(IOException e)
        {
            throw new ChangesColletorException("Error during writing header into csv file", e);
        }
    }


    public boolean collect(ItemChangeDTO change)
    {
        try
        {
            writeCsvLine(convertToCsvData(change));
        }
        catch(IOException e)
        {
            throw new ChangesColletorException("Errors during writing the change: " + change + " into csv file", e);
        }
        return true;
    }


    public void finish()
    {
        LOG.info("Generating Csv report finished");
    }


    private Map<Integer, String> convertToCsvData(ItemChangeDTO change)
    {
        return (Map<Integer, String>)ImmutableMap.builder()
                        .put(Integer.valueOf(0), change.getStreamId())
                        .put(Integer.valueOf(1), change.getItemPK().toString())
                        .put(Integer.valueOf(2), change.getItemComposedType())
                        .put(Integer.valueOf(3), change.getChangeType().getCode())
                        .put(Integer.valueOf(4), String.valueOf(change.getVersion().getTime()))
                        .put(Integer.valueOf(5), change.getInfo())
                        .build();
    }


    private void writeCsvLine(Map<Integer, String> line) throws IOException
    {
        this.csvWriter.write(CSVUtils.buildCsvLine(line, CSVConstants.DEFAULT_FIELD_SEPARATOR, CSVConstants.DEFAULT_QUOTE_CHARACTER, CSVConstants.DEFAULT_LINE_SEPARATOR));
        this.csvWriter.write(CSVConstants.DEFAULT_LINE_SEPARATOR);
    }
}
