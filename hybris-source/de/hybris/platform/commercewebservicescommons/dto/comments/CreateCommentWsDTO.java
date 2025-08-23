package de.hybris.platform.commercewebservicescommons.dto.comments;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "CreateComment", description = "Object of the comment, which can be used to add a comment.")
public class CreateCommentWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "text", value = "Text of the comment.", required = true, example = "Text of the comment")
    private String text;


    public void setText(String text)
    {
        this.text = text;
    }


    public String getText()
    {
        return this.text;
    }
}
