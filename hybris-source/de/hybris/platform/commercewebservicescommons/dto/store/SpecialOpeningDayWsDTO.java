package de.hybris.platform.commercewebservicescommons.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

@ApiModel(value = "SpecialOpeningDay", description = "Representation of a special opening day")
public class SpecialOpeningDayWsDTO extends OpeningDayWsDTO
{
    @ApiModelProperty(name = "date", value = "Date of special opening day")
    private Date date;
    @ApiModelProperty(name = "formattedDate", value = "Text representation of the date of special opening day")
    private String formattedDate;
    @ApiModelProperty(name = "closed", value = "Flag stating if special opening day is closed")
    private Boolean closed;
    @ApiModelProperty(name = "name", value = "Name of the special opening day event")
    private String name;
    @ApiModelProperty(name = "comment", value = "Comment field")
    private String comment;


    public void setDate(Date date)
    {
        this.date = date;
    }


    public Date getDate()
    {
        return this.date;
    }


    public void setFormattedDate(String formattedDate)
    {
        this.formattedDate = formattedDate;
    }


    public String getFormattedDate()
    {
        return this.formattedDate;
    }


    public void setClosed(Boolean closed)
    {
        this.closed = closed;
    }


    public Boolean getClosed()
    {
        return this.closed;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setComment(String comment)
    {
        this.comment = comment;
    }


    public String getComment()
    {
        return this.comment;
    }
}
