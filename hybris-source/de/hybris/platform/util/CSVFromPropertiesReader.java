package de.hybris.platform.util;

import de.hybris.bootstrap.util.LocaleHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CSVFromPropertiesReader extends CSVReader
{
    private static final Logger log = Logger.getLogger(CSVFromPropertiesReader.class);
    private final int linesToBuffer;
    private final boolean caseInsensitive;
    private final Map<String, Integer> name2columns;
    private final Pattern idAndNameMatcher;
    private final BufferedReader bufferedReader;
    private final String emptyCellValue;
    private final CSVWriter csvWriter;
    private final Map<String, Map<Integer, String>> id2csvLineMap;


    public static CSVFromPropertiesReaderBuilder builder(Reader reader)
    {
        return new CSVFromPropertiesReaderBuilder(reader);
    }


    public CSVFromPropertiesReader(String idAndNameRegexp, Map<String, Integer> name2columns, boolean caseInsentitiveAttributeNames, int linesToBuffer, String emptyCellValue, Reader reader)
    {
        super(reader);
        this.linesToBuffer = linesToBuffer;
        this.csvWriter = new CSVWriter(new StringWriter());
        this.name2columns = name2columns;
        this.caseInsensitive = caseInsentitiveAttributeNames;
        this.emptyCellValue = emptyCellValue;
        this.idAndNameMatcher = Pattern.compile(idAndNameRegexp, 2);
        this.id2csvLineMap = new LinkedHashMap<>();
        this.bufferedReader = new BufferedReader(reader);
    }


    protected String readSrcLineFromStream()
    {
        try
        {
            Map<Integer, String> csv = parseNextCSVLine();
            return (csv == null) ? null : toCsv(csv);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    protected String toCsv(Map<Integer, String> csv)
    {
        for(Map.Entry<String, Integer> entry : this.name2columns.entrySet())
        {
            Integer column = entry.getValue();
            if(!csv.containsKey(column) && this.emptyCellValue != null)
            {
                csv.put(column, this.emptyCellValue);
            }
        }
        return this.csvWriter.createCSVLine(csv);
    }


    protected Map<Integer, String> parseNextCSVLine() throws IOException
    {
        for(ParsedProperty prop = parseNextProperty(); prop != null; prop = parseNextProperty())
        {
            Map<Integer, String> csvLine = addPropertyToCSVLine(prop);
            if(csvLine != null && isComplete(csvLine))
            {
                removeFromBuffer(prop.id);
                return csvLine;
            }
            csvLine = removeFromBufferIfFull(this.linesToBuffer);
            if(csvLine != null)
            {
                return csvLine;
            }
        }
        return removeFromBufferIfFull(0);
    }


    protected boolean isComplete(Map<Integer, String> csvLine)
    {
        return (csvLine.size() == this.name2columns.size() + 1);
    }


    protected ParsedProperty parseNextProperty() throws IOException
    {
        for(String line = readNextPropertyLine(); line != null; line = readNextPropertyLine())
        {
            int equalSign = line.indexOf('=');
            if(equalSign > 0)
            {
                String key = line.substring(0, equalSign).trim();
                String value = line.substring(equalSign + 1).trim();
                if(StringUtils.isNotBlank(value))
                {
                    ParsedProperty prop = createPropertyIfMatching(key, value);
                    if(prop != null)
                    {
                        return prop;
                    }
                }
            }
        }
        return null;
    }


    private ParsedProperty createPropertyIfMatching(String key, String value)
    {
        ParsedProperty prop = null;
        Matcher matcher = this.idAndNameMatcher.matcher(key);
        if(matcher.matches())
        {
            if(matcher.groupCount() == 2)
            {
                String id = matcher.group(1);
                String name = matcher.group(2);
                prop = new ParsedProperty(key, value, id, this.caseInsensitive ? name.toLowerCase(LocaleHelper.getPersistenceLocale()) : name);
            }
            else
            {
                log.warn("Illegal property '" + key + "'='" + value + "' for pattern " + this.idAndNameMatcher);
            }
        }
        else
        {
            log.warn("illegal property '" + key + "'='" + value + "' for pattern " + this.idAndNameMatcher);
        }
        return prop;
    }


    protected String readNextPropertyLine() throws IOException
    {
        StringBuffer buffer = null;
        for(String line = this.bufferedReader.readLine(); line != null; line = this.bufferedReader.readLine())
        {
            line = line.trim();
            if(line.length() != 0 && line.charAt(0) != '#')
            {
                if(endsWithSlash(line))
                {
                    if(buffer == null)
                    {
                        buffer = new StringBuffer();
                    }
                    line = line.substring(0, line.length() - 1);
                    buffer.append(line);
                }
                else
                {
                    if(buffer != null)
                    {
                        buffer.append(line);
                        return buffer.toString();
                    }
                    return line;
                }
            }
        }
        return null;
    }


    protected void removeFromBuffer(String id)
    {
        this.id2csvLineMap.remove(id);
    }


    protected Map<Integer, String> addPropertyToCSVLine(ParsedProperty property)
    {
        Map<Integer, String> csvLine = null;
        Integer pos = lookupPosition(property);
        if(pos != null)
        {
            csvLine = getOrCreateCSVLineForID(property.id);
            String previous = csvLine.put(pos, property.value);
            if(previous != null)
            {
                log.warn("Ambiguous property '" + property.key + "'='" + property.value + "'. Found previous value '" + property.key + "'='" + previous + "' which is ignored!");
            }
        }
        return csvLine;
    }


    public Integer lookupPosition(ParsedProperty property)
    {
        Integer pos = this.name2columns.get(property.name);
        if(pos == null)
        {
            log.warn("Illegal property '" + property.key + "'='" + property.value + "' for pattern " + this.idAndNameMatcher + " - no mapping position for name '" + property.name + "' in " + this.name2columns);
        }
        return pos;
    }


    public Map<Integer, String> getOrCreateCSVLineForID(String id)
    {
        Map<Integer, String> csvLine = this.id2csvLineMap.get(id);
        if(csvLine == null)
        {
            csvLine = new HashMap<>(this.name2columns.size() * 2);
            csvLine.put(Integer.valueOf(0), id);
            this.id2csvLineMap.put(id, csvLine);
        }
        return csvLine;
    }


    private Map<Integer, String> removeFromBufferIfFull(int maxAllowedBufferedCSVLineCount)
    {
        if(this.id2csvLineMap.size() > maxAllowedBufferedCSVLineCount)
        {
            Iterator<Map.Entry<String, Map<Integer, String>>> iterator = this.id2csvLineMap.entrySet().iterator();
            if(iterator.hasNext())
            {
                Map.Entry<String, Map<Integer, String>> entry = iterator.next();
                Map<Integer, String> csvLine = entry.getValue();
                removeFromBuffer(entry.getKey());
                return csvLine;
            }
        }
        return null;
    }


    private int countPreceding(String line, int index, char ch)
    {
        int i;
        for(i = index - 1; i >= 0; i--)
        {
            if(line.charAt(i) != ch)
            {
                break;
            }
        }
        return index - 1 - i;
    }


    private boolean endsWithSlash(String line)
    {
        if(!line.endsWith("\\"))
        {
            return false;
        }
        return (countPreceding(line, line.length() - 1, '\\') % 2 == 0);
    }


    public void close() throws IOException
    {
        this.bufferedReader.close();
        markFinished();
    }


    public void closeQuietly()
    {
        try
        {
            if(this.bufferedReader != null)
            {
                close();
            }
        }
        catch(IOException ioe)
        {
            if(log.isDebugEnabled())
            {
                log.debug("An IOException occured during closing quietly the CSVReader! " + ioe.getMessage());
            }
        }
    }
}
