package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.Date;

public class CMSCommentData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String text;
    private String code;
    private Date creationtime;
    private String authorName;
    private String decisionName;
    private String decisionCode;
    private String originalActionCode;
    private Long createdAgoInMillis;


    public void setText(String text)
    {
        this.text = text;
    }


    public String getText()
    {
        return this.text;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setCreationtime(Date creationtime)
    {
        this.creationtime = creationtime;
    }


    public Date getCreationtime()
    {
        return this.creationtime;
    }


    public void setAuthorName(String authorName)
    {
        this.authorName = authorName;
    }


    public String getAuthorName()
    {
        return this.authorName;
    }


    public void setDecisionName(String decisionName)
    {
        this.decisionName = decisionName;
    }


    public String getDecisionName()
    {
        return this.decisionName;
    }


    public void setDecisionCode(String decisionCode)
    {
        this.decisionCode = decisionCode;
    }


    public String getDecisionCode()
    {
        return this.decisionCode;
    }


    public void setOriginalActionCode(String originalActionCode)
    {
        this.originalActionCode = originalActionCode;
    }


    public String getOriginalActionCode()
    {
        return this.originalActionCode;
    }


    public void setCreatedAgoInMillis(Long createdAgoInMillis)
    {
        this.createdAgoInMillis = createdAgoInMillis;
    }


    public Long getCreatedAgoInMillis()
    {
        return this.createdAgoInMillis;
    }
}
