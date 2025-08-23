package de.hybris.platform.commercewebservicescommons.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "UserSignUp", description = "Representation of an UserSignUp. Consists of fields required to register new customer")
public class UserSignUpWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "uid", value = "user id, unique string required to create new user. It can be email", required = true)
    private String uid;
    @ApiModelProperty(name = "firstName", value = "first name of the user", required = true)
    private String firstName;
    @ApiModelProperty(name = "lastName", value = "last name of the user", required = true)
    private String lastName;
    @ApiModelProperty(name = "titleCode")
    private String titleCode;
    @ApiModelProperty(name = "password", value = "user password", required = true)
    private String password;


    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public String getUid()
    {
        return this.uid;
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


    public void setTitleCode(String titleCode)
    {
        this.titleCode = titleCode;
    }


    public String getTitleCode()
    {
        return this.titleCode;
    }


    public void setPassword(String password)
    {
        this.password = password;
    }


    public String getPassword()
    {
        return this.password;
    }
}
