package de.hybris.platform.commercewebservicescommons.dto.comments;

import de.hybris.platform.commercewebservicescommons.dto.user.PrincipalWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "Comment", description = "Object of the comment, which can be added to any Item in the commerce suite.")
public class CommentWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "text", value = "Text of the comment.", required = true, example = "Text of the comment")
    private String text;
    @ApiModelProperty(name = "creationDate", value = "Date when the comment was created.", required = true, example = "yyyy-MM-dd HH:mm:ss+0000")
    private Date creationDate;
    @ApiModelProperty(name = "author", value = "Author of the comment.")
    private PrincipalWsDTO author;
    @ApiModelProperty(name = "fromCustomer", value = "Flag showing if the current customer was the author of the comment.", required = true, example = "true")
    private Boolean fromCustomer;


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


    public void setAuthor(PrincipalWsDTO author)
    {
        this.author = author;
    }


    public PrincipalWsDTO getAuthor()
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
