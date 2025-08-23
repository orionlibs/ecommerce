package de.hybris.platform.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;

public class FixedLengthCSVReader extends CSVReader
{
    private static final Logger LOGGER = Logger.getLogger(FixedLengthCSVReader.class);
    private final Map fieldRanges = new HashMap<>();
    private boolean trimming = false;


    public FixedLengthCSVReader(String fileName, String encoding) throws UnsupportedEncodingException, FileNotFoundException
    {
        super(fileName, encoding);
    }


    public FixedLengthCSVReader(File file, String encoding) throws UnsupportedEncodingException, FileNotFoundException
    {
        super(file, encoding);
    }


    public FixedLengthCSVReader(InputStream is, String encoding) throws UnsupportedEncodingException
    {
        super(is, encoding);
    }


    public FixedLengthCSVReader(Reader reader)
    {
        super(reader);
    }


    public FixedLengthCSVReader(String lines)
    {
        super(lines);
    }


    public void setFieldTrimming(boolean trim)
    {
        if(isReading())
        {
            throw new IllegalStateException("already reading stream, cannot set value");
        }
        this.trimming = trim;
    }


    public void addField(int startPosition, int endPosition, int columnNumber)
    {
        if(this.fieldRanges.containsKey(Integer.valueOf(columnNumber)))
        {
            throw new IllegalArgumentException("column already defined");
        }
        FixedRange fieldRange = new FixedRange(this, startPosition, endPosition);
        if(rangeInterleavesInMap(fieldRange))
        {
            throw new IllegalArgumentException("range interleaves with existing range");
        }
        this.fieldRanges.put(Integer.valueOf(columnNumber), fieldRange);
    }


    private boolean rangeInterleavesInMap(FixedRange testRange)
    {
        Iterator<FixedRange> iter = this.fieldRanges.values().iterator();
        while(iter.hasNext())
        {
            FixedRange range = iter.next();
            if(range.interleaves(testRange))
            {
                return true;
            }
        }
        return false;
    }


    protected Map parseLine(String line)
    {
        if(line.length() == 0 || (!isShowComments() && isCommentedOut(line)))
        {
            return Collections.EMPTY_MAP;
        }
        Map<Object, Object> ret = new HashMap<>();
        Iterator<Map.Entry> iter = this.fieldRanges.entrySet().iterator();
        while(iter.hasNext())
        {
            Map.Entry entry = iter.next();
            FixedRange range = (FixedRange)entry.getValue();
            if(range.x < line.length())
            {
                String column;
                if(range.y >= line.length())
                {
                    column = line.substring(range.x);
                }
                else
                {
                    column = line.substring(range.x, range.y + 1);
                }
                if(this.trimming)
                {
                    column = trim(column, true, true);
                }
                ret.put(entry.getKey(), column);
                continue;
            }
            LOGGER.warn("Can not parse field " + range + " and will skip field within record, because line ist too short. Line length=" + line
                            .length() + "=>last possible position=" + line
                            .length() - 1);
        }
        return ret;
    }
}
