package de.hybris.platform.commercefacades.user.data;

import java.io.Serializable;

public class RegisterData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String login;
    private String password;
    private String titleCode;
    private String firstName;
    private String lastName;


    public void setLogin(String login)
    {
        this.login = login;
    }


    public String getLogin()
    {
        return this.login;
    }


    public void setPassword(String password)
    {
        this.password = password;
    }


    public String getPassword()
    {
        return this.password;
    }


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
}
