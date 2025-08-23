package de.hybris.platform.ruleengine;

import java.io.Serializable;

public class ResultItem implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String message;
    private MessageLevel level;
    private String path;
    private int line;


    public void setMessage(String message)
    {
        this.message = message;
    }


    public String getMessage()
    {
        return this.message;
    }


    public void setLevel(MessageLevel level)
    {
        this.level = level;
    }


    public MessageLevel getLevel()
    {
        return this.level;
    }


    public void setPath(String path)
    {
        this.path = path;
    }


    public String getPath()
    {
        return this.path;
    }


    public void setLine(int line)
    {
        this.line = line;
    }


    public int getLine()
    {
        return this.line;
    }
}
