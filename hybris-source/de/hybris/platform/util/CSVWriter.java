package de.hybris.platform.util;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class CSVWriter implements Closeable
{
    private static final Logger log = Logger.getLogger(CSVWriter.class);
    private char commentchar;
    private char fieldseparator;
    private char textseparator;
    private String linebreak;
    private final BufferedWriter writer;
    private String[] lineSeparators;


    public CSVWriter(File file, String encoding) throws UnsupportedEncodingException, FileNotFoundException
    {
        this(file, encoding, false);
    }


    public CSVWriter(File file, String encoding, boolean append) throws UnsupportedEncodingException, FileNotFoundException
    {
        this(new FileOutputStream(file, append), encoding);
    }


    public CSVWriter(OutputStream os, String encoding) throws UnsupportedEncodingException
    {
        this(new OutputStreamWriter(os, (encoding != null) ? encoding : CSVConstants.DEFAULT_ENCODING));
    }


    public CSVWriter(String fileName, String encoding) throws UnsupportedEncodingException, FileNotFoundException
    {
        this(fileName, encoding, false);
    }


    public CSVWriter(String fileName, String encoding, boolean append) throws UnsupportedEncodingException, FileNotFoundException
    {
        this(new FileOutputStream(fileName, append), encoding);
    }


    public CSVWriter(Writer writer)
    {
        this.writer = new BufferedWriter(writer);
        this.commentchar = getDefaultCommentChar();
        this.fieldseparator = getDefaultFieldSeparator();
        this.textseparator = getDefaultTextSeparator();
        this.linebreak = getDefaultLineBreak();
        this.lineSeparators = getDefaultLineSeparators();
    }


    protected char getDefaultCommentChar()
    {
        return '#';
    }


    protected char getDefaultFieldSeparator()
    {
        return CSVConstants.DEFAULT_FIELD_SEPARATOR;
    }


    protected char getDefaultTextSeparator()
    {
        return CSVConstants.DEFAULT_QUOTE_CHARACTER;
    }


    protected String getDefaultLineBreak()
    {
        return CSVConstants.DEFAULT_LINE_SEPARATOR;
    }


    protected String[] getDefaultLineSeparators()
    {
        return CSVConstants.LINE_SEPARATORS;
    }


    public void close() throws IOException
    {
        this.writer.close();
    }


    public void closeQuietly()
    {
        try
        {
            if(this.writer != null)
            {
                close();
            }
        }
        catch(IOException ioe)
        {
            if(log.isDebugEnabled())
            {
                log.debug("An IOException occured during closing quietly the CSVWriter! " + ioe.getMessage());
            }
        }
    }


    private String createCSVField(String fielddata)
    {
        if(fielddata == null || fielddata.length() == 0)
        {
            return "";
        }
        Set<String> specials = new HashSet<>(Arrays.asList(this.lineSeparators));
        specials.add(Character.toString(getTextseparator()));
        specials.add(Character.toString(getFieldseparator()));
        specials.add(getLinebreak());
        StringBuilder buf = new StringBuilder(fielddata);
        if(CSVUtils.escapeString(buf, new String[] {Character.toString(getTextseparator())}, specials.<String>toArray(new String[specials.size()]), true))
        {
            buf.insert(0, getTextseparator());
            buf.append(getTextseparator());
        }
        return buf.toString();
    }


    public String createCSVLine(Map fields)
    {
        BitSet cells = new BitSet();
        int max = 0;
        for(Iterator<Map.Entry> iter = fields.entrySet().iterator(); iter.hasNext(); )
        {
            Map.Entry e = iter.next();
            int idx = ((Integer)e.getKey()).intValue();
            if(idx < 0)
            {
                throw new IllegalArgumentException("cell index < 0 (got " + idx + "=>" + e.getValue() + ")");
            }
            cells.set(idx);
            max = (max < idx) ? idx : max;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i <= max; i++)
        {
            if(i > 0)
            {
                sb.append(getFieldseparator());
            }
            if(cells.get(i))
            {
                String str = (String)fields.get(Integer.valueOf(i));
                if(str != null)
                {
                    sb.append(createCSVField(str));
                }
            }
        }
        return sb.toString();
    }


    public char getCommentchar()
    {
        return this.commentchar;
    }


    public char getFieldseparator()
    {
        return this.fieldseparator;
    }


    public char getTextseparator()
    {
        return this.textseparator;
    }


    public String getLinebreak()
    {
        return this.linebreak;
    }


    public Writer getWriter()
    {
        return this.writer;
    }


    public void setCommentchar(char commentchar)
    {
        this.commentchar = commentchar;
    }


    public void setFieldseparator(char fieldseparator)
    {
        this.fieldseparator = fieldseparator;
    }


    public void setTextseparator(char textseparator)
    {
        this.textseparator = textseparator;
    }


    public void setLinebreak(String linebreak)
    {
        this.linebreak = linebreak;
    }


    public int write(List data) throws IOException
    {
        int linenumber = 0;
        for(Iterator<Map> iter = data.iterator(); iter.hasNext(); )
        {
            write(iter.next());
            linenumber++;
        }
        return linenumber;
    }


    public void write(Map linedata) throws IOException
    {
        this.writer.write(createCSVLine(linedata));
        this.writer.write(getLinebreak());
    }


    public void writeComment(String scrline) throws IOException
    {
        writeSrcLine("" + getCommentchar() + getCommentchar());
    }


    public void writeSrcLine(String scrline) throws IOException
    {
        this.writer.write(scrline);
        this.writer.write(getLinebreak());
    }
}
