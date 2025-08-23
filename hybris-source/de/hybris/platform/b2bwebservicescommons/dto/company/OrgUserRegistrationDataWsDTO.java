package de.hybris.platform.b2bwebservicescommons.dto.company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "OrgUserRegistrationData", description = "Representation of data used for user registration operations. Consists of fields used to apply to create new customer")
public class OrgUserRegistrationDataWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "titleCode", value = "Code of the user's title", required = false, example = "mr")
    private String titleCode;
    @ApiModelProperty(name = "firstName", value = "First name of the user", required = true, example = "akiro")
    private String firstName;
    @ApiModelProperty(name = "lastName", value = "Last name of the user", required = true, example = "nakamura")
    private String lastName;
    @ApiModelProperty(name = "email", value = "Email of the user", required = true, example = "akiro.nakamura@rustic-hw.com")
    private String email;
    @ApiModelProperty(name = "message", value = "Contains info to approver, usually composed by UI with a template", required = false, example = "Department:Ground support; Position:Chief safe guard; Report to: steve jackson; comments: Please create new account for me")
    private String message;


    public void setTitleCode(String titleCode)
    {
        this.titleCode = titleCode;
    }


    public String getTitleCode()
    {
        return this.titleCode;
    }


    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }


    public String getFirstName()
    {
        return this.firstName;
    }


    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }


    public String getLastName()
    {
        return this.lastName;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }


    public String getEmail()
    {
        return this.email;
    }


    public void setMessage(String message)
    {
        this.message = message;
    }


    public String getMessage()
    {
        return this.message;
    }
}
