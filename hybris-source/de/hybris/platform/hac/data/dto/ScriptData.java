package de.hybris.platform.hac.data.dto;

import de.hybris.platform.scripting.engine.content.ScriptContent;

public class ScriptData
{
    private String code;
    private String exception;
    private ScriptContent content;


    public void setException(String exception)
    {
        this.exception = exception;
    }


    public String getException()
    {
        return this.exception;
    }


    public void setContent(ScriptContent content)
    {
        this.content = content;
    }


    public ScriptContent getContent()
    {
        return this.content;
    }


    public boolean hasError()
    {
        return (this.exception != null);
    }


    public String getCode()
    {
        return this.code;
    }


    public void setCode(String code)
    {
        this.code = code;
    }
}
