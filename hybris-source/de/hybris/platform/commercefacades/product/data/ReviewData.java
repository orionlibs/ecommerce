package de.hybris.platform.commercefacades.product.data;

import de.hybris.platform.commercefacades.user.data.PrincipalData;
import java.io.Serializable;
import java.util.Date;

public class ReviewData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String headline;
    private String comment;
    private Double rating;
    private Date date;
    private String alias;
    private PrincipalData principal;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setHeadline(String headline)
    {
        this.headline = headline;
    }


    public String getHeadline()
    {
        return this.headline;
    }


    public void setComment(String comment)
    {
        this.comment = comment;
    }


    public String getComment()
    {
        return this.comment;
    }


    public void setRating(Double rating)
    {
        this.rating = rating;
    }


    public Double getRating()
    {
        return this.rating;
    }


    public void setDate(Date date)
    {
        this.date = date;
    }


    public Date getDate()
    {
        return this.date;
    }


    public void setAlias(String alias)
    {
        this.alias = alias;
    }


    public String getAlias()
    {
        return this.alias;
    }


    public void setPrincipal(PrincipalData principal)
    {
        this.principal = principal;
    }


    public PrincipalData getPrincipal()
    {
        return this.principal;
    }
}
