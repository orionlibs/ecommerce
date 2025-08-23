package de.hybris.platform.b2bacceleratorfacades.order.data;

import de.hybris.platform.commercefacades.user.data.PrincipalData;
import java.io.Serializable;
import java.util.Date;

@Deprecated(since = "6.3", forRemoval = true)
public class B2BCommentData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String comment;
    private String code;
    private Date timeStamp;
    private PrincipalData ownerData;


    public void setComment(String comment)
    {
        this.comment = comment;
    }


    public String getComment()
    {
        return this.comment;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setTimeStamp(Date timeStamp)
    {
        this.timeStamp = timeStamp;
    }


    public Date getTimeStamp()
    {
        return this.timeStamp;
    }


    public void setOwnerData(PrincipalData ownerData)
    {
        this.ownerData = ownerData;
    }


    public PrincipalData getOwnerData()
    {
        return this.ownerData;
    }
}
