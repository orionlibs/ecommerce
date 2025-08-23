package de.hybris.platform.impex.jalo;

import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.util.CSVReader;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Stack;
import org.apache.log4j.Logger;

public class ReaderManager
{
    private static final Logger log = Logger.getLogger(ReaderManager.class);
    private final Stack<StackEntry> stack;


    public ReaderManager(CSVReader mainReader)
    {
        this.stack = new Stack<>();
        pushReader(mainReader, 0, "main script");
    }


    public void pushReader(CSVReader reader, int columnOffset, String locationText)
    {
        this.stack.push(new StackEntry(reader, columnOffset, locationText));
    }


    public CSVReader popReader()
    {
        try
        {
            StackEntry entry = this.stack.pop();
            try
            {
                entry.reader.close();
            }
            catch(IOException e)
            {
                log.error("Can not close reader of external file " + entry.location + " , because: " + e.getMessage());
            }
            return entry.reader;
        }
        catch(EmptyStackException e)
        {
            return null;
        }
    }


    public String getCurrentLocation()
    {
        StringBuilder buffer = new StringBuilder();
        boolean first = true;
        for(StackEntry entry : this.stack)
        {
            if(first)
            {
                first = false;
            }
            else
            {
                buffer.append("->");
            }
            buffer.append("line " + entry.reader.getCurrentLineNumber() + " at " + entry.location);
        }
        return buffer.toString();
    }


    public void clear()
    {
        while(!this.stack.empty())
        {
            popReader();
        }
    }


    public CSVReader peekReader()
    {
        try
        {
            return ((StackEntry)this.stack.peek()).reader;
        }
        catch(EmptyStackException e)
        {
            return null;
        }
    }


    public CSVReader getMainReader()
    {
        if(this.stack.size() > 0)
        {
            return ((StackEntry)this.stack.get(0)).reader;
        }
        return null;
    }


    public int getCurrentColumnOffset() throws JaloSystemException
    {
        try
        {
            return ((StackEntry)this.stack.peek()).columnOffset;
        }
        catch(EmptyStackException e)
        {
            throw new JaloSystemException(e, "No reader instance found!", 0);
        }
    }


    public int readerCount()
    {
        return this.stack.size();
    }
}
