package de.hybris.platform.util;

import de.hybris.bootstrap.xml.UnicodeReader;
import de.hybris.platform.jalo.JaloSystemException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CSVReader
{
    private static final Logger log = Logger.getLogger(CSVReader.class);
    private char[] commentOut = new char[] {'#'};
    private char[] fieldSeparator = new char[] {CSVConstants.DEFAULT_FIELD_SEPARATOR};
    private char textSeparator = CSVConstants.DEFAULT_QUOTE_CHARACTER;
    private boolean showComments = false;
    private boolean allowMultiLines = false;
    private final String multiLineSeparator = "\\";
    private int toSkip = 0;
    private int currentLineNumber = 0;
    private final BufferedReader reader;
    private String lastSourceLine = null;
    private Map<Integer, String> lastLine = null;
    private boolean finishedFlag = false;
    private long maxBufferLines = CSVConstants.DEFAULT_MAX_MULTI_LINES;
    private Map<Integer, CSVCellDecorator> cellDecorators = null;


    public CSVReader(String fileName, String encoding) throws UnsupportedEncodingException, FileNotFoundException
    {
        this((Reader)createReader(fileName, encoding));
    }


    public CSVReader(File file, String encoding) throws UnsupportedEncodingException, FileNotFoundException
    {
        this((Reader)createReader(file, encoding));
    }


    public CSVReader(InputStream is, String encoding) throws UnsupportedEncodingException
    {
        this((Reader)createReader(is, encoding));
    }


    public CSVReader(Reader reader)
    {
        this.reader = new BufferedReader(reader);
    }


    public CSVReader(String lines)
    {
        this.reader = new BufferedReader(new StringReader(lines));
    }


    private static UnicodeReader createReader(InputStream is, String encoding)
    {
        if(is == null)
        {
            throw new NullPointerException("Given input stream is null");
        }
        return new UnicodeReader(is, (encoding == null || encoding.length() == 0) ? CSVConstants.DEFAULT_ENCODING : encoding);
    }


    private static UnicodeReader createReader(File file, String encoding) throws FileNotFoundException
    {
        return createReader(new FileInputStream(file), encoding);
    }


    private static UnicodeReader createReader(String filename, String encoding) throws FileNotFoundException
    {
        return createReader(new FileInputStream(filename), encoding);
    }


    protected Map<Integer, CSVCellDecorator> getDecoratorMap(boolean create)
    {
        return (this.cellDecorators != null) ? this.cellDecorators : (create ? (this.cellDecorators = new HashMap<>()) :
                        Collections.EMPTY_MAP);
    }


    public void clearAllCellDecorators()
    {
        getDecoratorMap(false).clear();
    }


    public void clearCellDecorator(int position)
    {
        getDecoratorMap(false).remove(Integer.valueOf(position));
    }


    public void setCellDecorator(int position, CSVCellDecorator decorator)
    {
        if(decorator != null)
        {
            getDecoratorMap(true).put(Integer.valueOf(position), decorator);
        }
        else
        {
            getDecoratorMap(false).remove(Integer.valueOf(position));
        }
    }


    public boolean hasCellDecorators()
    {
        return !getDecoratorMap(false).isEmpty();
    }


    public CSVCellDecorator getCellDecorator(int position)
    {
        return getDecoratorMap(false).get(Integer.valueOf(position));
    }


    protected String readSrcLineFromStream()
    {
        String line = null;
        try
        {
            line = this.reader.readLine();
        }
        catch(IOException e)
        {
            try
            {
                this.reader.close();
            }
            catch(IOException ex)
            {
                log.warn("Can not close stream!", e);
            }
            throw new JaloSystemException(e);
        }
        if(line == null)
        {
            markFinished();
        }
        return (line != null) ? line : null;
    }


    public boolean finished()
    {
        return this.finishedFlag;
    }


    public final boolean readNextLine()
    {
        do
        {
            boolean doSkip = false;
            do
            {
                doSkip = mustSkip();
                List<String> multiLineBuffer = null;
                boolean gotMultiLine = false;
                do
                {
                    this.lastSourceLine = readSrcLineFromStream();
                    if(this.lastSourceLine == null)
                    {
                        return false;
                    }
                    boolean wrapped = false;
                    int wrappedLineNumber = this.currentLineNumber;
                    List<String> linebuffer = null;
                    wrapped = isWrappedLine(this.currentLineNumber, this.lastSourceLine, wrapped);
                    while(wrapped)
                    {
                        if(linebuffer == null)
                        {
                            linebuffer = new ArrayList<>();
                            linebuffer.add(this.lastSourceLine);
                        }
                        this.lastSourceLine = readSrcLineFromStream();
                        linebuffer.add(this.lastSourceLine);
                        if(this.lastSourceLine == null)
                        {
                            throw new IllegalStateException("reached EOF and got odd number of '" + getTextSeparator() + "'! File is corrupt. Error starts in line: \"" + (String)linebuffer
                                            .get(0) + "\" and occurs somewhere in the following lines.");
                        }
                        if(linebuffer.size() > this.maxBufferLines)
                        {
                            throw new IllegalStateException("Encounter a problem in line which starts with: " + (String)linebuffer.get(0) + ".\nAfter reading the next " + this.maxBufferLines
                                            + " lines, I give up. Check the file or set with CSVReader.setMaxBufferLines(int number) a greater number if you know that the csv file is ok");
                        }
                        wrapped = isWrappedLine(this.currentLineNumber, this.lastSourceLine, wrapped);
                    }
                    if(linebuffer != null)
                    {
                        StringBuilder sb = new StringBuilder();
                        int s = linebuffer.size();
                        for(int i = 0; i < s; i++)
                        {
                            if(i > 0)
                            {
                                sb.append(CSVConstants.DEFAULT_LINE_SEPARATOR);
                            }
                            sb.append(linebuffer.get(i));
                        }
                        linebuffer = null;
                        this.lastSourceLine = sb.toString();
                        this.currentLineNumber = wrappedLineNumber;
                    }
                    if(!this.allowMultiLines)
                    {
                        continue;
                    }
                    String l = trim(this.lastSourceLine, false, true);
                    Objects.requireNonNull(this);
                    if(l.endsWith("\\"))
                    {
                        if(multiLineBuffer == null)
                        {
                            multiLineBuffer = new LinkedList<>();
                        }
                        multiLineBuffer.add(l.substring(0, l.length() - 1));
                        gotMultiLine = true;
                    }
                    else if(gotMultiLine)
                    {
                        if(multiLineBuffer != null)
                        {
                            multiLineBuffer.add(this.lastSourceLine);
                        }
                        gotMultiLine = false;
                    }
                }
                while(this.allowMultiLines && gotMultiLine);
                notifyNextLine();
                if(multiLineBuffer != null)
                {
                    StringBuilder sb = new StringBuilder();
                    for(String s : multiLineBuffer)
                    {
                        sb.append(s);
                    }
                    this.lastSourceLine = sb.toString();
                    multiLineBuffer = null;
                }
                this.lastSourceLine = trim(this.lastSourceLine, true, true);
                this.lastLine = (this.lastSourceLine.length() > 0) ? parseLine(this.lastSourceLine) : null;
                if(this.lastLine == null || !hasCellDecorators())
                {
                    continue;
                }
                this.lastLine = applyDecorators(getDecoratorMap(false), this.lastLine);
            }
            while(doSkip);
        }
        while(this.lastLine == null);
        return true;
    }


    protected String trim(String src, boolean fromStart, boolean fromEnd)
    {
        if(src == null || src.length() == 0 || (!fromStart && !fromEnd))
        {
            return src;
        }
        int len = src.length();
        int st = 0;
        if(fromStart)
        {
            while(st < len && src.charAt(st) <= ' ' && !CSVUtils.isSeparator(this.fieldSeparator, src.charAt(st)))
            {
                st++;
            }
        }
        if(fromEnd)
        {
            while(st < len && src.charAt(len - 1) <= ' ' && !CSVUtils.isSeparator(this.fieldSeparator, src.charAt(len - 1)))
            {
                len--;
            }
        }
        return (st > 0 || len < src.length()) ? src.substring(st, len) : src;
    }


    public Map<Integer, String> getLine()
    {
        if(this.lastLine == null)
        {
            throw new IllegalStateException("end of stream already reached");
        }
        return this.lastLine;
    }


    public String getSourceLine()
    {
        if(this.lastSourceLine == null)
        {
            throw new IllegalStateException("end of stream already reached");
        }
        return this.lastSourceLine;
    }


    protected Map<Integer, String> parseLine(String line)
    {
        char[] separators = getFieldSeparator();
        Map<Integer, String> ret = null;
        int start = 0;
        int length = line.length();
        while(start < length)
        {
            boolean currentIsEscaped = (line.charAt(start) == getTextSeparator());
            for(int current = currentIsEscaped ? (start + 1) : start; current < length; current++)
            {
                char c = line.charAt(current);
                boolean foundSeparator = ((currentIsEscaped && c == getTextSeparator()) || (!currentIsEscaped && CSVUtils.isSeparator(separators, c)));
                boolean atEnd = (current + 1 == length);
                if(foundSeparator || atEnd)
                {
                    if(currentIsEscaped && !atEnd && line.charAt(current + 1) == getTextSeparator())
                    {
                        current++;
                    }
                    else
                    {
                        if(ret == null)
                        {
                            ret = new HashMap<>();
                        }
                        String value = currentIsEscaped ? CSVUtils.unescapeString(line.substring(start + 1, current).trim(), new char[] {getTextSeparator()}, true) : line.substring(start, foundSeparator ? current : (current + 1)).trim();
                        if(ret.isEmpty() && !isShowComments() && isCommentedOut(value))
                        {
                            return null;
                        }
                        ret.put(Integer.valueOf(ret.size()), value);
                        int nextSep = (atEnd && !foundSeparator) ? -1 : (!currentIsEscaped ? current : getNextSeparator(line, separators, current + 1));
                        if(nextSep > -1)
                        {
                            int nextChar = atEnd ? -1 : getNextNonWhitespace(line, nextSep + 1, separators);
                            if(nextChar > -1)
                            {
                                start = nextChar;
                                break;
                            }
                            ret.put(Integer.valueOf(ret.size()), "");
                            start = length;
                            break;
                        }
                        start = length;
                        break;
                    }
                }
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_MAP;
    }


    public void close() throws IOException
    {
        this.reader.close();
        markFinished();
    }


    public void closeQuietly()
    {
        try
        {
            if(this.reader != null)
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


    private boolean isWrappedLine(int lineNumber, String line, boolean wasWrappedLineBefore)
    {
        if(line == null || (!wasWrappedLineBefore && StringUtils.isBlank(line)))
        {
            return false;
        }
        char[] separators = getFieldSeparator();
        char textSep = getTextSeparator();
        boolean quoted = wasWrappedLineBefore;
        boolean cellStarted = quoted;
        boolean cellEnded = false;
        int s = line.length();
        for(int i = 0; i < s; i++)
        {
            char c = line.charAt(i);
            if(quoted)
            {
                if(c == textSep)
                {
                    if(i + 1 != s && line.charAt(i + 1) == textSep)
                    {
                        i++;
                    }
                    else
                    {
                        quoted = false;
                        cellEnded = true;
                    }
                }
            }
            else if(c == textSep)
            {
                if(cellEnded)
                {
                    throw new IllegalStateException("unexpected text separator " + textSep + " after cell end at " + i + " in line " + lineNumber + " : '" + line + "'");
                }
                if(!cellStarted)
                {
                    quoted = true;
                    cellStarted = true;
                }
            }
            else if(CSVUtils.isSeparator(separators, c))
            {
                cellStarted = false;
                cellEnded = false;
            }
            else if(!Character.isWhitespace(c))
            {
                if(cellEnded)
                {
                    throw new IllegalStateException("unexpected char " + c + " after cell end at " + i + " in line " + lineNumber + " : '" + line + "'");
                }
                cellStarted = true;
            }
        }
        return quoted;
    }


    private int getNextSeparator(String line, char[] separators, int startIndex)
    {
        int s = line.length();
        for(int i = startIndex; i < s; i++)
        {
            if(CSVUtils.isSeparator(separators, line.charAt(i)))
            {
                return i;
            }
        }
        return -1;
    }


    private int getNextNonWhitespace(String line, int startIndex, char[] separators)
    {
        int s = line.length();
        for(int i = startIndex; i < s; i++)
        {
            char c = line.charAt(i);
            if(CSVUtils.isSeparator(separators, c) || !Character.isWhitespace(c))
            {
                return i;
            }
        }
        return -1;
    }


    public void setTextSeparator(char textseparator)
    {
        if(isReading())
        {
            throw new IllegalStateException("already reading stream, cannot set value");
        }
        this.textSeparator = textseparator;
    }


    public boolean isShowComments()
    {
        return this.showComments;
    }


    protected boolean isCommentedOut(String line)
    {
        if(line == null || line.length() == 0)
        {
            return false;
        }
        char[] chars = this.commentOut;
        char first = line.charAt(0);
        int s = chars.length;
        for(int i = 0; i < s; i++)
        {
            if(first == chars[i])
            {
                return true;
            }
        }
        return false;
    }


    public void setShowComments(boolean showComments)
    {
        if(isReading())
        {
            throw new IllegalStateException("already reading stream, cannot set value");
        }
        this.showComments = showComments;
    }


    protected boolean isReading()
    {
        return (this.lastSourceLine != null);
    }


    public void setMultiLineMode(boolean on)
    {
        if(isReading())
        {
            throw new IllegalStateException("already reading stream, cannot set value");
        }
        this.allowMultiLines = on;
    }


    public boolean isMultiLineMode()
    {
        return this.allowMultiLines;
    }


    public void setCommentOut(char[] commentOut)
    {
        if(isReading())
        {
            throw new IllegalStateException("already reading stream, cannot set value");
        }
        this.commentOut = commentOut;
    }


    public void setFieldSeparator(char[] fieldseparator)
    {
        if(isReading())
        {
            throw new IllegalStateException("already reading stream, cannot set value");
        }
        this.fieldSeparator = fieldseparator;
    }


    public char[] getCommentOut()
    {
        return this.commentOut;
    }


    public char[] getFieldSeparator()
    {
        return this.fieldSeparator;
    }


    public char getTextSeparator()
    {
        return this.textSeparator;
    }


    public void setLinesToSkip(int i)
    {
        if(isReading())
        {
            throw new IllegalStateException("already reading stream, cannot set value");
        }
        if(i < 0 || i >= Integer.MAX_VALUE)
        {
            throw new IllegalStateException("value is not a positive integer");
        }
        this.toSkip = i;
    }


    public int getCurrentLineNumber()
    {
        return this.currentLineNumber;
    }


    protected void notifyNextLine()
    {
        this.currentLineNumber++;
        if(this.toSkip > 0)
        {
            this.toSkip--;
        }
    }


    protected boolean mustSkip()
    {
        return (this.toSkip > 0);
    }


    public void setMaxBufferLines(int number)
    {
        if(number > 0)
        {
            this.maxBufferLines = number;
        }
    }


    public boolean isFinished()
    {
        return this.finishedFlag;
    }


    protected void markFinished()
    {
        this.finishedFlag = true;
    }


    public static final Map<Integer, String>[] parse(CSVReader reader)
    {
        List<Map<Integer, String>> ret = new LinkedList<>();
        while(reader.readNextLine())
        {
            ret.add(reader.getLine());
        }
        return ret.<Map<Integer, String>>toArray((Map<Integer, String>[])new Map[ret.size()]);
    }


    public static final Map<Integer, String>[] parse(String lines, char[] fieldSeparator, char textSeparator)
    {
        CSVReader tmp = new CSVReader(lines);
        tmp.setFieldSeparator(fieldSeparator);
        tmp.setTextSeparator(textSeparator);
        return parse(tmp);
    }


    public static final Map<Integer, String>[] parse(String lines, char[] fieldSeparator)
    {
        CSVReader tmp = new CSVReader(lines);
        tmp.setFieldSeparator(fieldSeparator);
        return parse(tmp);
    }


    public static final Map<Integer, String>[] parse(String lines)
    {
        CSVReader tmp = new CSVReader(lines);
        return parse(tmp);
    }


    public static Map applyDecorators(Map<Integer, CSVCellDecorator> decoratorMap, Map line)
    {
        if(decoratorMap != null && !decoratorMap.isEmpty())
        {
            Map<Integer, String> patched = new HashMap<>(decoratorMap.size());
            for(Map.Entry<Integer, CSVCellDecorator> e : decoratorMap.entrySet())
            {
                Integer pos = e.getKey();
                patched.put(pos, ((CSVCellDecorator)e.getValue()).decorate(pos.intValue(), line));
            }
            for(Map.Entry<Integer, String> e : patched.entrySet())
            {
                String value = e.getValue();
                if(value != null)
                {
                    line.put(e.getKey(), value);
                    continue;
                }
                line.remove(e.getKey());
            }
        }
        return line;
    }
}
