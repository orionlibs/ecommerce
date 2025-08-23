package de.hybris.platform.commercefacades.comment.data;

import de.hybris.platform.commercefacades.user.data.PrincipalData;
import java.io.Serializable;
import java.util.Date;

public class CommentData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String text;
    private Date creationDate;
    private PrincipalData author;
    private Boolean fromCustomer;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setText(String text)
    {
        this.text = text;
    }


    public String getText()
    {
        return this.text;
    }


    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }


    public Date getCreationDate()
    {
        return this.creationDate;
    }


    public void setAuthor(PrincipalData author)
    {
        this.author = author;
    }


    public PrincipalData getAuthor()
    {
        return this.author;
    }


    public void setFromCustomer(Boolean fromCustomer)
    {
        this.fromCustomer = fromCustomer;
    }


    public Boolean getFromCustomer()
    {
        return this.fromCustomer;
    }
}
