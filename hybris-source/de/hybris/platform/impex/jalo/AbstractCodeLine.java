package de.hybris.platform.impex.jalo;

import java.util.Map;
import org.apache.log4j.Logger;

public abstract class AbstractCodeLine
{
    private static final Logger LOG = Logger.getLogger(AbstractCodeLine.class);
    private final String location;
    private final int lineNumber;
    private final String scriptContent;
    private final String marker;
    private final Map src;
    private Object result;


    public AbstractCodeLine(Map<Integer, String> csvLine, String marker, String scriptContent, int lineNumber, String location)
    {
        this.src = csvLine;
        this.marker = marker;
        this.scriptContent = scriptContent;
        this.lineNumber = lineNumber;
        this.location = location;
    }


    public Map<Integer, String> getSourceLine()
    {
        return this.src;
    }


    public String getExecutableCode()
    {
        return this.scriptContent;
    }


    public int getLineNumber()
    {
        return this.lineNumber;
    }


    public String getLocation()
    {
        return this.location;
    }


    public String toString()
    {
        return getLocation() + ":" + getLocation();
    }


    public String getMarker()
    {
        return this.marker;
    }


    public void setResult(Object result)
    {
        this.result = result;
    }


    public Object getResult()
    {
        return this.result;
    }
}
