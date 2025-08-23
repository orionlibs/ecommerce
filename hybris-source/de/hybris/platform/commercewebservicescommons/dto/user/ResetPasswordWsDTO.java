package de.hybris.platform.commercewebservicescommons.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "ResetPassword", description = "Representation of a Reset Password")
public class ResetPasswordWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "token", value = "token value which will be generated as unique string that will be sent with email to allow user for completing reset-password operation", required = true)
    private String token;
    @ApiModelProperty(name = "newPassword", value = "new password string which is required to complete process of resetting password", required = true)
    private String newPassword;


    public void setToken(String token)
    {
        this.token = token;
    }


    public String getToken()
    {
        return this.token;
    }


    public void setNewPassword(String newPassword)
    {
        this.newPassword = newPassword;
    }


    public String getNewPassword()
    {
        return this.newPassword;
    }
}
