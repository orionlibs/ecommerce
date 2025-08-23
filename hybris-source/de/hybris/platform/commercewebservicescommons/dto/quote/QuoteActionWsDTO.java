package de.hybris.platform.commercewebservicescommons.dto.quote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "QuoteAction", description = "Action for the quote.")
public class QuoteActionWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "action", value = "User's actions with the quote. Typical actions are: CANCEL, SUBMIT, ACCEPT, APPROVE, REJECT.", required = true, example = "SUBMIT")
    private String action;


    public void setAction(String action)
    {
        this.action = action;
    }


    public String getAction()
    {
        return this.action;
    }
}
